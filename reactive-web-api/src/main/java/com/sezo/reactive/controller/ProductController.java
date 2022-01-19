package com.sezo.reactive.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.model.ProductEvent;
import com.sezo.reactive.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/product")
public class ProductController {

	private ProductRepository repository;

	public ProductController(ProductRepository repository) {
		this.repository = repository;
	}

	public ProductController() {
		
	}
	
	@GetMapping("/all")
	Flux<Product> getAllProducts() {
		return repository.findAll();
	}

	@GetMapping("/id/{id}")
	Mono<ResponseEntity<Product>> getProduct(@PathVariable String id) {
		return repository.findById(id)
				.map(p -> ResponseEntity.ok(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Product> save(@RequestBody Product product) {
		return repository.save(product);
	}

	@PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<Product>> update(@PathVariable(value = "id") String id, @RequestBody Product product) {
		return repository.findById(id).flatMap(existingProduct -> {
			existingProduct.setName(product.getName());
			existingProduct.setPrice(product.getPrice());
			return repository.save(existingProduct);
		}).map(updatedProduct -> ResponseEntity.ok(updatedProduct)).defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@DeleteMapping("/delete/{id}")
	Mono<ResponseEntity<Void>> delete(@PathVariable String id) {

		return repository.findById(id).flatMap(p -> repository.delete(p)
				.then(Mono.just(ResponseEntity.ok().<Void>build()))
				.defaultIfEmpty(ResponseEntity.notFound().build()));
	}

	@DeleteMapping("/delete/all")
	Mono<Void> deleteApp() {
		return repository.deleteAll();
	}

	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductEvent> getProductEvents() {

		return Flux.interval(Duration.ofSeconds(1))
			   .map(i -> new ProductEvent(i, "Product event"));
	}

}
