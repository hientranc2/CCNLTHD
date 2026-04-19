package com.hientranc2.socialapi.repository;

import com.hientranc2.socialapi.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findByUserIdAndPostId(UUID userId, UUID postId);
    int countByPostId(UUID postId);
}