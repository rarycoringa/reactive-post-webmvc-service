package br.edu.ufrn.post.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class Resilience4jConfig {
    
    @Bean
    public CircuitBreaker userClientCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("userClientCircuitBreaker");
    }

    @Bean
    public Retry userClientRetry(RetryRegistry registry) {
        return registry.retry("userClientRetry");
    }

}
