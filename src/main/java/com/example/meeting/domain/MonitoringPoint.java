package com.example.meeting.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "MST_MONITORING_POINT") //TODO:名前を変更
public class MonitoringPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uuid; //UUIDで定義する
	//表示するセンサのID,または外部連携用。センサはこのIDを指定して送信してくる。（保管するときは、idに変換する）
	private String locationId;
	
	private String locationName;
	private String description;

}

