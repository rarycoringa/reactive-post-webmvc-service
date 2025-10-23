package br.edu.ufrn.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.post.model.Comment;
import br.edu.ufrn.post.record.CommentDTO;
import br.edu.ufrn.post.record.CreateCommentDTO;
import br.edu.ufrn.post.record.UserDTO;
import br.edu.ufrn.post.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentDTO> getAllByPostId(String postId) {
        return commentRepository.getAllByPostId(postId)
            .stream()
            .map(comment -> new CommentDTO(
                comment.getId(),
                comment.getContent(),
                new UserDTO(comment.getUserId(), null, null),
                comment.getCreatedAt()))
            .toList();
    }
    
    public CommentDTO save(String postId, CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment(
            createCommentDTO.content(),
            postId,
            createCommentDTO.userId());

        Comment savedComment = commentRepository.save(comment);
        
        return new CommentDTO(
            savedComment.getId(),
            savedComment.getContent(),
            new UserDTO(savedComment.getUserId(), null, null),
            savedComment.getCreatedAt());
    }
    
    public void delete(String id) {
        commentRepository.deleteById(id);
    }

}
