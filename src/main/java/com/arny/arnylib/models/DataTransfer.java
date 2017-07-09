package com.arny.arnylib.models;

public class DataTransfer {
	private String method;
	private Object content;

	public DataTransfer(String method, Object content) {
		this.method = method;
		this.content = content;
	}

	public DataTransfer() {
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "method:" + method + " content:" + content;
	}
}
