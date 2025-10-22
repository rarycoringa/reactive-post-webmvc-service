package br.edu.ufrn.post.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCommentDTO(
    String content,
    @JsonProperty("user_id") String userId
) {}
