package com.lixa.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.lixa.activity.R;

public class OrderStringUtil {

	/**
	 * 内容提供者列名
	 */
	public static final String USER_DATA_PROVIDE = "com.lixa.provide.USERS";

	/**
	 * 列名约束 constraint
	 */
	public static final String TEXT_NOT_NULL = "text not null";
	public static final String TEXT_DEFAULT_NULL = "text default null";
	public static final String INTEGER_NOT_NULL = "integer not null";
	public static final String INTEGER_DEFAULT_NULL = "integer default null";

	/**
	 * 菜单类型
	 */
	public static final String HOT_ORDER_MENU = "recai";
	public static final String COOL_ORDER_MENU = "liangcai";
	public static final String NOOLDE_ORDER_MENU = "mianshi";

	/**
	 * Handler 消息内容列表
	 */
	public static final int LOGIN_ERROR = 0; // 登陆失败
	public static final int LOGIN_SUCCESS = 1; // 登陆成功
	public static final int SERVER_ERROR = 2; // 服务器异常
	public static final int SERVER_NO_DATA = 3; // 服务器无数据
	public static final int DATA_DETAIL = 4; // 有数据，提示详细信息
	public static final int GO_ORDER = 5; // 可以点菜
	public static final int ERROR = 6; // 错误操作
	public static final int OK = 7; // 操作正常
	public static final int NEW_ORDER_FINASH = 8; // 新增更新完成
	public static final int UPDATE_ORDER_FINASH = 9; // 修改更新完成
	public static final int DELETE_ORDER_FINASH = 10; // 删除更新完成
	public static final int ERROR_ORDER_FINASH = 11; // 错误更新完成
	public static final int MAKE_ORDER_OK = 12; // 点菜数据发送完成
	public static final int MAKE_ORDER_ERROR = 13; // 点菜数据发送错误
	public static final int BASE_MODIFY_OK = 14; // 信息修改成功
	public static final int PASSWORD_MODIFY_OK = 15; // 信息修改成功
	public static final int EMAIL_EXISTS = 16; // 邮箱已经存在
	public static final int BASE_ERROR = 17; // 基本信息修改错误
	public static final int PASSWORD_ERROR = 18; // 密码修改错误
	public static final int PASSWORD_OLD_REEOR = 19; // 原密码错误

	/**
	 * 类型名称
	 */
	public static final String[] orderTypeName = {"热菜", "凉菜", "面食"};

	/**
	 * 类型值
	 */
	public static final String[] orderTypeValue = {"recai", "liangcai", "mianshi"};

	/**
	 * 是否保持用户名 key
	 */
	public static final String IS_USER_NAME = "is_save_uname";

	/**
	 * 是否保持密码 key
	 */
	public static final String IS_PASSWORD = "is_save_pwd";

	/**
	 * 用户名  key
	 */
	public static final String USERNAME = "username";

	/**
	 * 密码 key
	 */
	public static final String PASSWORD = "password";

	/**
	 * 餐桌的座位号，以0开头
	 */
	public static final String[] siteNumber = {
			"001","002","003","004","005",
			"006","007","008","009","010",
			"011","012","013","014","015",
			"016","017","018","019","020",
			"021","022","023","024","025",
			"026","027","028","029","030",
			"031","032","033","034","035",
			"036","037","038","039","040",
			"041","042","043","044","045",
			"046","047","048","049","050",
			"051","052","053","054","055",
			"056","057","058","059","060",
			"061","062","063","064","065",
			"066","067","068","069","070",
			"071","072","073","074","075",
			"076","077","078","079","080",
			"081","082","083","084","085",
			"086","087","088","089","090",
			"091","092","093","094","095",
			"096","097","098","099","100"

	};

	/**
	 * 城市名称
	 */
	public static String city[] = {
			"北京",
			"上海",
			"天津",
			"重庆",
			"唐山",
			"石家庄",
			"大连",
			"哈尔滨",
			"海口",
			"长春",
			"长沙",
			"成都",
			"福州",
			"广州",
			"贵阳",
			"杭州",
			"合肥",
			"呼和浩特",
			"济南",
			"昆明",
			"拉萨",
			"兰州",
			"南昌",
			"南京",
			"南宁",
			"青岛",
			"深圳",
			"沈阳",
			"太原",
			"乌鲁木齐",
			"武汉",
			"西安",
			"西宁",
			"厦门",
			"徐州",
			"银川",
			"郑州"
	};

