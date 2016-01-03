package com.henugao.taskmanager.domain;

import android.graphics.drawable.Drawable;

/**
 * ���̵������Ϣ
 * @author henugao
 *
 */
public class TaskInfo {
	
	//���̶�Ӧ��Ӧ�õ�ͼ�꣬������ÿһ�����̶���ͼ�꣬û�еĻ���Ҫ�ṩһ��Ĭ�ϵ�ͼ��
	private Drawable icon;
	
	//���̶�Ӧ�İ���
	private String packageName;
	
	//���̶�Ӧ��Ӧ����
	private String appName;
	
	//�Ƿ����û�����
	private boolean isUserApp;
	
	//��Ӧ����ռ�ڴ�Ĵ�С
	private int appMemSize;
	
	//�жϵ�ǰ��item��Ŀ�Ƿ񱻹�ѡ�ϣ�
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
