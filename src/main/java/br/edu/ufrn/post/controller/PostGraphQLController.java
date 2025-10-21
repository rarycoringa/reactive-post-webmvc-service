package br.edu.ufrn.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.service.PostGraphQLService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class PostGraphQLController {

    @Autowired
    private PostGraphQLService postService;

    @QueryMapping
    public Flux<PostDTO> getAll() {
        return postService.getAll();
    }

    @QueryMapping
    public Flux<PostDTO> getAllByUserId(@Argument String userId){
        return postService.getAllByUserId(userId);
    }
    
    @QueryMapping
    public Mono<PostDTO> getById(@Argument String id) {
        return postService.getById(id);
    }
    
    @MutationMapping
    public Mono<PostDTO> save(@Argument("createPostInput") CreatePostDTO createPostDTO) {
        return postService.save(createPostDTO);
    }
    
    @MutationMapping
    public Mono<Void> delete(@Argument String id) {
        return postService.delete(id);
    }

    @SchemaMapping(typeName = "Post", field = "user")
    public Mono<UserDTO> enrichUser(PostDTO post) {
        return postService.getUserById(post.user().id())
            .defaultIfEmpty(post.user());
    }

}
