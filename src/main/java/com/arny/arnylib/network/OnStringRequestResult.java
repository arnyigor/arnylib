package com.arny.arnylib.network;

public interface OnStringRequestResult {
	void onSuccess(String result);
	void onError(String error);
}