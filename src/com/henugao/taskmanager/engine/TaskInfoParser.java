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
	//获取手机里面所有应用的进程
	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager packageManager = context.getPackageManager();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			//获取进程名（即包名）
			String processName = runningAppProcessInfo.processName; 
			taskInfo.setPackageName(processName);
			try {
				//获取到内存基本信息
				/**
				 * 这里里面一共只有一个数据
				 */
				MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
				int totalPrivateDirty =  memoryInfo[0].getTotalPrivateDirty() * 1024;
				
				taskInfo.setAppMemSize(totalPrivateDirty);
				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				//得到应用的图标
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				//得到应用的名字
				String appName = (String) packageInfo.applicationInfo.loadLabel(packageManager);
				taskInfo.setAppName(appName);
				int flags = packageInfo.applicationInfo.flags;
				if((ApplicationInfo.FLAG_SYSTEM & flags)!=0){
					//系统应用
					taskInfo.setUserApp(false);
				}else{
					//用户应用
					taskInfo.setUserApp(true);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//当有些系统应用没有图标的时候，需要给其设置一个默认的图标
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
