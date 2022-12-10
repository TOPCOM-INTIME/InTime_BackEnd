package com.topcom.intime.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //User 클래스가 MySQL에 자동으로 테이블이 생성된다.
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //넘버링 전략 : 연결한 DBMS의 정책에 따름
	private int id;
	
	@Column(nullable=false, length=30, unique = true)
	private String email;
	
	@Column(nullable=false, length=100)
	private String password;
	
	@Column(unique = true)
	private String username;

	@Column
	private String roles; // USER , ADMIN
	
	private String oauth; //kakao or google
	
	@CreationTimestamp //시간이 자동으로 입력됨
	private Timestamp createDate;
	
	public List<String> getRoleList() {
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}
	
	private String deviceToken;
	private Integer lateCount;
}
