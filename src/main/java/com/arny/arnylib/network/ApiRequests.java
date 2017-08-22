package com.arny.arnylib.network;
import android.content.Context;
import android.support.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ApiRequests {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer())
            .create();



    public static void getApiResponse(Context context, String url, @NonNull final com.android.volley.Response.Listener<Object> listener, @NonNull final com.android.volley.Response.ErrorListener errorListener) {
        RequestQueue queue = NetworkService.getRequestQueue(context);
        queue.add(new GsonGetRequest<>( url, listener, errorListener));
    }

    public static GsonGetRequest<ArrayList<ApiResponse>> getApiResponseArray(String url, @NonNull final com.android.volley.Response.Listener<ArrayList<ApiResponse>> listener, @NonNull final com.android.volley.Response.ErrorListener errorListener) {
        return new GsonGetRequest<>( url, listener, errorListener);
    }

    public static GsonPostRequest getPostApiResponse(String url, JsonObject params, @NonNull final com.android.volley.Response.Listener<ApiResponse> listener, @NonNull final com.android.volley.Response.ErrorListener errorListener) {

        final GsonPostRequest gsonPostRequest = new GsonPostRequest<>(
                url,
                params.toString(),
                new TypeToken<ApiResponse>() {
                }.getType(),
                gson,
                listener,
                errorListener
        );

        gsonPostRequest.setShouldCache(false);

        return gsonPostRequest;
    }
}