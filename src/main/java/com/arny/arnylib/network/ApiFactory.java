package com.arny.arnylib.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
public class ApiFactory {
    private static Retrofit retrofit = null;
	private static ApiFactory instance = new ApiFactory();
	public static ApiFactory getInstance() {
		return instance;

	}
	private ApiFactory(){}

	private Retrofit getRetrofit(String baseUrl) {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();
		OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
				.build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
	}


	public <S> S createService(Class<S> serviceClass,String baseUrl) {
		return getRetrofit(baseUrl).create(serviceClass);
	}


}
