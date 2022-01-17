package com.sezo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class WebClientApp {

	private static final String URI = "http://localhost:8080/product";
	
	private WebClient webClient;

	public WebClientApp() {

		this.webClient = WebClient
				         .builder()
				         .baseUrl(URI)
				         .build();
	}
	
	
	public static void main(String[] args) {
		System.out.println("Start");
		WebClientApp app= new WebClientApp();
		
		app.postNewProduct().thenMany(app.getAllProducts())
		
		.take(1)
		.flatMap(p-> app.updateProduct(p.getId(), "latte", 99.2))
		.flatMap(p-> app.deleteProduct(p.getId()))
	    .thenMany(app.getAllProducts())
	    .thenMany(app.getAllEvents())
	    .subscribeOn(Schedulers.newSingle("My Thread"))
	    .subscribe(System.out::println);
	
		System.out.println("End");
	
	}
	
	private Mono<ResponseEntity<Product>> postNewProduct() {

		return webClient
				.post()
				.uri("/save")
				.body(Mono.just(new Product(null, "cake", 8.34)), Product.class)
				.exchangeToMono(response -> response.toEntity(Product.class))
				.doOnSuccess(o -> System.out.println("Post:" + o));

	}

	private Flux<Product> getAllProducts() {

		return webClient
				.get()
				.uri("/all")
				.retrieve().
				bodyToFlux(Product.class)
				.doOnNext(s -> System.out.println("Get Al Products:" + s));

	}

	private Mono<Product> updateProduct(String id, String name, Double price) {

		return webClient
				.put().uri("/update/{id}",id)
				.body(Mono.just(new Product(null, name, price)), Product.class)
				.retrieve()
				.bodyToMono(Product.class)
				.doOnSuccess(s -> System.out.println("Update Product:" + s));
	}
	

	private Mono<Void> deleteProduct(String id) {

		return webClient
				.delete()
				.uri("/delete/{id}",id)
				.retrieve()
				.bodyToMono(Void.class)
				.doOnSuccess(s -> System.out.println("Delete Product:" + s));
	}
	
	
	private Flux<ProductEvent> getAllEvents(){
		
		return webClient
				.get()
				.uri("/events")
				.retrieve()
				.bodyToFlux(ProductEvent.class);
	}

}
