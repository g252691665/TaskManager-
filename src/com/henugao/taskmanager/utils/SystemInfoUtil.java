package com.henugao.taskmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class SystemInfoUtil {

	
	/**
	 * 获取运行中进程的个数
	 * @author henugao
	 * @param context
	 * @return
	 */
	public static int getProcessCount(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		int processCount = runningAppProcesses.size();
		return processCount;
	}
	
	/**
	 * 获取剩余的内存信息
	 * @param context
	 * @return
	 */
	public static long getAvaliMem(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		long availMem = memoryInfo.availMem;
		return availMem;
	}
	
	/**
	 * 获得总内存
	 * @param context
	 * @return
	 */
	public static long getTotalMem(Context context) {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(new File("/proc/meminfo"));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String readLine = bufferedReader.readLine();
			StringBuffer sb = new StringBuffer();
			for (char c : readLine.toCharArray()) {
				if(c >= '0' && c <= '9'){
					sb.append(c);
				}
			}
			return Long.valueOf(sb.toString())*1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

		
	}
}
