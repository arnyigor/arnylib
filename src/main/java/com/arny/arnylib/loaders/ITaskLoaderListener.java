package com.arny.arnylib.loaders;

public interface ITaskLoaderListener {
	void onLoadFinished(Object data);
	void onCancelLoad();
}
