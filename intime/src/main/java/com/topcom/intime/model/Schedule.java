package com.topcom.intime.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "schedulePool")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@Column
	private String name;
	
	@Column
	private Integer time;
	
	@Column
	private String sourceName;
	
	@Column
	private String destName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedulePoolId")
	private SchedulePool schedulePool;
	
	@ColumnDefault(" 'PRE' ")//pre/ing/OFF
	private String status;
	
	@JsonFormat(timezone = "Asia/Seoul")
	@Column
	private Timestamp startTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	@Column
	private Timestamp readyTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	@Column
	private Timestamp endTime;

	@OneToMany(mappedBy = "schedule", targetEntity = ReadyPattern.class, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"schedule"})
	private List<ReadyPattern> ReadyPatterns;
}
