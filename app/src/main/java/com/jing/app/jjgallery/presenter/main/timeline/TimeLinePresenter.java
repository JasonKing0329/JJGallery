package com.jing.app.jjgallery.presenter.main.timeline;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.model.main.timeline.TimeLineCallback;
import com.jing.app.jjgallery.model.main.timeline.TimeLineController;
import com.jing.app.jjgallery.viewsystem.main.timeline.ITimeLineView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public class TimeLinePresenter implements TimeLineCallback {

    private ITimeLineView timeLineView;
    private TimeLineController timeLineController;

    public TimeLinePresenter(ITimeLineView timeLineView) {
        this.timeLineView = timeLineView;
        timeLineController = new TimeLineController(this);
    }

    public void loadTimeLineItems() {
        timeLineController.loadTimeLineItems();
    }

    @Override
    public void onTimeLineItemsReady() {
        if (timeLineView != null) {
            timeLineView.onTimeLineItemsReady();
        }
    }

    public List<FileBean> getFileBeanList() {
        return timeLineController.getFileBeanList();
    }

    public List<String> getIndicatorBkList() {
        return timeLineController.getIndicatorBkList();
    }

    public List<String> getHeaderList() {
        return timeLineController.getHeaderList();
    }

    public HashMap<String, List<FileBean>> getContentMap() {
        return timeLineController.getContentMap();
    }
}
