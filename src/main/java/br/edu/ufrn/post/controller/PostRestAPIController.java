package br.edu.ufrn.post.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufrn.post.client.UserClient;
import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.repository.PostRepository;
import br.edu.ufrn.post.service.PostService;
import jakarta.ws.rs.QueryParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PostRestAPIController {

    private final PostService postService;

    public PostRestAPIController(
        PostRepository repository,
        @Qualifier("userRestAPIClient") UserClient client
    ) {
        this.postService = new PostService(repository, client);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PostDTO> getAll() {
        return postService.getAll()
            .flatMap(this::enrichUser);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PostDTO> getAllByUserId(@QueryParam("user_id") String userId) {
        return postService.getAllByUserId(userId)
            .flatMap(this::enrichUser);
    }
    
    @GetMapping("/{id}")
    public Mono<PostDTO> getById(@PathVariable String id) {
        return postService.getById(id)
            .flatMap(this::enrichUser);
    }
    
    @PostMapping
    public Mono<PostDTO> save(@RequestBody CreatePostDTO createPostDTO) {
        return postService.save(createPostDTO)
            .flatMap(this::enrichUser);
    }
    
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return postService.delete(id);
    }

    public Mono<PostDTO> enrichUser(PostDTO post) {
        return postService.getUserById(post.user().id())
            .map(
                user -> new PostDTO(
                    post.id(),
                    post.content(),
                    user,
                    post.createdAt()
                )
            )
            .defaultIfEmpty(post);
    }

}
