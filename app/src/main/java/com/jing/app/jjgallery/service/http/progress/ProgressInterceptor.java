package com.jing.app.jjgallery.service.http.progress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ProgressInterceptor implements Interceptor {

    ProgressListener mProgressListener;

    public ProgressInterceptor(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), mProgressListener))
                .build();
    }
}
