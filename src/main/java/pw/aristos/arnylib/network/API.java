package pw.aristos.arnylib.network;

import android.content.Context;
import android.content.Intent;
import org.json.JSONObject;
import java.util.HashMap;
public class API {
	public static void requestJSON(Intent intent,Context context, int method, String url, JSONObject params) {
		ApiService.onStartRequest(intent,context,ApiService.EXTRA_KEY_TYPE_ASYNC,ApiService.API_SERVICE_OPERATION_JSON,method,url,params);
	}

	public static void requestString(Intent intent,Context context,int method,String url) {
		ApiService.onStartRequest(intent,context,ApiService.EXTRA_KEY_TYPE_ASYNC,ApiService.API_SERVICE_OPERATION_STRING,method,url,new JSONObject());
	}
}
