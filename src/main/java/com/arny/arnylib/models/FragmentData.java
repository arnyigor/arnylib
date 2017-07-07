package com.arny.arnylib.models;

public class FragmentData {
	private String method;
	private String content;

	public FragmentData(String method, String content) {
		this.method = method;
		this.content = content;
	}

	public FragmentData() {
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
