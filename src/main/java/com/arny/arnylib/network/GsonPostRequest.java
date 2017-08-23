package com.arny.arnylib.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Convert a JsonElement into a list of objects or an object with Google Gson.
 * <p>
 * The JsonElement is the response object for a {@link com.android.volley.Request.Method} POST call.
 *
 * @author https://plus.google.com/+PabloCostaTirado/about
 */
public class GsonPostRequest<T> extends JsonRequest<T> {
    private final Gson gson;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url           URL of the request to make
     * @param listener      is the listener for the right answer
     * @param errorListener is the listener for the wrong answer
     */
    public GsonPostRequest (String url, String body, Map<String, String> headers,
     Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, body, listener, errorListener);
        this.gson = new Gson();
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success(gson.fromJson(json, JsonElement.class), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}