package com.henugao.taskmanager.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程的相关信息
 * @author henugao
 *
 */
public class TaskInfo {
	
	//进程对应的应用的图标，但不是每一个进程都有图标，没有的话需要提供一个默认的图标
	private Drawable icon;
	
	//进程对应的包名
	private String packageName;
	
	//进程对应的应用名
	private String appName;
	
	//是否是用户进程
	private boolean isUserApp;
	
	//该应用所占内存的大小
	private int appMemSize;
	
	//判断当前的item条目是否被勾选上，
	private boolean checked;
	

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public long getAppMemSize() {
		return appMemSize;
	}

	public void setAppMemSize(int appMemSize) {
		this.appMemSize = appMemSize;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	
	
	
	

}
