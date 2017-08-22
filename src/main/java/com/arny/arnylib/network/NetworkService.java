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
import com.arny.arnylib.interfaces.OnJSONObjectResult;
import com.arny.arnylib.interfaces.OnStringRequestResult;
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
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				successCallback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, String url, JSONObject params, final OnJSONObjectResult successCallback) {

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, final OnStringRequestResult successCallback) {
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

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, method, url, params, new JSONObject(), new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, JSONObject headers, final OnStringRequestResult successCallback) {
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, method, url, params, headers, new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				successCallback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, int method, String url, JSONObject params, JSONObject headers, final OnJSONObjectResult successCallback) {

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(context, method, url, params, headers, new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				successCallback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

	private static class HttpAsyncStringRequest extends AsyncTask<Void, Void, Boolean> {
		private final OnStringRequestResult result;
		private String url;
		private JSONObject params;
		private JSONObject headers;
		private Context context;
		private int apiMethod;



		private HttpAsyncStringRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnStringRequestResult result) {
			this.result = result;
			params = requestParams;
			url = requestUrl;
			this.apiMethod = apiMethod;
			this.context = context;
			this.headers = headers;
			Log.i(HttpAsyncJsonRequest.class.getSimpleName(), "HttpAsyncStringRequest: url = " + url + "; params:" + params+ "; apiMethod:" + apiMethod+"; headers:" + headers);
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			StringRequest request = new StringRequest(apiMethod, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.i("api", " << Api onResponse: " + response);
					result.onSuccess(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
                    result.onError(ApiUtils.getVolleyError(error));
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					try {
						return ApiUtils.getJsonObjectToHashMap(params);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return new HashMap<>();
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					try {
						return ApiUtils.getJsonObjectToHashMap(headers);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					return new HashMap<>();
				}
			};
			request.setTag(url);
			queue.add(request);
			return true;
		}
	}

    private static class HttpAsyncJsonRequest extends AsyncTask<Void, Void, Boolean> {
		private OnJSONObjectResult result;
		private String url;
		private JSONObject params;
		private JSONObject headers;
		private Context context;
		private int apiMethod;

		private HttpAsyncJsonRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnJSONObjectResult result) {
			this.result = result;
			this.params = requestParams;
			this.url = requestUrl;
			this.apiMethod = apiMethod;
			this.context = context;
			this.headers = headers;
			Log.i(HttpAsyncJsonRequest.class.getSimpleName(), "HttpAsyncJsonRequest: url = " + url + "; params:" + params+ "; apiMethod:" + apiMethod+"; headers:" + headers);
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(apiMethod, url, params, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Log.i("api", " << Api onResponse: " + response);
					result.onSuccess(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
                    result.onError(ApiUtils.getVolleyError(error));
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					try {
						if (params != null) {
							return ApiUtils.getJsonObjectToHashMap(params);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return new HashMap<>();
				}

                @Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					try {
						if (headers != null) {
							return ApiUtils.getJsonObjectToHashMap(headers);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return new HashMap<>();
				}
			};
			jsonObjectRequest.setTag(url);
			queue.add(jsonObjectRequest);
			return true;
		}
	}

}
