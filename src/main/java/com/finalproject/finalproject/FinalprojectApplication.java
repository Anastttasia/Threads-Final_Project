package com.finalproject.finalproject;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


class ItemData {
    private String url;
    private String price;
    private String description;

    ItemData(String _url, String _price, String _description) {
        this.url = _url;
        this.price = _price;
        this.description = _description;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }
}

class ParseItemThread extends Thread {

    private String url;
    private ItemData result;

    ParseItemThread(String itemUrl) {
        super();

        this.url = itemUrl;
        this.result = null;
    }

    public void run() {
        System.out.println("Start parse item by URL : " + this.url);

        if (this.url == null)
        {
            System.out.println("Error - URL is NULL");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String html = restTemplate.getForObject(this.url, String.class);

        if (html == null)
        {
            System.out.println("Error - html is NULL");
            return;
        }

        Document doc = Jsoup.parse(html);

        Element priceElement = doc.getElementsByClass("product__price").first();
        Element descriptionElement = doc.getElementsByClass("product__descr").first();

        String priceStr = priceElement != null ? priceElement.text() : "Price Not Found";
        String descriptionStr = descriptionElement != null ? descriptionElement.text() : "Description Not Found";

        this.result = new ItemData(this.url, priceStr, descriptionStr);

        System.out.println("Finished parse item by URL : " + this.url);
    }

    public ItemData getResult() {
        return this.result;
    }
}


@SpringBootApplication
@RestController
public class FinalprojectApplication {

    static int parsedCount;

	public static void main(String[] args) {
		SpringApplication.run(FinalprojectApplication.class, args);
	}

    @GetMapping("/parsed")
    public ResponseEntity getParsed(@RequestParam(value="urls") String urls) {

        if (urls == null)
        {
            return ResponseEntity.badRequest().build();
        }

        String[] urlsArray = urls.split(",");

        ParseItemThread[] parseItemThreads = new ParseItemThread[urlsArray.length];

        for (int i = 0; i < urlsArray.length; i++){
            parseItemThreads[i] = new ParseItemThread(urlsArray[i]);
        }

        for (ParseItemThread thread : parseItemThreads)
        {
            thread.start();
        }

        boolean isThreadsFinished = false;

        while (!isThreadsFinished)
        {
            int finishedThreads = 0;

            for (ParseItemThread thread : parseItemThreads)
            {
                finishedThreads += thread.getState() == Thread.State.TERMINATED ? 1 : 0;
            }

            isThreadsFinished = finishedThreads == parseItemThreads.length;
        }

        ArrayList<ItemData> result = new ArrayList<ItemData>();

        for (ParseItemThread thread : parseItemThreads)
        {
            result.add(thread.getResult());
        }

        return ResponseEntity.ok(result);
    }

}
