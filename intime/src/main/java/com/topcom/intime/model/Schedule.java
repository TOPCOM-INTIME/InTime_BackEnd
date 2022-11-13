package com.topcom.intime.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@JsonFormat(timezone = "Asia/Seoul")
	@Column
	private Timestamp time;
	
	@Column
	private String sourceName;
	
	@Column
	private String sourceX;
	
	@Column
	private String sourceY;
	
	@Column
	private String destName;
	
	@Column
	private String destX;
	
	@Column 
	private String destY;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedulePoolId")
	private SchedulePool schedulePool;
	
	@ColumnDefault(" 'PRE' ")//pre/ing/OFF
	private String status;
	
	//on/off
	
}