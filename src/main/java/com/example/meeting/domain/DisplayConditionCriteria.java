package com.example.meeting.domain;

import java.util.Date;

import lombok.Data;

/**
 * 会議室情報を表示する範囲を指定するクラス。
 * （デフォルトは３ヶ月位か？）
 * @author ac12955
 *
 */
@Data
public class DisplayConditionCriteria {
	//表示開始年月日
	Date displayDateFrom;
	//表示終了年月日
	Date displayDateTo;
	
	//監視ポイント名称（対象が多くなった時にLike検索できるようにしたほうがよいかも…最初はいらない？）
	String laneName;
	
}
