package com.king.service.gdb.bean;

import java.util.List;

public class Star {

	private int id;
	private String name;
	
	private List<Record> recordList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Record> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}
}
