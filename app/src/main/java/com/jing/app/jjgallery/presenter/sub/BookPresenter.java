package com.jing.app.jjgallery.presenter.sub;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.model.sub.BookHelper;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.viewsystem.sub.book.IBookView;

import java.util.List;

/**
 * Created by JingYang on 2016/7/28 0028.
 * Description:
 */
public class BookPresenter {

    private IBookView bookView;
    private BookHelper bookHelper;

    public BookPresenter(IBookView bookView) {
        this.bookView = bookView;
        bookHelper = new BookHelper();
    }


    public void loadDatasByFolder(String folderPath) {
        new LoadTask().execute(folderPath);
    }

    public void loadDatasByOlder(int orderId) {
        new LoadTask().execute(orderId);
    }

    private class LoadTask extends AsyncTask<Object, Void, List<List<ImageValue>>> {
        @Override
        protected List<List<ImageValue>> doInBackground(Object... params) {
            List<String> pathList = null;
            if (params[0] instanceof String) {// folder
                pathList = new FolderManager().loadPathList((String) params[0]);
            }
            else {// order
                SOrderManager manager = new SOrderManager(null);
                SOrder order = manager.queryOrder((Integer) params[0]);
                manager.loadOrderItems(order);
                pathList = order.getImgPathList();
            }
            return bookHelper.orderPageItems(pathList);
        }

        @Override
        protected void onPostExecute(List<List<ImageValue>> datas) {
            bookView.onDatasReady(datas);
            super.onPostExecute(datas);
        }
    }
}
