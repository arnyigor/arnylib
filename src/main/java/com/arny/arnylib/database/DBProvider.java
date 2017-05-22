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

    public static void isDB(Context context) {
        connectDB(context).getVersion();
        disconnectDB();
    }

    private static void disconnectDB() {
        dbHelper.close();
    }

    private static SQLiteDatabase connectDB(Context context) {
        if (dbHelper != null)
            dbHelper.close();
        dbHelper = new DBHelper(context);
        return dbHelper.getWritableDatabase();
    }
}