package com.hientranc2.socialapi.service;

import com.hientranc2.socialapi.model.*;
import com.hientranc2.socialapi.repository.FriendRequestRepository;
import com.hientranc2.socialapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService; // Tiêm chip thông báo

    // 1. Gửi lời mời kết bạn
    @Transactional
    public String sendRequest(String senderUsername, UUID receiverId) {
        User sender = userRepository.findByUsername(senderUsername).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Không thể tự kết bạn với chính mình!");
        }

        Optional<FriendRequest> existingRequest = friendRequestRepository.findFriendship(sender, receiver);
        
        if (existingRequest.isPresent()) {
            FriendRequest request = existingRequest.get();
            if (request.getStatus() == FriendshipStatus.ACCEPTED) return "Hai người đã là bạn bè.";
            if (request.getStatus() == FriendshipStatus.PENDING) return "Lời mời đang chờ xử lý.";
        }

        FriendRequest newRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendshipStatus.PENDING)
                .build();
        friendRequestRepository.save(newRequest);

        // Bắn thông báo cho người nhận
        notificationService.createNotification(
                receiver, sender, NotificationType.FRIEND_REQUEST,
                sender.getId(), sender.getFullName() + " đã gửi cho bạn một lời mời kết bạn."
        );

        return "Đã gửi yêu cầu kết bạn!";
    }

    // 2. Chấp nhận lời mời
    @Transactional
    public String acceptRequest(String receiverUsername, UUID requestId) {
        User receiver = userRepository.findByUsername(receiverUsername).orElseThrow();
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời"));

        if (!request.getReceiver().getId().equals(receiver.getId())) {
            throw new RuntimeException("Bạn không có quyền duyệt lời mời này");
        }

        request.setStatus(FriendshipStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // Bắn thông báo ngược lại cho người đã gửi lời mời
        notificationService.createNotification(
                request.getSender(), receiver, NotificationType.FRIEND_ACCEPT,
                receiver.getId(), receiver.getFullName() + " đã chấp nhận lời mời kết bạn của bạn."
        );

        return "Đã trở thành bạn bè!";
    }

    // 3. Xem danh sách lời mời đang chờ duyệt
    public List<FriendRequest> getPendingRequests(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return friendRequestRepository.findAllByReceiverAndStatus(user, FriendshipStatus.PENDING);
    }
}