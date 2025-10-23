package br.edu.ufrn.post.controller;

import java.util.List;

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

@Controller
public class CommentGraphQLController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserGraphQLClient userClient;
    
    @MutationMapping
    public CommentDTO saveComment(
        @Argument("postId") String postId,
        @Argument("createCommentInput") CreateCommentDTO createCommentDTO
    ) {
        return commentService.save(postId, createCommentDTO);
    }
    
    @MutationMapping
    public void deleteComment(@Argument String id) {
        commentService.delete(id);
    }

    @SchemaMapping(typeName = "Post", field = "comments")
    public List<CommentDTO> comments(PostDTO post) {
        return commentService.getAllByPostId(post.id());
    }

    @SchemaMapping(typeName = "Comment", field = "user")
    public UserDTO enrichUser(CommentDTO comment) {
        return userClient.getById(comment.user().id());
    }

}
