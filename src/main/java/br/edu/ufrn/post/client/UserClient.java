package br.edu.ufrn.post.client;

import br.edu.ufrn.post.record.UserDTO;

public interface UserClient {
    public UserDTO getById(String userId);
}
