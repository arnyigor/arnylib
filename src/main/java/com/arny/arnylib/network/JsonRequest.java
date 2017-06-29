package com.arny.arnylib.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class JsonRequest extends JsonObjectRequest {

	private JSONObject headers;

	public JsonRequest(int method, String url, JSONObject jsonRequest, JSONObject headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public JsonRequest(String url, JSONObject jsonRequest,JSONObject headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		this.headers = headers;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		try {
			return NetworkService.getJsonObjectToHashMap(headers);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
