package br.edu.ufrn.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.post.model.Post;
import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostDTO> getAll() {
        return postRepository.findAll()
            .stream()
            .map(post -> new PostDTO(
                post.getId(),
                post.getContent(),
                new UserDTO(post.getUserId(), null, null),
                post.getCreatedAt()))
            .toList();
    }

    public List<PostDTO> getAllByUserId(String userId) {
        return postRepository.findAllByUserId(userId)
            .stream()
            .map(post -> new PostDTO(
                post.getId(),
                post.getContent(),
                new UserDTO(post.getUserId(), null, null),
                post.getCreatedAt()))
            .toList();
    }
    
    public PostDTO getById(String id) {
        Post post = postRepository.findById(id).get();

        return new PostDTO(
            post.getId(),
            post.getContent(),
            new UserDTO(post.getUserId(), null, null),
            post.getCreatedAt());
    }
    
    public PostDTO save(CreatePostDTO createPostDTO) {
        Post post = new Post(
            createPostDTO.content(),
            createPostDTO.userId()
        );

        Post savedPost = postRepository.save(post);
        
        return new PostDTO(
            savedPost.getId(),
            savedPost.getContent(),
            new UserDTO(savedPost.getUserId(), null, null),
            savedPost.getCreatedAt());
    }
    
    public void delete(String id) {
        postRepository.deleteById(id);
    }

}
