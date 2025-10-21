package br.edu.ufrn.post.client;

import br.edu.ufrn.post.record.UserDTO;
import reactor.core.publisher.Mono;

public interface UserClient {
    public Mono<UserDTO> getById(String userId);
}
