package com.arny.arnylib.network;

import org.json.JSONObject;
interface OnJSONRequestResult {
	void onResult(JSONObject object);
	void onError(String error);
}