	/**
	 * 城市代码 城市坐标
	 */
	public static String cityCode[] = {
			"39930000,116279998",//北京
			"31399999,121470001",//上海
			"39099998,117169998",//天津
			"29520000,106480003",//重庆
			"39669998,118150001",//唐山
			"38029998,114419998",//石家庄
			"38900001,121629997",//大连
			"45750000,126769996",//哈尔滨
			"20030000,110349998",//海口
			"43900001,125220001",//长春
			"28229999,112870002",//长沙
			"30670000,104019996",//成都
			"26079999,119279998",//福州
			"23129999,113319999",//广州
			"26579999,106720001",//贵阳
			"30229999,120169998",//杭州
			"31870000,117230003",//合肥
			"40819999,111680000",//呼和浩特
			"36680000,116980003",//济南
			"25020000,102680000",//昆明
			"29657589,91132050",//拉萨
			"36040000,103879997",//兰州
			"28600000,115919998",//南昌
			"32000000,118800003",//南京
			"22819999,108349998",//南宁
			"36069999,120330001",//青岛
			"22549999,114099998",//深圳
			"41770000,123430000",//沈阳
			"37779998,112550003",//太原
			"43779998,87620002",//乌鲁木齐
			"30620000,114129997",//武汉
			"34299999,108930000",//西安
			"36619998,101769996",//西宁
			"24479999,118080001",//厦门
			"34279998,117150001",//徐州
			"38479999,106220001",//银川
			"34720001,113650001"//郑州
	};

	/**
	 * 验证邮箱是否合法
	 * @param umail
	 * @return
	 */
	public static boolean emailRule(String umail) {
		boolean result = false;
		String reg = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
		if(umail.matches(reg)){
			result = true;
		}else{
			result = false;
		}

		return result;
	}

	/**
	 * 返回 format 格式的时间字符串
	 * 时间格式为 yyyy-MM-dd HH:mm:ss
	 * yyyy 返回4位年份
	 * MM 返回2位月份
	 * dd 返回2位日
	 * 时间类同
	 * @return 相应日期类型的字符串
	 */
	public static String getCurrentDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date()).toString();
	}


	public static Bitmap getBitMapForStringURL(String urlString) {

		URL url = null;
		Bitmap bitmap = null;

		try {
			url = new URL(urlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 进度对话框
	 * @param context
	 * @param title
	 * @param message
	 * @param canCelable
	 * @param indeterminate
	 */
	public static ProgressDialog createProgressDialog(Context context, String title,
													  String message, boolean canCelable, boolean indeterminate) {
		ProgressDialog p = new ProgressDialog(context);
		p.setIcon(R.drawable.progress);
		p.setTitle(title);
		p.setMessage(message);
		p.setCancelable(canCelable);
		p.setIndeterminate(indeterminate);
		return p;
	}

	/**
	 * 传递登陆数据，添加数据到Intent中 Bundle->Intent 数据key都为data
	 * 结合 getDataFromIntent(Intent) 使用
	 * @param intent
	 * @param str id,loginid,password,nikename,phone,email,gender,create_at	
	 */
	public static void putDataIntoIntent(Intent intent, String str) {
		Bundle bundle = new Bundle();
		bundle.putString("data", str);
		intent.putExtra("data", bundle);
	}

	/**
	 * 传递登陆数据，从Intent中获取Bundle数据
	 * 结合 putDataIntoIntent(Intent, String) 使用
	 * @param intent
	 * @return String id,loginid,password,nikename,phone,email,gender,create_at	
	 */
	public static String getDataFromIntent(Intent intent) {
		Bundle bundle = intent.getBundleExtra("data");
		String res = bundle.getString("data");;
		return res;
	}


	public static boolean phoneNumberRule(String phone) {
		boolean result = false;
		long min = 13000000000L;
		long max = 18999999999L;
		long data = 0;
		try {
			data = Long.parseLong(phone);
		} catch (NumberFormatException e) {
			result = false;
			e.printStackTrace();
		}
		if(phone.length()!=11)
			result = false;
		else if(data < min || data > max)
			result = false;
		else
			result = true;
		return result;
	}
}
