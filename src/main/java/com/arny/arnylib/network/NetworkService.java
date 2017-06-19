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

    public static Document htmlPostRequest(String url, JSONObject params ) {
        try {
            HashMap<String, String> mapParams = getJsonObjectToHashMap(params);
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
    public  static Document htmlGetRequest(String url, JSONObject params ) {
        try {
            HashMap<String, String> mapParams = getJsonObjectToHashMap(params);
           return  Jsoup.connect(url).data(mapParams).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    private static HashMap<String, String> getJsonObjectToHashMap(JSONObject params) throws JSONException {
        HashMap<String, String> mapParams = new HashMap<>();
        for(int i = 0; i<params.names().length(); i++){
            mapParams.put(params.names().getString(i), (String) params.get(params.names().getString(i)));
        }
        return mapParams;
    }

	public static void apiRequest(final Context context,int method, String url, JSONObject params, final OnStringRequestResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
		HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(method,url, params, new OnStringRequestResult() {
			@Override
			public void onSuccess(String result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		}, context);
		httpAsyncRequest.execute((Void) null);
	}

    public static void apiRequest(final Context context, String url, JSONObject params, final OnStringRequestResult successCallback) {
        Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());
        HttpAsyncStringRequest httpAsyncRequest = new HttpAsyncStringRequest(url, params, new OnStringRequestResult() {
            @Override
            public void onSuccess(String result) {
                successCallback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                Log.e("api", " << Api Response Error: " + error);
                successCallback.onError(error);
            }
        }, context);
        httpAsyncRequest.execute((Void) null);
    }

    public static void apiRequest(final Context context, String url, JSONObject params, final OnJSONObjectResult successCallback) {
        Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());

        HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(url, params, new OnJSONObjectResult() {
            @Override
            public void onResult(JSONObject object) {
                successCallback.onResult(object);
            }

            @Override
            public void onError(String error) {
                Log.e("api", " << Api Response Error: " + error);
                successCallback.onError(error);
            }
        }, context);
        asyncJsonRequest.execute((Void) null);
    }

	public static void apiRequest(final Context context,int method,  String url, JSONObject params, final OnJSONObjectResult successCallback) {
		Log.i("api", " >> Api Request: " + url + " with params: " + params.toString());

		HttpAsyncJsonRequest asyncJsonRequest = new HttpAsyncJsonRequest(method,url, params, new OnJSONObjectResult() {
			@Override
			public void onResult(JSONObject object) {
				successCallback.onResult(object);
			}

			@Override
			public void onError(String error) {
				Log.e("api", " << Api Response Error: " + error);
				successCallback.onError(error);
			}
		}, context);
		asyncJsonRequest.execute((Void) null);
	}

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    private static class HttpAsyncStringRequest extends AsyncTask<Void, Void, Boolean> {
        private final OnStringRequestResult successCallback;
        String url;
        JSONObject params;
        Context context;
        int apiMethod;

        HttpAsyncStringRequest(String requestUrl, JSONObject requestParams, final OnStringRequestResult successCallback, Context context) {
            this.successCallback = successCallback;
            params = requestParams;
            url = requestUrl;
            this.apiMethod = Request.Method.GET;
            this.context = context;
        }
        public HttpAsyncStringRequest(int apiMethod, String requestUrl, JSONObject requestParams, final OnStringRequestResult successCallback, Context context) {
            this.successCallback = successCallback;
            params = requestParams;
            url = requestUrl;
            this.apiMethod = apiMethod;
            this.context = context;
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
            }){
                @Override
                protected Map<String, String> getParams() {
                    try {
                        return Utility.toMap(params);
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
        private final OnJSONObjectResult successCallback;
        String url;
        JSONObject params;
        Context context;
        int apiMethod;

        HttpAsyncJsonRequest(String requestUrl, JSONObject requestParams, final OnJSONObjectResult successCallback, Context context) {
            this.successCallback = successCallback;
            params = requestParams;
            url = requestUrl;
            this.apiMethod = Request.Method.GET;
            this.context = context;
        }
       HttpAsyncJsonRequest(int apiMethod, String requestUrl, JSONObject requestParams, final OnJSONObjectResult successCallback, Context context) {
            this.successCallback = successCallback;
            params = requestParams;
            url = requestUrl;
            this.apiMethod = apiMethod;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... p) {
            RequestQueue queue = getRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(apiMethod, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    successCallback.onResult(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    successCallback.onError(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
            return true;
        }
    }

}
