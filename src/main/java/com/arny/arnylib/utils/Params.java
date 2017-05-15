package com.arny.arnylib.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
public class Params {
	private JSONObject params, writeParams;
	private JSONObject nodeObject, writeObject;
	private String nodeString, writeString;
	private JSONArray nodeArray, writeArray;

	public Params(JSONObject params) {
		this.params = params;
	}

	public Params(String params) throws JSONException {
		this.params = new JSONObject(params);
	}

	public JSONObject getNodeObject() {
		return nodeObject;
	}

	public void setNodeObject(JSONObject nodeObject) {
		this.nodeObject = nodeObject;
	}

	public String getNodeString() {
		return nodeString;
	}

	public void setNodeString(String nodeString) {
		this.nodeString = nodeString;
	}

	public JSONArray getNodeArray() {
		return nodeArray;
	}

	public void setNodeArray(JSONArray nodeArray) {
		this.nodeArray = nodeArray;
	}

	public String getParam(String path, String defaultVal) {
		String result;
		resetParams();
		setNodeObject(getParams());
		try {
			parseJsonPath(path);
			result = getNodeString();
		} catch (JSONException e) {
			e.printStackTrace();
			result = defaultVal;
		}
		return result;
	}

	public String getParam(String path) {
		String result = null;
		resetParams();
		setNodeObject(getParams());
		try {
			parseJsonPath(path);
			if (getNodeObject() != null) {
				result = String.valueOf(getNodeObject());
			} else if (getNodeString() != null) {
				result = getNodeString();
			}else if (getNodeArray() != null) {
				result = String.valueOf(getNodeArray());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean setParam(String path, Object val) {
		resetParams();
		setNodeObject(getParams());
		setWriteObject(new JSONObject());
		try {
			writeJsonPath(path, val);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}



	private void writeJsonPathReversive(String path, Object val) throws JSONException {
		Object nodeObject = null;
		String[] nodes = path.split("\\.");
		for (String node : nodes) {
			initWriteObject();
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: node = " + node);
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: getNodeObject() = " + getNodeObject());
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: getWriteObject() = " + getWriteObject());
			boolean end = node.hashCode()==nodes[nodes.length - 1].hashCode() ;
			Iterator<String> paramsKeys = getNodeObject().keys();
			while (paramsKeys.hasNext()) {
				String nodeKey = paramsKeys.next();
				if (node.equals(nodeKey)) {
					Log.d(Params.class.getSimpleName(), "inside params: "+node+ " finded = " + nodeKey);
					nodeObject = getNodeObject().get(nodeKey);
					Log.d(Params.class.getSimpleName(), "inside params: nodeObject = " + nodeObject);
					getWriteObject().put(node, nodeObject);
					Log.d(Params.class.getSimpleName(), "inside params: getWriteObject() = " + getWriteObject());
					setNodeObject(new JSONObject(String.valueOf(nodeObject)));
					Log.d(Params.class.getSimpleName(), "inside params: getNodeObject() = " + getNodeObject());
				}
			}
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: end = " + end);
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: getNodeObject() = " + getNodeObject());
			Log.d(Params.class.getSimpleName(), "writeJsonPathReversive: getWriteObject() = " + getWriteObject());

		}


	}


	private void writeJsonPath(String path, Object val) throws JSONException {
		Object pathNodeObj = null;
		String[] nodes = path.split("\\.");
		for (int i = 0; i < nodes.length; i++) {
			String node = nodes[i];
			boolean end = node.equals(nodes[nodes.length - 1]);
			Log.d(Params.class.getSimpleName(), "!!!!!!!!!!!writeJsonPath: node = " + node + " !!!!!!!!!!!!!");
			Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeObject before= " + getNodeObject());
			Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeString before= " + getNodeString());
			Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeArray before= " + getNodeArray());
			Log.d(Params.class.getSimpleName(), "writeJsonPath: getWriteObject before= " + getWriteObject());
			boolean newPathElem = false;
			if (getNodeObject() != null) {
				if (getNodeObject().has(node)) {
					pathNodeObj = getNodeObject().get(node);
					Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj = " + pathNodeObj);
				}else{
					newPathElem = true;
					pathNodeObj = getNodeObject().put(node,val);
					Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj = " + pathNodeObj);
				}
			} else if (getNodeString() != null)  {
				pathNodeObj = getNodeString();
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj = " + pathNodeObj);
			} else if (getNodeArray() != null)  {
				pathNodeObj = getNodeArray().get(Integer.parseInt(node));
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj = " + pathNodeObj);
			}
			if (newPathElem) {
				Log.d(Params.class.getSimpleName(), "writeJsonPath: newPathElem = " + newPathElem);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeObject after = " + getNodeObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeString after = " + getNodeString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: getNodeArray after = " + getNodeArray().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: getWriteObject after = " + getWriteObject().toString());
			}

			if (pathNodeObj instanceof JSONArray) {
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof JSONArray -> getWriteObject() = " + getWriteObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof JSONArray -> getNodesObjects() = " + getNodeObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: node pathNodeObj = " + pathNodeObj);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: new val = " + val);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: end = " + end);
				if (end){
					getWriteObject().put(node, val);
					Log.d(Params.class.getSimpleName(), "rewtire old JSONArray: getNodeObject() = " + getNodeObject().toString());
					Log.d(Params.class.getSimpleName(), "rewtire old JSONArray: getWriteObject() = " + getWriteObject().toString());
				}else{
					getWriteObject().put(node, pathNodeObj);
				}
				setNodeObject(null);
				setNodeArray(new JSONArray(String.valueOf(pathNodeObj)));
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof JSONArray -> getWriteObject() = " + getWriteObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof JSONArray -> getNodesObject() = " + getNodeObject().toString());
			} else if (pathNodeObj instanceof String || pathNodeObj instanceof Integer) {
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof String -> getWriteObject() = " + getWriteObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof String -> getNodesObject() = " + getNodeObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: node pathNodeObj = " + pathNodeObj);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: new val = " + val);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: end = " + end);
				if (end) {
					getNodeObject().put(node, val);
					getWriteObject().put(nodes[0], getNodeObject());
					Log.d(Params.class.getSimpleName(), "rewtire old string: getNodeObject() = " + getNodeObject().toString());
					Log.d(Params.class.getSimpleName(), "rewtire old string: getWriteObject() = " + getWriteObject().toString());
				}else{
					getWriteObject().put(node, pathNodeObj);
				}
				setNodeObject(null);
				setNodeArray(null);
				setNodeString(String.valueOf(pathNodeObj));
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof String -> getWriteObject() = " + getWriteObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof String -> getNodesObject() = " + getNodeObject().toString());
			} else if (pathNodeObj instanceof JSONObject) {
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof JSONObject -> getWriteObject() = " + getWriteObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: pathNodeObj instanceof JSONObject -> getNodesObject() = " + getNodeObject().toString());
				Log.d(Params.class.getSimpleName(), "writeJsonPath: node pathNodeObj = " + pathNodeObj);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: new val = " + val);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: end = " + end);
				Log.d(Params.class.getSimpleName(), "writeJsonPath: newPathElem = " + newPathElem);
				if (end) {
					if (newPathElem) {
						Log.d(Params.class.getSimpleName(), "writeJsonPath: oldPath = " +node + " value = " + val + " pathNodeObj = " + pathNodeObj );
						getNodeObject().put(node, val);
						getWriteObject().put(node, getNodeObject());
						Log.d(Params.class.getSimpleName(), "rewtire old JSONObject: getNodeObject() = " + getNodeObject());
						Log.d(Params.class.getSimpleName(), "rewtire old JSONObject: getWriteObject() = " + getWriteObject());
					}else{
						Log.d(Params.class.getSimpleName(), "writeJsonPath: node = " +node +  " value = " + val + " pathNodeObj = " + pathNodeObj );
						Log.d(Params.class.getSimpleName(), "END : getNodeObject() = " + getNodeObject());
						Log.d(Params.class.getSimpleName(), "END : getWriteObject() = " + getWriteObject());
						getNodeObject().put(node, val);
						if (i != 0) {
							getWriteObject().put(nodes[0], getNodeObject());
						}else{
							setWriteObject(getNodeObject());
						}

						Log.d(Params.class.getSimpleName(), "END 2 : getNodeObject() = " + getNodeObject());
						Log.d(Params.class.getSimpleName(), "END  2: getWriteObject() = " + getWriteObject());
					}
				}else{
					Log.d(Params.class.getSimpleName(), "writeJsonPath!!!!!!:oldnode =  \""+node+"\" not end -> getNodeObject() = " + getNodeObject() + " pathNodeObj = " + pathNodeObj);
//					getNodeObject().put(node, pathNodeObj);
//					setWriteObject(getNodeObject());
					getWriteObject().put(node, pathNodeObj);//TODO неверно на втором круге
				}
				setNodeObject(new JSONObject(String.valueOf(pathNodeObj)));
				setNodeArray(null);
				setNodeString(null);
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof JSONObject -> getWriteObject() = " + getWriteObject());
				Log.d(Params.class.getSimpleName(), "writeJsonPath 2: pathNodeObj instanceof JSONObject -> getNodesObject() = " + getNodeObject());
			}
			Log.d(Params.class.getSimpleName(), "writeJsonPath: \""+node+"\" getWriteObject() = " + getWriteObject());
			Log.d(Params.class.getSimpleName(), "writeJsonPath: \""+node+"\" getNodesObject() = " + getNodeObject());
		}
		Iterator<String> paramsKeys = getParams().keys();
		while (paramsKeys.hasNext()) {
			String key = paramsKeys.next();
			if (getWriteObject().has(key)) {
				Log.d(Params.class.getSimpleName(), "getWriteObject: " + getWriteObject().get(key));
				getParams().put(key, getWriteObject().get(key));
			}
			Log.d(Params.class.getSimpleName(), "getParams: " + getParams().get(key));
//			Log.d(Params.class.getSimpleName(), "hasNext: " + key);
//			Object oldParams = getParams().get(key);
//			Log.d(Params.class.getSimpleName(), "hasNext: oldParams = " + oldParams);
//			Object newParams;
//			if (getParams().has(key)) {
//				newParams = getWriteObject().get(key);
//				Log.d(Params.class.getSimpleName(), "writeJsonPath: newParams = " + newParams);
//				getParams().put(key, newParams);
//			}else{
//				getParams().put(key, oldParams);
//			}
		}
		Log.d(Params.class.getSimpleName(), "writeJsonPath: getParams() = " + getParams());
	}

	private void initWriteObject() {
		if (getWriteObject() == null) {
			setWriteObject(new JSONObject());
		}
	}

	private void parseJsonPath(String path) throws JSONException {
		Object obj = null;
		String[] pathArr = path.split("\\.");
		for (String pathElem : pathArr) {
//				Log.d(Params.class.getSimpleName(), "getStringParam: pathElem = " + pathElem);
			if (getNodeObject() != null) {
				obj = getNodeObject().get(pathElem);
			} else {
				obj = getNodeArray().get(Integer.parseInt(pathElem));
			}
//				Log.d(Params.class.getSimpleName(), "getStringParam: obj = " + obj);
			if (obj instanceof JSONArray) {
				setNodeObject(null);
				setNodeArray(new JSONArray(String.valueOf(obj)));
//					Log.d(Params.class.getSimpleName(), "getStringParam: JSONArray = " + obj);
			} else if (obj instanceof String || obj instanceof Integer) {
//					Log.d(Params.class.getSimpleName(), "getStringParam: String = " + obj);
				setNodeObject(null);
				setNodeArray(null);
				setNodeString(String.valueOf(obj));
			} else if (obj instanceof JSONObject) {
				setNodeObject(new JSONObject(String.valueOf(obj)));
				setNodeArray(null);
//					Log.d(Params.class.getSimpleName(), "getStringParam: JSONObject = " + obj);
			}
		}
	}

	private void resetParams() {
		setNodeArray(null);
		setNodeObject(null);
		setNodeString(null);
		setWriteObject(null);
	}

	public JSONObject getParam(String path, JSONObject defaultVal) {
		JSONObject result;
		setNodeObject(getParams());
		try {
			parseJsonPath(path);
			result = getNodeObject();
		} catch (JSONException e) {
			e.printStackTrace();
			result = defaultVal;
		}
		resetParams();
		return result;
	}

	public JSONArray getParam(String path, JSONArray defaultVal) {
		JSONArray result;
		setNodeObject(getParams());
		try {
			parseJsonPath(path);
			result = getNodeArray();
		} catch (JSONException e) {
			e.printStackTrace();
			result = defaultVal;
		}
		resetParams();
		return result;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public JSONObject getWriteObject() {
		return writeObject;
	}

	public void setWriteObject(JSONObject writeObject) {
		this.writeObject = writeObject;
	}

	public String getWriteString() {
		return writeString;
	}

	public void setWriteString(String writeString) {
		this.writeString = writeString;
	}

	public JSONArray getWriteArray() {
		return writeArray;
	}

	public void setWriteArray(JSONArray writeArray) {
		this.writeArray = writeArray;
	}

	public JSONObject getWriteParams() {
		return writeParams;
	}

	public void setWriteParams(JSONObject writeParams) {
		this.writeParams = writeParams;
	}
}
