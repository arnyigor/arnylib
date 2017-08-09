package com.arny.arnylib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ToastMaker {

	public static void toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void toastError(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toasty.error(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

	}

	public static void toastInfo(Context context, String message) {
		Toasty.info(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void toastSuccess(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toasty.success(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
	}

}