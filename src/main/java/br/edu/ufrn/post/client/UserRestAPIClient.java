package br.edu.ufrn.post.client;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.edu.ufrn.post.record.UserDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import reactor.core.publisher.Mono;

@Component
public class UserRestAPIClient implements UserClient {

    @Autowired
    private ReactiveRedisTemplate<String, UserDTO> cache;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private Retry retry;

    private final WebClient client;
    
    private static final Logger logger = LoggerFactory.getLogger(UserRestAPIClient.class);

    public UserRestAPIClient(
        WebClient.Builder builder,
        @Value("${user.restapi.base-url}") String baseUrl
    ) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    private Mono<UserDTO> fetchAndCacheFallback(String userId, Throwable ex) {
        return Mono.just(new UserDTO(userId, null, null))
            .doOnNext(obj -> logger.warn("Fallback triggered for userId={} due to {}", userId, ex.toString()));
    }

    private Mono<UserDTO> fetchAndCache(String userId, String key) {
        return client.get()
            .uri("/users/{id}", userId)
            .retrieve()
            .bodyToMono(UserDTO.class)
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .onErrorResume(ex -> fetchAndCacheFallback(userId, ex))
            .flatMap(user -> cache.opsForValue()
                .set(key, user, Duration.ofMinutes(1))
                .doOnSuccess(v -> logger.info(
                    "Cache miss for key=[{}]. Fetched userId={} and cached for {} minute(s)", key, userId, 1))
                .thenReturn(user));
    }

    @Override
    public Mono<UserDTO> getById(String userId) {
        String key = "restapi:user:" + userId;

        return cache.opsForValue()
            .get(key)
            .doOnNext(obj -> logger.info(
                "Cache hit for key=[{}]. Returning cached UserDTO for userId={}", key, userId))
            .switchIfEmpty(fetchAndCache(userId, key));
    }

}
