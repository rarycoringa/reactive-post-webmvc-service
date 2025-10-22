package br.edu.ufrn.post.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String content;

    private String userId;

    @Indexed
    private String postId;

    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdAt;

    public Comment(String content, String postId, String userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;

        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
