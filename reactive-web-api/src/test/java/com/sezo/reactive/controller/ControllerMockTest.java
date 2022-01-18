package com.sezo.reactive.controller;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;
import reactor.core.publisher.Flux;


@ExtendWith(SpringExtension.class)
public class ControllerMockTest {
	

	private WebTestClient client;
	
    @MockBean
	private  ProductRepository repository;
	
	@Autowired

	
	private List<Product>  expectedList;
	

		
	@BeforeEach
	void beforeEach() {		
		this.client= WebTestClient
				     .bindToController(new ProductController(repository))
				     .configureClient()
				     .baseUrl("/product")
				     .build();	
		
		Product p = new Product(null, "Apple", 2.2);
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

	
	public void testProductFoundById() {
		
		Product product= expectedList.get(0);
		client.get()
		      .uri("/id{id}",product.getId())
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBody(Product.class)
		      .isEqualTo(product);	      
	}
	
}
