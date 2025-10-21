package br.edu.ufrn.post.service;

import org.springframework.stereotype.Service;

import br.edu.ufrn.post.client.UserClient;
import br.edu.ufrn.post.model.Post;
import br.edu.ufrn.post.record.CreatePostDTO;
import br.edu.ufrn.post.record.PostDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.repository.PostRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserClient userClient;

    public PostService(
        PostRepository postRepository,
        UserClient userClient
    ) {
        this.postRepository = postRepository;
        this.userClient = userClient;
    }

    public Flux<PostDTO> getAll() {
        return postRepository.findAll()
            .map(
                post -> new PostDTO(
                    post.getId(),
                    post.getContent(),
                    new UserDTO(
                        post.getUserId(),
                        null,
                        null
                    ),
                    post.getCreatedAt()
                )
            );
    }

    public Flux<PostDTO> getAllByUserId(String userId) {
        return postRepository.getAllByUserId(userId)
            .map(
                post -> new PostDTO(
                    post.getId(),
                    post.getContent(),
                    new UserDTO(
                        post.getUserId(),
                        null,
                        null
                    ),
                    post.getCreatedAt()
                )
            );
    }
    
    public Mono<PostDTO> getById(String id) {
        return postRepository.findById(id)
            .map(
                post -> new PostDTO(
                    post.getId(),
                    post.getContent(),
                    new UserDTO(
                        post.getUserId(),
                        null,
                        null
                    ),
                    post.getCreatedAt()
                )
            );
    }
    
    public Mono<PostDTO> save(CreatePostDTO createPostDTO) {
        Post postModel = new Post(
            createPostDTO.content(),
            createPostDTO.userId()
        );

        return postRepository.save(postModel)
            .map(
                post -> new PostDTO(
                    post.getId(),
                    post.getContent(),
                    new UserDTO(
                        post.getUserId(),
                        null,
                        null
                    ),
                    post.getCreatedAt()
                )
            );
    }
    
    public Mono<Void> delete(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<UserDTO> getUserById(String userId) {
        return userClient.getById(userId);
    }
}
