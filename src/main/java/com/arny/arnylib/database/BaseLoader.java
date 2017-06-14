package com.arny.arnylib.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

public abstract class BaseLoader extends AsyncTaskLoader<Cursor> {

	public BaseLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	public Cursor loadInBackground() {
		try {
			return apiCall();
		} catch (Exception e) {
			return null;
		}
	}

	protected abstract Cursor apiCall() throws Exception;
}