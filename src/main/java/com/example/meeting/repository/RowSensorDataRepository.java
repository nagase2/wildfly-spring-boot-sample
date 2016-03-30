package com.example.meeting.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.meeting.domain.RowSensorData;

@Repository
@Transactional
public interface RowSensorDataRepository extends JpaRepository<RowSensorData, Long>{

	 /**
	   * 日付を指定して選択
	   * 値が登録されていない場合は規定の範囲（３ヶ月位？）に自動的に設定するようにする
	   * @return
	   */
	  @Query("select c from RowSensorData c where  :dateFrom <= c.receivedDate AND c.receivedDate < :dateTo "
	  		+ "order by c.sensorId, c.receivedDate")
	  List<RowSensorData> findRowSensordataByDateRange(
			  @Param("dateFrom") Date createdDateFrom,
	          @Param("dateTo") Date createdDateTo
			  );

	  //TODO:日付で並べたい
	  @Query("select c from RowSensorData c where c.sensorId = :rowSensorId ")
	  List<RowSensorData> findRowSensordataBySensorId(@Param("rowSensorId")String rRensorId);

	  
	  @Query("select c from RowSensorData c")
	  List<RowSensorData> findAllRowSensordata();
	  
	  

}
