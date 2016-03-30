package com.example.meeting.web;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.meeting.domain.RowSensorData;
import com.example.meeting.service.SensorDataService;

@RestController
//@RequestMapping("data")
@Slf4j
public class SensorDataController {
	
	@Autowired
	SensorDataService sensorDataService;
	
	/**
	 * センサからこのメソッドを呼び出すことでセンサーデータを登録する。
	 * @param value センサで読み取った値
	 * @param date 読み取った時間。nullまたは値がない場合は、受信した時間を代わりに利用する。
	 * devices/33d8824f9deee514852ee77258d74b42/streams/PIR_sensor/value
	 * curl -i -X PUT http://localhost:7776/data/PIR_sensor/value -H "X-KEY: xxxxxxxxxx" -H "Content-Type: application/json" -d "{ \"value\": \"40\" }"
	 * http://localhost:7776/data/3/value/25
	 */
	//@RequestMapping("/list")
	//メソッド＝PUT
	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "data/{sensorId}/value/{value}")
	//URLの２つめを引数にする
	String registerSensorData(@PathVariable String sensorId,
			@PathVariable Integer value,
			HttpServletRequest request)
			//,@RequestParam(value = "ID", defaultValue = "") String id) 
			{
		
		long start = System.currentTimeMillis();
		
		//まずは受け取った値を確認する。
		log.info("sensorId is "+ sensorId +" value is "+ value);
		//String userAgent = request.getHeader("user-agent");
		//log.info("user agent in header is "+ userAgent);
		
		UUID uuid2 = UUID.randomUUID();
		
		RowSensorData rSensorData = new RowSensorData();
		rSensorData.setId(uuid2.toString()); //コレはオートインクリメントにする？
		rSensorData.setReceivedDate(new Date());
		rSensorData.setSensorId(sensorId);
		rSensorData.setSensorValue(value);
		//受け取った値をDBに保存（サービス呼ぶ）
		sensorDataService.saveSensorData(rSensorData);
		
		long stop = System.currentTimeMillis();
		System.out.println("データ登録１件にかかった時間は " + (stop - start) + " ミリ秒です。");

		return "done";
	}
}
