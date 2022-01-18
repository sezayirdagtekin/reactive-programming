package com.sezo.reactive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@ExtendWith(SpringExtension.class)
@ComponentScan("com.sezo")
public class ControllerMockTest {
	
	private static final String BASE_URL = "http://localhost:8080/product";
	
    @MockBean
	private  ProductRepository repository;
    
	private WebTestClient client;
	
    List<Product>  expectedList= new ArrayList<>();;
	
		
	@BeforeEach
	void beforeEach() {		
		this.client= WebTestClient
				     .bindToController(new ProductController(repository))
				     .configureClient()
				     .baseUrl(BASE_URL)
				     .build();	
		
		Product p = new Product("1", "Apple", 2.2);
		expectedList.add(p);
	}
	
	
	@Test
	public void testGetAllPRoducts() {
		
		when(repository.findAll()).thenReturn(Flux.fromIterable(this.expectedList));
		
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

	@Test
	public void testGetProductdById() {
		Product product = expectedList.get(0);
		when(repository.findById(anyString())).thenReturn(Mono.just(product));
	
		client.get()
		      .uri("/id/{id}",product.getId())
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBody(Product.class)
		      .isEqualTo(product);	      
	}
	
}
