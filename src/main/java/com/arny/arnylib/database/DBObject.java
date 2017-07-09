package com.arny.arnylib.database;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
public abstract class DBObject {
	protected abstract Field[] getFields();
	protected Object cls;
	public String getColumn(String fieldname){
		Map<String, String> fieldToMap = new HashMap<>();
		for (Field field : getFields()) {
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

	public String getFromCursor(){
		Map<String, Object> fieldToMap = new HashMap<>();
		for (Field field : getFields()) {
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
		return fieldToMap.get("").toString();
	}

//	public ContentValues getContentValue(String fieldname){
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
