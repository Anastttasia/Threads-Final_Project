package com.finalproject.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.finalproject.model.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, String> {

}
