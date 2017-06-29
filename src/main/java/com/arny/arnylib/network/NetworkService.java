package com.arny.arnylib.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arny.arnylib.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetworkService extends IntentService {
	protected static RequestQueue requestQueue;

	public static RequestQueue getRequestQueue(Context context) {
		if (requestQueue == null)
			requestQueue = Volley.newRequestQueue(context);
		return requestQueue;
	}

	public NetworkService() {
		super("NetworkService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	}

	public static Document htmlPostRequest(String url, JSONObject params) {
		try {
			HashMap<String, String> mapParams = Utility.toMap(params);
			return Jsoup.connect(url)
					.data(mapParams)
					.timeout(5000)
					.post();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Document htmlGetRequest(String url, JSONObject params) {
		try {
			HashMap<String, String> mapParams = Utility.toMap(params);
			return Jsoup.connect(url).data(mapParams).timeout(5000).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void apiRequest(final Context context, String url, JSONObject params, final OnStringRequestResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, String url, JSONObject params, final OnJSONObjectResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject object) {
				successCallback.onSuccess(object);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, final OnStringRequestResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, method, url, params, new JSONObject(), new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, final OnJSONObjectResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, method, url, params, new JSONObject(), new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject object) {
				successCallback.onSuccess(object);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, JSONObject headers, final OnStringRequestResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, method, url, params, headers, new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, JSONObject headers, final OnJSONObjectResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, method, url, params, headers, new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject object) {
				successCallback.onSuccess(object);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	private static class HttpAsyncStringRequest extends AsyncTask<Void, Void, Boolean> {
		private final OnStringRequestResult successCallback;
		private String url;
		private JSONObject params;
		private JSONObject headers;
		private Context context;
		private int apiMethod;

		private HttpAsyncStringRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnStringRequestResult successCallback) {
			this.successCallback = successCallback;
			params = requestParams;
			url = requestUrl;
			this.apiMethod = apiMethod;
			this.context = context;
			this.headers = headers;
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			StringRequest request = new StringRequest(apiMethod, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					successCallback.onSuccess(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					successCallback.onError(error.toString());
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					try {
						return Utility.toMap(params);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return new HashMap<>();
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					try {
						return Utility.toMap(headers);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					return new HashMap<>();
				}
			};

			request.setRetryPolicy(new RetryPolicy() {
				@Override
				public int getCurrentTimeout() {
					return DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4;
				}

				@Override
				public int getCurrentRetryCount() {
					return 3;
				}

				@Override
				public void retry(VolleyError error) throws VolleyError {
					Log.e("http", "HTTP Retry Error:", error);
					successCallback.onError(error.getMessage());
				}
			});
			queue.add(request);
			return true;
		}
	}

	private static class HttpAsyncJsonRequest extends AsyncTask<Void, Void, Boolean> {
		private OnJSONObjectResult successCallback;
		private String url;
		private JSONObject params;
		private JSONObject headers;
		private Context context;
		private int apiMethod;

		private HttpAsyncJsonRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnJSONObjectResult successCallback) {
			this.successCallback = successCallback;
			params = requestParams;
			url = requestUrl;
			this.apiMethod = apiMethod;
			this.context = context;
			this.headers = headers;
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(apiMethod, url, params, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					successCallback.onSuccess(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					successCallback.onError(error.toString());
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					try {
						return Utility.toMap(params);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return new HashMap<>();
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					try {
						return Utility.toMap(headers);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					return new HashMap<>();
				}
			};
			jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
				@Override
				public int getCurrentTimeout() {
					return DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4;
				}

				@Override
				public int getCurrentRetryCount() {
					return 3;
				}

				@Override
				public void retry(VolleyError error) throws VolleyError {
					Log.e("http", "HTTP Retry Error:", error);
					successCallback.onError(error.getMessage());
				}
			});
			queue.add(jsonObjectRequest);
			return true;
		}
	}

}
