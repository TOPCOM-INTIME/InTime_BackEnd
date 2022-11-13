package com.topcom.intime.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SchedulePool {

	@Id
	@Column(name = "id")
	private String id;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
//	
//	@Column
//	private Integer leaderId;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp time;
	
	@Column
	private String destName;
	
//	@ElementCollection(fetch = FetchType.LAZY)
//    private List<User> members;
	
	@OneToMany(mappedBy = "schedulePool", targetEntity = Schedule.class, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"schedulePool"})
	private List<Schedule> schedules;
	
	@OneToOne(mappedBy = "schedulePool", targetEntity = SchedulePoolMembers.class, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"schedulePool"})
	private List<SchedulePoolMembers> members;
		
	@ColumnDefault(" 'INVITING' ")//inviting/pre/ing
	private String status;
	
	
}