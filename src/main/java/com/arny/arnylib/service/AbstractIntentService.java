package com.arny.arnylib.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.arny.arnylib.utils.Logcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractIntentService extends IntentService {
    /*Extras*/
    public static final String ACTION = "AbstractIntentService.action";
    public static final String EXTRA_KEY_OPERATION_ID = "AbstractIntentService.operaton.id";
    public static final String EXTRA_KEY_TYPE = "AbstractIntentService.type";
    public static final String EXTRA_KEY_OPERATION = "AbstractIntentService.operaton";
    public static final int EXTRA_KEY_TYPE_SYNC = 0;
    public static final int EXTRA_KEY_TYPE_ASYNC = 1;
    public static final String EXTRA_KEY_OPERATION_RESULT = "AbstractIntentService.operaton.result";
    public static final String EXTRA_KEY_OPERATION_FINISH = "AbstractIntentService.operaton.finish";
    public static final String EXTRA_KEY_OPERATION_FINISH_SUCCESS = "AbstractIntentService.operaton.success";
    public static final String EXTRA_KEY_OPERATION_DATA = "AbstractIntentService.operatonId.data";
    /*other*/
    protected static OperationProvider operation;
    private static ArrayList<OperationProvider> operationsQueue;

    protected abstract void runOperation(OperationProvider provider,OnOperationResult result);

    public static OperationProvider getOperation() {
        return operation;
    }

    public static void setOperation(OperationProvider operation) {
        AbstractIntentService.operation = operation;
    }

    protected interface OnOperationResult {
        void resultSuccess(OperationProvider provider);
        void resultFail(OperationProvider provider);
    }

    public AbstractIntentService() {
        super("AbstractIntentService");
        operationsQueue = new ArrayList<>();
    }


    // Launching the service
    public static void onStartOperation(Intent intent,Context context,int type, int id, HashMap<String,Object> operationData) {
        context.startService(intent.putExtra(AbstractIntentService.EXTRA_KEY_OPERATION,
                        new OperationProvider(id,type,operationData)));
    }


    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private final AtomicInteger tasks_left = new AtomicInteger(0);
    private final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);

    protected void tasksLeft(int tasks_left) {
    }

    private final ThreadPoolExecutor ThreadPoolExecutor = new ParallelThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private class ParallelThreadPoolExecutor extends ThreadPoolExecutor {
        public ParallelThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            final int active_count = tasks_left.decrementAndGet();
            if (active_count == 0) {
                onDestroy();
            } else {
                tasksLeft(active_count);
            }
        }
    }

    public class Task implements Runnable {
        private final Intent intent;

        public Task(final Intent intent) {
            this.intent = intent;
        }

        public void run() {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                OperationProvider provider = (OperationProvider)extras.getSerializable(EXTRA_KEY_OPERATION);
	            Log.i(Task.class.getSimpleName(), "run: provider = " + provider);
	            executeOperation(provider);
            }
        }
    }

    @NonNull
    private Intent initProadcastIntent() {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            OperationProvider provider = (OperationProvider)extras.getSerializable(EXTRA_KEY_OPERATION);
            Logcat.d(AbstractIntentService.class.getSimpleName(), "onHandleIntent: provider ="  + provider.getId());
            int type = provider.getType();
            if (type == EXTRA_KEY_TYPE_SYNC) {
                setQueue(provider);
            } else {
                ThreadPoolExecutor.execute(new Task(intent));
            }
        }
    }

    private void setQueue(OperationProvider provider) {
        if (operationsQueue.isEmpty()) {
            operationsQueue.add(provider);
            restartOperation();
        } else {
            operationsQueue.add(provider);
        }
    }

    private void restartOperation() {
        if (!operationsQueue.isEmpty()) {
            executeOperation(operationsQueue.get(0));
            operationsQueue.remove(0);
            if (!operationsQueue.isEmpty()) {
                restartOperation();
            }
        }
    }

    private void executeOperation(final OperationProvider operation) {
        setOperation(operation);
        runOperation(operation, new OnOperationResult() {
            @Override
            public void resultSuccess(OperationProvider provider) {
                sendOperationResult(provider);
            }

            @Override
            public void resultFail(OperationProvider provider) {
                sendOperationResult(provider);
            }
        });
    }

    private void sendOperationResult(OperationProvider provider) {
	    Log.i(AbstractIntentService.class.getSimpleName(), "sendOperationResult: provider = " + provider);
	    Intent intent = initProadcastIntent();
	    intent.putExtra(EXTRA_KEY_OPERATION , provider);
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}