package com.example.meeting.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.meeting.domain.DisplayConditionCriteria;
import com.example.meeting.domain.MonitoringPoint;
import com.example.meeting.domain.RowSensorData;
import com.example.meeting.domain.UsageHistory;
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
public class MeetingRoomService {
	private final int DIFFSEC_THRESHOLD = 300;
	private final int LIGHT_SENSOR_THRESHOLD = 500;
	
	@Autowired
	RowSensorDataRepository rowSensorDataRepository;
	@Autowired
	MonitoringPointRepository monitoringPointRepository;

	/**
	 * 表示範囲を受け取り、それに合致したデータを元に表示用のJSONを作るためのクラスに変換する。 変換する単位は、どこかで指定できるようにする。
	 * デフォルト値は… ５分間状態が変化しなかった場合は最後のタイミングで打ち切る。 ただし、今から１０分以内のデータについては、秒単位で更新する。
	 * 
	 * @param displayDateFrom
	 *            表示開始年月日
	 * @param displayDateTo
	 *            表示終了年月日
	 * 
	 * @return
	 */
	public List<UsageHistory> getDisplayData(DisplayConditionCriteria dispcriteria) {

		// データを検索
		/*
		 * レポジトリを呼び出し下記の処理 1.DB検索（渡した日付の範囲のデータを取得） 2.サマリ処理
		 * データを日付順に並べて５分以内に連続したデータがあるかぎり取得し続ける 3.出力処理
		 * 次のデータがなければ、処理終了して処理開始したデータの開始時間とし、 最後データの日付を終了時間とする。 繰り返し
		 */
		log.info("見つかったMonitoringPoint:" + monitoringPointRepository.count());
		
		
		log.info("見つかったRowData=" + rowSensorDataRepository.count());

		// TODO:条件をつける。
		
		//List<RowSensorData> rsd = rowSensorDataRepository.findRowSensordataBySensorId("SEKJ-DKSDJ-JKLJL31");
		
		//まずここでセンサの情報を取得
		List<RowSensorData> rsdList = rowSensorDataRepository.findRowSensordataByDateRange(new Date("2011/05/01"),new Date("2018/05/01"));
		
		//取得したデータをマージして、データを作成する
		List<UsageHistory> dispData = generateDisplayData(rsdList);
		
		return dispData;
	}

	/**
	 * リポジトリから受信したデータを加工し、表示データを作成する
	 * @param rowSensorData リポジトリから受信したデータ
	 * @return 
	 */
	private List<UsageHistory> generateDisplayData(List<RowSensorData> rsdList){
		log.info("受信したデータの数___//"
				+ "" + rsdList.size());
		
		Iterator iterator = rsdList.iterator();
		List<UsageHistory> uhList = new ArrayList<>();
		
		RowSensorData rsd = null;
		while(iterator.hasNext()) {
			//ここでデータのIDを元にセンサーの種類を判断する必要あり。（今は決め打ち）
			if(rsd==null){
				rsd = (RowSensorData)iterator.next(); //read next only for the first time.
			}
			//ここでLaneに応じて閾値を設定する
			int highThreshold;
			if(rsd.getSensorId().equals("5")){ //人感センサ
				highThreshold = 1;
			}else{ //その他
				highThreshold = 500;
			}
			
			RowSensorData rsd_next=null;
			Date currnetDate =rsd.getReceivedDate();
			Date endDate = null;
			int classDiffCount =0;
			//log.info(rsd.toString());
			
			//次のデータを参照
			while(iterator.hasNext()){
				rsd_next=(RowSensorData)iterator.next();
				
				//日付を比較し、規定値）いないかどうかを判断
				int diffSec = differenceSec(currnetDate,rsd_next.getReceivedDate());
				//log.info(diffSec + " was diff sec value");
				if((diffSec < DIFFSEC_THRESHOLD&&
						rsd.getSensorId().equals(rsd_next.getSensorId())==true)&&
						classDiffCount<=3){//規定値以内、かつ、クラス、レーンが同じなら
					//when the class is different, count the value up
					if(rsd.getClassName(highThreshold)!=rsd_next.getClassName(highThreshold)){
						classDiffCount++;
					}else{
						classDiffCount = 0;
					}
							
					//次を読む
					currnetDate = rsd_next.getReceivedDate();
					endDate = rsd_next.getReceivedDate();
				}else{
				//out of threshold or different lane
					//set the value to rsd
					
					
					break;
				}
			}
			
			
			UsageHistory uhistory = new UsageHistory();
			uhistory.setId(rsd.getId());
			uhistory.setLaneId(rsd.getSensorId());
			
			uhistory.setCssClass(rsd.getClassName(highThreshold)); //record-unattend
			uhistory.setStartDate(rsd.getReceivedDate());
			//Date d = rsd.getReceivedDate();
			
			//ここで受信日に一時間足したデータをセットするようにする。
			//直接変更すると、DBの値も変更されてしまうので注意！
//	        Calendar cal = Calendar.getInstance();
//	        cal.setTime(d); // 現在時刻を設定
//	        cal.add(Calendar.MINUTE, 1);
	        
			uhistory.setEndDate(endDate);
			uhistory.setDescription("説明です.....");
			
			//作成したデータを追加
			uhList.add(uhistory);
			
			rsd = rsd_next;
		}
		
		return uhList;
	}
	
	/**
	 * 監視ポイントの一覧を取得
	 * オーナ情報とか、権限コントロールとかは条件として渡す必要が有るかもしれない。将来的に。
	 * 最初は、すべての情報を固定的に表示する仕様とする。
	 * @return
	 */
	public List<MonitoringPoint> getMonitorintPoints() {
		log.info("見つかったMonitoringPoint:" + monitoringPointRepository.count());
		
		List<MonitoringPoint> mtp = monitoringPointRepository.findAll();
		return mtp;
	}
	

	// /**
	// * １秒以内に処理が返ってこなかったらタイムアウト
	// */
	// @Transactional(timeout=1)
	// public List<Content> findAllTransactional(){
	// return contentRepository.findAll();
	// }
	
	/**
	 * 2つの日付の差を求めます。
	 * java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 * 
	 * 計算方法は以下となります。
	 * 1.最初に2つの日付を long 値に変換します。
	 * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
	 * 　経過ミリ秒数となります。
	 * 2.次にその差を求めます。
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 　日付の差を求めることができます。
	 * 　※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 * 
	 * @param date1    日付 java.util.Date
	 * @param date2    日付 java.util.Date
	 * @return    2つの差分（秒）
	 */
	public static int differenceSec(Date date1,Date date2) {
	    long datetime1 = date1.getTime();
	    long datetime2 = date2.getTime();
	    long diffSec = (datetime2 - datetime1) / 1000;
	    return (int)diffSec; 
	}


}
