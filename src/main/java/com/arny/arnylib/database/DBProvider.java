package com.arny.arnylib.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import com.arny.arnylib.BuildConfig;
import com.arny.arnylib.files.FileUtils;
import com.arny.arnylib.utils.Stopwatch;
import com.arny.arnylib.utils.ToastMaker;
import com.arny.arnylib.utils.Utility;
import com.arny.java.utils.UtilsKt;
import io.reactivex.Observable;
import org.chalup.microorm.MicroOrm;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class DBProvider {

	public static long insertDB(String table, ContentValues contentValues, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "insertDB: table:" + table + " contentValues:" + contentValues);
		long rowID = 0;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowID = db.insert(table, null, contentValues);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowID;
	}

	public static int insertReplaceDB(Context context, String table, String where, String[] args, ContentValues cv) {
		Log.d(DBProvider.class.getSimpleName(), "insertReplaceDB: table:" + table + " ContentValues:" + cv + " where:" + where + " args:" + Arrays.toString(args));
		Cursor cursor = selectDB(table, null, where, args, null, context);
		if (cursor != null && cursor.moveToFirst()) {
			return updateDB(table, cv, where, args, context);
		} else {
			return (int) insertDB(table, cv, context);
		}
	}

	public static long insertOrUpdateDB(Context context, String table, ContentValues contentValues) {
		Log.d(DBProvider.class.getSimpleName(), "insertOrUpdateDB: table:" + table + " contentValues:" + contentValues);
		long rowID = 0;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowID = db.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowID;
	}

	public static Cursor selectDB(String table, String[] columns, String where, String orderBy, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "selectDB: table:" + table + " columns:" + Arrays.toString(columns) + " where:" + where + " orderBy:" + orderBy);
		return connectDB(context).query(table, columns, where, null, null, null, orderBy);
	}

	public static Cursor selectDB(String table, String[] columns, String where, String[] whereArgs, String orderBy, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "selectDB: table:" + table + " columns:" + Arrays.toString(columns) + " where:" + where + " whereArgs:" + Arrays.toString(whereArgs) + " orderBy:" + orderBy);
		return connectDB(context).query(table, columns, where, whereArgs, null, null, orderBy);
	}

	public static Cursor queryDB(String sqlQuery, String[] selectionArgs, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "queryDB: query:" + sqlQuery + " selectionArgs:" + Arrays.toString(selectionArgs));
		return connectDB(context).rawQuery(sqlQuery, selectionArgs);
	}

	public static int deleteDB(String table, String where, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "deleteDB: table:" + table + " where:" + where);
		int rowCount;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowCount = db.delete(table, where, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowCount;
	}

	public static int deleteDB(String table, String where, String[] whereArgs, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "deleteDB: table:" + table + " where:" + where + "=whereArgs:" + Arrays.toString(whereArgs));
		int rowCount = 0;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowCount = db.delete(table, where, whereArgs);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowCount;
	}

	public static int updateDB(String table, ContentValues contentValues, String where, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "updateDB: table:" + table + " contentValues:" + contentValues + " where:" + where);
		int rowCount = 0;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowCount = db.update(table, contentValues, where, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowCount;
	}

	public static int updateDB(String table, ContentValues contentValues, String where, String[] whereArgs, Context context) {
		Log.d(DBProvider.class.getSimpleName(), "updateDB: table:" + table + " contentValues:" + contentValues + " where:" + where + " whereArgs:" + Arrays.toString(whereArgs));
		int rowCount = 0;
		SQLiteDatabase db = connectDB(context);
		db.beginTransaction();
		rowCount = db.update(table, contentValues, where, whereArgs);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowCount;
	}

	public static void initDB(Context context, String name, int version) {
		DBHelper.dbName = name;
		DBHelper.dbVersion = version;
		SQLiteDatabase db = connectDB(context);
		db.getVersion();
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
		return Observable.fromCallable(() -> getCursorObjectList(cursor, clazz));
	}

	public static <T> Observable<ArrayList<T>> getObjectsListRx(Context context, String table, String[] columns, String where, String[] whereArgs, String orderBy, Class<? extends T> clazz) {
		return Observable.fromCallable(() -> getCursorObjectList(selectDB(table, columns, where, whereArgs, orderBy, context), clazz));
	}

	public static <T> Observable<T> getCursorObjectRx(Cursor cursor, Class<?> clazz) {
		return Observable.fromCallable(() -> getCursorObject(cursor, clazz));
	}

	public static <T> Observable<T> getObjectRx(Context context, String table, String[] columns, String where, String[] whereArgs, String orderBy, Class<?> clazz) {
		return Observable.fromCallable(() -> getCursorObject(selectDB(table, columns, where, whereArgs, orderBy, context), clazz));
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
		return insertOrUpdateDB(context, table, values);
	}

	public static <T> Observable<Long> saveObjectRx(Context context, String table, T o) {
		return Observable.fromCallable(() -> insertOrUpdateDB(context, table, new MicroOrm().toContentValues(o)));
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
				String msg = " `" + field.getName() + "` " + UtilsKt.getSQLType(fldType) + ",";
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

	/**
	 * Сортировка файлов миграций Room
	 *
	 * @param filenames
	 * @return
	 */
	@NotNull
    private static ArrayList<String> getSortedRoomMigrations(ArrayList<String> filenames) {
        ArrayList<String> list = new ArrayList<>();
        for (String filename : filenames) {
			String match = getRoomMigrationMatch(filename);
			if (!Utility.empty(match)) {
				int start = 0;
				int end = 0;
				try {
					start = getRoomMigrationVersion(filename, 0);
					end = getRoomMigrationVersion(filename, 1);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                if (start != end && end != 0 && start != 0) {
                    list.add(filename);
                }
            }
		}
		Collections.sort(list, (o1, o2) -> {
			int start1 = 0;
			int end1 = 0;
			int start2 = 0;
			int end2 = 0;
			try {
				String match1 = getRoomMigrationMatch(o1);
				String match2 = getRoomMigrationMatch(o2);
				if (!Utility.empty(match1) && !Utility.empty(match2)) {
					start1 = getRoomMigrationVersion(o1, 0);
					end1 = getRoomMigrationVersion(o1, 1);
					start2 = getRoomMigrationVersion(o2, 0);
					end2 = getRoomMigrationVersion(o2, 1);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (start1 == start2 && end1 == end2) {
				return 0;
			}
			int compareStart = Integer.compare(start1, start2);
			if (compareStart != 0) {
				return compareStart;
			}
			int compareEnd = Integer.compare(end1, end2);
			if (compareEnd != 0) {
				return compareEnd;
			}
			return 0;
		});
		return list;
	}

	/**
	 * Версия миграции
	 *
	 * @param filename имя файла
	 * @param position 0 - start|1 - finish
	 * @return номер версии
	 */
	public static int getRoomMigrationVersion(String filename, int position) {
		return Integer.parseInt(getRoomMigrationMatch(filename).split("_")[position]);
	}

	/**
	 * Нахождение версий миграций библиотеки Room
	 *
	 * @param filename
	 * @return
	 */
	public static String getRoomMigrationMatch(String filename) {
		return Utility.match(filename, "^room_{1}(\\d+_{1}\\d+)_{1}.*\\.sql", 1);
	}

    @NonNull
    private static Migration addRoomMigration(Context context, int startVersion, int endVersion, String migrationsFile) {
        return new Migration(startVersion, endVersion) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                runRoomMigrations(context, database, migrationsFile);
            }
        };
    }

    public static Migration[] getRoomMigrations(Context context) {
        ArrayList<String> migrationsFiles = getSortedRoomMigrations(FileUtils.listAssetFiles(context, "migrations"));
        Log.d(DBProvider.class.getSimpleName(), "getRoomMigrations: migrationsFiles:" + migrationsFiles);
        ArrayList<Migration> migrationArrayList = new ArrayList<>();
        for (String migrationsFile : migrationsFiles) {
//            String sql = Utility.readAssetFile(context, "migrations", migrationsFile);
            int start = getRoomMigrationVersion(migrationsFile, 0);
            int end = getRoomMigrationVersion(migrationsFile, 1);
            migrationArrayList.add(addRoomMigration(context, start, end, migrationsFile));
        }
        Migration[] migrations = new Migration[migrationArrayList.size()];
        for (int i = 0; i < migrationArrayList.size(); i++) {
            migrations[i] = migrationArrayList.get(i);
        }
        return migrations;
    }

    private static void runRoomMigrations(Context context, SupportSQLiteDatabase database, String migrationsFile) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        String sql = Utility.readAssetFile(context, "migrations", migrationsFile);
        int migrationVersion = getRoomMigrationVersion(migrationsFile, 0);
        int version = database.getVersion();
        Log.d(DBProvider.class.getSimpleName(), "runRoomMigrations: migrationsFile:" + migrationsFile + " version:" + version + " end:" + migrationVersion);
        boolean canMigrate = version == 0 || version == migrationVersion;
        if (canMigrate) {
            if (sql != null) {
                String[] sqls = sql.split(";");
                database.beginTransaction();
                try {
                    for (String s : sqls) {
                        if (!Utility.empty(s)) {
                            database.execSQL(s);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                database.endTransaction();
            }
        }
        Log.d(DBProvider.class.getSimpleName(), "runRoomMigrations: database ALL execSQL...OK time:" + stopwatch.getElapsedTimeSecs(3));
        stopwatch.stop();
    }

    public static void runRoomMigrations(Context context, SupportSQLiteDatabase database) {
        ArrayList<String> migrationsFiles = getSortedRoomMigrations(FileUtils.listAssetFiles(context, "migrations"));
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (String migrationsFile : migrationsFiles) {
            String sql = Utility.readAssetFile(context, "migrations", migrationsFile);
            int migrationVersion = getRoomMigrationVersion(migrationsFile, 0);
            int version = database.getVersion();
            Log.i(DBProvider.class.getSimpleName(), "runRoomMigrations: migrationsFile:" + migrationsFile + " version:" + version + " end:" + migrationVersion);
            boolean canMigrate = version == 0 || version == migrationVersion;
            if (canMigrate) {
                if (sql != null) {
                    String[] sqls = sql.split(";");
                    database.beginTransaction();
                    try {
                        for (String s : sqls) {
                            if (!Utility.empty(s)) {
                                database.execSQL(s);
                            }
                        }
                        database.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    database.endTransaction();
                }
            }
        }
        Log.i(DBProvider.class.getSimpleName(), "runRoomMigrations: database ALL execSQL...OK time:" + stopwatch.getElapsedTimeSecs(3));
        stopwatch.stop();
    }

}
