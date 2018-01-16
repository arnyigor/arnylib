package com.arny.arnylib.network;

import android.annotation.SuppressLint;
import android.util.Log;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
public class ApiUtils {

    public static <T> T getResponse(Object response, Class cls) {
        String name = response.getClass().getSimpleName();
        Log.d(ApiUtils.class.getSimpleName(), "getResponse: class: " + name + " response = " + response);
        return (T) new Gson().fromJson(String.valueOf(response), cls);
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
            if (params.names() != null) {
                for (int i = 0; i < params.names().length(); i++) {
                    mapParams.put(params.names().getString(i), (String) params.get(params.names().getString(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(NetworkService.class.getSimpleName(), "getJsonObjectToHashMap: mapParams = " + mapParams);
        return mapParams;
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
