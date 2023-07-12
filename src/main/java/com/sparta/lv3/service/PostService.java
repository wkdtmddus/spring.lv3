package com.sparta.lv3.service;

import com.sparta.lv3.dto.PostRequestDto;
import com.sparta.lv3.dto.PostResponseDto;
import com.sparta.lv3.entity.Post;
import com.sparta.lv3.entity.User;
import com.sparta.lv3.entity.UserRoleEnum;
import com.sparta.lv3.repository.PostRepository;
import com.sparta.lv3.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, String username) {
        // RequestDto => entity
        Post post = new Post(requestDto, username);

        // entity => save()
        postRepository.save(post);

        // entity => responseDto
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    // 전체 게시글 조회
    public List<PostResponseDto> getAllposts() {
        List<PostResponseDto> allPost = postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(PostResponseDto::new).toList();
        return allPost;
    }

    // 선택한 게시글 조회
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto responseDto = new PostResponseDto(post);

        return responseDto;
    }

    // 게시글 수정 메서드
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, String username, Long id) {
        Post post = findPost(id); // 수정하고자 하는 게시글을 id 값으로 조회하여 Post 객체 생성

        User user = findUser(username); // 현재 사용자의 ROLE을 확인하기 위해 User 객체 생성

        // 현재 로그인 된 사용자의 권한을 확인하여 ADMIN 권한을 가진 경우 게시글 수정
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            post.update(requestDto);

            return new PostResponseDto(post);
        }
        // 게시글의 작성자와 현재 로그인 한 사용자의 username을 비교
        if (!post.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
        // 게시글 수정
        post.update(requestDto);
        return new PostResponseDto(post);
    }


    // 게시글 삭제 메서드
    public ResponseEntity<String> deletePost(String username, Long id) {
        Post post = findPost(id); // 삭제하고자 하는 id 값을 가진 Post 객체 생성

        User user = findUser(username); // 현재 사용자의 ROLE을 확인하기 위해 User 객체 생성

        // 현재 로그인 된 사용자의 권한을 확인하여 ADMIN 권한을 가진 경우 게시글 삭제
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            postRepository.delete(post);
            return ResponseEntity.ok("삭제 완료");
        }

        // 게시글 작성자와 로그인 된 사용자의 username을 비교
        if (!post.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 게시글 삭제
        postRepository.delete(post);
        return ResponseEntity.ok("삭제 완료");
    }

    // id값으로 게시글 찾는 메서드
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NullPointerException("게시글을 찾을 수 없습니다.")
        );
    }

    // 현재 로그인된 사용자 객체 반환하는 메서드
    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
