package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.model.main.timeline.TimeLineCallback;
import com.jing.app.jjgallery.model.main.timeline.TimeLineController;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.IWaterfall;

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

    public void loadDatas() {
        timeLineController.loadTimeLineItems();
    }

    @Override
    public void onTimeLineItemsReady() {
        waterfall.onLoadDatas(timeLineController.getFileBeanList());
    }
}
