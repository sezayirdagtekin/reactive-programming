package com.sezo.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.sezo.reactive.handler.ProductHandler;

@Configuration
public class ProductRouter  {
	
	@Bean
	public RouterFunction<ServerResponse> routeExample (ProductHandler handler) {
	    return RouterFunctions.route(
	    		 RequestPredicates.GET("/pr/all").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getAllProducts)
	    		.and(RouterFunctions.route(RequestPredicates.GET("/pr/id/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getProduct))
	    		.and(RouterFunctions.route(RequestPredicates.POST("/pr/save").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::savePRoduct))
		        .and(RouterFunctions.route(RequestPredicates.PUT("/pr/update/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::updatePRoduct))
		    	.and(RouterFunctions.route(RequestPredicates.DELETE("/pr/delete/all").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteAllProduct))
	    		.and(RouterFunctions.route(RequestPredicates.DELETE("/pr/delete/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteProduct))
	    		.and(RouterFunctions.route(RequestPredicates.GET("/pr/events").and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)), handler::getProductEvents));
    	
	    
	  }

}
