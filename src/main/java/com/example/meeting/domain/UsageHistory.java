package com.example.meeting.domain;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
 * 画面表示する際のJSONの元になるクラス。
 * RowSensorDataクラスを元にこのクラスに変換をする。
 * テーブルに保管されているセンサ情報からこのクラスを生成する。
 * まとめる時間単位（秒・分）については、検討の必要あり。
 * 
 * 一つの案としては下記の通り
 *  最初のONから５分以内にONを受け取った場合は、データをくっつける。
 * 
 * 処理速度によっては、このデータを一度テーブルに保管する必要が有るかもしれない。
 */
@Data
public class UsageHistory {
	//画面内で重複しなければ良いから、先頭レコードのIDをそのまま流用する。
	@Id
	@JsonProperty("id")
	private String id;
	//表示する列ID。
	@JsonProperty("lane")
	private String laneId;
	//出力の色。ステータスで変更する
	@JsonProperty("class")
	private String cssClass;
	//開始時間
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone="JST")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss+09:00",timezone="JST")
	@JsonProperty("start")
	private Date startDate; 
	//終了時間 //00+09:00
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss+09:00",timezone="JST")
	@JsonProperty("end")
	private Date endDate; 
	//説明
	@JsonProperty("desc")
	private String description;
	
//	@Id
//	private String laneId;
//	private String laneName;
//	private String status;
//	private LocalDateTime since; 
//	private String description;
	
}

//
//INSERT INTO MST_SENSOR_MASTER (SENSOR_ID, SENSOR_NAME, DESCRIPTION ) 
//VALUES ('SEKJ-DKSDJ-JKLJL3','会議室C','センサ3です');
//
//--センサトランザクション
//INSERT INTO TR_SENSOR_DATA (SENSOR_DATA_ID, SENSOR_ID, SENSOR_VALUE,RECEIVED_DATE) 
//VALUES (1,'SEKJ-DKSDJ-JKLJL1',43, '2016-02-18T03:00:00+09:00');