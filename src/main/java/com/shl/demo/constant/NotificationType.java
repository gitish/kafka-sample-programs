package com.lloyds.demo.constant;

public enum NotificationType {
	EMAIL("email"),
	SMS("sms"),
	PUSH("push"),
	OTHER("other");
	String value;
	NotificationType(String value){
		this.value=value;
	}
	public static NotificationType getType(String value){
		for(NotificationType type:NotificationType.values()){
			if(type.getValue().equals(value)){
				return type;
			}
		}
		return null;
	}
	public String getValue(){
		return this.value;
	}
}
