package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.model.main.timeline.TimeLineCallback;
import com.jing.app.jjgallery.model.main.timeline.TimeLineController;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.IWaterfall;

import java.util.List;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public class WaterfallPresenter extends BasePresenter implements TimeLineCallback {

    private IWaterfall waterfall;
    private TimeLineController timeLineController;

    public WaterfallPresenter(IWaterfall waterfall) {
        this.waterfall = waterfall;
        timeLineController = new TimeLineController(this);
    }

    /**
     * 异步方法，加载所有的文件
     */
    public void loadAllFileDatas() {
        timeLineController.loadTimeLineItems();
    }

    @Override
    public void onTimeLineItemsReady() {
        waterfall.onLoadDataFinished(timeLineController.getFileBeanList());
    }

    /**
     * 同步方法，加载列表items
     * @param sOrder
     */
    public void loadSOrderItems(SOrder sOrder) {
        new SOrderManager(null).loadOrderItems(sOrder);
        waterfall.onLoadDataFinished(sOrder);
    }

    /**
     * 同步方法，加载文件夹items
     * @param folder
     */
    public void loadFolderItems(String folder) {
        List<FileBean> list = new FolderManager().loadFileBeanList(folder);
        waterfall.onLoadDataFinished(list);
    }
}
