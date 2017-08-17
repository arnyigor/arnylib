package com.arny.arnylib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

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

    public static HashMap <String, String> toMap(JSONObject object) throws JSONException {
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
     *
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
     *
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

    public static String match(String where,String pattern,int groupnum){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(where);
        while(m.find()) {
            if (!m.group(groupnum).equals("")) {
                return m.group(groupnum);
            }
        }
        return null;
    }

    public static String getDateTime(long milliseconds, String format) {
        milliseconds = (milliseconds == 0) ? Calendar.getInstance().getTimeInMillis() : milliseconds;
        format = (format == null) ? "dd MMM yyyy HH:mm:ss.sss" : format;
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(milliseconds));
    }

    public static String getDateTime(long milliseconds) {
        milliseconds = (milliseconds == 0) ? Calendar.getInstance().getTimeInMillis() : milliseconds;
        return (new SimpleDateFormat("dd MMM yyyy HH:mm:ss.sss", Locale.getDefault())).format(new Date(milliseconds));
    }

    public static String getDateTime() {
        return (new SimpleDateFormat("dd MMM yyyy HH:mm:ss.sss", Locale.getDefault())).format(new Date(System.currentTimeMillis()));
    }

    public static boolean isTimeOlderMins(int min,long time) {
        long now = System.currentTimeMillis();
        long diff =  now-time ;
        return  (diff /(1000 *  60 )) > min;
    }

    public static String getDateTime(String format) {
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(System.currentTimeMillis()));
    }

    public static long convertTimeStringToLong(String myTimestamp, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date;
        try {
            date = formatter.parse(myTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return date.getTime();
    }

    /**
     * @param date
     * @param format
     * @return String datetime
     */
    public static String getDateTime(Date date, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long milliseconds = calendar.getTimeInMillis();
            format = (format == null || format.trim().equals("")) ? "dd MMM yyyy HH:mm:ss.sss" : format;
            return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int range = max - min + 1;
        return rnd.nextInt(range) + min;
    }

    public static double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean empty(Object obj ) {
        if (obj == null) {
            return true;
        }else{
            if (obj instanceof String) {
                String s = (String) obj;
                return s.trim().equals("null") || s.trim().isEmpty();
            } else {
                return false;
            }
        }
    }


    public static long getDifDays(String date) {
        String calendarFormat = "dd MMM yyyy";
        String current = Utility.getDateTime(calendarFormat);
        Date taskDate = new Date(Utility.convertTimeStringToLong(date, calendarFormat));
        Date curr = new Date(Utility.convertTimeStringToLong(current, calendarFormat));
        long diff = taskDate.getTime() - curr.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static long getDifDays(String date1,String date2) {
        String calendarFormat = "dd MMM yyyy";
        String current = Utility.getDateTime(calendarFormat);
        Date taskDate = new Date(Utility.convertTimeStringToLong(date1, calendarFormat));
        Date curr = new Date(Utility.convertTimeStringToLong(date2, calendarFormat));
        long diff = taskDate.getTime() - curr.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static Boolean isRequestSuccess(JSONObject result) {
        try {
            return Boolean.valueOf(result.getString("success"));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isRequestSuccess(JSONObject result,String strResponseKey,String strResponseVal) {
        try {
            return result.getString(strResponseKey).equals(strResponseVal);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String clearNullString(String input){
        if (Utility.empty(input) || input.equals("null")) return "";
        else return input;
    }

    public static boolean isEmailValid(String email) {
        String result = match(email, EMAIL_PATTERN,0);
        return !empty(result);
    }

    /**
     * Чтение файлов assets,в папке
     * @param folder
     * @return
     */
    public static ArrayList<String> listAssetFiles(Context context, String folder) {
        ArrayList<String> fileNames = new ArrayList<>();
        if (context ==null){
            return null;
        }
        try {
            String[] files = context.getAssets().list(folder);
            Collections.addAll(fileNames, files);
            return fileNames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readAssetFile(Context context, String folder,String fileName){
        InputStream input;
        try {
            input = context.getAssets().open(folder + "/"  + fileName);
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

	public static ArrayList<String> getJsonObjectKeys(Gson gson,String  result) {
        ArrayList<String> keys = new ArrayList<>();
        try {
            Iterator keysToCopyIterator = new JSONObject(gson.fromJson(result, JsonElement.class).toString()).keys();
            while(keysToCopyIterator.hasNext()) {
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
        while(iterator.hasNext()) {
            keys.add(iterator.next());
        }
        return keys;
    }

    public static String getJsonObjVal(String result, String key) {
        Gson gson = new Gson();
        ArrayList<String> keys = Utility.getJsonObjectKeys(gson,result);
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

    public static <T> boolean contains(ArrayList<T>array, T v) {
        for (T e : array) {
            if (v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static void iterHashMap(Map<String,Object> map){
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
        }
    }

    public static <T> boolean contains(final T[] array, T v) {
        for (T e : array) {
            if (v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static void setJsonParam(JSONObject params, String col, String val) {
        try {
            params.put(col, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}