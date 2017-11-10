package com.arny.arnylib.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arny.arnylib.interfaces.OnJSONArrayResult;
import com.arny.arnylib.interfaces.OnJSONObjectResult;
import com.arny.arnylib.interfaces.OnStringRequestResult;
import com.arny.arnylib.utils.Utility;
import org.json.JSONArray;
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

	public static void apiRequest(final Context context, String url, JSONObject params, final OnStringRequestResult callback) {
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
                callback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
                callback.onError(error);
			}
		});
		httpAsyncRequest.execute((Void) null);
	}

	public static void apiRequest(final Context context, String url, JSONObject params, final OnJSONObjectResult callback) {

		HttpAsyncObjectRequest asyncJsonRequest = new HttpAsyncObjectRequest(context, Request.Method.GET, url, params, new JSONObject(), new OnJSONObjectResult() {
			@Override
			public void onSuccess(JSONObject result) {
                callback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
                callback.onError(error);
			}
		});
		asyncJsonRequest.execute((Void) null);
	}

    public static void apiRequest(final Context context, String url, JSONObject params, JSONObject headers, final OnJSONArrayResult callback) {

        HttpAsyncArrayRequest asyncJsonRequest = new HttpAsyncArrayRequest(context, Request.Method.GET, url, params, headers, new OnJSONArrayResult() {
            @Override
            public void onSuccess(JSONArray result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
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

		HttpAsyncObjectRequest asyncJsonRequest = new HttpAsyncObjectRequest(context, method, url, params, new JSONObject(), new OnJSONObjectResult() {
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

		HttpAsyncObjectRequest asyncJsonRequest = new HttpAsyncObjectRequest(context, method, url, params, headers, new OnJSONObjectResult() {
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
			Log.d(HttpAsyncObjectRequest.class.getSimpleName(), "HttpAsyncStringRequest: url = " + url + "; params:" + params+ "; apiMethod:" + apiMethod+"; headers:" + headers);
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			StringRequest request = new StringRequest(apiMethod, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.d("api", " << Api onResponse: " + response);
					result.onSuccess(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
                    Log.e("api", " << Api onResponse: " + error);
                    result.onError(ApiUtils.getVolleyError(error));
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
                    return ApiUtils.getJsonObjectToHashMap(params);
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiUtils.getJsonObjectToHashMap(headers);
				}
			};
			request.setTag(url);
			queue.add(request);
			return true;
		}
	}

    private static class HttpAsyncObjectRequest extends AsyncTask<Void, Void, Boolean> {
		private OnJSONObjectResult result;
		private String url;
		private JSONObject params;
		private JSONObject headers;
		private Context context;
		private int apiMethod;

		private HttpAsyncObjectRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnJSONObjectResult result) {
			this.result = result;
			this.params = requestParams;
			this.url = requestUrl;
			this.apiMethod = apiMethod;
			this.context = context;
			this.headers = headers;
			Log.d(HttpAsyncObjectRequest.class.getSimpleName(), "HttpAsyncObjectRequest: url = " + url + "; params:" + params+ "; apiMethod:" + apiMethod+"; headers:" + headers);
		}

		@Override
		protected Boolean doInBackground(Void... p) {
			RequestQueue queue = getRequestQueue(context);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(apiMethod, url, params, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Log.d("api", " << Api onResponse: " + response);
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
                    return ApiUtils.getJsonObjectToHashMap(params);
				}

                @Override
				public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiUtils.getJsonObjectToHashMap(headers);
				}
			};
			jsonObjectRequest.setTag(url);
			queue.add(jsonObjectRequest);
			return true;
		}
	}

    private static class HttpAsyncArrayRequest extends AsyncTask<Void, Void, Boolean> {
        private final OnJSONArrayResult result;
        private String url;
        private JSONObject params;
        private JSONObject headers;
        private Context context;
        private int apiMethod;

        private HttpAsyncArrayRequest(Context context, int apiMethod, String requestUrl, JSONObject requestParams, JSONObject headers, final OnJSONArrayResult result) {
            this.result = result;
            params = requestParams;
            url = requestUrl;
            this.apiMethod = apiMethod;
            this.context = context;
            this.headers = headers;
            Log.d(HttpAsyncObjectRequest.class.getSimpleName(), "HttpAsyncStringRequest: url = " + url + "; params:" + params+ "; apiMethod:" + apiMethod+"; headers:" + headers);
        }

        @Override
        protected Boolean doInBackground(Void... p) {
            RequestQueue queue = getRequestQueue(context);
            JsonArrayRequest request = new JsonArrayRequest(apiMethod, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    result.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result.onError(ApiUtils.getVolleyError(error));
                }
            });
            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 30 * 1000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 2;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            request.setTag(url);
            queue.add(request);
            return true;
        }
    }

}
