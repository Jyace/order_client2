package com.lixa.provide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lixa.util.OrderDataBaseUtil;
import com.lixa.util.OrderStringUtil;


public class OrderAdapter {
	

	private String tableName = "orders";
	

	public static final String ID = "_id";
	public static final String ORDER_ID = "order_id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String VERSION = "version";
	public static final String PRICE = "price";
	public static final String IMAGE_PATH = "image_path";
	public static final String COLLECT = "collect";
	public static final String CREATE_AT = "create_at";
	
	private SQLiteDatabase sdb ;
	private OrderDataBaseUtil orderDB;
	
	public OrderAdapter(Context context){
		orderDB = new OrderDataBaseUtil(context, tableName, new String[]{ORDER_ID, NAME, DESCRIPTION, TYPE, PRICE, VERSION, IMAGE_PATH, COLLECT, CREATE_AT},
				new String[]{OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, 
					OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, 
					OrderStringUtil.TEXT_NOT_NULL});
		sdb = orderDB.openWriteDB();
	}
	

	public void openDB(){
		sdb = orderDB.openWriteDB();
	}
	

	public long saveOrder(String orderId, String name, String desc, String type, String price, String version, String imagePath, String createAt){
		ContentValues values = new ContentValues();
		
		values.put(ORDER_ID, orderId);
		values.put(NAME, name);
		values.put(DESCRIPTION, desc);
		values.put(TYPE, type);
		values.put(PRICE, price);
		values.put(VERSION, version);
		values.put(IMAGE_PATH, imagePath);
		values.put(COLLECT, "-1");
		values.put(CREATE_AT, createAt);

		long tag = sdb.insert(tableName, null, values);
		return tag;
		
	}

	public int deleteOrder(String orderId){
		return sdb.delete(tableName, ORDER_ID + "=?", new String[]{orderId});
	}

	public int updateOrder(String name, String desc, String type, String price, String version, String imagePath, String orderId){
		
		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(DESCRIPTION, desc);
		values.put(TYPE, type);
		values.put(PRICE, price);
		values.put(VERSION, version);
		values.put(IMAGE_PATH, imagePath);
		
		return sdb.update(tableName, values, ORDER_ID + "=?", new String[]{orderId});
		
	}
	

	public Cursor queryOrder(){
		
		Cursor cur = sdb.query(tableName, new String[]{ID, ORDER_ID, NAME, DESCRIPTION, TYPE, PRICE, VERSION, CREATE_AT},
				null, null, null, null, "create_at desc");
		
		return cur;
	}

	public Cursor queryOrderVersion(){
		Cursor cur = sdb.query(tableName, new String[]{ORDER_ID, VERSION}, null, null, null, null, null);
		return cur;
	}

	public Cursor queryOrderListByType(String type){
		Cursor cursor = sdb.query(tableName, new String[]{ID, NAME, DESCRIPTION, PRICE, CREATE_AT}, TYPE+"=?", new String[]{type}, null, null, null);
		return cursor;
	}
	

	public Cursor queryOrderListifCollect(){
		Cursor cursor = sdb.query(tableName, new String[]{ID, NAME, DESCRIPTION, PRICE, CREATE_AT}, COLLECT+"=?", new String[]{"0"}, null, null, null);
		return cursor;
	}

	public Cursor queryOrderListById(String _id){
		Cursor cursor = sdb.query(tableName, new String[]{ID, ORDER_ID, NAME, PRICE, DESCRIPTION, CREATE_AT, COLLECT, IMAGE_PATH}, ID+"=?", new String[]{_id}, null, null, null);
		return cursor;
	}
	

	public void closeDB(Cursor cursor) {
		if(null != cursor && !cursor.isClosed())
			cursor.close();
		closeDB();
	}

	public void closeDB() {
		if(null != sdb && sdb.isOpen())
			sdb.close();
	}


	public int collectOrder(String id) {
		
		ContentValues values = new ContentValues();
		values.put(COLLECT, "0");
		
		return sdb.update(tableName, values, ID + "=?", new String[]{id});
	}
	

	public int disCollectOrder(String id) {
		
		ContentValues values = new ContentValues();
		values.put(COLLECT, "-1");
		
		return sdb.update(tableName, values, ID + "=?", new String[]{id});
	}
}
