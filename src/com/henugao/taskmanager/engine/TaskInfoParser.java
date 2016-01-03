package com.henugao.taskmanager.engine;

import java.util.ArrayList;
import java.util.List;

import com.henugao.taskmanager.R;
import com.henugao.taskmanager.domain.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class TaskInfoParser {
	//��ȡ�ֻ���������Ӧ�õĽ���
	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager packageManager = context.getPackageManager();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			//��ȡ����������������
			String processName = runningAppProcessInfo.processName; 
			taskInfo.setPackageName(processName);
			try {
				//��ȡ���ڴ������Ϣ
				/**
				 * ��������һ��ֻ��һ������
				 */
				MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
				int totalPrivateDirty =  memoryInfo[0].getTotalPrivateDirty() * 1024;
				
				taskInfo.setAppMemSize(totalPrivateDirty);
				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				//�õ�Ӧ�õ�ͼ��
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				//�õ�Ӧ�õ�����
				String appName = (String) packageInfo.applicationInfo.loadLabel(packageManager);
				taskInfo.setAppName(appName);
				int flags = packageInfo.applicationInfo.flags;
				if((ApplicationInfo.FLAG_SYSTEM & flags)!=0){
					//ϵͳӦ��
					taskInfo.setUserApp(false);
				}else{
					//�û�Ӧ��
					taskInfo.setUserApp(true);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//����ЩϵͳӦ��û��ͼ���ʱ����Ҫ��������һ��Ĭ�ϵ�ͼ��
				taskInfo.setAppName(processName);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				System.out.println("====drable error=====");
				e.printStackTrace();
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
