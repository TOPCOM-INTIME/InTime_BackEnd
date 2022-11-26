package com.topcom.intime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topcom.intime.model.User;


// @Repository 생략가능
public interface UserRepository extends JpaRepository<User, Integer>{

	//Select * From user Where username = 1?;
    Optional<User> findByEmail(String email); //query method
    Boolean existsByUsername(String username);
    List<User> findAllById(int useridx);
    Optional<User> findByUsername(String username);
//	public User findByEmail(String email);

}


//JPA Naming 전략
//Select * From user WHERE username = ? AND password= ?; <<인 함수가 자동으로 생성된다 (대문자 주의)
//User findByUsernameAndPassword(String username, String password);

//위 함수와 동일한 기능
//@Query(value="SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
//User login(String username, String password);