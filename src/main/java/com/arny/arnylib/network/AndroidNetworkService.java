package com.arny.arnylib.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.arny.arnylib.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	public static void apiGetRequest(String url, String tag, final OnJSONArrayResult requestResult) {
		Log.i("api", " >> Api Request: " + url);
		AndroidNetworking.get(url)
				.setTag(tag)
				.setPriority(Priority.LOW)
				.build()
				.getAsJSONArray(new JSONArrayRequestListener() {
					@Override
					public void onResponse(JSONArray response) {
						Log.i("api", " <<  Api Response success: " + response);
						requestResult.onSuccess(response);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
						requestResult.onError(anError.getErrorDetail());
					}
				});
	}

	public static void apiGetRequest(String url, String tag, final OnStringRequestResult requestResult) {
		Log.i("api", " >> Api Request: " + url);
		AndroidNetworking.get(url)
				.setTag(tag)
				.setPriority(Priority.LOW)
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						Log.i("api", " <<  Api Response success: " + response);
						requestResult.onSuccess(response);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
						requestResult.onError(anError.getErrorDetail());
					}
				});
	}

	public static void apiGetRequest(String url, String tag, final OnJSONObjectResult result) {
		Log.i("api", " >> Api Request: " + url);
		AndroidNetworking.get(url)
				.setTag(tag)
				.setPriority(Priority.LOW)
				.build()
				.getAsJSONObject(new JSONObjectRequestListener() {
					@Override
					public void onResponse(JSONObject response) {
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

	public static void apiPostRequest(String url, String tag, JSONObject params, final OnJSONObjectResult requestResult) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		AndroidNetworking.post(url)
				.addJSONObjectBody(params)
				.setTag(tag)
				.setPriority(Priority.MEDIUM)
				.build()
				.getAsJSONObject(new JSONObjectRequestListener() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("api", " <<  Api Response success: " + response);
						requestResult.onSuccess(response);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
						requestResult.onError(anError.getErrorDetail());
					}
				});
	}

	public static void apiPostRequest(String url, String tag, JSONObject params, final OnStringRequestResult requestResult) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		AndroidNetworking.post(url)
				.addJSONObjectBody(params)
				.setTag(tag)
				.setPriority(Priority.MEDIUM)
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						Log.i("api", " <<  Api Response success: " + response);
						requestResult.onSuccess(response);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
						requestResult.onError(anError.getErrorDetail());
					}
				});
	}

	public static void apiPostRequest(String url, String tag, JSONObject params, final OnJSONArrayResult arrayResult) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		AndroidNetworking.post(url)
				.addJSONObjectBody(params)
				.setTag(tag)
				.setPriority(Priority.MEDIUM)
				.build()
				.getAsJSONArray(new JSONArrayRequestListener() {
					@Override
					public void onResponse(JSONArray response) {
						Log.i("api", " <<  Api Response success: " + response);
						arrayResult.onSuccess(response);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("api", " <<  Api Response error: " + anError.getErrorDetail());
						arrayResult.onError(anError.getErrorDetail());
					}
				});
	}

}
