package com.arny.arnylib.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBLoader extends BaseLoader {
	private SQLiteDatabase sqLiteDatabase;//// TODO: 14.06.2017  
	public DBLoader(Context context) {
		super(context);
		sqLiteDatabase = new DBHelper(context).getWritableDatabase();
//		table, contentValues, where, whereArgs
	}

	@Override
	protected Cursor apiCall() throws  Exception {
		return null;
	}

	@Override
	protected void onForceLoad() {
		super.onForceLoad();
	}
}
