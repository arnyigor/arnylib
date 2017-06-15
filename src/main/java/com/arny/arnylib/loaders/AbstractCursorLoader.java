package com.arny.arnylib.loaders;

/* w w w .j  a va 2 s . c o m*/
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import com.arny.arnylib.utils.DroidUtils;
import com.arny.arnylib.utils.Utility;

/**
 * This class can be useful in case of that you need to work with Cursor object without usage of the ContentResolver.
 * @author vsouhrada
 * @see android.support.v4.content.AsyncTaskLoader< android.database.Cursor>
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public abstract class AbstractCursorLoader extends AsyncTaskLoader<Cursor> {

	private Cursor lastCursor;

	/**
	 * Create and return {@linkplain android.database.Cursor} which is currently using.
	 *
	 * @return Cursor which is currently using.
	 * @since 1.0.0
	 */
	abstract protected Cursor buildCursor();

	/**
	 * Default constructor
	 *
	 * @param context current application environment
	 * @since 1.0.0
	 */
	public AbstractCursorLoader(Context context) {
		super(context);
	}

	/**
	 * Run it on worker thread to perform the data load.
	 *
	 * @since 1.0.0
	 */
	@Override
	public Cursor loadInBackground() {
		Cursor cursor = buildCursor();
		if (cursor != null) {
			// Ensure the cursor window is filled
			cursor.getCount();
			DroidUtils.dumpCursor(cursor);
			// TODO register Content OBSERVERS
		}

		return cursor;
	}

	/**
	 * Runs on the UI thread. Delivering the results from the background thread to
	 * whatever is using the Cursor.
	 *
	 * @param cursor
	 *          current Cursor
	 * @since 1.0.0
	 */
	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}

			return;
		}

		Cursor oldCursor = lastCursor;
		lastCursor = cursor;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(cursor);
		}

		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Starts an asynchronous load of list data. When the result is ready the
	 * callbacks will be called on the UI thread. If a previous load has been
	 * completed and is still valid the result may be passed to the callbacks
	 * immediately.
	 *
	 * Must be called from the UI thread
	 *
	 * @since 1.0.0
	 */
	@Override
	protected void onStartLoading() {
		if (lastCursor != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(lastCursor);
		}

		if (takeContentChanged() || lastCursor == null) {
			// If the cursor has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Must be called from the UI thread, triggered by a call to stopLoading().
	 *
	 * @since 1.0.0
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param cursor instance of current {@link android.database.Cursor} object which is wrapped by this class.
	 * @since 1.0.0
	 */
	@Override
	public void onCanceled(Cursor cursor) {
		// Attempt to cancel the current asynchronous load.
		super.onCanceled(cursor);

		// The load has been canceled, so we should release the resources
		// associated with cursor.
		onReleaseResources(cursor);
	}

	/**
	 * Handles a request to resetting loader.
	 * This will always be called from the process's main thread.
	 *
	 * @since 1.0.0
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader has been stopped.
		onStopLoading();

		// At this point we can release the resources associated with lastCursor.
		onReleaseResources(lastCursor);

		lastCursor = null;
	}

	/**
	 * Release the resources associated with {@link android.database.Cursor}
	 *
	 * @param cursor
	 *          instance of Cursor
	 * @since 1.0.0
	 */
	protected void onReleaseResources(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

}