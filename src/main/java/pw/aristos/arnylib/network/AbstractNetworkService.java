package pw.aristos.arnylib.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.json.JSONObject;
import pw.aristos.arnylib.service.OperationProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractNetworkService extends IntentService {
    /*Extras*/
    public static final String ACTION = "AbstractIntentService.action";
    public static final String EXTRA_KEY_OPERATION_ID = "AbstractIntentService.operaton.id";
    public static final String EXTRA_KEY_TYPE = "AbstractIntentService.type";
    public static final String EXTRA_KEY_OPERATION = "AbstractIntentService.operaton";
    public static final int EXTRA_KEY_TYPE_SYNC = 0;
    public static final int EXTRA_KEY_TYPE_ASYNC = 1;
    public static final int API_SERVICE_OPERATION_JSON = 1111;
    public static final int API_SERVICE_OPERATION_STRING = 1110;
    public static final String EXTRA_KEY_OPERATION_RESULT = "AbstractIntentService.operaton.result";
    public static final String EXTRA_KEY_OPERATION_FINISH = "AbstractIntentService.operaton.finish";
    public static final String EXTRA_KEY_OPERATION_FINISH_SUCCESS = "AbstractIntentService.operaton.success";
    public static final String EXTRA_KEY_OPERATION_DATA = "AbstractIntentService.operatonId.data";
    /*other*/
    protected static OperationProvider operation;
    private static ArrayList<OperationProvider> operationsQueue;

    protected abstract void runJSONRequest(OperationProvider provider, OnJSONRequestResult result);
    protected abstract void runStringRequest(OperationProvider provider, OnStringRequestResult result);

    protected static OperationProvider getOperation() {
        return operation;
    }

    protected void setOperation(OperationProvider operation) {
        Log.i(AbstractNetworkService.class.getSimpleName(), "setOperation: operatonId = " + operation.getId());
        AbstractNetworkService.operation = operation;
    }

    public AbstractNetworkService() {
        super("AbstractIntentService");
        operationsQueue = new ArrayList<>();
    }


    // Launching the service
    protected static void onStartRequest(Intent intent, Context context, int type, int code,int method,String url,JSONObject params) {
        HashMap<String, Object> operationData = new HashMap<>();
        operationData.put("method", method);
        operationData.put("url", url);
        operationData.put("params", params);
        context.startService(intent.putExtra(AbstractNetworkService.EXTRA_KEY_OPERATION,
                        new OperationProvider(code,type,operationData)));
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
            Log.i(ParallelThreadPoolExecutor.class.getSimpleName(), "afterExecute: active_count = " + active_count);
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
                OperationProvider provider = extras.getParcelable(EXTRA_KEY_OPERATION);
                executeOperation(provider);
            }
        }
    }

    @NonNull
    private Intent initProadcastIntent() {
        Log.i(AbstractNetworkService.class.getSimpleName(), "initProadcastIntent: ");
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            OperationProvider provider = extras.getParcelable(EXTRA_KEY_OPERATION);
            Log.i(AbstractNetworkService.class.getSimpleName(), "onHandleIntent: provider = " + provider.getId());
            int type = provider.getType();
            if (type == EXTRA_KEY_TYPE_SYNC) {
                setQueue(provider);
            } else {
                ThreadPoolExecutor.execute(new Task(intent));
            }
        }
    }

    private void setQueue(OperationProvider provider) {
        Log.i(AbstractNetworkService.class.getSimpleName(), "setQueue: provider = " + provider.getId());
        if (operationsQueue.isEmpty()) {
            operationsQueue.add(provider);
            restartOperation();
        } else {
            operationsQueue.add(provider);
        }
        Log.i(AbstractNetworkService.class.getSimpleName(), "setQueue: operationsQueue size = " + operationsQueue.size());
    }

    private void restartOperation() {
        Log.i(AbstractNetworkService.class.getSimpleName(), "restartOperation: operationsQueue = " + operationsQueue);
        if (!operationsQueue.isEmpty()) {
            executeOperation(operationsQueue.get(0));
            operationsQueue.remove(0);
            if (!operationsQueue.isEmpty()) {
                restartOperation();
            }
        }
    }

    private void executeOperation(final OperationProvider operation) {
        Log.i(AbstractNetworkService.class.getSimpleName(), "executeOperation: operation = " + operation.getId());
        setOperation(operation);
        if (operation.getId() == API_SERVICE_OPERATION_JSON) {
            runJSONRequest(operation, new OnJSONRequestResult() {
                @Override
                public void onResult(JSONObject object) {
                    operation.setFinished(true);
                    operation.setSuccess(true);
                    operation.setResult(object.toString());
                    sendOperationResult(operation);
                }
                @Override
                public void onError(String error) {
                    operation.setFinished(true);
                    operation.setSuccess(false);
                    operation.setResult(error);
                    sendOperationResult(operation);
                }
            });
        }else{
            runStringRequest(operation, new OnStringRequestResult() {
                @Override
                public void onSuccess(String result) {
                    operation.setFinished(true);
                    operation.setSuccess(true);
                    operation.setResult(result);
                    sendOperationResult(operation);
                }

                @Override
                public void onError(String error) {
                    operation.setFinished(true);
                    operation.setSuccess(false);
                    operation.setResult(error);
                    sendOperationResult(operation);
                }
            });
        }
    }

    private void sendOperationResult(OperationProvider provider) {
        Intent intent = initProadcastIntent();
        intent.putExtra(EXTRA_KEY_OPERATION , provider);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}