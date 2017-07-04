package com.arny.arnylib.interfaces;

import org.json.JSONArray;
public interface OnJSONArrayResult {
	void onSuccess(JSONArray array);
	void onError(String error);
}
