package com.example.meeting.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * センサから受け取った生データのEntityクラス
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TR_ROW_SENSOR_DATA")
public class RowSensorData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	protected String id; // データ受信時に自動で振られるID
	/*
	 * センサーID
	 */
	protected String sensorId;// どのセンサーの情報かを示す情報
	protected int sensorValue;// 受け取った値
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone="JST")
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
	protected Date receivedDate; // データを受け取った日時を記録

	// INSERT INTO TR_ROW_SENSOR_DATA (SENSOR_DATA_ID, SENSOR_ID,
	// SENSOR_VALUE,RECEIVED_DATE)
	// VALUES (1,'SEKJ-DKSDJ-JKLJL1',43, '2016-02-18T03:00:00+09:00');
	// SENSOR_DATA_ID, SENSOR_ID, SENSOR_VALUE,RECEIVED_DATE
	
	public String getClassName(int threthold){
		if(isDataHigh(threthold)==true){
			return "record-attend";
		}else{
			return "record-unattend";
		}
	}
	
	public boolean isDataHigh(int threthold){
		int value = getSensorValue();
		
		if(threthold < value){
			return true;
		}else{
			return false;
		}
	}
	
}

//
// INSERT INTO MST_SENSOR_MASTER (SENSOR_ID, SENSOR_NAME, DESCRIPTION )
// VALUES ('SEKJ-DKSDJ-JKLJL3','会議室C','センサ3です');
//
// --センサトランザクション
// INSERT INTO TR_SENSOR_DATA (SENSOR_DATA_ID, SENSOR_ID,
// SENSOR_VALUE,RECEIVED_DATE)
// VALUES (1,'SEKJ-DKSDJ-JKLJL1',43, '2016-02-18T03:00:00+09:00');