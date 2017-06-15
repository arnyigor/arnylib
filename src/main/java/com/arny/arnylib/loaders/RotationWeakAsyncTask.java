package com.arny.arnylib.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public abstract class RotationWeakAsyncTask extends AsyncTask<Void, Void, Void> {
    private static WeakReference<Context> context = null;
    private static WeakReference<String> stringResult = null;
    private static RotationWeakAsyncTask currentTask = null;
    private static OnTaskFinishedListener onFinishListener = null;
    private static ProgressDialog pd;
    private int requestID = 0;

    public interface OnTaskFinishedListener {
        void onTaskFinished(int i);
    }

    public abstract void run();

    public static boolean startNewTask(RotationWeakAsyncTask task, OnTaskFinishedListener l, int requestID) {
        if (task == null) {
            return false;
        }
        if (currentTask != null && currentTask.getStatus() != Status.FINISHED) {
            return false;
        }
        currentTask = task;
        currentTask.requestID = requestID;
        setOnFinishedListener(l);
        currentTask.execute(new Void[0]);
        showProgressIfNeed();
        return true;
    }
	public RotationWeakAsyncTask(Context context, String resulttxt) {
		setContext(context,resulttxt);
	}

	public RotationWeakAsyncTask(Context context) {
		setContext(context,"");
	}

    public static void setContext(Context context, OnTaskFinishedListener l) {
        setContext(context,"");
        setOnFinishedListener(l);
    }

    private static void setContext(Context con,String res) {
        context = new WeakReference(con);
        stringResult = new WeakReference(res);
        if (con == null) {
            destroyProgress();
        } else {
            showProgressIfNeed();
        }
    }

    public static void resetContext() {
        setOnFinishedListener(null);
        context = null;
        destroyProgress();
    }

    private static void setOnFinishedListener(OnTaskFinishedListener l) {
        onFinishListener = l;
    }

    private static void showProgressIfNeed() {
        if (currentTask != null && currentTask.getStatus() != Status.FINISHED && context != null) {
            Context c = (Context) context.get();
            if (c != null) {
                if (pd != null && pd.isShowing()) {
                    destroyProgress();
                }
                pd = ProgressDialog.show(c, "Подождите", "Действие выполняется...", true, false);
            }
        }
    }

    private static void destroyProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        pd = null;
    }

    protected Void doInBackground(Void... params) {
        run();
        return null;
    }

    protected void onPostExecute(Void arg) {
        if (context != null && ((Context) context.get()) != null) {
            showResult();
            destroyProgress();
            currentTask = null;
            if (onFinishListener != null) {
                onFinishListener.onTaskFinished(this.requestID);
            }
        }
    }

    private void showResult() {
        if (context != null) {
            Context c = (Context) context.get();
	        String r = (String) stringResult.get();
            if (c != null && r!=null) {
                Toast.makeText(c, r, Toast.LENGTH_LONG).show();
            }
        }
    }
}
