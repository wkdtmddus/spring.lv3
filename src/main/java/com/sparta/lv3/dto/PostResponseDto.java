package com.sparta.lv3.dto;

import com.sparta.lv3.entity.Comment;
import com.sparta.lv3.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponseDto> commentList = new ArrayList<>();


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.commentList = (post.getCommentList() == null) ? null : post.getCommentList()
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(CommentResponseDto::new)
                .toList();
    }
}