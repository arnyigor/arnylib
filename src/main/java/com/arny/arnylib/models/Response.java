package com.arny.arnylib.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
public class Response {

	@Nullable
	private Object mAnswer;

	private Object mRequestResult;

	public Response() {
		mRequestResult = null;
	}

	@NonNull
	public Object getRequestResult() {
		return mRequestResult;
	}

	public Response setRequestResult(Object requestResult) {
		mRequestResult = requestResult;
		return this;
	}

	@Nullable
	public <T> T getTypedAnswer() {
		if (mAnswer == null) {
			return null;
		}
		//noinspection unchecked
		return (T) mAnswer;
	}

	public Response setAnswer(@Nullable Object answer) {
		mAnswer = answer;
		return this;
	}

	public void save(Context context) {
	}
}