package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public class SurfPresenter {

    private SOrderManager sOrderManager;
    private Encrypter encrypter;

    public SurfPresenter() {
        sOrderManager = new SOrderManager(null);
        encrypter = EncrypterFactory.create();
    }

    public SOrder queryOrder(int orderId) {
        return sOrderManager.queryOrder(orderId);
    }

    public void getOrderItemList(SOrder order) {
        sOrderManager.loadOrderItems(order);
    }

    public List<String> loadFromFolder(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File files[] = file.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File file, String name) {

                    return name.endsWith(encrypter.getFileExtra());
                }
            });
            for (File f:files) {
                list.add(f.getPath());
            }
        }
        return list;
    }

    public boolean isGifImage(String path) {
        return encrypter.isGifFile(path);
    }
}
