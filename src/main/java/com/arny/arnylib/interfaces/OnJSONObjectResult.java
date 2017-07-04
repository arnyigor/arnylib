package com.arny.arnylib.interfaces;

import org.json.JSONObject;
public interface OnJSONObjectResult {
	void onSuccess(JSONObject result);
	void onError(String error);
}
