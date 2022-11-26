package com.topcom.intime.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class ReadyPattern {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false, length=20)
	private String name;
	
	@Column(nullable = false)
	private int time;
	
	@Column
	private Integer orderInSchedule;
	
	private Integer userId;
	
	@ManyToOne
	@JoinColumn(name = "scheduleId")
	private Schedule schedule;
	
	@ColumnDefault(" 'false' ")
	private String isInGroup;
	
	
}
