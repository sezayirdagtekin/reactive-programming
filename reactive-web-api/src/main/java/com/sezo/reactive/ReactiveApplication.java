package com.sezo.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveApplication  implements CommandLineRunner{

	@Autowired
	
	private ProductRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Product> productFux= Flux.just(
				new Product(null,"Apple",2.2),
				new Product(null,"Orange",2.4),
				new Product(null,"Banana",6.8)
							
				).flatMap(repository::save);
		
		
		productFux.thenMany(repository.findAll()).subscribe(System.out::println);
		
	}
	

}
