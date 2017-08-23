package com.arny.arnylib.network;
import android.content.Context;
import android.support.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Map;

public class ApiRequest {

    public static void apiResponse(Context context, String url, Map<String, String> headers, @NonNull final Response.Listener<Object> listener, @NonNull final Response.ErrorListener errorListener) {
        RequestQueue queue = NetworkService.getRequestQueue(context);
        GsonGetRequest request = new GsonGetRequest<>(url, headers, listener, errorListener);
        request.setTag(url);
        queue.add(request);
    }

    public static void apiResponse(Context context, String url, JsonObject params, Map<String, String> headers, @NonNull final Response.Listener<Object> listener, @NonNull final Response.ErrorListener errorListener) {
        RequestQueue queue = NetworkService.getRequestQueue(context);
        GsonPostRequest gsonPostRequest = new GsonPostRequest<>(url, params.toString(), headers, listener, errorListener);
        gsonPostRequest.setTag(url);
        gsonPostRequest.setShouldCache(false);
        queue.add(gsonPostRequest);
    }
}