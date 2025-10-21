package br.edu.ufrn.post.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.edu.ufrn.post.record.UserDTO;
import reactor.core.publisher.Mono;

@Component
@Qualifier("userRestAPIClient")
public class UserRestAPIClient implements UserClient {
    
    private final WebClient client;

    public UserRestAPIClient(
        @Value("${user.restapi.base-url}") String baseUrl
    ) {
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Mono<UserDTO> getById(String userId) {
        return client.get()
            .uri("/{id}", userId)
            .retrieve()
            .bodyToMono(UserDTO.class);
    }
}
