package com.arny.arnylib.loaders;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.arny.arnylib.utils.DateTimeUtils;
import com.arny.arnylib.utils.Utility;
public class MyLoader extends AbstractLoader<String>  {
	private String data;

	public  MyLoader(Context context, String data ) {
		super(context);
		this.data = data;
	}

	@Override
	public String loadInBackground() {
		for (int i = 0; i < 10; i++) {
			Log.d(MyLoader.class.getSimpleName(), "hudeOperation: i = " + i);
			SystemClock.sleep(1000);
		}
		return "data = " + data + " time = " + DateTimeUtils.getDateTime();
	}

}