package com.sezo.reactive.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.sezo.reactive.model.Product;
import com.sezo.reactive.repository.ProductRepository;


@WebFluxTest(ProductController.class)
public class Junit5WebFluxControllerTest {
	
	@Autowired
	private WebTestClient client;
	
    @MockBean
	private  ProductRepository repository;
	
	private List<Product>  expectedList;
		
	@BeforeEach
	void beforeEach() {		

		Product p = new Product(null, "Apple", 2.2);
		expectedList.add(p);
	}
	
	
	@Test
	public void testGetAllPRoducts() {
		client.get()
		      .uri("product/all")
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
		      .uri("product/id/{id}",product.getId())
		      .exchange()
		      .expectStatus()
		      .isOk()
		      .expectBody(Product.class)
		      .isEqualTo(product);	      
	}
	
}
