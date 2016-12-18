package com.king.service.gdb.bean;

public abstract class RecordSingleScene extends Record {

	private String sceneName;
	private int scoreScene;
	private int scoreRim;
	private int scoreBJob;
	private int scoreForePlay;
	private int scoreRhythm;
	private int scoreCum;
	private int scoreCShow;
	private int scoreSpeicial;
	private String specialDesc;
	private int scoreFk;
	private int scoreNoCond;
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public int getScoreScene() {
		return scoreScene;
	}
	public void setScoreScene(int scoreScene) {
		this.scoreScene = scoreScene;
	}
	public int getScoreRim() {
		return scoreRim;
	}
	public void setScoreRim(int scoreRim) {
		this.scoreRim = scoreRim;
	}
	public int getScoreBJob() {
		return scoreBJob;
	}
	public void setScoreBJob(int scoreBJob) {
		this.scoreBJob = scoreBJob;
	}
	public int getScoreForePlay() {
		return scoreForePlay;
	}
	public void setScoreForePlay(int scoreForePlay) {
		this.scoreForePlay = scoreForePlay;
	}
	public int getScoreRhythm() {
		return scoreRhythm;
	}
	public void setScoreRhythm(int scoreRhythm) {
		this.scoreRhythm = scoreRhythm;
	}
	public int getScoreCum() {
		return scoreCum;
	}
	public void setScoreCum(int scoreCum) {
		this.scoreCum = scoreCum;
	}
	public int getScoreCShow() {
		return scoreCShow;
	}
	public void setScoreCShow(int scoreCShow) {
		this.scoreCShow = scoreCShow;
	}
	public int getScoreSpeicial() {
		return scoreSpeicial;
	}
	public void setScoreSpeicial(int scoreSpeicial) {
		this.scoreSpeicial = scoreSpeicial;
	}
	public int getScoreFk() {
		return scoreFk;
	}
	public void setScoreFk(int scoreFk) {
		this.scoreFk = scoreFk;
	}
	public int getScoreNoCond() {
		return scoreNoCond;
	}
	public void setScoreNoCond(int scoreNoCond) {
		this.scoreNoCond = scoreNoCond;
	}
	public String getSpecialDesc() {
		return specialDesc;
	}
	public void setSpecialDesc(String specialDesc) {
		this.specialDesc = specialDesc;
	}
	
}
