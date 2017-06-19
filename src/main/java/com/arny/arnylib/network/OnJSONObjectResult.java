package com.arny.arnylib.network;

import org.json.JSONObject;
public interface OnJSONObjectResult {
	void onResult(JSONObject object);
	void onError(String error);
}
