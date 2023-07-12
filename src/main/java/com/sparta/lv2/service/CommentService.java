package com.sparta.lv2.service;

import com.sparta.lv2.dto.CommentRequestDto;
import com.sparta.lv2.dto.CommentResponseDto;
import com.sparta.lv2.entity.Comment;
import com.sparta.lv2.entity.Post;
import com.sparta.lv2.entity.User;
import com.sparta.lv2.entity.UserRoleEnum;
import com.sparta.lv2.repository.CommentRepository;
import com.sparta.lv2.repository.PostRepository;
import com.sparta.lv2.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // 댓글 작성 메서드
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username) {
        Post post = findPost(postId);

        Comment comment = new Comment(post, requestDto, username);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 수정 메서드
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, String username, Long postId, Long id) {
        Comment comment = findComment(postId, id);
        User user = findUser(username); // 사용자 권한 확인을 위해 User 객체 생성

        // 현재 로그인 된 사용자의 권한을 확인하여 ADMIN 권한을 가진 경우 댓글 수정
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(requestDto, username);

            return new CommentResponseDto(comment);
        }

        // 댓글의 작성자와 현재 로그인 한 사용자의 username을 비교
        if (!comment.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 댓글 수정
        comment.update(requestDto, username);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제 메서드
    public ResponseEntity<String> deleteComment(String username, Long postId, Long id) {
        Comment comment = findComment(postId, id);
        User user = findUser(username); // 현재 사용자의 ROLE을 확인하기 위해 User 객체 생성

        // 현재 로그인 된 사용자의 권한을 확인하여 ADMIN 권한을 가진 경우 게시글 삭제
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment);
            return ResponseEntity.ok("삭제 완료");
        }

        // 게시글 작성자와 로그인 된 사용자의 username을 비교
        if (!comment.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 게시글 삭제
        commentRepository.delete(comment);
        return ResponseEntity.ok("삭제 완료");
    }

    // 게시글 찾는 메서드
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
    }

    // 특정 게시글의 댓글 찾는 메서드
    private Comment findComment(Long postId, Long id) {
        return commentRepository.findByPostIdAndIdOrderByCreatedAtDesc(postId, id)
                .orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없습니다."));
    }

    // jwt에서 뽑은 username으로 User 객체 반환하는 메서드
    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
