package pw.aristos.arnylib.service;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import org.json.JSONObject;
import pw.aristos.arnylib.network.ApiSendService;
import pw.aristos.arnylib.network.MyResultReceiver;
import pw.aristos.arnylib.network.OnStringRequestResult;

public class Operations extends AbstractIntentService {
    private MyResultReceiver receiver;

    public Operations() {
        super();
    }

    @Override
    protected void runOperation(OperationProvider provider, OnOperationResult operationResult) {
        Log.d(Operations.class.getSimpleName(), "runOperation: " + provider.getId());
        switch (provider.getId()) {
            case 1:
                String test_url ="https://pik.ru/luberecky/singlepage?data=ChessPlan&id=2b3ecc9b-bfad-e611-9fbe-001ec9d5643c&private_key=1&format=json&domain=pik.ru";
                for (int i = 0; i < 50; i++) {
                    ApiSendService.apiRequest(getApplicationContext(), test_url, new JSONObject(), new OnStringRequestResult() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(Operations.class.getSimpleName(), "onSuccess: result = " + result.length());
                            receiver = new MyResultReceiver(new Handler());
                            Bundle bundle = new Bundle();
                            bundle.putString("result", "test_test_test");
                            receiver.send(Activity.RESULT_OK, bundle);
                        }

                        @Override
                        public void onError(String error) {
                            Log.d(Operations.class.getSimpleName(), "onError: error = " + error);
                        }
                    });
                }
                break;
        }
    }
}
