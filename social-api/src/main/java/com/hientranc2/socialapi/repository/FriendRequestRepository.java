package com.hientranc2.socialapi.repository;

import com.hientranc2.socialapi.model.FriendRequest;
import com.hientranc2.socialapi.model.FriendshipStatus;
import com.hientranc2.socialapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
    
    // Kiểm tra xem 2 người đã từng có tương tác gửi lời mời nào chưa (dù A gửi B hay B gửi A)
    @Query("SELECT f FROM FriendRequest f WHERE (f.sender = :user1 AND f.receiver = :user2) OR (f.sender = :user2 AND f.receiver = :user1)")
    Optional<FriendRequest> findFriendship(@Param("user1") User user1, @Param("user2") User user2);

    // Lấy danh sách những người "đang chờ mình duyệt"
    List<FriendRequest> findAllByReceiverAndStatus(User receiver, FriendshipStatus status);
}