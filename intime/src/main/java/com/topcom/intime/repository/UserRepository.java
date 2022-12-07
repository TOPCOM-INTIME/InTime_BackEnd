package com.topcom.intime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.topcom.intime.model.User;

import io.lettuce.core.dynamic.annotation.Param;


// @Repository 생략가능
public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByEmail(String email); //query method
    Boolean existsByUsername(String username);
    List<User> findAllById(int useridx);
    Optional<User> findByUsername(String username);

    @Modifying
	@Query(value="SELECT * FROM User u WHERE u.roles = :type", nativeQuery = true)
	public List<User> mfindAllUsersByType(@Param("type")String type);

}