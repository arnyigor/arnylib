package com.arny.arnylib.network;

import android.annotation.SuppressLint;
import android.util.Log;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
public class ApiUtils {

    public static <T> T fromJson(Object response, Class cls) {
        return fromJson(response, cls, new Gson());
    }

    public static <T> T fromJson(Object response, Class cls, Gson gson) {
        String name = response.getClass().getSimpleName();
        Log.i(ApiUtils.class.getSimpleName(), "fromJson: class: " + name + " response = " + response + " to:" +cls.getSimpleName());
        return (T) gson.fromJson(String.valueOf(response), cls);
    }

    public static String toJson(Object object) {
        return toJson(object, new Gson());
    }

    public static String toJson(Object object, Gson gson) {
        return gson.toJson(object);
    }

    public static <T> ArrayList<T> convertArray(JsonArray jArr, Class<?> clazz) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            for (int i = 0, l = jArr.size(); i < l; i++) {
                list.add((T) new Gson().fromJson(jArr.get(i), clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static HashMap<String, String> getJsonObjectToHashMap(JSONObject params) {
        HashMap<String, String> mapParams = new HashMap<>();
        try {
            for (Map.Entry<String, Object> entry : jsonToMap(params).entrySet()) {
                mapParams.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(NetworkService.class.getSimpleName(), "getJsonObjectToHashMap: mapParams = " + mapParams);
        return mapParams;
    }


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();
        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static String getVolleyError(VolleyError error) {
        String message;
        if (error != null && error.networkResponse != null) {
            message = "code:" + error.networkResponse.statusCode + "; data:" + new String(error.networkResponse.data);
        } else {
            if (error != null) {
                message = error.getMessage();
            } else {
                message = "Error = null";
            }
        }
        Log.e("api", " << Api onErrorResponse message = " + message);
        return message;
    }


    public static boolean isHostAvailable(String host, int port, int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(host, port);
            sock.connect(sockaddr, timeoutMs); // This will block no more than timeoutMs
            sock.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendSocketData(String host, int port, String data){
        try {
            // The line below illustrates the default port 6101 for mobile printers 9100 is the default port number
            // for desktop and tabletop printers
            Socket clientSocket=new Socket(host, port);
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream() );
            //The data being sent in the lines below illustrate CPCL  one can change the data for the corresponding
            //language being used (ZPL, EPL)
            dos.writeBytes(data);
//            dos.writeUTF("If procrastination was a sport, I would compete in it later.");
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
