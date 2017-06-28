package com.arny.arnylib.loaders;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.AsyncTaskLoader;
import org.json.JSONObject;
public class JSONLoader extends AsyncTaskLoader<JSONObject> {

	public JSONLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	public JSONObject loadInBackground() {
		SystemClock.sleep(2000);
		return new JSONObject();

	}
}