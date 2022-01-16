package com.sezo.reactor;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import reactor.core.publisher.Flux;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.2.7")
class ReactiveApplicationTests {

	@Test
	void monoTest() throws InterruptedException {
		
	Flux<Integer> fluxOne=Flux.range(1, 5).delayElements(Duration.ofSeconds(1));
	Flux<Integer> fluxSecond=Flux.range(1, 5).delayElements(Duration.ofSeconds(1));
	
	
Flux.zip(fluxOne, fluxSecond,(a,b)-> a+" -"+b)
.subscribe(System.out::println);
	
	Thread.sleep(20000);

	}

}
