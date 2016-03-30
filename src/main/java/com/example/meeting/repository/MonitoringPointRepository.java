package com.example.meeting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.meeting.domain.MonitoringPoint;

@Repository
@Transactional
public interface MonitoringPointRepository extends JpaRepository<MonitoringPoint, Long>{

	 /**
	   * Like検索
	   * @return
	   */
	 // @Query("select c from RowSensorData c where c.sensorId like CONCAT('%',CONCAT(:n.sensorId, '%') and n =:rowSensorData)")
	 // List<RowSensorData> findRowSensordata(@Param("rowSensorData")RowSensorData rSensorData);

	  @Query("select c from MonitoringPoint c")
	  List<MonitoringPoint> findAlldata();

}
