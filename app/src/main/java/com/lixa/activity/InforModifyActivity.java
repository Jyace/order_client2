package com.lixa.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import com.lixa.util.OrderHttpUtil;
import com.lixa.util.OrderStringUtil;
import com.lixa.util.OrderUrlUtil;

public class InforModifyActivity extends TabActivity {
	private static final String BASE_INFOR = "base_tab";
	private static final String PASSWORD_INFOR = "password_tab";
	private String str;
	private String userData[] = null;
	private String gender;
	private TabHost tabHost;
	private TextView loginId;
	private TextView nikeName;
	private TextView email;
	private TextView phone;
	private RadioGroup genderGroup;
	private RadioButton genderBoy;
	private RadioButton genderGril;
	private Button baseSubmit;

	private TextView oldPassword;
	private TextView newPassword;
	private TextView re_newPassword;
	private Button passwordSubmit;

	private ProgressDialog proDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infor_modify);
		// 初始化组件
		initObject();
		//id,loginid,password,nikename,phone,email,gender,create_at
		str = OrderStringUtil.getDataFromIntent(getIntent());
		userData = str.split(",");
		gender = userData[6];
		//设置基本信息
		setUserBaseData();

		tabHost = getTabHost();
		TabSpec tabBase = tabHost.newTabSpec(BASE_INFOR);
		tabBase.setIndicator("修改个人信息", getResources().getDrawable(R.drawable.base_infor));
		tabBase.setContent(R.id.modify_infor_base);
		tabHost.addTab(tabBase);

		TabSpec tabPwd = tabHost.newTabSpec(PASSWORD_INFOR);
		tabPwd.setIndicator("修改登陆密码", getResources().getDrawable(R.drawable.password_infor));
		tabPwd.setContent(R.id.modify_infor_password);
		tabHost.addTab(tabPwd);

		tabHost.setCurrentTab(0);

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals(BASE_INFOR))
					setUserBaseData();

				if(tabId.equals(PASSWORD_INFOR)){
					oldPassword.setText("");
					newPassword.setText("");
					re_newPassword.setText("");
				}
			}
		});

		setButtonOnclickListener();
	}

	/**
	 * 设置组件的点击事件
	 */
	private void setButtonOnclickListener() {
		/**
		 * 修改基本信息
		 */
		baseSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(InforModifyActivity.this);
				if("".equals(nikeName.getText().toString().trim())){
					builder.setTitle("昵称为空").setMessage("昵称为空，请输入昵称！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if("".equals(email.getText().toString().trim())){
					builder.setTitle("邮箱为空").setMessage("邮箱为空，请输入邮箱！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if(!OrderStringUtil.emailRule(email.getText().toString().trim())){
					builder.setTitle("邮箱不合法").setMessage("邮箱不合法，请重新输入！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if("".equals(phone.getText().toString().trim())){
					builder.setTitle("手机号码为空").setMessage("手机号码为空，请输入手机号码！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if(!OrderStringUtil.phoneNumberRule(phone.getText().toString().trim())){
					builder.setTitle("手机号码不合法").setMessage("手机号码不合法，请输入手机号码！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				/**
				 * 修改个人信息
				 */
				StringBuilder requestString = new StringBuilder();
				requestString.append("loginid=").append(userData[1]).append("&nikename=")
						.append(nikeName.getText().toString());
				requestString.append("&email=").append(email.getText().toString());
				requestString.append("&phone=").append(phone.getText().toString());
				requestString.append("&gender=").append(gender);
				final String requestUrl = OrderHttpUtil.BASE_URL + OrderUrlUtil.MODIFY_BASE_INFOR + requestString;

				proDlg = OrderStringUtil.createProgressDialog(InforModifyActivity.this, "正在提交数据",
						"正在发送请求，请稍后...", false, true);
				proDlg.show();

				new Thread(){
					@Override
					public void run() {
						String res = OrderHttpUtil.getHttpPostResultForUrl(requestUrl);

						/**
						 * res 结果判断
						 * -1 修改错误
						 * 0 修改成功
						 * 1 邮箱已经存在
						 */
						Message m = new Message();
						if("0".equals(res))
							m.what = OrderStringUtil.BASE_MODIFY_OK;
						else if("1".equals(res))
							m.what = OrderStringUtil.EMAIL_EXISTS;
						else
							m.what = OrderStringUtil.BASE_ERROR;
						handler.sendMessage(m);
					}
				}.start();



			}
		});

		/**
		 * 修改密码
		 */
		passwordSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(InforModifyActivity.this);
				if("".equals(oldPassword.getText().toString().trim())){
					builder.setTitle("原密码为空").setMessage("原密码为空，新输入原密码！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if("".equals(newPassword.getText().toString().trim())){
					builder.setTitle("新密码为空").setMessage("新密码为空，新输入新密码！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if(!re_newPassword.getText().toString().trim().equals(newPassword.getText().toString().trim())){
					builder.setTitle("两次密码不相符").setMessage("两次数据密码不相符，请确认！")
							.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}

				/**
				 * 修改密码
				 */
				StringBuilder requestString = new StringBuilder();
				requestString.append("loginid=").append(userData[1])
						.append("&oldpwd=").append(oldPassword.getText().toString().trim())
						.append("&newpwd=").append(newPassword.getText().toString().trim());
				final String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.MODIFY_PASSWORD_INFOR + requestString;
				proDlg = OrderStringUtil.createProgressDialog(InforModifyActivity.this,
						"提交请求", "正在提交请求数据，请稍后...", false, true);
				proDlg.show();
				new Thread(){
					@Override
					public void run() {
						/**
						 * res 结果判断
						 * -1 修改错误
						 * 0 修改成功
						 * 1 原密码错误
						 */
						String res = OrderHttpUtil.getHttpPostResultForUrl(url);
						Message m = new Message();
						if("-1".equals(res))
							m.what = OrderStringUtil.PASSWORD_ERROR;
						else if("0".equals(res))
							m.what = OrderStringUtil.PASSWORD_MODIFY_OK;
						else
							m.what = OrderStringUtil.PASSWORD_OLD_REEOR;
						handler.sendMessage(m);

					}
				}.start();
			}
		});

		/**
		 * 设置性别
		 */
		genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == genderBoy.getId())
					gender = "M";
				else if(checkedId == genderGril.getId())
					gender = "F";
			}
		});
	}

	/**
	 * 设置基本信息数据
	 */
	private void setUserBaseData() {
		loginId.setText(userData[1]);

		if(!"昵称".equals(userData[3]))
			nikeName.setText(userData[3]);

		if(!"手机".equals(userData[4]))
			phone.setText(userData[4]);

		email.setText(userData[5]);

		if("M".equals(userData[6]))
			genderBoy.setChecked(true);
		else if("F".equals(userData[6]))
			genderGril.setChecked(true);
	}

	/**
	 * 初始化组件
	 */
	private void initObject() {
		loginId = (TextView)findViewById(R.id.modify_login_id);
		nikeName = (TextView)findViewById(R.id.modify_nike_name);
		email = (TextView)findViewById(R.id.modify_email_account);
		phone = (TextView)findViewById(R.id.modify_phone_number);
		genderGroup = (RadioGroup)findViewById(R.id.modify_gender);
		genderBoy = (RadioButton)findViewById(R.id.modify_gender_boy);
		genderGril = (RadioButton)findViewById(R.id.modify_gender_gril);
		baseSubmit = (Button)findViewById(R.id.modify_base_submit);
		findViewById(R.id.modify_base_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InforModifyActivity.this, OrderMainMenu.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});

		oldPassword = (TextView)findViewById(R.id.modify_old_password);
		newPassword = (TextView)findViewById(R.id.modify_new_password);
		re_newPassword = (TextView)findViewById(R.id.modify_re_new_password);
		passwordSubmit = (Button)findViewById(R.id.modify_password_submit);
		findViewById(R.id.modify_password_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InforModifyActivity.this, OrderMainMenu.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
	}

	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(InforModifyActivity.this);
			proDlg.dismiss();
			switch(msg.what){
				case OrderStringUtil.BASE_MODIFY_OK:
					builder.setTitle("信息修改成功").setMessage("信息修改成功，需要重新登录更新信息.")
							.setIcon(R.drawable.alert_ok).setCancelable(false).setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(InforModifyActivity.this, OrderMainActivity.class);
							startActivity(i);
						}
					}).setNegativeButton("稍后登陆", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(InforModifyActivity.this, OrderMainMenu.class);
							OrderStringUtil.putDataIntoIntent(i, str);
							startActivity(i);
						}
					}).show();
					break;
				case OrderStringUtil.EMAIL_EXISTS:
					builder.setTitle("邮箱已经存在").setMessage("邮箱已经存在，请换用其他邮箱")
							.setIcon(R.drawable.alert_wanring).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					break;
				case OrderStringUtil.BASE_ERROR:
					builder.setTitle("错误").setMessage("服务器错误，请稍后重试！")
							.setIcon(R.drawable.alert_error).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					break;
				case OrderStringUtil.PASSWORD_ERROR:
					builder.setTitle("错误").setMessage("服务器错误，请稍后重试！")
							.setIcon(R.drawable.alert_error).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					break;
				case OrderStringUtil.PASSWORD_OLD_REEOR:
					builder.setTitle("原密码错误").setMessage("原密码错误，请确认！")
							.setIcon(R.drawable.alert_wanring).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					break;
				case OrderStringUtil.PASSWORD_MODIFY_OK:
					builder.setTitle("密码修改成功").setMessage("密码修改成功，请重新登录")
							.setIcon(R.drawable.alert_ok).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(InforModifyActivity.this, OrderMainActivity.class);
							startActivity(i);
						}
					}).show();
					break;
			}
		};
	};
}
