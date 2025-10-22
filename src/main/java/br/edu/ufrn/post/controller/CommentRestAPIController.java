package br.edu.ufrn.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import br.edu.ufrn.post.service.CommentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts/{post_id}/comments")
public class CommentRestAPIController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRestAPIClient userClient;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommentDTO> getAllByPostId(@PathVariable("post_id") String postId) {
        return commentService.getAllByPostId(postId)
            .flatMap(this::enrichUser);
    }

    @PostMapping
    public Mono<CommentDTO> save(
        @PathVariable("post_id") String postId,
        @RequestBody CreateCommentDTO createCommentDTO
    ) {
        return commentService.save(postId, createCommentDTO)
            .flatMap(this::enrichUser);
    }
    
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return commentService.delete(id);
    }

    public Mono<CommentDTO> enrichUser(CommentDTO comment) {
        return userClient.getById(comment.user().id())
            .map(
                user -> new CommentDTO(
                    comment.id(),
                    comment.content(),
                    user,
                    comment.createdAt()
                )
            )
            .defaultIfEmpty(comment);
    }

}
