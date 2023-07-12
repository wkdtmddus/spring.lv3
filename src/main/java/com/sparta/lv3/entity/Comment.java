package com.sparta.lv3.entity;

import com.sparta.lv3.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(Post post, CommentRequestDto requestDto, String username) {
        this.post = post;
        this.username = username;
        this.comment = requestDto.getComment();
    }


    public void update(CommentRequestDto requestDto, String username) {
        this.comment = requestDto.getComment();
        this.username = username;
    }
}
