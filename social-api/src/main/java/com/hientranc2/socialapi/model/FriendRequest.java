package com.hientranc2.socialapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friend_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Người gửi lời mời

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // Người nhận lời mời

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status; // PENDING, ACCEPTED, REJECTED

    @CreationTimestamp
    private LocalDateTime createdAt;
}