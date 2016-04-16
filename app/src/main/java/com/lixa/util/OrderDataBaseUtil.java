package com.lixa.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OrderDataBaseUtil extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "order_db";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DEFAULT_ID = "_id integer primary key autoincrement";
	
	private String tableName;
	private String columns[];
	private String constraints[];
	

	public OrderDataBaseUtil(Context ctx, String tbname, String cols[], String cons[]) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		

		tableName = tbname;
		columns = cols;
		constraints = cons;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ").append(tableName);
		sql.append("(").append(DEFAULT_ID).append(",");
		for(int i = 0; i < columns.length; i++){
			if(i < columns.length -1)
				sql.append(columns[i]).append(" ").append(constraints[i]).append(",");
			else 
				sql.append(columns[i]).append(" ").append(constraints[i]);
		}
		sql.append(")");
		Log.i("CREATE_TB==>>", sql.toString());
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + tableName;
		Log.i("DROP_TB==>>", sql);
		db.execSQL(sql);
		onCreate(db);
	}

	public SQLiteDatabase openReadDB() {
		return this.getReadableDatabase();
	}
	

	public SQLiteDatabase openWriteDB() {
		return getWritableDatabase();
	}

}
