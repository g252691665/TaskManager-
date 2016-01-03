package com.henugao.taskmanager.activities;


import com.henugao.taskmanager.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskManagerSettingActivity extends Activity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		
	}

	private void initUI() {
		setContentView(R.layout.activity_task_manager_setting);
		CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//使得checkbox记得上次选中的状态；
		cb_status.setChecked(sp.getBoolean("is_show_system", false));
		
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					Editor edit = sp.edit();
					edit.putBoolean("is_show_system", true);
					edit.commit();
				}else{
					Editor edit = sp.edit();
					edit.putBoolean("is_show_system", false);
					edit.commit();
				}
			}
		});
	}
	

}
