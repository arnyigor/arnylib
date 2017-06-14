package com.arny.arnylib.utils;

import android.util.Log;

public class Logcat {
	private static boolean SB_ALLOW_LOG = true;

	public static void e(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.e(tag, message);
		}
	}

	public static void d(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.d(tag, message);
		}
	}

	public static void v(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.v(tag, message);
		}
	}

	public static void i(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.i(tag, message);
		}
	}

	public static void w(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.w(tag, message);
		}
	}

	public static void wtf(String tag, String message) {
		if (SB_ALLOW_LOG) {
			Log.wtf(tag, message);
		}
	}
}
