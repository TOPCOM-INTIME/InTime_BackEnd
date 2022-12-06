package com.topcom.intime.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "schedules")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class SchedulePool {

//	@Id
//	@Column(name = "id")
//	private String id;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leaderId")
	private User leader;
	
//	
//	@JsonFormat(timezone = "Asia/Seoul")
//	private Timestamp time;
//	
//	@Column
//	private String name;
//	
//	@Column
//	private String destName;
	
	@OneToMany(mappedBy = "schedulePool", targetEntity = Schedule.class, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"schedulePool"})
	private List<Schedule> schedules;
	
//	@OneToOne(mappedBy = "schedulePool", targetEntity = SchedulePoolMembers.class, cascade = CascadeType.REMOVE)
//	@JsonIgnoreProperties({"schedulePool"})
//	private List<SchedulePoolMembers> members;
		
//	private String status;
	
	
}
