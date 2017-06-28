package com.arny.arnylib.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AndroidNetworkService extends IntentService {
	public AndroidNetworkService() {
		super("AndroidNetworkService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	}

	@NonNull
	private static HashMap<String, String> getJsonObjectToHashMap(JSONObject params) throws JSONException {
		HashMap<String, String> mapParams = new HashMap<>();
		for (int i = 0; i < params.names().length(); i++) {
			mapParams.put(params.names().getString(i), (String) params.get(params.names().getString(i)));
		}
		return mapParams;
	}

	public static void canselApiRequest(String tag) {
		AndroidNetworking.forceCancel(tag);  // All the requests with the given tag will be cancelled , even if any percent threshold is set , it will be cancelled forcefully.
	}

	public static void canselAllApiRequest() {
		AndroidNetworking.forceCancelAll();  // All the requests with the given tag will be cancelled , even if any percent threshold is set , it will be cancelled forcefully.
	}

	public static void apiBuildRequest(String url, int method, JSONObject params, final OnStringRequestResult result) {
		apiBuildRequest(url, method, params, null, result);
	}

	public static void apiBuildRequest(String url, int method, JSONObject params, JSONObject headers, final OnJSONObjectResult result){
		ANRequest.GetRequestBuilder getRequestBuilder = new ANRequest.GetRequestBuilder(url, method);
		StringBuilder builder = new StringBuilder();
		builder.append(" >> Api Request: ");
		builder.append(" url ");
		builder.append(url);
		if (headers != null) {
			try {
				getRequestBuilder.addHeaders(getJsonObjectToHashMap(headers));
				builder.append(" headers ");
				builder.append(headers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				getRequestBuilder.addPathParameter(getJsonObjectToHashMap(params));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = getRequestBuilder.build();
		Log.i("api", " >> Api Request: " + url + " with params: " + params);
		anRequest.getAsJSONObject(new JSONObjectRequestListener() {
			@Override
			public void onResponse(JSONObject response) {
				Log.i("api", " <<  Api Response success: " + response);
				result.onSuccess(response);
			}
			@Override
			public void onError(ANError error) {
				Log.e("api", " <<  Api Response error: " + error.getErrorDetail());
				result.onError(error.getErrorDetail());
			}
		});
	}

	public static void apiBuildRequest(String url, int method, JSONObject params, JSONObject headers, final OnStringRequestResult result){
		ANRequest.GetRequestBuilder getRequestBuilder = new ANRequest.GetRequestBuilder(url, method);
		if (headers != null) {
			try {
				getRequestBuilder.addHeaders(getJsonObjectToHashMap(headers));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				getRequestBuilder.addPathParameter(getJsonObjectToHashMap(params));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = getRequestBuilder.build();
		Log.i("api", " >> Api Request: " + url + " with params: " + params);

		anRequest.getAsString(new StringRequestListener() {
			@Override
			public void onResponse(String response) {
				Log.i("api", " <<  Api Response success: " + response);
				result.onSuccess(response);
			}

			@Override
			public void onError(ANError anError) {
				Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
				result.onError(anError.getErrorDetail());
			}
		});
	}
}
