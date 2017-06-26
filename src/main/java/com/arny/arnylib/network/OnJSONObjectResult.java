package com.arny.arnylib.network;

import org.json.JSONObject;
public interface OnJSONObjectResult {
	void onSuccess(JSONObject object);
	void onError(String error);
}
