package com.sparta.lv2.controller;

import com.sparta.lv2.dto.CommentRequestDto;
import com.sparta.lv2.dto.CommentResponseDto;
import com.sparta.lv2.jwt.JwtUtil;
import com.sparta.lv2.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/{postId}")
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    // 댓글 작성 API
    @PostMapping("/comment")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization"));

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfo(token);
            return commentService.createComment(postId, requestDto, username);
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    // 댓글 수정 API
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization"));

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfo(token); // jwt에서 username 가져오기
            return commentService.updateComment(requestDto, username, postId, id);
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    // 댓글 삭제 API
    @DeleteMapping("/comment/{id}")
    ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long id, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization"));

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfo(token); // jwt에서 username 가져오기
            return commentService.deleteComment(username, postId, id);
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }
}
