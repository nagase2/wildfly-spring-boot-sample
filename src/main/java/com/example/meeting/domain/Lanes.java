package com.example.meeting.domain;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


/**
 * レーンのJSONクラス用。
 * Entityも兼ねる。
 * 
 */
//@Entity
//@Table(name="MST_LANE")
@Data
public class Lanes {
	@Id
	private String laneId;
	//表示するセンサのID
	private String sensorId;
	private String laneName;
	private String status;
	private Date statusLastUpdateDate; 
	private String description;
	
}
