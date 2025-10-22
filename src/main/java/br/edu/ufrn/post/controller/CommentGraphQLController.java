package br.edu.ufrn.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import br.edu.ufrn.post.client.UserGraphQLClient;
import br.edu.ufrn.post.record.CommentDTO;
import br.edu.ufrn.post.record.CreateCommentDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.service.CommentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class CommentGraphQLController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserGraphQLClient userClient;
    
    @MutationMapping
    public Mono<CommentDTO> saveComment(
        @Argument("postId") String postId,
        @Argument("createCommentInput") CreateCommentDTO createCommentDTO
    ) {
        return commentService.save(postId, createCommentDTO);
    }
    
    @MutationMapping
    public Mono<Void> deleteComment(@Argument String id) {
        return commentService.delete(id);
    }

    @SchemaMapping(typeName = "Post", field = "comments")
    public Flux<CommentDTO> comments(PostDTO post) {
        return commentService.getAllByPostId(post.id());
    }

    @SchemaMapping(typeName = "Comment", field = "user")
    public Mono<UserDTO> enrichUser(CommentDTO comment) {
        return userClient.getById(comment.user().id())
            .defaultIfEmpty(comment.user());
    }

}
