package com.arny.arnylib.network;

import org.json.JSONArray;
public interface OnJSONArrayResult {
	void onSuccess(JSONArray array);
	void onError(String error);
}
