package com.arny.arnylib.service;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

public class OperationProvider implements Serializable {
    private int id,type;
    private boolean finished, success;
    private HashMap<String, Object> operationData;
    private String result;

	@Override
	public String toString() {
		return "id:" + id + "; finished:" + finished + "; success:" + success + "; operationData" + operationData;
	}
	public OperationProvider(Bundle extras) {
        this.id = extras.getInt(AbstractIntentService.EXTRA_KEY_OPERATION_ID);
        this.finished = extras.getBoolean(AbstractIntentService.EXTRA_KEY_OPERATION_FINISH);
        this.success = extras.getBoolean(AbstractIntentService.EXTRA_KEY_OPERATION_FINISH_SUCCESS);
        this.operationData = extras.getParcelable(AbstractIntentService.EXTRA_KEY_OPERATION_DATA);
        this.result = extras.getString(AbstractIntentService.EXTRA_KEY_OPERATION_RESULT);
    }

    public OperationProvider(int id, int type, HashMap<String, Object> operationData) {
        this.id = id;
        this.operationData = operationData;
        this.type = type;
    }

    public void setOperationData(HashMap<String, Object> operationData) {
        this.operationData = operationData;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isSuccess() {
        return success;
    }

    public HashMap<String, Object> getOperationData() {
        return operationData;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}