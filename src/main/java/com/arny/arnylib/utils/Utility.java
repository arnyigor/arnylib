package com.arny.arnylib.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.*;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	private static final String TIME_SEPARATOR_TWICE_DOT = ":";
	private static final String TIME_SEPARATOR_DOT = ".";

	public static String trimInside(String text) {
		return text.trim().replace(" ", "");
	}

	public static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {
		Map<String, String> retMap = new HashMap<String, String>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
		HashMap<String, String> map = new HashMap<String, String>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, String.valueOf(value));
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public static JSONObject concat(JSONObject[] objs) {
		ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
		for (JSONObject o : objs) {
			if (o.length() > 0) {
				jsonObjectArrayList.add(o);
			}
		}
		JSONObject merged = new JSONObject();
		JSONObject[] jsonObjects = jsonObjectArrayList.toArray(
				new JSONObject[jsonObjectArrayList.size()]);
		for (JSONObject obj : jsonObjects) {
			Iterator it = obj.keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = null;
				try {
					value = obj.get(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					merged.put(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return merged;
	}

	/**
	 * Remove the file extension from a filename, that may include a path.
	 * <p>
	 * e.g. /path/to/myfile.jpg -> /path/to/myfile
	 */
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * Return the file extension from a filename, including the "."
	 * <p>
	 * e.g. /path/to/myfile.jpg -> .jpg
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}

		int index = indexOfExtension(filename);

		if (index == -1) {
			return filename;
		} else {
			return filename.substring(index);
		}
	}

	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(".");
		int lastDirSeparator = filename.lastIndexOf("/");
		if (lastDirSeparator > extensionPos) {
			return -1;
		}
		return extensionPos;
	}

	public static ArrayList<String> sortDates(ArrayList<String> dates, final String format) {
		Collections.sort(dates, new Comparator<String>() {
			private SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

			@Override
			public int compare(String o1, String o2) {
				int result = -1;
				try {
					result = sdf.parse(o1).compareTo(sdf.parse(o2));
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
				return result;
			}
		});
		return dates;
	}

	public static boolean matcher(String regex, String string) {
		return Pattern.matches(regex, string);
	}

	public static String match(String where, String pattern, int groupnum) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(where);
		while (m.find()) {
			if (!m.group(groupnum).equals("")) {
				return m.group(groupnum);
			}
		}
		return null;
	}

	public static int[] bubbleSort(int[] arr) {
		for (int i = arr.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					int t = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = t;
				}
			}
		}
		return arr;
	}

    public static boolean empty(Object obj) {
		if (obj == null) {
			return true;
		} else {
			if (obj instanceof String) {
				String s = (String) obj;
				return s.trim().equals("null") || s.trim().isEmpty();
			}else if (obj instanceof List) {
                return ((List) obj).isEmpty();
            } else {
				return false;
			}
		}
	}

	public static Boolean isRequestSuccess(JSONObject result) {
		try {
			return Boolean.valueOf(result.getString("success"));
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Boolean isRequestSuccess(JSONObject result, String strResponseKey, String strResponseVal) {
		try {
			return result.getString(strResponseKey).equals(strResponseVal);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String clearNullString(String input) {
		if (Utility.empty(input) || input.equals("null")) return "";
		else return input;
	}

	public static boolean isEmailValid(String email) {
		String result = match(email, EMAIL_PATTERN, 0);
		return !empty(result);
	}

	public static String readAssetFile(Context context, String folder, String fileName) {
		InputStream input;
		try {
			input = context.getAssets().open(folder + "/" + fileName);
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();
			return new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String> getJsonObjectKeys(Gson gson, String result) {
		ArrayList<String> keys = new ArrayList<>();
		try {
			Iterator keysToCopyIterator = new JSONObject(gson.fromJson(result, JsonElement.class).toString()).keys();
			while (keysToCopyIterator.hasNext()) {
				keys.add((String) keysToCopyIterator.next());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return keys;
	}

	public static ArrayList<String> getJsonArrayKeys(JSONObject object) {
		ArrayList<String> keys = new ArrayList<>();
		Iterator<String> iterator = object.keys();
		while (iterator.hasNext()) {
			keys.add(iterator.next());
		}
		return keys;
	}

	public static String getJsonObjVal(String result, String key) {
		Gson gson = new Gson();
		ArrayList<String> keys = Utility.getJsonObjectKeys(gson, result);
		JsonObject jsonObject = gson.fromJson(result, JsonElement.class).getAsJsonObject();
		if (keys.contains(key)) {
			return jsonObject.get(key).toString();
		}
		return "";
	}

	public static double getTimeDiff(long starttime) {
		return (double) (System.currentTimeMillis() - starttime) / 1000;
	}

	public static Float[] interpolate(float oldCnt, float newcnt, int cnt) {
		float diff = newcnt - oldCnt;
		float onePoint = diff / cnt;
		float current = oldCnt;
		ArrayList<Float> arr = new ArrayList<>();
		while (true) {
			arr.add(current);
			current = current + onePoint;
			if (current >= newcnt) {
				break;
			}
		}
		arr.add(newcnt);

		Float[] tointer = new Float[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			tointer[i] = arr.get(i);
		}
		return tointer;
	}

	public static <T> boolean contains(ArrayList<T> array, T v) {
		for (T e : array) {
			if (v.equals(e)) {
				return true;
			}
		}
		return false;
	}

	public static void iterHashMap(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
		}
	}

	public static String stringContains(String where, String[] cases, String[] answers) {
		for (int i = 0; i < cases.length; i++) {
			if (where.contains(cases[i])) return answers[i];
		}
		return where;
	}

	public static <T> boolean contains(final T[] array, T v) {
		for (T e : array) {
			if (v.equals(e)) {
				return true;
			}
		}
		return false;
	}

	public static void setJsonParam(JSONObject params, String col, Object val) {
		try {
			params.put(col, val);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static <V, T> ArrayList<T> getValuesFromMap(HashMap<V, T> hashMap) {
		ArrayList<T> list = new ArrayList<>();
		for (Map.Entry<V, T> entry : hashMap.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}

	public static <V, T> ArrayList<V> getKeysFromMap(HashMap<V, T> map) {
		ArrayList<V> list = new ArrayList<>();
		for (Map.Entry<V, T> entry : map.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}

	public static String getFields(Object o) {
		Collection<Field> fields = getFields(o.getClass());
		StringBuilder builder = new StringBuilder();
		builder.append(o.getClass().getSimpleName()).append("(");
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				String msg = field.getName() + ":'" + field.get(o) + "'(" + field.getType().getSimpleName() + ");";
				builder.append(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		builder.append(") \n");
		return builder.toString();
	}

	/**
	 * Get all fields of a class.
	 *
	 * @param clazz The class.
	 * @return All fields of a class.
	 */
	public static Collection<Field> getFields(Class<?> clazz) {
		Map<String, Field> fields = new HashMap<>();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.getName().equalsIgnoreCase("shadow$_klass_") && !field.getName().equalsIgnoreCase("serialVersionUID") && !field.getName().equalsIgnoreCase("$change") && !field.getName().equalsIgnoreCase("shadow$_monitor_")) {
					if (!fields.containsKey(field.getName())) {
						fields.put(field.getName(), field);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fields.values();
	}

	public static String getFields(Object cls, String[] include) {
		Collection<Field> fields = getFields(cls.getClass());
		StringBuilder builder = new StringBuilder();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				String msg = "\n" + field.getName() + ":" + field.get(cls) + "; ";
				for (String s : include) {
					if (s.equalsIgnoreCase(field.getName())) {
						builder.append(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		return builder.toString();
	}

	public static <T>  Observable<T> IOThreadObservable( Observable<T> observable) {
		return observable.subscribeOn(Schedulers.io());
	}

	public static <T> Observable<T> IOThreadObservable(Scheduler scheduler, Observable<T> observable) {
		return observable.subscribeOn(scheduler);
	}

	public static <T> Observable<T> observeOnMainThread(Observable<T> observable) {
		return observable.observeOn(AndroidSchedulers.mainThread());
	}

	public static <T> Observable<T> mainThreadObservable(Observable<T> observable) {
		return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}

	public static <T> Single<T> mainThreadObservable(Single<T> observable) {
		return mainThreadObservable(Schedulers.io(), observable);
	}

	public static <T> Single<T> mainThreadObservable(Scheduler scheduler, Single<T> observable) {
		return observable.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
	}

	public static <T> Observable<T> mainThreadObservable(Scheduler scheduler, Observable<T> observable) {
		return observable.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
	}

	public static <T> Flowable<T> mainThreadObservable(Flowable<T> flowable) {
		return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}

	public static <T> Flowable<T> mainThreadObservable(Scheduler scheduler, Flowable<T> flowable) {
		return flowable.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
	}


	@NonNull
	public static <T> ArrayList<T> getListCopy(List<T> list) {
		ArrayList<T> arrayList = new ArrayList<>();
		arrayList.addAll(list);
		ArrayList<T> listCopy = new ArrayList<>(arrayList.size());
		listCopy.addAll((ArrayList<T>) arrayList.clone());
		Collections.copy(listCopy, list);
		return listCopy;
	}

	public static String listToString(List<String> strings) {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (String s : strings) {
			if (!first) {
				res.append(",");
				first = false;
			}
			res.append(s);
		}
		return res.toString();
	}

	public static <T> String objectsListToString(List<T> tList) {
		StringBuilder res = new StringBuilder();
		for (T s : tList) {
			res.append(s.toString());
            res.append(",");
        }
        res.delete(res.length() - 1, res.length());
        return res.toString();
	}

	@NonNull
	public static <T> ArrayList<T> getExcludeList(ArrayList<T> list, List<T> items, Comparator<T> comparator) {
		ArrayList<T> res = new ArrayList<>();
		for (T t : list) {
			int pos = Collections.binarySearch(items, t, comparator);
			if (pos < 0) {
				res.add(t);
			}
		}
		return res;
	}

	public static String getThread() {
		return Thread.currentThread().getName();
	}

}
