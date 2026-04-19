package com.hientranc2.socialapi.service;

import com.hientranc2.socialapi.dto.PostResponseDTO;
import com.hientranc2.socialapi.model.Post;
import com.hientranc2.socialapi.model.User;
import com.hientranc2.socialapi.repository.CommentRepository;
import com.hientranc2.socialapi.repository.PostRepository;
import com.hientranc2.socialapi.repository.ReactionRepository;
import com.hientranc2.socialapi.repository.ShareRepository;
import com.hientranc2.socialapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    // Khai báo 3 anh bảo vệ để đi đếm số lượng tương tác
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final ShareRepository shareRepository;

    // 1. Hàm tạo bài viết mới
    public Post createPost(String username, String content, String mediaUrl) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
                
        Post newPost = Post.builder()
                .user(user)
                .content(content)
                .mediaUrl(mediaUrl)
                .build();
                
        return postRepository.save(newPost);
    }

    // 2. Hàm lấy bảng tin (Newsfeed) có Phân trang và đóng gói DTO
    public List<PostResponseDTO> getNewsFeed(int page, int size) {
        
        // Tạo đối tượng Pageable để quy định: Lấy trang số mấy? Mỗi trang bao nhiêu bài?
        Pageable pageable = PageRequest.of(page, size);

        // Xuống Database lấy ra đúng 1 Trang (Page) chứa các bài viết
        Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        // Duyệt qua từng bài viết thô, biến nó thành chiếc hộp DTO xịn xò
        return postPage.stream().map(post -> {
            return PostResponseDTO.builder()
                    .id(post.getId())
                    .authorName(post.getUser().getFullName())
                    .content(post.getContent())
                    .mediaUrl(post.getMediaUrl())
                    .createdAt(post.getCreatedAt())
                    // Gọi các Repository đi đếm số tương tác cho bài viết này
                    .totalReactions(reactionRepository.countByPostId(post.getId()))
                    .totalComments(commentRepository.countByPostId(post.getId()))
                    .totalShares(shareRepository.countByPostId(post.getId()))
                    .build();
        }).collect(Collectors.toList());
    }
}