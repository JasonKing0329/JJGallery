package com.jing.app.jjgallery.service.http;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/9/5.
 */
public class GdbHttpClient extends BaseHttpClient {

    private GdbService gdbService;

    private GdbHttpClient() {
        super();
    }

    @Override
    protected void createService(Retrofit retrofit) {
        gdbService = retrofit.create(GdbService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final GdbHttpClient INSTANCE = new GdbHttpClient();
    }

    //获取单例
    public static GdbHttpClient getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public GdbService getGdbService() {
        return gdbService;
    }

}
