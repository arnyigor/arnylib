package com.arny.arnylib.network;

import android.util.Log;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
public class ApiUtils {

    public static <T> ArrayList<T> convert(JsonArray jArr) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            for (int i = 0, l = jArr.size(); i < l; i++) {
                list.add((T) jArr.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static HashMap<String, String> getJsonObjectToHashMap(JSONObject params) throws JSONException {
        HashMap<String, String> mapParams = new HashMap<>();
        if (params.names() != null) {
            for (int i = 0; i < params.names().length(); i++) {
                mapParams.put(params.names().getString(i), (String) params.get(params.names().getString(i)));
            }
        }
        Log.i(NetworkService.class.getSimpleName(), "getJsonObjectToHashMap: mapParams = " + mapParams);
        return mapParams;
    }

    public static String getVolleyError(VolleyError error) {
        String message;
        if (error != null && error.networkResponse != null) {
            message = "code:" + error.networkResponse.statusCode + "; data:" + new String(error.networkResponse.data);
        } else {
            if (error != null) {
                message = error.getMessage();
            } else {
                message = "Error = null";
            }
        }
        Log.e("api", " << Api onErrorResponse message = " + message);
        return message;
    }

    public static <T> T getResponse(Object response, Class cls) {
        String name = response.getClass().getSimpleName();
        Log.i(ApiUtils.class.getSimpleName(), "getResponse: class: " + name + " response = " + response);
        return (T) new Gson().fromJson(String.valueOf(response), cls);
    }
}