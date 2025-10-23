package br.edu.ufrn.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufrn.post.client.UserRestAPIClient;
import br.edu.ufrn.post.record.CommentDTO;
import br.edu.ufrn.post.record.CreateCommentDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.service.CommentService;

@RestController
@RequestMapping("/posts/{post_id}/comments")
public class CommentRestAPIController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRestAPIClient userClient;

    @GetMapping
    public List<CommentDTO> getAllByPostId(@PathVariable("post_id") String postId) {
        return commentService.getAllByPostId(postId)
            .stream()
            .map(this::enrichUser)
            .toList();
    }

    @PostMapping
    public CommentDTO save(
        @PathVariable("post_id") String postId,
        @RequestBody CreateCommentDTO createCommentDTO
    ) {
        return enrichUser(commentService.save(postId, createCommentDTO));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        commentService.delete(id);
    }

    public CommentDTO enrichUser(CommentDTO comment) {
        UserDTO user = userClient.getById(comment.user().id());
        return new CommentDTO(
            comment.id(),
            comment.content(),
            user,
            comment.createdAt());
    }

}
