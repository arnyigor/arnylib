package com.arny.arnylib.network;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class ApiResponseDeserializer implements JsonDeserializer<ApiResponse> {

    @Override
    public ApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Log.i(ApiResponseDeserializer.class.getSimpleName(), "deserialize: json = " +json);
        final ApiResponse apiResponse = new ApiResponse();
        final JsonObject jsonObject = json.getAsJsonObject();
        Log.i(ApiResponseDeserializer.class.getSimpleName(), "deserialize: jsonObject = " +jsonObject);
        apiResponse.setTitle(jsonObject.get("title").getAsString());
        apiResponse.setBody(jsonObject.get("body").getAsString());

        return apiResponse;
    }
}