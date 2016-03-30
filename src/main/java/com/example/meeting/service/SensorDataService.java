package com.example.meeting.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.meeting.domain.RowSensorData;
import com.example.meeting.repository.MonitoringPointRepository;
import com.example.meeting.repository.RowSensorDataRepository;

/**
 * 
 * @author nagase
 */
// サービスクラスであることを示す。Componentと意味は変わらない
@Service
@Transactional
@Slf4j
public class SensorDataService {
	@Autowired
	RowSensorDataRepository rowSensorDataRepository;
	@Autowired
	MonitoringPointRepository monitoringPointRepository;

	/**
	 * 
	 * @return
	 */
	public RowSensorData saveSensorData(RowSensorData rSensorData) {
		
		//登録しようとしているデータのLaneIDが存在するかどうかをチェック
		
		
		//リポジトリを呼んで保存
		return rowSensorDataRepository.save(rSensorData);
		
	//	return 0;
	}

	
	
}
