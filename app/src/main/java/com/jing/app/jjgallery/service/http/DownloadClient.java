package com.jing.app.jjgallery.service.http;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jing.app.jjgallery.service.http.progress.ProgressInterceptor;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.orhanobut.logger.Logger;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件下载
 */
public class DownloadClient {

    public static final String BASE_URL = "http://192.168.9.206:8080/JJGalleryServer/";

    private DownloadService mDownloadService;

    public DownloadClient(ProgressListener progressListener) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient client;
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (progressListener != null) {
            ProgressInterceptor progress = new ProgressInterceptor(progressListener);
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(progress)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        mDownloadService = retrofit.create(DownloadService.class);
    }

    public DownloadService getDownloadService() {
        return mDownloadService;
    }

}
