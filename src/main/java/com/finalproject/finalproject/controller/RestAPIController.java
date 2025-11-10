package com.finalproject.finalproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.finalproject.finalproject.model.ProductModel;
import com.finalproject.finalproject.repository.ProductRepository;


class ParseItemThread extends Thread {

    private ProductRepository productRepository;
    private String url;
    private String html;

    ParseItemThread(ProductRepository productRepository, String itemUrl, String html) {
        super();

        this.productRepository = productRepository;
        this.url = itemUrl;
        this.html = html;
    }

    public void run() {
        System.out.println("Start parse item by URL : " + this.url);

        Document doc = Jsoup.parse(this.html);

        System.out.println("0 : " + this.url);

        Element priceElement = doc.getElementsByClass("product__price").first();
        Element descriptionElement = doc.getElementsByClass("product__descr").first();

        String priceStr = priceElement != null ? priceElement.text() : "Price Not Found";
        String descriptionStr = descriptionElement != null ? descriptionElement.text() : "Description Not Found";

        System.out.println("1 : " + this.url);

        Optional<ProductModel> productData = productRepository.findById(this.url);

        System.out.println("2 : " + this.url);

        if(productData.isPresent()) {
            System.out.println("3 : " + this.url);
            ProductModel product = productData.get();
            product.setPrice(priceStr);
            product.setDescription(descriptionStr);
            productRepository.save(product);
            System.out.println("4 : " + this.url);
        }
        else {
            System.out.println("5 : " + this.url);
            productRepository.save(new ProductModel(this.url, priceStr, descriptionStr));
        }

        System.out.println("Finished parse item by URL : " + this.url);
    }
}


@RestController
public class RestAPIController {

    @Autowired
    ProductRepository productRepository;

    ScheduledExecutorService parserThreadsExecutor;

    @PostMapping("/parse")
    public void parseUrls(@RequestParam(value="urls") String urls) {

        if (urls == null)
        {
            return;
        }

        if (parserThreadsExecutor != null)
        {
            parserThreadsExecutor.shutdown();
        }

        parserThreadsExecutor = Executors.newScheduledThreadPool(1);

        String[] urlsArray = urls.split(",");

        ArrayList<ParseItemThread> parseItemThreads = new ArrayList<>();

        for (String url : urlsArray) {
            RestTemplate restTemplate = new RestTemplate();
            String html = null;

            try {
                html = restTemplate.getForObject(url, String.class);
            }
            catch(Exception e) {
                System.out.println("Error - url is bad " + url);
                continue;
            }

            if (html == null)
            {
                System.out.println("Error - html is NULL for " + url);
                continue;
            }

            ParseItemThread thread = new ParseItemThread(productRepository, url, html);
            parserThreadsExecutor.scheduleAtFixedRate(thread, 0, 1, TimeUnit.MINUTES);
            parseItemThreads.add(thread);
        }
    }

    @GetMapping("/allParsed")
    public ResponseEntity<ArrayList<ProductModel>> getAllParsed() {

        ArrayList<ProductModel> productsData = new ArrayList<ProductModel>();

        productRepository.findAll().forEach(productsData::add);

        if (productsData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productsData, HttpStatus.OK);
    }
}
