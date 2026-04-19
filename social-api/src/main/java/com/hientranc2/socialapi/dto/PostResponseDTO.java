package com.hientranc2.socialapi.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PostResponseDTO {
    private UUID id;
    private String authorName;   // Chỉ lấy tên người đăng, giấu hoàn toàn id và password
    private String content;
    private String mediaUrl;
    private LocalDateTime createdAt;
    
    // Phần quan trọng nhất: Thống kê tương tác
    private int totalReactions;
    private int totalComments;
    private int totalShares;
}