package br.edu.ufrn.post.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufrn.post.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> getAllByPostId(String postId);
    
}
