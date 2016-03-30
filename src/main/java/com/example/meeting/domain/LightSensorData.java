package com.example.meeting.domain;

public class LightSensorData extends RowSensorData{
	public boolean isDataHigh(){
		
		int value = getSensorValue();
		
		if(value < 500){
			return true;
		}else{
			return false;
		}
	}
	
	public String getClassName(){
		if(isDataHigh()==true){
			return "record-attend";
		}else{
			return "record-unattend";
		}
	}
}
