package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topcom.intime.model.Forum;

public interface ForumRepository extends JpaRepository<Forum, Integer> {

	List<Forum> findAllBywriterId(int writerId);

}
