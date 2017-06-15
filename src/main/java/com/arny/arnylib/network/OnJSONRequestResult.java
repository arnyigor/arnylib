package com.arny.arnylib.network;

import org.json.JSONObject;
public interface OnJSONRequestResult {
	void onResult(JSONObject object);
	void onError(String error);
}
