package com.lixa.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import com.lixa.provide.NoteAdapter;
import com.lixa.util.OrderStringUtil;

public class OrderNoteActivity extends TabActivity {

	private final String ADD_NOTE = "add_note";
	private final String QUERY_NOTE = "query_note";
	private String str;
	private String date;
	private String addDate;
	private NoteAdapter noteDb;

	private EditText noteAddTitle;
	private EditText noteAddContent;
	private EditText noteQueryDate;

	private ListView noteQueryList;
	private Cursor cursor;

	private Button noteAddSubmit;
	private Button noteQuerySubmit;

	private TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_note);

		noteDb = new NoteAdapter(OrderNoteActivity.this);

		str = OrderStringUtil.getDataFromIntent(getIntent());
		date = getIntent().getStringExtra("date");
		addDate = getIntent().getStringExtra("addDate");
		((TextView)findViewById(R.id.note_date)).setText("时间："+date);

		tabHost = getTabHost();
		TabSpec addNote = tabHost.newTabSpec(ADD_NOTE);
		addNote.setIndicator("添加留言", getResources().getDrawable(R.drawable.note_add));
		addNote.setContent(R.id.note_add_layout);
		tabHost.addTab(addNote);

		tabHost = getTabHost();
		TabSpec queryNote = tabHost.newTabSpec(QUERY_NOTE);
		queryNote.setIndicator("查询留言", getResources().getDrawable(R.drawable.note_query));
		queryNote.setContent(R.id.note_query_layout);
		tabHost.addTab(queryNote);

		tabHost.setCurrentTab(0);

		/**
		 * 初始化组件
		 */
		initObject();

		/**
		 * 添加监听事件
		 */
		setListenerObject();
	}

	/**
	 * 添加监听事件
	 */
	private void setListenerObject() {
		/**
		 * 添加备忘录
		 */
		noteAddSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = noteAddTitle.getText().toString().trim();
				String content = noteAddContent.getText().toString().trim();
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderNoteActivity.this);
				if("".equals(title)){
					builder.setTitle("标题为空").setMessage("标题为空，请输入标题").setIcon(R.drawable.alert_wanring)
							.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if("".equals(content)){
					builder.setTitle("内容为空").setMessage("留言内容为空，请输入内容").setIcon(R.drawable.alert_wanring)
							.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				String loginid = str.split(",")[1];
				String createAt = OrderStringUtil.getCurrentDate("yyyy-MM-dd");
				// 添加备忘录
				noteDb.closeDB();
				noteDb.openDB();
				Long r = noteDb.saveNote(title, content, loginid,addDate,createAt);
				noteDb.closeDB();
				if(r.intValue()!=-1){
					builder.setTitle("添加成功").setMessage("添加成功，您可以查看留言了！").setIcon(R.drawable.alert_add)
							.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						//Intent i = new Intent(OrderNoteActivity.this, OrderCalendarActivity.class);
						//OrderStringUtil.putDataIntoIntent(i, str);
						//startActivity(i);
							tabHost.setCurrentTab(1);
						}
					}).show();
				}else{
					builder.setTitle("成功").setMessage("添加成功，可以查看留言了！").setIcon(R.drawable.alert_error)
							.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {


							tabHost.setCurrentTab(1);

						}
					}).show();
				}
			}
		});

		/**
		 * 查询备忘录
		 */
		noteQuerySubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderNoteActivity.this);
				String date = noteQueryDate.getText().toString().trim();
				if("".equals(date)){
					builder.setTitle("时间为空").setMessage("时间为空，请输入时间").setIcon(R.drawable.alert_wanring)
							.setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				noteDb.closeDB();
				noteDb.openDB();
				cursor = noteDb.queryNotes(date);
				ListAdapter adapter = new SimpleCursorAdapter(OrderNoteActivity.this,
						R.layout.item_list_module, cursor,new String[]{NoteAdapter.ID, NoteAdapter.TITLE, NoteAdapter.CREATE_AT},
						new int[]{R.id.note_id,R.id.note_title,R.id.note_ref_date});
				noteQueryList.setAdapter(adapter);
				noteDb.closeDB();
			}
		});
		noteQueryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				cursor.moveToPosition(arg2);
				int title_index = cursor.getColumnIndex(NoteAdapter.TITLE);
				String t = cursor.getString(title_index);
				int content_index = cursor.getColumnIndex(NoteAdapter.CONTENT);
				String c = cursor.getString(content_index);

				View v = getLayoutInflater().inflate(R.layout.note_content, null);

				TextView title = (TextView)v.findViewById(R.id.note_detail_title);
				TextView content = (TextView)v.findViewById(R.id.note_detail_content);

				title.setText("标题：" + t);
				content.setText("内容：\n    " + c);

				AlertDialog.Builder builder = new AlertDialog.Builder(OrderNoteActivity.this);
				builder.setTitle("备忘录内容").setIcon(R.drawable.alert_ok).setView(v)
						.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
			}

		});
	}

	/**
	 * 初始化组件
	 */
	private void initObject() {
		noteAddTitle = (EditText)findViewById(R.id.note_add_title);
		noteAddContent = (EditText)findViewById(R.id.note_add_content);
		noteQueryDate = (EditText)findViewById(R.id.note_query_date);
		noteQueryList = (ListView)findViewById(R.id.row_note_query_list);
		noteAddSubmit = (Button)findViewById(R.id.note_add_submit);
		noteQuerySubmit = (Button)findViewById(R.id.note_query_submit);
		findViewById(R.id.note_add_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(OrderNoteActivity.this, OrderMainMenu.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
		findViewById(R.id.note_query_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(OrderNoteActivity.this, OrderMainMenu.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
	}
}
