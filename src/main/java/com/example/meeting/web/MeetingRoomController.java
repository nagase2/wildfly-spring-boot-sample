package com.example.meeting.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.meeting.domain.DisplayConditionCriteria;
import com.example.meeting.domain.Lanes;
import com.example.meeting.domain.MonitoringPoint;
import com.example.meeting.domain.UsageHistory;
import com.example.meeting.service.MeetingRoomService;

@RestController
@RequestMapping("meeting")
@Slf4j
public class MeetingRoomController {

	@Autowired
	private MeetingRoomService meetingRoomService;

	/**
	 * TODO:ここで、会議室利用実績を解析した結果をRESTで返す。
	 * ログインは、M2xと同じようにKeyを渡すことで回避できるようにしたい。（最初はなくても良い）
	 * 
	 * @param model
	 * @param contentForm
	 * @return
	 */
	@RequestMapping("/list")
	Map<String, List> showContentList(Model model, DisplayConditionCriteria dispCriteria) {
		log.info("Meeting Listがよばれました");
		// TODO:ここでタイマースタート
		long start = System.currentTimeMillis();
		
		
		//----------ここから下はサンプルプログラム-------
		// 会議室の一覧を作成
		List<Lanes> clist1 = new ArrayList<Lanes>();
		Lanes cf1 = new Lanes();
		cf1.setLaneId("sdf-sdf-sd-fdf");
		cf1.setLaneName("lane name");
		clist1.add(cf1);
		
		List<MonitoringPoint> monitoringPointList = meetingRoomService.getMonitorintPoints();

		
//		// 実績情報の解析結果を作成
//		List<UsageHistory> clist2 = new ArrayList<UsageHistory>();
//		UsageHistory cf2 = new UsageHistory();
//		cf2.setLaneId("asd--sdfd-sf");
//		cf2.setCssClass("record");
//		clist2.add(cf2);
		
		List<UsageHistory> rSensorDataList = meetingRoomService.getDisplayData(dispCriteria);
		log.info("受信データ："+rSensorDataList.size());

		// Mapそれぞれ作成したデータを入れて返す。
		HashMap<String, List> map = new HashMap<String, List>();
		map.put("lanes", monitoringPointList);
		map.put("items", rSensorDataList);

		// TODO:ここでタイマー終了＆結果を表示するようにしたい。
		long stop = System.currentTimeMillis();
		System.out.println("件数は"+rSensorDataList.size()+"件　　実行にかかった時間は " + (stop - start) + " ミリ秒です。");

		return map;
		// return "/customers/nagase.html";
	}
	
	/**
	 * センサからこのメソッドを呼び出すことでセンサーデータを登録する。
	 * @param value センサで読み取った値
	 * @param date 読み取った時間。nullまたは値がない場合は、受信した時間を代わりに利用する。
	 * devices/33d8824f9deee514852ee77258d74b42/streams/PIR_sensor/value
	 * curl -i -X PUT http://localhost:7776/devices/dddddddddd/PIR_sensor/value -H "X-KEY: xxxxxxxxxx" -H "Content-Type: application/json" -d "{ \"value\": \"40\" }"
	 */

}
