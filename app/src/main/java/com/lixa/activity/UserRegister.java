package com.lixa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lixa.util.OrderHttpUtil;
import com.lixa.util.OrderStringUtil;
import com.lixa.util.OrderUrlUtil;

public class UserRegister extends Activity{

	private ImageButton m_register;
	private ImageButton m_reset;
	private ImageButton m_go_back;

	private EditText username;
	private EditText password;
	private EditText email;

	private RadioGroup m_gender;
	private RadioButton m_boy;
	private RadioButton m_gril;

	private CheckBox m_accept;

	private String gender = "";
	private boolean accept = false;

	private ProgressDialog proDlg;
	private String res;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);

		/**
		 * 实例化组件
		 */
		findAllViewById();

		/**
		 * 注册事件
		 */
		registerViewListener();

	}

	/**
	 * 注册事件
	 */
	private void registerViewListener() {
		// 注册
		m_register.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				final String uname = username.getText().toString();
				final String upwd = password.getText().toString();
				final String umail = email.getText().toString();

				if("".equals(uname.trim())){ // 用户名为空！

					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_account_null)
							.setMessage(R.string.login_account_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
									username.setText("");
									password.setText("");
									email.setText("");
								}
							}).show();
					return ;

				}
				if("".equals(upwd)){
					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_password_null)
							.setMessage(R.string.login_password_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
									password.setText("");
									email.setText("");
								}
							}).show();
					return ;
				}
				if("".equals(umail.trim())){
					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_email_null)
							.setMessage(R.string.login_email_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
									email.setText("");
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						// 点击取消按钮
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).show();
					return ;
				}
				if(!OrderStringUtil.emailRule(umail)){
					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_email_invalid)
							.setMessage(R.string.login_email_invalid)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
									email.setText("");
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						// 点击取消按钮
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).setNeutralButton("还是取消", new DialogInterface.OnClickListener() {
						// 点击取消按钮
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).show();
					return ;
				}
				if("".equals(gender)){
					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_gender_null)
							.setMessage(R.string.login_gender_null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
					return ;
				}
				if(!accept){
					AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
					builder.setIcon(R.drawable.alert_wanring)
							.setTitle(R.string.login_accept_no)
							.setMessage(R.string.login_accept_no)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								// 点击确定按钮
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
					return ;
				}

				proDlg = OrderStringUtil.createProgressDialog(UserRegister.this, getResources().getString(R.string.pro_title),
						getResources().getString(R.string.pro_message), true, true);
				proDlg.show();

				new Thread(){
					@Override
					public void run() {
						/**
						 * 1  验证用户是否存在，不存在，注册
						 * 2  注册成功，返回账号和密码显示
						 * 3  登录
						 */
						String registerString = "loginId=" + uname + "&password=" + upwd + "&email=" + umail + "&gender=" + gender;
						String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.REGISTER_URL + registerString;

						res = OrderHttpUtil.getHttpPostResultForUrl(url);
						handler.sendEmptyMessage(1);
					}
				}.start();

			}

		});

		// 清空
		m_reset.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				username.setText("");
				password.setText("");
				email.setText("");
				m_accept.setChecked(false);
				m_boy.setChecked(false);
				m_gril.setChecked(false);
			}
		});

		// 返回
		m_go_back.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UserRegister.this, OrderMainActivity.class);
				startActivity(intent);
			}
		});

		// 性别
		m_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(m_boy.getId() == checkedId)
					gender = "M";
				else if(m_gril.getId() == checkedId)
					gender = "F";
				else
					gender = "";
			}
		});

		// 同意条款
		m_accept.setOnClickListener(new RadioGroup.OnClickListener() {
			public void onClick(View v) {
				if(m_accept.isChecked())
					accept = true;
				else
					accept = false;
			}
		});

	}

	/**
	 * 提示注册信息
	 * 1 注册成功
	 * 0 注册失败
	 * 2 邮箱已存在
	 * 3 登陆ID已存在
	 * exception 网络异常
	 * @param res
	 */
	protected void showRegisterMesg(String res) {
		if("0".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("注册失败")
					.setMessage("注册失败，请稍后再试！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			return ;
		}
		if("1".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
			builder.setIcon(R.drawable.alert_add)
					.setTitle("注册成功")
					.setMessage("恭喜您，注册成功，请登陆！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(UserRegister.this, OrderMainActivity.class);
							startActivity(intent);
						}
					}).show();
			return ;
		}
		if("2".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("邮箱已存在")
					.setMessage("邮箱已存在，请使用其它邮箱！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).show();
			return ;
		}
		if("3".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("登陆账号已存在")
					.setMessage("登陆账号已存在，请使用其它账号！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							username.setText("");
						}
					}).show();
			return ;
		}
	}

	/**
	 * 实例化组件
	 */
	private void findAllViewById() {
		m_register = (ImageButton)findViewById(R.id.imb_reg_register);
		m_reset = (ImageButton)findViewById(R.id.imb_reg_reset);
		m_go_back = (ImageButton)findViewById(R.id.imb_reg_go_back);

		username = (EditText)findViewById(R.id.text_reg_username);
		password = (EditText)findViewById(R.id.text_reg_password);
		email = (EditText)findViewById(R.id.text_reg_email);

		m_gender = (RadioGroup)findViewById(R.id.gender_radio);
		m_boy = (RadioButton)findViewById(R.id.radio_boy);
		m_gril = (RadioButton)findViewById(R.id.radio_gril);

		m_accept = (CheckBox)findViewById(R.id.cb_accept);
	}

	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			proDlg.dismiss();
			showRegisterMesg(res);
		};
	};

}
