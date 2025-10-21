package br.edu.ufrn.post.service;

import org.springframework.stereotype.Service;

import br.edu.ufrn.post.client.UserRestAPIClient;

@Service
public class PostRestAPIService extends PostService {

    public PostRestAPIService(UserRestAPIClient client) {
        super(client);
    }

}
