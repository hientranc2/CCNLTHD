package com.hientranc2.socialapi.controller;

import com.hientranc2.socialapi.model.FriendRequest;
import com.hientranc2.socialapi.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    // Gửi lời mời: POST /api/friends/request/{receiverId}
    @PostMapping("/request/{receiverId}")
    public ResponseEntity<String> sendRequest(Principal principal, @PathVariable UUID receiverId) {
        return ResponseEntity.ok(friendRequestService.sendRequest(principal.getName(), receiverId));
    }

    // Đồng ý: POST /api/friends/accept/{requestId}
    @PostMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptRequest(Principal principal, @PathVariable UUID requestId) {
        return ResponseEntity.ok(friendRequestService.acceptRequest(principal.getName(), requestId));
    }

    // Lấy danh sách chờ duyệt: GET /api/friends/pending
    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(Principal principal) {
        return ResponseEntity.ok(friendRequestService.getPendingRequests(principal.getName()));
    }
}