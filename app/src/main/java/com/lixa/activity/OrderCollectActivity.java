package com.lixa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lixa.provide.OrderAdapter;
import com.lixa.util.OrderStringUtil;

public class OrderCollectActivity extends Activity {
	private ListView collectList;
	private TextView tv_bg;

	private String str;

	private ProgressDialog proDlg;
	private OrderAdapter orderDB;

	private ListAdapter adapter;

	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_collect);

		collectList = (ListView)findViewById(R.id.collect_order_menu_list);
		tv_bg = (TextView)findViewById(R.id.collect_order_image);
		orderDB = new OrderAdapter(OrderCollectActivity.this);

		str = OrderStringUtil.getDataFromIntent(getIntent());

		proDlg = OrderStringUtil.createProgressDialog(OrderCollectActivity.this, "加载数据", "正在加载数据，请稍后...", true, false);
		proDlg.show();

		new Thread(){
			@Override
			public void run() {

				cursor = orderDB.queryOrderListifCollect();
				Log.i("TAB___",cursor.getCount()+"");
				Message m = new Message();
				m.what = OrderStringUtil.OK;
				handler.sendMessage(m);
			}
		}.start();
	}

	private void setDataForList() {
		adapter = new SimpleCursorAdapter(OrderCollectActivity.this, R.layout.item_list_module,
				cursor, new String[]{OrderAdapter.ID, OrderAdapter.NAME,OrderAdapter.PRICE,OrderAdapter.CREATE_AT},
				new int[]{R.id.order_db_id,R.id.order_db_name,R.id.order_db_price,R.id.order_db_create_at});
		orderDB.closeDB();
		collectList.setAdapter(adapter);
		tv_bg.setVisibility(View.VISIBLE);

		/**
		 * 单击事件
		 */
		collectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if(cursor.moveToPosition(arg2)){
					cursor.moveToPosition(arg2);
					int id_index = cursor.getColumnIndex(OrderAdapter.ID);
					String id = cursor.getString(id_index);
					Intent i = new Intent(OrderCollectActivity.this, OrderDetailActivity.class);
					i.putExtra("ID", id);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			}
		});

		/**
		 * 选中事件
		 */
		collectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				cursor.moveToPosition(arg2);
				int desc_index = cursor.getColumnIndex(OrderAdapter.DESCRIPTION);
				String desc = cursor.getString(desc_index);
				Toast.makeText(OrderCollectActivity.this, desc, Toast.LENGTH_LONG).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	};

	private Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			proDlg.dismiss();
			switch(msg.what){
				case OrderStringUtil.OK:
					setDataForList();
					break;
				default:
					break;
			}
		}
	};
}
