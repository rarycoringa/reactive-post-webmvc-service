package br.edu.ufrn.post.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.edu.ufrn.post.record.UserDTO;
import reactor.core.publisher.Mono;

@Component
public class UserGraphQLClient implements UserClient {

    private final HttpGraphQlClient client;

    public UserGraphQLClient(
        WebClient.Builder builder,
        @Value("${user.graphql.base-url}") String baseUrl
    ) {
        this.client = HttpGraphQlClient.builder(builder.baseUrl(baseUrl)).build();
    }

    @Override
    public Mono<UserDTO> getById(String userId) {
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
            .toEntity(UserDTO.class);
    }
}
