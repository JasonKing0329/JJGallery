package com.jing.app.jjgallery.bean.http;

public class AppCheckBean {
	private boolean isAppUpdate;
	private String appVersion;
	private String appName;
	private long appSize;
	private boolean isGdbDatabaseUpdate;
	private String gdbDabaseVersion;
	private String gdbDabaseName;
	private String gdbDabaseSize;
    public long getAppSize() {
		return appSize;
	}

	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	public String getGdbDabaseSize() {
		return gdbDabaseSize;
	}

	public void setGdbDabaseSize(String gdbDabaseSize) {
		this.gdbDabaseSize = gdbDabaseSize;
	}

	public boolean isAppUpdate() {
		return isAppUpdate;
	}

	public void setAppUpdate(boolean isAppUpdate) {
		this.isAppUpdate = isAppUpdate;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getGdbDabaseVersion() {
		return gdbDabaseVersion;
	}

	public void setGdbDabaseVersion(String gdbDabaseVersion) {
		this.gdbDabaseVersion = gdbDabaseVersion;
	}

	public boolean isGdbDatabaseUpdate() {
		return isGdbDatabaseUpdate;
	}

	public void setGdbDatabaseUpdate(boolean isGdbDatabaseUpdate) {
		this.isGdbDatabaseUpdate = isGdbDatabaseUpdate;
	}

	public String getGdbDabaseName() {
		return gdbDabaseName;
	}

	public void setGdbDabaseName(String gdbDabaseName) {
		this.gdbDabaseName = gdbDabaseName;
	}

}
