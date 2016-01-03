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
	//�û������б�
	private List<TaskInfo> userTaskInfos;
	//ϵͳ�����б�
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
		task_process_count.setText("���еĽ���"+processCount+"��");
		avaliMem = SystemInfoUtil.getAvaliMem(MainActivity.this);
		totalMem = SystemInfoUtil.getTotalMem(MainActivity.this);
		task_memory.setText("ʣ���ڴ�/���ڴ棺"+Formatter.formatFileSize(MainActivity.this, avaliMem)+"/"+
				Formatter.formatFileSize(MainActivity.this, totalMem));
		
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//�õ���ǰItem����Ķ���
				Object itemAtPosition = list_view.getItemAtPosition(position);
				if (itemAtPosition != null && itemAtPosition instanceof TaskInfo) {
					TaskInfo taskInfo = (TaskInfo)itemAtPosition;
					ViewHolder holder = (ViewHolder) view.getTag();
					//�жϵ�ǰ��item�Ƿ񱻹�ѡ��
					/**
					 * �������ѡ���ˣ��ͱ��û�й�ѡ
					 * ���û�й�ѡ�ϣ��ͱ�ɹ�ѡ
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
		//���ӽ������ý�����ת����֮��ˢ������
		if (tia != null) {
			tia.notifyDataSetChanged();
		}
	}
	private class TaskInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//�ж��Ƿ���ʾϵͳ����
			/**
			 * ���Ҫ��ʾϵͳ���̣���ʾ����ĿΪ�û�����+ϵͳ����+2��textView
			 * �������ʾϵͳ���̣���ʾ����ĿΪ�û�����+1��textView
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
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
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
				// ��0��λ����ʾ��Ӧ���� �û����̵ĸ����ı�ǩ��
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û����̣�" + userTaskInfos.size() + "��");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ���̣�" + systemTaskInfos.size() + "��");
				return tv;
			}
			
			TaskInfo taskInfo;
			if (position < (userTaskInfos.size() + 1)) {
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
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
			viewHolder.tv_mem_size.setText("�ڴ�ռ�ã�"+Formatter.formatFileSize(MainActivity.this, taskInfo.getAppMemSize()));
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
	 * ȫѡ���е�checkbox
	 * @param view
	 */
	public void selectAll(View view){
		for (TaskInfo taskInfo : taskInfos) {
			//�жϵ�ǰ�ĳ����Ƿ����Լ��ĳ���
			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}	
			taskInfo.setChecked(true);
		}
		//һ��Ҫע�⣬ֻҪ���ݸı䣬һ��Ҫˢ��
		tia.notifyDataSetChanged();
	}

	/**
	 * ��ѡ
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
	 * ɱ��ѡ���Ľ���
	 * @param view
	 */
	public void killProcess(View view){
		//��ɱ�����̣����ȱ���õ����̹�����
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//ע�⣺�ڵ���һ�����ϵ�ʱ�򣬲����޸ļ��ϵĴ�С����������Ҫ����һ���б�
		//���Ҫɱ���Ľ��̵ļ��ϣ��ȵ����ϵ�����ϣ���ɾ��
		List<TaskInfo> killLists = new ArrayList<TaskInfo>();
		//������ܽ��̵ĸ���
		int totalCount = 0;
		//������ڴ�����
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
			//�ж��Ƿ����û�app
			if (taskInfo.isUserApp()) {
				userTaskInfos.remove(taskInfo);
			}else{
				systemTaskInfos.remove(taskInfo);
			}
		}
		processCount = processCount - totalCount;
		avaliMem = avaliMem + killMem;
		task_process_count.setText("���еĽ���"+processCount+"��");
		task_memory.setText("ʣ���ڴ�/���ڴ棺"+Formatter.formatFileSize(MainActivity.this,avaliMem)+"/"+
				Formatter.formatFileSize(MainActivity.this, totalMem));
		tia.notifyDataSetChanged();
		Toast.makeText(MainActivity.this, "��������"+totalCount+"�����̣��ͷ���"+Formatter.formatFileSize(MainActivity.this, killMem)+"�ڴ�",Toast.LENGTH_SHORT).show();
	}
	
	//�򿪽��̵����ý���
	public void openSetting(View view) {
		Intent intent = new Intent(this,TaskManagerSettingActivity.class);
		startActivity(intent);
	}
}

