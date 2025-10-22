package br.edu.ufrn.post.record;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentDTO(
    String id,
    String content,
    UserDTO user,
    @JsonProperty("created_at") @JsonFormat(shape = JsonFormat.Shape.STRING) Instant createdAt
) {}
