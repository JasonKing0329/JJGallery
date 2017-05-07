package com.king.service.gdb.bean;

public abstract class Record {

	private int id;
	private int sequence;
	private Folder parent;
	private String directory;
	private String name;
	private String path;
	private int score;
	private int scoreBasic;
	private int scoreExtra;
	private int scoreFeel;
	private int scoreStory;
	private int HDLevel;
	private long lastModifyTime;
	private int deprecated;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public Folder getParent() {
		return parent;
	}
	public void setParent(Folder parent) {
		this.parent = parent;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScoreBasic() {
		return scoreBasic;
	}
	public void setScoreBasic(int scoreBasic) {
		this.scoreBasic = scoreBasic;
	}
	public int getScoreExtra() {
		return scoreExtra;
	}
	public void setScoreExtra(int scoreExtra) {
		this.scoreExtra = scoreExtra;
	}
	public int getScoreFeel() {
		return scoreFeel;
	}
	public void setScoreFeel(int scoreFeel) {
		this.scoreFeel = scoreFeel;
	}
	public int getScoreStory() {
		return scoreStory;
	}
	public void setScoreStory(int scoreStory) {
		this.scoreStory = scoreStory;
	}
	public int getHDLevel() {
		return HDLevel;
	}
	public void setHDLevel(int hDLevel) {
		HDLevel = hDLevel;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public int getDeprecated() {
		return deprecated;
	}
	public void setDeprecated(int deprecated) {
		this.deprecated = deprecated;
	}

}
