package com.arny.arnylib.loaders;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
	private T mResult;
	private boolean isRunning;

	public AbstractLoader(Context context) {
		super(context);
	}

	public static boolean checkLoaderRun(LoaderManager lm, int myIdLoader) {
		AbstractLoader loader = getLoader(lm, myIdLoader);
		return loader != null && loader.isRunning;
	}

	public static<T> T getLoader(LoaderManager lm, int myIdLoader) {
		return (T) lm.<T>getLoader(myIdLoader);
	}

	@Override
	public void deliverResult(T result) {
		Log.d(AbstractLoader.class.getSimpleName(), "deliverResult: ");
		if (isReset()) {
			releaseResources(result);
			return;
		}

		T oldResult = mResult;
		mResult = result;

		if (isStarted()) {
			super.deliverResult(result);
		}

		if (oldResult != result && oldResult != null) {
			releaseResources(oldResult);
		}
		isRunning = false;
	}

	@Override
	public void onCanceled(T result) {
		super.onCanceled(result);
		releaseResources(result);
		Log.d(AbstractLoader.class.getSimpleName(), "onCanceled: ");
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		releaseResources(mResult);
		mResult = null;
		Log.d(AbstractLoader.class.getSimpleName(), "onReset: ");
	}

	@Override
	protected void onStartLoading() {
		Log.d(AbstractLoader.class.getSimpleName(), "onStartLoading: ");
		isRunning = true;
		if (mResult != null) {
			deliverResult(mResult);
		}
		if (takeContentChanged() || mResult == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	protected void releaseResources(T result) {
	}
}