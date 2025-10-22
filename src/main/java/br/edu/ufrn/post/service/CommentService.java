package br.edu.ufrn.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.post.model.Comment;
import br.edu.ufrn.post.record.CommentDTO;
import br.edu.ufrn.post.record.CreateCommentDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.repository.CommentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Flux<CommentDTO> getAllByPostId(String postId) {
        return commentRepository.getAllByPostId(postId)
            .map(
                comment -> new CommentDTO(
                    comment.getId(),
                    comment.getContent(),
                    new UserDTO(
                        comment.getUserId(),
                        null,
                        null
                    ),
                    comment.getCreatedAt()
                )
            );
    }
    
    public Mono<CommentDTO> save(String postId, CreateCommentDTO createCommentDTO) {
        Comment commentModel = new Comment(
            createCommentDTO.content(),
            postId,
            createCommentDTO.userId()
        );

        return commentRepository.save(commentModel)
            .map(
                comment -> new CommentDTO(
                    comment.getId(),
                    comment.getContent(),
                    new UserDTO(
                        comment.getUserId(),
                        null,
                        null
                    ),
                    comment.getCreatedAt()
                )
            );
    }
    
    public Mono<Void> delete(String id) {
        return commentRepository.deleteById(id);
    }

}
