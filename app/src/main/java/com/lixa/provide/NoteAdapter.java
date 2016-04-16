package com.lixa.provide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lixa.util.OrderDataBaseUtil;
import com.lixa.util.OrderStringUtil;


public class NoteAdapter {
	

	private String tableName = "notes";

	public static final String ID = "_id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String LOGINID = "loginid";
	public static final String REFDATE = "refdate";
	public static final String CREATE_AT = "create_at";
	
	private SQLiteDatabase sdb ;
	private OrderDataBaseUtil orderDB;
	
	public NoteAdapter(Context context){
		orderDB = new OrderDataBaseUtil(context, tableName, new String[]{TITLE, CONTENT, LOGINID, REFDATE,CREATE_AT},
				new String[]{OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, 
					OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL});

		sdb = orderDB.openWriteDB();

	}
	

	public void openDB(){
		sdb = orderDB.openWriteDB();
	}
	

	public long saveNote(String title, String content, String loginid,String refDate,  String createAt){
		ContentValues values = new ContentValues();
		
		values.put(TITLE, title);
		values.put(CONTENT, content);
		values.put(LOGINID, loginid);
		values.put(REFDATE, refDate);
		values.put(CREATE_AT, createAt);
		
		long tag = sdb.insert(tableName, null, values);
		return tag;
		
	}
	

	public Cursor queryNotes(String date){
		
		Cursor cur = sdb.query(tableName, new String[]{ID, TITLE, CONTENT, LOGINID, REFDATE, CREATE_AT},
				REFDATE+"=?", new String[]{date}, null, null, "create_at desc");
		
		return cur;
	}
	

	public int queryNumbers(String date) {
		Cursor cur = sdb.query(tableName, new String[]{ID},
				REFDATE+"=?", new String[]{date}, null, null, null);
		
		return cur.getCount();
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

}
