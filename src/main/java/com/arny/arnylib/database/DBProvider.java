package com.arny.arnylib.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import com.arny.arnylib.BuildConfig;
import com.arny.arnylib.utils.Utility;
import com.arny.java.utils.KtlUtilsKt;
import io.reactivex.Observable;
import org.chalup.microorm.MicroOrm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DBProvider {

    public static long insertDB(String table, ContentValues contentValues, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "insertDB: table:" + table + " contentValues:" + contentValues  );
        }
        long rowID = 0;
        try {
            rowID = connectDB(context).insert(table, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDB(context);
        return rowID;
    }

	public static int insertReplaceDB(Context context, String table, String where, String[] args, ContentValues cv){
        Cursor cursor = null;
        try {
            cursor = selectDB(table, null, where,args,null,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null && cursor.moveToFirst()) {
			return updateDB(table, cv, where,args, context);
		}else{
			return (int) insertDB(table, cv, context);
		}
	}

	public static long insertOrUpdateDB(Context context, String table, ContentValues contentValues) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "insertOrUpdateDB: table:" + table + " contentValues:" + contentValues  );
        }
        long rowID = 0;
        try {
            rowID = connectDB(context).insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDB(context);
		return rowID;
	}

    public static Cursor selectDB(String table, String[] columns, String where, String orderBy, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "selectDB: table:" + table + " columns:" + Arrays.toString(columns) + " where:"+  where + " orderBy:"+  orderBy);
        }
        Cursor query = null;
        try {
            query = connectDB(context).query(table, columns, where, null, null, null, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }

    public static Cursor selectDB(String table, String[] columns, String where, String[] whereArgs, String orderBy, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "selectDB: table:" + table + " columns:" + Arrays.toString(columns) + " where:"+  where  + " whereArgs:"+ Arrays.toString(whereArgs) + " orderBy:"+  orderBy);
        }
        Cursor query = null;
        try {
            query = connectDB(context).query(table, columns, where, whereArgs, null, null, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }

    public static Cursor queryDB(String sqlQuery, String[] selectionArgs, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "queryDB: query:" + sqlQuery + " selectionArgs:" + Arrays.toString(selectionArgs));
        }
        Cursor cursor = null;
        try {
            cursor = connectDB(context).rawQuery(sqlQuery, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public static int deleteDB(String table, String where, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "deleteDB: table:" + table + " where:" + where);
        }
        int rowCount = 0;
        try {
            rowCount = connectDB(context).delete(table, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDB(context);
        return rowCount;
    }

    public static int deleteDB(String table, String where, String[] whereArgs, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "deleteDB: table:" + table + " where:" + where + "=whereArgs:" + Arrays.toString(whereArgs));
        }
        int rowCount = 0;
        try {
            rowCount = connectDB(context).delete(table, where, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDB(context);
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "updateDB: table:" + table + " contentValues:" + contentValues + " where:" + where);
        }
        int rowCount = 0;
        try {
            rowCount = connectDB(context).update(table, contentValues, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDB(context);
        return rowCount;
    }

    public static int updateDB(String table, ContentValues contentValues, String where, String[] whereArgs, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(DBProvider.class.getSimpleName(), "updateDB: table:" + table + " contentValues:" + contentValues + " where:" + where + " whereArgs:" + Arrays.toString(whereArgs));
        }
        int rowCount = 0;
        try {
            rowCount = connectDB(context).update(table, contentValues, where, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static SQLiteDatabase connectDB(Context context) {
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

    public static <T> ArrayList<T> getCursorObjectList(Cursor cursor, Class<? extends T> clazz) {
        ArrayList<T> queue = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    queue.add(new MicroOrm().fromCursor(cursor, clazz));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return queue;
    }

    public static <T> Observable<ArrayList<T>> getCursorObjectsListRx(Cursor cursor, Class<? extends T> clazz) {
        return Observable.create(e -> {
            e.onNext(getCursorObjectList(cursor, clazz));
            e.onComplete();
        });
    }

    public static <T> Observable<ArrayList<T>> getObjectsListRx(Context context, String table, String[] columns, String where, String[] whereArgs, String orderBy, Class<? extends T> clazz) {
        return Observable.create(e -> {
            e.onNext(getCursorObjectList(selectDB(table,columns,where,whereArgs,orderBy,context), clazz));
            e.onComplete();
        });
    }

    public static <T> Observable<T> getCursorObjectRx(Cursor cursor, Class<?> clazz) {
        return Observable.create(e -> {
            e.onNext(getCursorObject(cursor, clazz));
            e.onComplete();
        });
    }

    public static <T> Observable<T> getObjectRx(Context context, String table, String[] columns, String where, String[] whereArgs, String orderBy, Class<?> clazz) {
        return Observable.create(e -> {
            e.onNext(getCursorObject(selectDB(table,columns,where,whereArgs,orderBy,context), clazz));
            e.onComplete();
        });
    }

    public static <T> T getCursorObject(Cursor cursor, Class<?> clazz) {
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                T obj = (T) new MicroOrm().fromCursor(cursor, clazz);
                cursor.close();
                return obj;
            }
        }
        return null;
    }

    public static <T> long saveObject(Context context, String table, T o) {
        ContentValues values = new MicroOrm().toContentValues(o);
		return insertOrUpdateDB(context,table, values);
	}

    public static <T> Observable<Long> saveObjectRx(Context context, String table, T o) {
        return Observable.create(e -> {
            e.onNext(insertOrUpdateDB(context, table, new MicroOrm().toContentValues(o)));
            e.onComplete();
        });
    }

	public static String convertToSQLTable(Object o, String[] fldsToSave) {
		Collection<Field> fields = new ArrayList<>();
		if (fldsToSave.length > 0) {
			for (Field field : Utility.getFields(o.getClass())) {
				for (String s : fldsToSave) {
					if (s.equalsIgnoreCase(field.getName())) {
						fields.add(field);
					}
				}
			}
		} else {
			fields = Utility.getFields(o.getClass());
		}
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS `");
		builder.append(getSqlTable(o.getClass()));
		builder.append("` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT, ");
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				String fldType = field.getType().getSimpleName();
				String msg = " `" + field.getName() + "` " + KtlUtilsKt.getSQLType(fldType) + ",";
				builder.append(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		builder.append(")");
		String res = builder.toString();
		res = res.replaceAll("\\,\\)$", ");");
		return res;
	}

	public static <T> String fillSQLTable(List<T> objects, String[] fldsToSave) {
		Object o = objects.get(0);
		Collection<Field> fields = new ArrayList<>();
		if (fldsToSave.length > 0) {
			for (Field field : Utility.getFields(o.getClass())) {
				for (String s : fldsToSave) {
					if (s.equalsIgnoreCase(field.getName())) {
						fields.add(field);
					}
				}
			}
		} else {
			fields = Utility.getFields(o.getClass());
		}
		StringBuilder preBuilder = new StringBuilder();
		preBuilder.append("INSERT INTO `");
		preBuilder.append(o.getClass().getSimpleName());
		preBuilder.append("` (");
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				String msg = field.getName();
				preBuilder.append(msg);
				preBuilder.append(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		preBuilder.append(")");
		preBuilder.deleteCharAt(preBuilder.length() - 2);
		preBuilder.append(" VALUES(");
		String preFields = preBuilder.toString();
		StringBuilder res = new StringBuilder();
		for (Object object : objects) {
			res.append(preFields);
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					String cls = field.getType().getSimpleName();
					Object val = field.get(object);
					if (cls.equalsIgnoreCase("String")) {
						res.append("'");
						String valRes = val == null ? "" : String.valueOf(val);
						res.append(valRes);
						res.append("'");
					} else {
						String valRes = val == null ? "0" : String.valueOf(val);
						res.append(valRes);
					}
					res.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
				field.setAccessible(false);
			}
			res.deleteCharAt(res.length() - 1);
			res.append(");\n");
		}
		return res.toString();
	}

	@NonNull
	public static String getSqlTable(Class<?> aClass) {
		return aClass.getSimpleName().toLowerCase();
	}
}
