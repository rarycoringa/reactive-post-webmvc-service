package br.edu.ufrn.post.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufrn.post.model.Comment;
import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> getAllByPostId(String postId);
    
}
