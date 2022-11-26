package com.topcom.intime.repository;

import com.topcom.intime.model.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Integer> {
    List<Friends> findAllByUserId(int useridx);
    List<Friends> findAllByFriendId(int friendIdx);
    Optional<Friends> findByFriendIdAndUserId(int friendId, int useridx);
    Boolean existsByFriendIdAndUserId(int friendId, int useridx);
}
