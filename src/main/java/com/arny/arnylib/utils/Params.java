package com.arny.arnylib.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Params {
	private JSONObject params;
	private JSONObject nodeObject;
	private String nodeString;
	private JSONArray nodeArray;

	public Params(JSONObject params) {
		this.params = params;
	}

	public Params(String params) {
		try {
			this.params = new JSONObject(params);
		} catch (JSONException e) {
			this.params = new JSONObject();
			e.printStackTrace();
		}
	}

	public Params() {
		this.params = new JSONObject();
	}

	private JSONObject getNodeObject() {
		return nodeObject;
	}

	private void setNodeObject(JSONObject nodeObject) {
		this.nodeObject = nodeObject;
	}

	private String getNodeString() {
		return nodeString;
	}

	private void setNodeString(String nodeString) {
		this.nodeString = nodeString;
	}

	private JSONArray getNodeArray() {
		return nodeArray;
	}

	private void setNodeArray(JSONArray nodeArray) {
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
		if (getParams() == null) {
			return null;
		}
		if (getParams().length() == 0) {
			return null;
		}
		String result = null;
		resetParams();
		setNodeObject(getParams());
		try {
			parseJsonPath(path);
			if (getNodeObject() != null) {
				result = String.valueOf(getNodeObject());
			} else if (getNodeString() != null) {
				result = getNodeString();
			} else if (getNodeArray() != null) {
				result = String.valueOf(getNodeArray());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean setParam(String path, Object val) {
		return setParamVal(path, val);
	}

	public boolean setParam(String path, String val) {
		return setParamVal(path, val);
	}

	public boolean setParam(String path, int val) {
		return setParamVal(path, val);
	}

	public boolean setParam(String path, double val) {
		return setParamVal(path, val);
	}

	public boolean setParam(String path, JSONArray val) {
		return setParamVal(path, val);
	}

	public boolean setParam(String path, JSONObject val) {
		return setParamVal(path, val);
	}

	private boolean setParamVal(String path, Object val) {
		JSONObject jsonObject = getParams();
		try {
			setProperty(jsonObject, path, val);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeParam(String path) {
		JSONObject jsonObject = getParams();
		try {
			unsetProperty(jsonObject, path);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private JSONObject unsetProperty(JSONObject jsonObject, String keys) throws JSONException {
		String[] keyMain = keys.split("\\.");
		for (String keym : keyMain) {
			boolean hasKey = jsonObject.has(keym);
//            System.out.println("jsonObject = " + jsonObject);
			String key;
			if (hasKey) {
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					key = (String) iterator.next();
//                    System.out.println("key = " + key);
//                    System.out.println("keym = " + keym);
					if (key.equals(keym)) {
//                        System.out.println("jsonObject.optJSONArray(key) = " + jsonObject.optJSONArray(key));
//                        System.out.println("jsonObject.optJSONObject(key) = " + jsonObject.optJSONObject(key));
//                        System.out.println("jsonObject.optString(key) = " + jsonObject.optString(key));
//                        System.out.println("valueNew = " + valueNew);
						if (((jsonObject.optJSONArray(key) == null) && (jsonObject.optJSONObject(key) == null) && (jsonObject.optString(key) == null)) || (keym.equals(keyMain[keyMain.length - 1]))) {
							jsonObject.remove(key);
							return jsonObject;
						} else if (jsonObject.optJSONObject(key) != null && jsonObject.optJSONArray(key) == null) {
							jsonObject = jsonObject.getJSONObject(key);
							break;
						} else if (jsonObject.optString(key) != null && jsonObject.optJSONArray(key) == null) {
							jsonObject.put(key, new JSONObject());
							jsonObject = jsonObject.getJSONObject(key);
							break;
						} else if (jsonObject.optJSONArray(key) != null) {
							jsonObject.put(key, new JSONObject());
							jsonObject = jsonObject.getJSONObject(key);
						}
					}
				}
			}

		}
		return jsonObject;
	}

	private JSONObject setProperty(JSONObject jsonObject, String keys, Object valueNew) throws JSONException {
		String[] keyMain = keys.split("\\.");
		for (String keym : keyMain) {
			boolean hasKey = jsonObject.has(keym);
//            System.out.println("jsonObject = " + jsonObject);
			String key;
			if (hasKey) {
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					key = (String) iterator.next();
//                    System.out.println("key = " + key);
//                    System.out.println("keym = " + keym);
					if (key.equals(keym)) {
//                        System.out.println("jsonObject.optJSONArray(key) = " + jsonObject.optJSONArray(key));
//                        System.out.println("jsonObject.optJSONObject(key) = " + jsonObject.optJSONObject(key));
//                        System.out.println("jsonObject.optString(key) = " + jsonObject.optString(key));
//                        System.out.println("valueNew = " + valueNew);
						if (((jsonObject.optJSONArray(key) == null) && (jsonObject.optJSONObject(key) == null) && (jsonObject.optString(key) == null)) || (keym.equals(keyMain[keyMain.length - 1]))) {
							jsonObject.put(key, valueNew);
							return jsonObject;
						} else if (jsonObject.optJSONObject(key) != null && jsonObject.optJSONArray(key) == null) {
							jsonObject = jsonObject.getJSONObject(key);
							break;
						} else if (jsonObject.optString(key) != null && jsonObject.optJSONArray(key) == null) {
							jsonObject.put(key, new JSONObject());
							jsonObject = jsonObject.getJSONObject(key);
							break;
						} else if (jsonObject.optJSONArray(key) != null) {
							jsonObject.put(key, new JSONObject());
							jsonObject = jsonObject.getJSONObject(key);
						}
					}
				}
			} else {
				if (!keym.equals(keyMain[keyMain.length - 1])) {
					jsonObject.put(keym, new JSONObject());
					jsonObject = jsonObject.getJSONObject(keym);
				} else {
					jsonObject = jsonObject.put(keym, valueNew);
				}
			}

		}
		return jsonObject;
	}

	private void parseJsonPath(String path) throws JSONException {
		Object obj;
		String[] pathArr = path.split("\\.");
		for (String pathElem : pathArr) {
//				System.out.println("getStringParam: pathElem = " + pathElem);
			if (getNodeObject() != null) {
				if (!getNodeObject().has(pathElem)) {
					resetParams();
					return;
				}
				obj = getNodeObject().get(pathElem);
			} else {
				obj = getNodeArray().get(Integer.parseInt(pathElem));
			}
//				System.out.println("getStringParam: obj = " + obj);
			if (obj instanceof JSONArray) {
				setNodeObject(null);
				setNodeArray(new JSONArray(String.valueOf(obj)));
//					System.out.println("getStringParam: JSONArray = " + obj);
			} else if (obj instanceof String || obj instanceof Integer) {
//					System.out.println("getStringParam: String = " + obj);
				setNodeObject(null);
				setNodeArray(null);
				setNodeString(String.valueOf(obj));
			} else if (obj instanceof JSONObject) {
				setNodeObject(new JSONObject(String.valueOf(obj)));
				setNodeArray(null);
//					System.out.println("getStringParam: JSONObject = " + obj);
			}
		}
	}

	private void resetParams() {
		setNodeArray(null);
		setNodeObject(null);
		setNodeString(null);
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

	private String getStringParams() {
        if (params == null) {
            return null;
        }
        return String.valueOf(params);
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public void setParams(String params) {
		try {
			this.params = new JSONObject(params);
		} catch (JSONException e) {
			this.params = new JSONObject();
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return getStringParams();
	}
}