package com.arny.arnylib.loaders;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
public class TaskLoader extends ListLoaderCallbacks<String> {
	public TaskLoader(ListFragment fragment) {
		super(fragment);
	}

	@Override
	protected void onLoadFinished(String result) {

	}

	@Override
	public Loader<BetterAsyncTask.TaskResult<String>> onCreateLoader(int id, Bundle args) {
		return null;
	}
}
