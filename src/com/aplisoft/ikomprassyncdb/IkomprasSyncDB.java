package com.aplisoft.ikomprassyncdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IkomprasSyncDB {
	public static final String DB_NAME = "syncdb.db";
	public static final int SCHEMA_VERSION = 1;
	
	public static final String SQL_CREATE_TABLE =
			"CREATE TABLE stocity ("+
			"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"name text)";
	public static final String SQL_DROP_TABLE =
			"DROP TABLE IF EXISTS stocity";
	
	private Context mContext;


	public SyncDBHelper mDBHelper;
	public SQLiteDatabase mDB;
	
	private final class SyncDBHelper extends SQLiteOpenHelper {
		public SyncDBHelper(Context context) {
			super(context, DB_NAME, null, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DROP_TABLE);
			onCreate(db);			
		}
			
	}

	public IkomprasSyncDB(Context context) {
		mContext = context;
		mDBHelper = new SyncDBHelper(mContext);
	}
	public void openWrite(){
		//mDBHelper = new SyncDBHelper(mContext);
		mDB = mDBHelper.getWritableDatabase();
		
	}
	public void openRead(){
		//mDBHelper = new SyncDBHelper(mContext);
		mDB = mDBHelper.getReadableDatabase();
	}
}
