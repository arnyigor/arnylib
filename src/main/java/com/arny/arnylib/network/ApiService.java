package com.arny.arnylib.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ApiService extends IntentService {
	public ApiService() {
		super("ApiService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	}

	@NonNull
	public static HashMap<String, String> getJsonObjectToHashMap(JSONObject params) throws JSONException {
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

	public static void apiBuildRequest(String url, final OnStringRequestResult result) {
		apiBuildRequest(url, Method.GET, null, result);
	}

	public static void apiBuildRequest(String url, final OnJSONObjectResult result) {
		apiBuildRequest(url, Method.GET, null, result);
	}

	public static void apiBuildRequest(String url, JSONObject params, final OnStringRequestResult result) {
		apiBuildRequest(url, Method.GET, params, result);
	}

	public static void apiBuildRequest(String url, JSONObject params, final OnJSONObjectResult result) {
		apiBuildRequest(url, Method.GET, params, result);
	}

	public static void apiBuildRequest(String url, int method, final OnStringRequestResult result) {
		apiBuildRequest(url, method, null, result);
	}

	public static void apiBuildRequest(String url, int method, final OnJSONObjectResult result) {
		apiBuildRequest(url, method, null, result);
	}

	public static void apiBuildRequest(String url, int method, JSONObject params, final OnStringRequestResult result) {
		switch (method) {
			case Method.GET:
				apiBuildGetRequest(url, params, null, result);
				break;
			case Method.POST:
				apiBuildPostRequest(url, method,params, null, result);
				break;
			case Method.PUT:
				apiBuildPostRequest(url, method,params, null, result);
				break;
		}
	}

	public static void apiBuildRequest(String url, int method, JSONObject params, final OnJSONObjectResult result) {
		switch (method) {
			case Method.GET:
				apiBuildGetRequest(url, params, null, result);
				break;
			case Method.POST:
				apiBuildPostRequest(url, method,params, null, result);
				break;
			case Method.PUT:
				apiBuildPostRequest(url, method,params, null, result);
				break;
		}
	}

	public static void apiBuildGetRequest(String url ,JSONObject params, JSONObject headers, final OnJSONObjectResult result) {
		ANRequest.GetRequestBuilder requestBuilder = new ANRequest.GetRequestBuilder(url);
		StringBuilder builder = new StringBuilder();
		builder.append(" >> Api Request: ");
		builder.append(" ur: ");
		builder.append(url);

		if (headers != null) {
			try {
				requestBuilder.addHeaders(getJsonObjectToHashMap(headers));
				builder.append("; headers: ");
				builder.append(headers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				requestBuilder.addPathParameter(getJsonObjectToHashMap(params));
				builder.append(" params: ");
				builder.append(params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = requestBuilder.build();
		Log.i("api", builder.toString());
		anRequest.getAsJSONObject(new JSONObjectRequestListener() {
			@Override
			public void onResponse(JSONObject response) {
				Log.i("api", " <<  Api Response success: " + response);
				result.onSuccess(response);
			}

			@Override
			public void onError(ANError error) {
				Log.e("api", " <<  Api Response error Detail: " + error.getErrorDetail());
				Log.e("api", " <<  Api Response error Body: " + error.getErrorBody());
				Log.e("api", " <<  Api Response error Code: " + error.getErrorCode());
				result.onError(error.getErrorDetail());
			}
		});
	}

	public static void apiBuildGetRequest(String url, JSONObject params, JSONObject headers, final OnStringRequestResult result) {
		ANRequest.GetRequestBuilder getRequestBuilder = new ANRequest.GetRequestBuilder(url);
		StringBuilder builder = new StringBuilder();
		builder.append(" >> Api Request: ");
		builder.append(" ur: ");
		builder.append(url);
		if (headers != null) {
			try {
				getRequestBuilder.addHeaders(getJsonObjectToHashMap(headers));
				builder.append("; headers: ");
				builder.append(headers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				getRequestBuilder.addPathParameter(getJsonObjectToHashMap(params));
				builder.append(" params: ");
				builder.append(params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = getRequestBuilder.build();
		Log.i("api", builder.toString());
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

	/**
	 * apiBuildPostRequest
	 * @param url request url
	 * @param method (GET,POST,PUT)
	 * @param params (JSONobject)
	 * @param headers (JSONObject)
	 * @param result (JSONObject or String)
	 */
	public static void apiBuildPostRequest(String url, int method, JSONObject params, JSONObject headers, final OnJSONObjectResult result) {
		ANRequest.PostRequestBuilder requestBuilder = new ANRequest.PostRequestBuilder(url, method);
		StringBuilder builder = new StringBuilder();
		builder.append(" >> Api Request: ");
		builder.append(" ur: ");
		builder.append(url);

		if (headers != null) {
			try {
				requestBuilder.addHeaders(getJsonObjectToHashMap(headers));
				builder.append("; headers: ");
				builder.append(headers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				requestBuilder.addPathParameter(getJsonObjectToHashMap(params));
				builder.append(" params: ");
				builder.append(params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = requestBuilder.build();
		Log.i("api", builder.toString());
		anRequest.getAsJSONObject(new JSONObjectRequestListener() {
			@Override
			public void onResponse(JSONObject response) {
				Log.i("api", " <<  Api Response success: " + response);
				result.onSuccess(response);
			}

			@Override
			public void onError(ANError error) {
				Log.e("api", " <<  Api Response error Detail: " + error.getErrorDetail());
				Log.e("api", " <<  Api Response error Body: " + error.getErrorBody());
				Log.e("api", " <<  Api Response error Code: " + error.getErrorCode());
				result.onError(error.getErrorDetail());
			}
		});
	}

	public static void apiBuildPostRequest(String url, int method, JSONObject params, JSONObject headers, final OnStringRequestResult result) {
		ANRequest.PostRequestBuilder getRequestBuilder = new ANRequest.PostRequestBuilder(url, method);
		StringBuilder builder = new StringBuilder();
		builder.append(" >> Api Request: ");
		builder.append(" ur: ");
		builder.append(url);
		if (headers != null) {
			try {
				getRequestBuilder.addHeaders(getJsonObjectToHashMap(headers));
				builder.append("; headers: ");
				builder.append(headers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (params != null) {
			try {
				getRequestBuilder.addPathParameter(getJsonObjectToHashMap(params));
				builder.append(" params: ");
				builder.append(params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ANRequest anRequest = getRequestBuilder.build();
		Log.i("api", builder.toString());
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
