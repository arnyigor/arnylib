package com.arny.arnylib.database;

import android.database.Cursor;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
public abstract class DBObject {
	protected abstract Object getClassObject();
	protected Object cls;
	protected Field[] clsFields;

	public String getColumn(String fieldname){
		cls = getClassObject();
		clsFields = cls.getClass().getDeclaredFields();
		Map<String, String> fieldToMap = new HashMap<>();
		for (Field field : clsFields) {
			System.out.println("field = " + field);
			try {
				Log.i(DBObject.class.getSimpleName(), "getColumn: fld = " +"(" + field.getType() + ") " + field.getName() + " = " + field.get(cls) + ", ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Column attr = field.getAnnotation(Column.class);
			if (attr != null) {
				fieldToMap.put(field.getName(), attr.name());
			}
		}
		return fieldToMap.get(fieldname);
	}

	public Object getFromCursor(Cursor cursor){
		cls = getClassObject();
		clsFields = cls.getClass().getDeclaredFields();
		Map<String, Object> fieldToMap = getColumnsObjectMap(clsFields, cls);
		for(Map.Entry<String, Object> entry : fieldToMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			String resStr = null;
			int resInt = 0;
			float resFloat = 0;
			double resDouble = 0;
			JSONObject resJO = null;
			JSONArray resJA = null;
			if (value instanceof String) {
				resStr = (String) value;
			}
		}
		return fieldToMap.get("").toString();
	}

	private static Map<String, Object> getColumnsObjectMap(Field[] clsFields, Object cls) {
		Map<String, Object> fieldToMap = new HashMap<>();
		for (Field field : clsFields) {
			try {
				System.out.println("field = " + field);
				Column attr = field.getAnnotation(Column.class);
				if (attr != null) {
					fieldToMap.put(field.getName(), attr.name());
				}

				Log.i(DBObject.class.getSimpleName(), "getColumn: fld = " + "(" + field.getType() + ") " + field.getName() + " = " + field.get(cls) + ", ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fieldToMap;
	}

//	public ContentValues getContentValues(String fieldname){
//		Map<String, ContentValues> fieldToMap = new HashMap<>();
//		for (Field field : getFields()) {
//			System.out.println("field = " + field);
//			Column attr = field.getAnnotation(Column.class);
//			if (attr != null) {
//				try {
//					ContentValues values = new ContentValues();
//					Log.i(DBObject.class.getSimpleName(), "getColumn: fld = " +"(" + field.getType() + ") " + field.getName() + " = " + field.get(cls) + ", ");
//					values.put(attr.name(),field.get(cls));
//
//					fieldToMap.put(field.getName(), );
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return fieldToMap.get(fieldname);
//	}
}
