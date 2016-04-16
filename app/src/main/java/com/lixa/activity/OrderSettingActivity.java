package com.lixa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.lixa.util.OrderStringUtil;

public class OrderSettingActivity extends Activity {
	private CheckBox save_uname;
	private CheckBox save_pwd;
	private Button save_data_btn;
	private boolean pwd = false;
	private boolean uname = false;
	private String str ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_setting);
		str = OrderStringUtil.getDataFromIntent(getIntent());

		save_uname = (CheckBox)findViewById(R.id.setting_save_uname);
		save_pwd = (CheckBox)findViewById(R.id.setting_save_pwd);
		save_data_btn = (Button)findViewById(R.id.setting_submit_btn);

		final SharedPreferences share = getSharedPreferences(OrderStringUtil.USER_DATA_PROVIDE,PreferenceActivity.MODE_PRIVATE);
		boolean u = share.getBoolean(OrderStringUtil.IS_USER_NAME, false);
		boolean p = share.getBoolean(OrderStringUtil.IS_PASSWORD, false);
		if(u) save_uname.setChecked(true);
		if(p) save_pwd.setChecked(true);

		save_data_btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(save_uname.isChecked())
					uname = true;
				if(save_pwd.isChecked())
					pwd = true;

				SharedPreferences.Editor editor = share.edit();
				if(uname)
					editor.putBoolean(OrderStringUtil.IS_USER_NAME, true);
				else
					editor.putBoolean(OrderStringUtil.IS_USER_NAME, false);
				if(pwd)
					editor.putBoolean(OrderStringUtil.IS_PASSWORD, true);
				else
					editor.putBoolean(OrderStringUtil.IS_PASSWORD, false);
				editor.commit();
				AlertDialog.Builder b = new AlertDialog.Builder(OrderSettingActivity.this);
				b.setTitle("设置成功").setMessage("您的信息已经设置成功！").setIcon(R.drawable.alert_ok)
						.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(OrderSettingActivity.this, OrderMainMenu.class);
						OrderStringUtil.putDataIntoIntent(i, str);
						startActivity(i);
					}
				}).show();
			}
		});

	}

}
