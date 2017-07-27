package com.jing.app.jjgallery.gdb.presenter.surf;

import com.jing.app.jjgallery.gdb.view.surf.ISurfView;
import com.jing.app.jjgallery.http.bean.request.FolderRequest;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.service.http.GdbHttpClient;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 11:10
 */
public class SurfPresenter {

    private ISurfView surfView;

    public SurfPresenter(ISurfView surfView) {
        this.surfView = surfView;
    }

    public void surf(String type, String folder) {
        FolderRequest request = new FolderRequest();
        request.setType(type);
        request.setFolder(folder);
        GdbHttpClient.getInstance().getGdbService().requestSurf(request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FolderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        surfView.onRequestFail();
                    }

                    @Override
                    public void onNext(FolderResponse bean) {
                        surfView.onFolderReceived(bean);
                    }
                });
    }

}
