package br.edu.ufrn.post.service;

import org.springframework.stereotype.Service;

import br.edu.ufrn.post.client.UserGraphQLClient;

@Service
public class PostGraphQLService extends PostService {
    
    public PostGraphQLService(UserGraphQLClient client) {
        super(client);
    }

}
