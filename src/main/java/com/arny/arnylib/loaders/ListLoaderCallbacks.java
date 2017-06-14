package com.arny.arnylib.loaders;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

public abstract class ListLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<BetterAsyncTask.TaskResult<T>> {
	private ListFragment mFragment;

	public ListLoaderCallbacks(ListFragment fragment) {
		mFragment = fragment;
	}

	public final void onLoadFinished(Loader<BetterAsyncTask.TaskResult<T>> loader, BetterAsyncTask.TaskResult<T> data) {
		if (data.success()) {
			onLoadFinished(data.getObject());
		} else {
			Log.e(mFragment.getClass().getName(), "Error in loader", data.getException());
			mFragment.setEmptyText(data.getException().toString());
			onLoadFinished(null);
		}
	}

	public void onLoaderReset(Loader<BetterAsyncTask.TaskResult<T>> loader) {
		if (!mFragment.isVisible()) {
			return;
		}
		mFragment.setEmptyText(null);
		mFragment.setListAdapter(null);
	}

	protected abstract void onLoadFinished(T result);
}