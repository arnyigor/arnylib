package com.arny.arnylib.network;

import org.json.JSONArray;
public interface OnJSONArrayResult {
	void onResult(JSONArray array);
	void onError(String error);
}
