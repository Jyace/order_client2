package com.lixa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lixa.provide.NoteAdapter;
import com.lixa.util.CalendarUtil;
import com.lixa.util.OrderStringUtil;

public class OrderCalendarActivity extends Activity {

	private String str;
	private NoteAdapter noteDB;

	private TextView[] calendarText;
	private TextView calendarDescription;

	private ImageButton previousYear;
	private ImageButton nextYear;
	private ImageButton nowDate;
	private ImageButton previousMonth;
	private ImageButton nextMonth;

	private String year;
	private String month;
	private String day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_calendar);
		str = OrderStringUtil.getDataFromIntent(getIntent());

		year = OrderStringUtil.getCurrentDate("yyyy");
		month = OrderStringUtil.getCurrentDate("MM");
		day = OrderStringUtil.getCurrentDate("dd");

		/**
		 * 初始化组件
		 */
		initObject();

		/**
		 * 给组件设置数据
		 */
		setDataToObject();

		/**
		 * 添加组件监听事件
		 */
		addListenerOnObject();

		/**
		 * 给文本添加按钮
		 */
		setTextListener();

	}

	/**
	 * 给文本添加按钮
	 */
	private void setTextListener() {

	}

	/**
	 * 添加组件监听事件
	 */
	private void addListenerOnObject() {
		previousYear.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!"1901".equals(year)){
					year = (Integer.parseInt(year) - 1) + "";
					setDataToObject();
				}
			}
		});
		nextYear.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!"2100".equals(year)){
					year = (Integer.parseInt(year) + 1) + "";
					setDataToObject();
				}
			}
		});
		previousMonth.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("1".equals(month)){
					if(!"1901".equals(year)){
						year = (Integer.parseInt(year) - 1) + "";
						month = "12";
					}
				} else {
					month = (Integer.parseInt(month) - 1) + "";
				}
				setDataToObject();
			}
		});
		nextMonth.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("12".equals(month)){
					if(!"2100".equals(year)){
						year = (Integer.parseInt(year) + 1) + "";
						month = "1";
					}
				} else {
					month = (Integer.parseInt(month) + 1) + "";
				}
				setDataToObject();
			}
		});
		nowDate.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				year = OrderStringUtil.getCurrentDate("yyyy");
				month = OrderStringUtil.getCurrentDate("MM");
				day = OrderStringUtil.getCurrentDate("dd");
				setDataToObject();
			}
		});
	}

	/**
	 * 给组件设置数据
	 */
	private void setDataToObject() {
		noteDB = new NoteAdapter(OrderCalendarActivity.this);
		CalendarUtil cu = new CalendarUtil();
		String chineseDay = "";
		String chineseMonth = "";
		chineseMonth = cu.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
		chineseDay = cu.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
		StringBuilder buf = new StringBuilder();
		buf.append("当前：").append(year).append("-")
				.append(month).append("-").append(day).append("，农历：" + chineseMonth + chineseDay);
		calendarDescription.setText(buf);
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month);

		int days = CalendarUtil.daysInGregorianMonth(y, m);
		int start = CalendarUtil.dayOfWeek(y, m, 1);

		/**
		 * 前面为空部分
		 */
		for(int i = 0; i < start; i ++){
			calendarText[i].setText(" \n ");
			calendarText[i].setBackgroundResource(0);
		}

		/**
		 * 日历正文
		 */
		for(int i = 0; i < days; i ++){
			chineseDay = cu.getChineseDay(y,m,Integer.parseInt(CalendarUtil.daysOfMonth[i]));
			if("初一".equals(chineseDay))
				chineseDay = cu.getChineseMonth(y,m,Integer.parseInt(CalendarUtil.daysOfMonth[i]));
			calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.calendar_text_bg));
			String text = CalendarUtil.daysOfMonth[i] + "\n" + chineseDay;
			calendarText[start+ i - 1].setText(text);

			/**
			 * 周末判断
			 */
			if(restDay(year, month, CalendarUtil.daysOfMonth[i])){
				calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.rest_color));
			}

			/**
			 * 备忘录判断
			 */
			if(hansSomthing(year, month, CalendarUtil.daysOfMonth[i])){
				calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.red));
			}

			/**
			 * 点击事件
			 */
			calendarText[start+ i - 1].setOnClickListener(new TextView.OnClickListener() {
				@Override
				public void onClick(View v) {
					CalendarUtil cutil = new CalendarUtil();
					String chineseDay = "";
					String chineseMonth = "";
					String d = ((TextView)v).getText().toString().substring(0, ((TextView)v).getText().toString().lastIndexOf('\n'));
					chineseMonth = cutil.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					chineseDay = cutil.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					StringBuilder buf = new StringBuilder();
					buf.append("当前：").append(year).append("-")
							.append(month).append("-").append(d).append("，农历：").append(chineseMonth).append(chineseDay);
					calendarDescription.setText(buf);
				}
			});

			/**
			 * 长时间点击事件
			 */
			calendarText[start+ i - 1].setOnLongClickListener(new TextView.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					CalendarUtil c = new CalendarUtil();
					StringBuilder date = new StringBuilder();
					String d = ((TextView)v).getText().toString().substring(0, ((TextView)v).getText().toString().lastIndexOf('\n'));
					String chineseMonth = c.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					String chineseDay = c.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					date.append(year).append("-").append(month).append("-").append(d)
							.append("，农历：").append(chineseMonth).append(chineseDay);
					Intent i  = new Intent(OrderCalendarActivity.this, OrderNoteActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					i.putExtra("date", date.toString());
					i.putExtra("addDate", year + "-" + month + "-" + d);
					startActivity(i);
					return false;
				}
			});
		}
		noteDB.closeDB();
		/**
		 * 后面为空部分
		 */
		for(int i = days+start-1; i < calendarText.length; i ++){
			calendarText[i].setText(" \n ");
			calendarText[i].setBackgroundResource(0);
		}

	}

	/**
	 * 备忘录判断
	 */
	private boolean hansSomthing(String y, String m, String d) {
		int count = noteDB.queryNumbers(y + "-" + m + "-" + d);
		if(count > 0)
			return true;
		return false;
	}

	/**
	 * 周末判断
	 */
	private boolean restDay(String y, String m, String d) {

		int dow = CalendarUtil.dayOfWeek(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));

		boolean flag = false;
		// 周末
		if(1 == dow || 7 == dow)
			flag = true;

		return flag;
	}

	/**
	 * 初始化组件
	 */
	private void initObject() {

		// 描述文本
		calendarDescription =(TextView)findViewById(R.id.calendar_text_description);

		// 按钮
		previousYear = (ImageButton)findViewById(R.id.calendar_year_previous_btn);
		nextYear = (ImageButton)findViewById(R.id.calendar_year_next_btn);
		previousMonth = (ImageButton)findViewById(R.id.calendar_month_previous_btn);
		nextMonth = (ImageButton)findViewById(R.id.calendar_month_next_btn);
		nowDate = (ImageButton)findViewById(R.id.calendar_now_date_btn);

		// 日期
		int i = 0;
		calendarText = new TextView[42];
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_1);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_2);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_3);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_4);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_5);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_6);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_7);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_8);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_9);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_10);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_11);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_12);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_13);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_14);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_15);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_16);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_17);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_18);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_19);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_20);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_21);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_22);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_23);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_24);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_25);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_26);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_27);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_28);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_29);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_30);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_31);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_32);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_33);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_34);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_35);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_36);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_37);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_38);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_39);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_40);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_41);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_42);
	}
}
