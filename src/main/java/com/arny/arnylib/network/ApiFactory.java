package com.arny.arnylib.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
public class ApiFactory {
    private static ApiFactory instance = new ApiFactory();
	private long timeout;
	private HttpLoggingInterceptor.Level level;

	public static ApiFactory getInstance() {
        return instance;
    }

    private ApiFactory() {
    }

    private Retrofit getRetrofit(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	    logging.setLevel(level);
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
	    httpClient.connectTimeout(timeout, TimeUnit.SECONDS);
        httpClient.readTimeout(timeout, TimeUnit.SECONDS);
        httpClient.followRedirects(true);
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
		        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

	public <S> S createService(Class<S> serviceClass, String baseUrl,long timeout,HttpLoggingInterceptor.Level level) {
		this.timeout = timeout;
		this.level = level;
		return getRetrofit(baseUrl).create(serviceClass);
	}

    public <S> S createService(Class<S> serviceClass, String baseUrl) {
	    level = HttpLoggingInterceptor.Level.HEADERS;
	    timeout = 120;
        return getRetrofit(baseUrl).create(serviceClass);
    }

}
