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
import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.service.PostService;
import jakarta.ws.rs.QueryParam;

@RestController
@RequestMapping("/posts")
public class PostRestAPIController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRestAPIClient userClient;

    @GetMapping
    public List<PostDTO> getAll() {
        return postService.getAll()
            .stream()
            .map(this::enrichUser)
            .toList();
    }

    @GetMapping(params = "user_id")
    public List<PostDTO> getAllByUserId(@QueryParam("user_id") String userId) {
        return postService.getAllByUserId(userId)
            .stream()
            .map(this::enrichUser)
            .toList();
    }
    
    @GetMapping("/{id}")
    public PostDTO getById(@PathVariable String id) {
        return enrichUser(postService.getById(id));
    }
    
    @PostMapping
    public PostDTO save(@RequestBody CreatePostDTO createPostDTO) {
        return enrichUser(postService.save(createPostDTO));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        postService.delete(id);
    }

    public PostDTO enrichUser(PostDTO post) {
        UserDTO user = userClient.getById(post.user().id());

        return new PostDTO(
            post.id(),
            post.content(),
            user,
            post.createdAt());
    }

}
