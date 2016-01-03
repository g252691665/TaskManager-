package com.henugao.taskmanager.activities;


import java.util.ArrayList;
import java.util.List;

import com.henugao.taskmanager.R;
import com.henugao.taskmanager.domain.TaskInfo;
import com.henugao.taskmanager.engine.TaskInfoParser;
import com.henugao.taskmanager.utils.SystemInfoUtil;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView task_process_count;
	private TextView task_memory;
	private ListView list_view;
	
	private List<TaskInfo> taskInfos;
	//用户进程列表
	private List<TaskInfo> userTaskInfos;
	//系统进程列表
	private List<TaskInfo> systemTaskInfos;
	private TaskInfoAdapter tia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initUI();
		initData();
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			tia = new TaskInfoAdapter();
			list_view.setAdapter(tia);
		};
	};
	private int processCount;
	private long avaliMem;
	private long totalMem;
	private SharedPreferences sp;
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(){
			@Override
			public void run() {
				taskInfos = TaskInfoParser.getTaskInfos(MainActivity.this);
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : taskInfos) {
					if(taskInfo.isUserApp()){
						userTaskInfos.add(taskInfo);
					}else{
						systemTaskInfos.add(taskInfo);
					}
				}
				handler.sendEmptyMessage(0);
				
			}
		}.start();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main); 
		task_process_count = (TextView) findViewById(R.id.tv_task_process_count);
		task_memory = (TextView) findViewById(R.id.tv_task_memory);
		list_view = (ListView) findViewById(R.id.list_view);
		processCount = SystemInfoUtil.getProcessCount(MainActivity.this);
		task_process_count.setText("运行的进程"+processCount+"个");
		avaliMem = SystemInfoUtil.getAvaliMem(MainActivity.this);
		totalMem = SystemInfoUtil.getTotalMem(MainActivity.this);
		task_memory.setText("剩余内存/总内存："+Formatter.formatFileSize(MainActivity.this, avaliMem)+"/"+
				Formatter.formatFileSize(MainActivity.this, totalMem));
		
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//得到当前Item点击的对象
				Object itemAtPosition = list_view.getItemAtPosition(position);
				if (itemAtPosition != null && itemAtPosition instanceof TaskInfo) {
					TaskInfo taskInfo = (TaskInfo)itemAtPosition;
					ViewHolder holder = (ViewHolder) view.getTag();
					//判断当前的item是否被勾选上
					/**
					 * 如果被勾选上了，就变成没有勾选
					 * 如果没有勾选上，就变成勾选
					 */
					if(taskInfo.isChecked()){
						taskInfo.setChecked(false);
						holder.cb_app_status.setChecked(false);
					}else{
						taskInfo.setChecked(true);
						holder.cb_app_status.setChecked(true);
					}
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//当从进程设置界面跳转回来之后，刷新数据
		if (tia != null) {
			tia.notifyDataSetChanged();
		}
	}
	private class TaskInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//判断是否显示系统进程
			/**
			 * 如果要显示系统进程，显示的条目为用户进程+系统进程+2个textView
			 * 如果不显示系统进程，显示的条目为用户进程+1个textView
			 */
			boolean is_show_system = sp.getBoolean("is_show_system", false);
			if (is_show_system) {
				return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
			}else {
				return userTaskInfos.size() + 1;
			}
		
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return null;
			} else if (position == userTaskInfos.size() + 1) {
				return null;
			}

			TaskInfo taskInfo;

			if (position < (userTaskInfos.size() + 1)) {
				// 用户程序
				taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
															// 位置需要-1
			} else {
				// 系统程序
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo =systemTaskInfos.get(location);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (position == 0) {
				// 第0个位置显示的应该是 用户进程的个数的标签。
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户进程：" + userTaskInfos.size() + "个");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统进程：" + systemTaskInfos.size() + "个");
				return tv;
			}
			
			TaskInfo taskInfo;
			if (position < (userTaskInfos.size() + 1)) {
				// 用户进程
				taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
															// 位置需要-1
			} else {
				// 系统进程
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemTaskInfos.get(location);
			}
			View view;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}else{
				viewHolder = new ViewHolder();
				view = View.inflate(MainActivity.this,R.layout.item_task_manager , null);
				viewHolder.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
				viewHolder.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
				viewHolder.tv_mem_size = (TextView)view.findViewById(R.id.tv_mem_size);
				viewHolder.cb_app_status = (CheckBox) view.findViewById(R.id.cb_app_status);
				view.setTag(viewHolder);
			}
			viewHolder.iv_app_icon.setImageDrawable(taskInfo.getIcon());
			viewHolder.tv_app_name.setText(taskInfo.getAppName());
			viewHolder.tv_mem_size.setText("内存占用："+Formatter.formatFileSize(MainActivity.this, taskInfo.getAppMemSize()));
			if (taskInfo.isChecked()) {
				viewHolder.cb_app_status.setChecked(true);
			}else{
				viewHolder.cb_app_status.setChecked(false);
			}
			
			
			return view;
		}
		
	}
	static class ViewHolder{
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_mem_size;
		CheckBox cb_app_status;
	}
	
	/**
	 * 全选所有的checkbox
	 * @param view
	 */
	public void selectAll(View view){
		for (TaskInfo taskInfo : taskInfos) {
			//判断当前的程序是否是自己的程序
			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}	
			taskInfo.setChecked(true);
		}
		//一定要注意，只要数据改变，一定要刷新
		tia.notifyDataSetChanged();
	}

	/**
	 * 反选
	 * @param view
	 */
	public void selectOpposite(View view){
		for (TaskInfo taskInfo : taskInfos) {
			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}
			if (taskInfo.isChecked()) {
				taskInfo.setChecked(false);
			}else{	
				taskInfo.setChecked(true);
			}
		}
		tia.notifyDataSetChanged();
	}
	/**
	 * 杀死选定的进程
	 * @param view
	 */
	public void killProcess(View view){
		//想杀死进程，首先必须得到进程管理器
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//注意：在迭代一个集合的时候，不能修改集合的大小，所以这里要再用一个列表，
		//存放要杀死的进程的集合，等到集合迭代完毕，再删除
		List<TaskInfo> killLists = new ArrayList<TaskInfo>();
		//清理的总进程的个数
		int totalCount = 0;
		//清理的内存总数
		int killMem = 0;
		for (TaskInfo taskInfo : userTaskInfos) {
			if(taskInfo.isChecked()){
				activityManager.killBackgroundProcesses(taskInfo.getPackageName());
				//userTaskInfos.remove(taskInfo);
				killLists.add(taskInfo);
				totalCount++;
				killMem += taskInfo.getAppMemSize();
			}
		}
		for (TaskInfo taskInfo : systemTaskInfos) {
			if(taskInfo.isChecked()){
				activityManager.killBackgroundProcesses(taskInfo.getPackageName());
				killLists.add(taskInfo);
				//systemTaskInfos.remove(taskInfo);
				totalCount++;
				killMem += taskInfo.getAppMemSize();
			}
		}
		for (TaskInfo taskInfo : killLists) {
			//判断是否是用户app
			if (taskInfo.isUserApp()) {
				userTaskInfos.remove(taskInfo);
			}else{
				systemTaskInfos.remove(taskInfo);
			}
		}
		processCount = processCount - totalCount;
		avaliMem = avaliMem + killMem;
		task_process_count.setText("运行的进程"+processCount+"个");
		task_memory.setText("剩余内存/总内存："+Formatter.formatFileSize(MainActivity.this,avaliMem)+"/"+
				Formatter.formatFileSize(MainActivity.this, totalMem));
		tia.notifyDataSetChanged();
		Toast.makeText(MainActivity.this, "共清理了"+totalCount+"个进程，释放了"+Formatter.formatFileSize(MainActivity.this, killMem)+"内存",Toast.LENGTH_SHORT).show();
	}
	
	//打开进程的设置界面
	public void openSetting(View view) {
		Intent intent = new Intent(this,TaskManagerSettingActivity.class);
		startActivity(intent);
	}
}

