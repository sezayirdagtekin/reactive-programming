package com.sezo.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.sezo.reactive.model.Product;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,String> {

}
