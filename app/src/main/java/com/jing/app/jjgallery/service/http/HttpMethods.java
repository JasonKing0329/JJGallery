package com.jing.app.jjgallery.service.http;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/9/1.
 */
public class HttpMethods {

    public static final String BASE_URL = "http://192.168.9.206:8080/JJGalleryServer/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GdbService gdbService;

    private HttpMethods() {

        // 查看通信LOG，可以输入OkHttp来过滤
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置连接超时
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 设置log打印器
        builder.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();

        gdbService = retrofit.create(GdbService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public GdbService getGdbService() {
        return gdbService;
    }

}
