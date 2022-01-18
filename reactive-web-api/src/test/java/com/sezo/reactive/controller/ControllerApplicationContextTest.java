package com.sezo.reactive.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sezo.reactive.controller.ProductController;
import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ProductController.class)
public class ControllerApplicationContextTest {
	

	private WebTestClient client;
	
	@Autowired
	private  ProductRepository repository;
	
	@Autowired
	private ApplicationContext context;
	
	private List<Product>  expectedList;
	

		
	@BeforeEach
	void beforeEach() {		
		this.client= WebTestClient
				     .bindToApplicationContext(context)
				     .configureClient()
				     .baseUrl("/product")
				     .build();		
		this.expectedList= repository.findAll().collectList().block();	
	}
	
	
	@Test
	public void testGetAllPRoducts() {
		client.get()
		      .uri("/all")
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBodyList(Product.class)
		      .isEqualTo(expectedList);
	}
	
	@Test
	public void testProductNotFound() {
           client.get()
                 .uri("/xxx")
                 .exchange()
                 .expectStatus()
                 .isNotFound();              
	}

	
	public void testProductFoundById() {
		
		Product product= expectedList.get(0);
		client.get()
		      .uri("/id/{id}",product.getId())
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBody(Product.class)
		      .isEqualTo(product);	      
	}
	
}
