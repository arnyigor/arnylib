package com.arny.arnylib.database;

import android.content.ContentValues;
public class Content {

	ContentValues cv;

	public Content() {
		cv = new ContentValues();
	}
	public Content id(String tag, int val) {
		cv.put(tag, val);
		return this;
	}

	public Content add(String tag, int val) {
		cv.put(tag, val);
		return this;
	}

	public Content add(String tag, String val) {
		cv.put(tag, val);
		return this;
	}

	public Content add(String tag, boolean val) {
		cv.put(tag, val);
		return this;
	}

	public Content add(String tag, float val) {
		cv.put(tag, val);
		return this;
	}

	public Content add(String tag, long val) {
		cv.put(tag, val);
		return this;
	}

	public ContentValues get() {
		return cv;
	}
}