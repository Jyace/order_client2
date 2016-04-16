package com.lixa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lixa.util.OrderHttpUtil;
import com.lixa.util.OrderStringUtil;
import com.lixa.util.OrderUrlUtil;

public class OrderMainActivity extends Activity {
	private ImageButton m_login;
	private ImageButton m_clear;
	private ImageButton m_register;

	private EditText m_username;
	private EditText m_password;

	private String uname;
	private String pwd;
	private ProgressDialog prgDialog;
	private String res;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		m_login = (ImageButton)findViewById(R.id.imb_login);
		m_clear = (ImageButton)findViewById(R.id.imb_clear);
		m_register = (ImageButton)findViewById(R.id.imb_register);

		m_username = (EditText)findViewById(R.id.text_username);
		m_password = (EditText)findViewById(R.id.text_password);

		initUserNamePassword();

		/**
		 * 添加监听器
		 */
		setImageButtonListener();

	}
	/**
	 * 更具用的个性设置来初始化用户名和密码
	 */
	private void initUserNamePassword() {

		SharedPreferences share = getSharedPreferences(OrderStringUtil.USER_DATA_PROVIDE, PreferenceActivity.MODE_PRIVATE);

		String u_name = share.getString(OrderStringUtil.USERNAME, "");
		m_username.setText(u_name);

		String u_pwd = share.getString(OrderStringUtil.PASSWORD, "");
		m_password.setText(u_pwd);

	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences share = getSharedPreferences(OrderStringUtil.USER_DATA_PROVIDE, PreferenceActivity.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();

		boolean name = share.getBoolean(OrderStringUtil.IS_USER_NAME, false);
		boolean pwd = share.getBoolean(OrderStringUtil.IS_PASSWORD, false);
		if(name)
			editor.putString(OrderStringUtil.USERNAME, m_username.getText().toString());
		else
			editor.remove(OrderStringUtil.USERNAME);
		if(pwd)
			editor.putString(OrderStringUtil.PASSWORD, m_password.getText().toString());
		else
			editor.remove(OrderStringUtil.PASSWORD);
		editor.commit();
	}

	/**
	 * 添加监听器
	 */
	private void setImageButtonListener() {

		// 登陆
		m_login.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				uname = m_username.getText().toString();
				pwd = m_password.getText().toString();
				if("".equals(uname)){
					AlertDialog.Builder builder = new AlertDialog.Builder(OrderMainActivity.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_account_null)
							.setMessage(R.string.login_account_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {}
							}).show();
					return ;
				}
				if("".equals(pwd)){
					AlertDialog.Builder builder = new AlertDialog.Builder(OrderMainActivity.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_password_null)
							.setMessage(R.string.login_password_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
					return ;
				}

				// 显示登陆对话框
				prgDialog = new ProgressDialog(OrderMainActivity.this);
				prgDialog.setIcon(R.drawable.progress);
				prgDialog.setTitle("请稍等");
				prgDialog.setMessage("正在登陆，请稍等...");
				prgDialog.setCancelable(false);
				prgDialog.setIndeterminate(true);
				prgDialog.show();

				login();

			}

		});

		// 清空用户名和密码
		m_clear.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				m_username.setText("");
				m_password.setText("");
			}
		});

		// 注册
		m_register.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(OrderMainActivity.this, UserRegister.class);
				startActivity(intent);
			}
		});

	}

	protected void login() {
		new Thread(){
			@Override
			public void run() {
				String loginString = "loginId=" + uname + "&password=" + pwd;
				String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.LOGIN_URL + loginString;
				res = OrderHttpUtil.getHttpPostResultForUrl(url);

				Message m = new Message();

				if("-1".equals(res))
					m.what = OrderStringUtil.LOGIN_ERROR;
				else
					m.what = OrderStringUtil.LOGIN_SUCCESS;

				proHandle.sendMessage(m);

			}
		}.start();
	}

	private Handler proHandle = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			AlertDialog.Builder builder = new AlertDialog.Builder(OrderMainActivity.this);

			prgDialog.dismiss();
			switch(msg.what){
				case OrderStringUtil.LOGIN_ERROR:
					builder.setIcon(R.drawable.alert_error)
							.setTitle("错误")
							.setMessage("用户名或密码错误，请确认")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
					break;
				case OrderStringUtil.LOGIN_SUCCESS:
					builder.setIcon(R.drawable.alert_ok)
							.setTitle("登陆成功")
							.setMessage("恭喜您，登陆成功")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(OrderMainActivity.this, OrderMainMenu.class);

									Bundle bundle = new Bundle();
									bundle.putString("data", res);

									intent.putExtra("data", bundle);

									startActivity(intent);
								}
							}).show();
					break;
			}
		}
	};

}
