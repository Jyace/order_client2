package com.lixa.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.lixa.provide.OrderAdapter;
import com.lixa.util.OrderStringUtil;

public class OrderListActivity extends Activity {

	private Spinner orderSpinner;
	private ListView menuList;
	private Button queryMenu;
	
	private int spinner_index;
	
	private OrderAdapter orderDb;
	private Cursor cursor;
	
	private String str;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		
		str = OrderStringUtil.getDataFromIntent(getIntent());
		
		orderSpinner = (Spinner)findViewById(R.id.choise_order_type);
		menuList = (ListView)findViewById(R.id.menu_list_view);
		queryMenu = (Button)findViewById(R.id.query_order_by_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderListActivity.this,
					android.R.layout.simple_spinner_item, OrderStringUtil.orderTypeName);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		orderSpinner.setAdapter(adapter);
		
		queryMenu.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				orderDb = new OrderAdapter(OrderListActivity.this);
				Long index = orderSpinner.getSelectedItemId();
				spinner_index = index.intValue();
				
				cursor = orderDb.queryOrderListByType(OrderStringUtil.orderTypeValue[spinner_index]);
				
				ListAdapter orderAdapter = new SimpleCursorAdapter(OrderListActivity.this, R.layout.item_list_module,
						cursor, new String[]{OrderAdapter.ID, OrderAdapter.NAME,OrderAdapter.PRICE,OrderAdapter.CREATE_AT},
						new int[]{R.id.order_db_id,R.id.order_db_name,R.id.order_db_price,R.id.order_db_create_at});
				findViewById(R.id.order_title_image).setVisibility(View.VISIBLE);
				menuList.setAdapter(orderAdapter);
				orderDb.closeDB();
			}
		});
		

		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(cursor.moveToPosition(arg2)){
					cursor.moveToPosition(arg2);
					int id_index = cursor.getColumnIndex(OrderAdapter.ID);
					String id = cursor.getString(id_index);
					Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
					i.putExtra("ID", id);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			}
		});
		


		menuList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				cursor.moveToPosition(arg2);
				int desc_index = cursor.getColumnIndex(OrderAdapter.DESCRIPTION);
				String desc = cursor.getString(desc_index);
				Toast.makeText(OrderListActivity.this, desc, Toast.LENGTH_LONG).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	
		((Button)findViewById(R.id.go_list_menu_btn)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(OrderListActivity.this, OrderMainMenu.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
	}
}
