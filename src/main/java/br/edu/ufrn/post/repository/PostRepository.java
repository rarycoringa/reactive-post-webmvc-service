package br.edu.ufrn.post.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufrn.post.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> getAllByUserId(String userId);
    
}
