package com.jing.app.jjgallery.service.http;

import com.jing.app.jjgallery.bean.http.GdbCheckNewFileBean;
import com.jing.app.jjgallery.bean.http.GdbMoveResponse;
import com.jing.app.jjgallery.bean.http.GdbRequestMoveBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface GdbService {

    @GET("checkNew")
    Observable<GdbCheckNewFileBean> checkNewFile(@Query("type") String type);

    @POST("requestMove")
    Observable<GdbMoveResponse> requestMoveImages(@Body GdbRequestMoveBean data);
}
