package com.king.service.gdb.bean;

import java.util.ArrayList;
import java.util.List;

public class Star {

	private int id;
	private String name;
	private int recordNumber;
	private int beTop;
	private int beBottom;
	private float average;
	private int max;
	private int min;
	private float cAverage;
	private int cMax;
	private int cMin;
	
	private List<Record> recordList;
	private List<Integer> scoreList;
	private List<Integer> cScoreList;
	
	public Star() {
		scoreList = new ArrayList<>();
		cScoreList = new ArrayList<>();
		cMin = Integer.MAX_VALUE;
	}
	
	public List<Integer> getScoreList() {
		return scoreList;
	}

	public List<Integer> getcScoreList() {
		return cScoreList;
	}

	public void addScore(int score) {
		scoreList.add(score);
	}

	public void addcScore(int score) {
		cScoreList.add(score);
	}
	
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

	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public int getBeTop() {
		return beTop;
	}
	public void setBeTop(int beTop) {
		this.beTop = beTop;
	}
	public int getBeBottom() {
		return beBottom;
	}
	public void setBeBottom(int beBottom) {
		this.beBottom = beBottom;
	}
	public float getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public float getcAverage() {
		return cAverage;
	}
	public void setcAverage(float cAverage) {
		this.cAverage = cAverage;
	}
	public int getcMax() {
		return cMax;
	}
	public void setcMax(int cMax) {
		this.cMax = cMax;
	}
	public int getcMin() {
		return cMin;
	}
	public void setcMin(int cMin) {
		this.cMin = cMin;
	}
	
}
