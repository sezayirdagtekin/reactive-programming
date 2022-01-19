package com.sezo.reactive.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;

import reactor.core.publisher.Flux;


@WebFluxTest(controllers =  ProductController.class)
public class Junit5WebFluxControllerTest {
	
	@Autowired
	private WebTestClient client;
	
    @MockBean
	private  ProductRepository repository;
	
    List<Product>  expectedList = new ArrayList<>();
		
	@BeforeEach
	void beforeEach() {		

		Product p = new Product("1", "Apple", 2.2);
		expectedList.add(p);
	}
	
	
	@Test
	public void testGetAllPRoducts() {
		
		when(repository.findAll()).thenReturn(Flux.fromIterable(this.expectedList));
		
		client.get()
		      .uri("http://localhost:8080/product/all")
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBodyList(Product.class)
		      .isEqualTo(expectedList);
	}
	
	@Test
	public void testProductNotFound() {
           client.get()
                 .uri("http://localhost:8080/product/xxx")
                 .exchange()
                 .expectStatus()
                 .isNotFound();              
	}

	@Test
	public void testProductFoundById() {
		
		Product product= expectedList.get(0);
		client.get()
		      .uri("http://localhost:8080/product/id/{id}",product.getId())
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBody(Product.class)
		      .isEqualTo(product);	      
	}
	
}
