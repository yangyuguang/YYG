package com.pami.bean;

import com.google.gson.Gson;

public class BaseBean {

	@Override
	public String toString() {
		return toJson();
	}
	
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
