package br.edu.ufrn.post.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import br.edu.ufrn.post.record.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class UserGraphQLClient implements UserClient {

    private final HttpSyncGraphQlClient client;

    private static final Logger logger = LoggerFactory.getLogger(UserGraphQLClient.class);

    public UserGraphQLClient(
        @LoadBalanced RestTemplate restTemplate,
        @Value("${user.graphql.base-url}") String baseUrl
    ) {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        this.client = HttpSyncGraphQlClient.create(RestClient.create(restTemplate));
    }

    public UserDTO fallbackGetById(String userId, Throwable ex) {
        logger.warn("Fallback triggered for userId={} due to {}", userId, ex.toString());
        return new UserDTO(userId, null, null);
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    @Retry(name = "userClientRetry")
    @CircuitBreaker(name = "userClientCircuitBreaker", fallbackMethod = "fallbackGetById")
    public UserDTO getById(String userId) {
        logger.info("Fetching userId={} from user service.", userId);
        
        String query = """
            query GetById($id: String!) {
                getById(id: $id) {
                    id
                    name
                    age
                    createdAt
                }
            }
            """;
        
        return client.document(query)
            .variable("id", userId)
            .retrieve("getById")
            .toEntity(UserDTO.class)
            .block();
    }

}
