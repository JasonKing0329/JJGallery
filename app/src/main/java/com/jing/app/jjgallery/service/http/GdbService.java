package com.jing.app.jjgallery.service.http;

import com.jing.app.jjgallery.http.bean.request.FolderRequest;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.http.bean.request.GdbCheckNewFileBean;
import com.jing.app.jjgallery.http.bean.response.GdbMoveResponse;
import com.jing.app.jjgallery.http.bean.request.GdbRequestMoveBean;

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

    @POST("surfFolder")
    Observable<FolderResponse> requestSurf(@Body FolderRequest data);
}
