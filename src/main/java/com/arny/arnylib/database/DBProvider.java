package com.arny.arnylib.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import org.chalup.microorm.MicroOrm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DBProvider {

    private static DBHelper dbHelper = null;

    public static long insertDB(String table, ContentValues contentValues, Context context) {
        long rowID = connectDB(context).insert(table, null, contentValues);
        disconnectDB(context);
        return rowID;
    }

	public static int insertReplaceDB(Context context, String table, String where, String[] args, ContentValues cv){
		Cursor cursor = selectDB(table, null, where,args,null,context);
//		String dumb = DroidUtils.dumpCursor(cursor);
		if (cursor != null && cursor.moveToFirst()) {
			return updateDB(table, cv, where,args, context);
		}else{
			return (int) insertDB(table, cv, context);
		}
	}

	public static long insertOrUpdateDB(Context context, String table, ContentValues contentValues) {
		long rowID = connectDB(context).insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
		disconnectDB(context);
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
        disconnectDB(context);
        return rowCount;
    }

    public static int deleteDB(String table, String where, String[] whereArgs, Context context) {
        int rowCount = connectDB(context).delete(table, where, whereArgs);
        disconnectDB(context);
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, Context context) {
        int rowCount = connectDB(context).update(table, contentValues, where, null);
        disconnectDB(context);
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, String[] whereArgs, Context context) {
        int rowCount = connectDB(context).update(table, contentValues, where, whereArgs);
        disconnectDB(context);
        return rowCount;
    }

	public static void initDB(Context context,String name,int version) {
		DBHelper.dbName = name;
		DBHelper.dbVersion = version;
		connectDB(context).getVersion();
		disconnectDB(context);
	}

    private static void disconnectDB(Context context) {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private static synchronized SQLiteDatabase connectDB(Context context) {
        return DBHelper.getInstance(context).getWritableDatabase();
    }

	public static int getCursorInt(Cursor cursor, String columnname) {
		return cursor.getInt(cursor.getColumnIndex(columnname));
	}


	public static long getCursorLong(Cursor cursor, String columnname) {
		return cursor.getLong(cursor.getColumnIndex(columnname));
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


    public static <T> ArrayList<T> getCursorObjectList(Cursor cursor, Class<?> clazz) {
        ArrayList<T> queue = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    queue.add((T)new MicroOrm().fromCursor(cursor, clazz));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return queue;
    }

    public static <T> T getCursorObject(Cursor cursor, Class<?> clazz) {
        List<T> queue = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                T obj = (T) new MicroOrm().fromCursor(cursor, clazz);
                cursor.close();
                return obj;
            }
        }
        return null;
    }

    public static  <T> void saveObject(Context context, String table, T o) {
		ContentValues values = new MicroOrm().toContentValues(o);
		insertOrUpdateDB(context,table, values);
	}

    public static <T> long saveObjectRow(Context context, String table, T o) {
        ContentValues values = new MicroOrm().toContentValues(o);
        return insertOrUpdateDB(context,table, values);
    }

	public static <T> Callable<T> getCallable(final String table, final Class<T> clazz, final Context context) {
		return new Callable<T>() {
			@Override
			public T call() {
				Cursor curs = connectDB(context).query(table, null, null, null, null, null, null);
				return getCursorObject(curs, clazz);
			}
		};
	}

	public static <T> Observable<T> makeObservable(final String table, final Class<Object> clazz, final Context context) {
		return Observable.defer(new Callable<ObservableSource<? extends T>>() {
			@Override
			public ObservableSource<? extends T> call() throws Exception {
				return Observable.fromCallable(getCallable(table, clazz, context)).map(new Function<Object, T>() {
					@Override
					public T apply(Object o) throws Exception {
						return (T)o;
					}
				});
			}
		});
	}
}
