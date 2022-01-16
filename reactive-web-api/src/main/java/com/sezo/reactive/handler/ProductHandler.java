package com.sezo.reactive.handler;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.sezo.reactive.model.Product;
import com.sezo.reactive.model.ProductEvent;
import com.sezo.reactive.repository.ProductRepository;

@Component
public class ProductHandler {

	private ProductRepository repository;

	public ProductHandler(ProductRepository repository) {
		this.repository = repository;
	}

	public Mono<ServerResponse> getAllProducts(ServerRequest request) {

		Flux<Product> products = repository.findAll();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(products, Product.class);

	}

	public Mono<ServerResponse> getProduct(ServerRequest request) {

		String id = request.pathVariable("id");

		Mono<Product> productMondo = repository.findById(id);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		return productMondo.flatMap(
				p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(p)))
				.switchIfEmpty(notFound);

	}

	public Mono<ServerResponse> savePRoduct(ServerRequest request) {

		Mono<Product> productMono = request.bodyToMono(Product.class);

		return productMono.flatMap(p -> ServerResponse.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(repository.save(p), Product.class));

	}

	public Mono<ServerResponse> updatePRoduct(ServerRequest request) {
		String id = request.pathVariable("id");
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		Mono<Product> existingProductMono = repository.findById(id);
		Mono<Product> productMono = request.bodyToMono(Product.class);

		return productMono
				.zipWith(existingProductMono,(product, existingProduct) -> new Product(existingProduct.getId(), product.getName(),product.getPrice()))
				.flatMap(pr -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.save(pr),Product.class))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> deleteProduct(ServerRequest request) {
		String id = request.pathVariable("id");
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		Mono<Product> monoPpoduct = repository.findById(id);

		return monoPpoduct
				.flatMap(p -> ServerResponse.ok().build(repository.delete(p)))
				.switchIfEmpty(notFound);

	}

	public Mono<ServerResponse> deleteAllProduct(ServerRequest request) {
		return ServerResponse.
				ok()
				.build(repository.deleteAll());
	}

	public Mono<ServerResponse> getProductEvents(ServerRequest request) {

		Flux<ProductEvent> eventFlux = Flux.interval(Duration.ofSeconds(1)) 
				                       .map(val -> new ProductEvent(val, "Product event"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(eventFlux, Product.class);

	}
}
