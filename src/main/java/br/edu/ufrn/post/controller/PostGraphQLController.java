package br.edu.ufrn.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import br.edu.ufrn.post.client.UserGraphQLClient;
import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.service.PostService;

@Controller
public class PostGraphQLController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserGraphQLClient userClient;

    @QueryMapping
    public List<PostDTO> getAll() {
        return postService.getAll();
    }

    @QueryMapping
    public List<PostDTO> getAllByUserId(@Argument String userId){
        return postService.getAllByUserId(userId);
    }
    
    @QueryMapping
    public PostDTO getById(@Argument String id) {
        return postService.getById(id);
    }
    
    @MutationMapping
    public PostDTO save(@Argument("createPostInput") CreatePostDTO createPostDTO) {
        return postService.save(createPostDTO);
    }
    
    @MutationMapping
    public void delete(@Argument String id) {
        postService.delete(id);
    }

    @SchemaMapping(typeName = "Post", field = "user")
    public UserDTO enrichUser(PostDTO post) {
        return userClient.getById(post.user().id());
    }

}
