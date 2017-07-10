package com.arny.arnylib.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBProvider {

    private static DBHelper dbHelper = null;

    public static long insertDB(String table, ContentValues contentValues, Context context) {
        long rowID = connectDB(context).insert(table, null, contentValues);
        disconnectDB();
        return rowID;
    }

	public static long insertOrUpdateDB(String table, ContentValues contentValues, Context context) {
		long rowID = connectDB(context).insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
		disconnectDB();
		return rowID;
	}

    public static Cursor selectDB(String table, String[] columns, String where, String orderBy, Context context) {
        return connectDB(context).query(table, columns, where, null, null, null, orderBy);
    }

    public static Cursor selectDB(String table, String[] columns, String where, String[] whereArgs, String orderBy, Context context) {
        return connectDB(context).query(table, columns, where, whereArgs, null, null, orderBy);
    }

    public static Cursor queryDB(String sqlQuery, String[] selectionArgs, Context context) {
        return connectDB(context).rawQuery(sqlQuery, selectionArgs);
    }

    public static int deleteDB(String table, String where, Context context) {
        int rowCount = connectDB(context).delete(table, where, null);
        disconnectDB();
        return rowCount;
    }

    public static int deleteDB(String table, String where, String[] whereArgs, Context context) {
        int rowCount = connectDB(context).delete(table, where, whereArgs);
        disconnectDB();
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, Context context) {
        int rowCount = connectDB(context).update(table, contentValues, where, null);
        disconnectDB();
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, String[] whereArgs, Context context) {
        int rowCount = connectDB(context).update(table, contentValues, where, whereArgs);
        disconnectDB();
        return rowCount;
    }

	public static void initDB(Context context,String name) {
		DBHelper.dbName = name;
		connectDB(context).getVersion();
		disconnectDB();
	}

    private static void disconnectDB() {
        dbHelper.close();
    }

    private static synchronized SQLiteDatabase connectDB(Context context) {
        if (dbHelper != null)
            dbHelper.close();
        dbHelper = new DBHelper(context);
        return dbHelper.getWritableDatabase();
    }

	public static int getCursorInt(Cursor cursor, String columnname) {
		return cursor.getInt(cursor.getColumnIndex(columnname));
	}


	public static String getCursorString(Cursor cursor, String columnname) {
		return cursor.getString(cursor.getColumnIndex(columnname));
	}

	public static boolean getCursorBoolean(Cursor cursor, String columnname) {
		return Boolean.parseBoolean(getCursorString(cursor, columnname));
	}

	public static double getCursorDouble(Cursor cursor, String columnname) {
		return Double.parseDouble(getCursorString(cursor, columnname));
	}
}
