package com.jing.app.jjgallery.bean.http;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class GdbCheckNewFileBean {
    private boolean isStarExisted;
    private boolean isRecordExisted;
    private List<String> starNames;
    private List<String> recordNames;

    public boolean isStarExisted() {
        return isStarExisted;
    }

    public void setStarExisted(boolean starExisted) {
        isStarExisted = starExisted;
    }

    public boolean isRecordExisted() {
        return isRecordExisted;
    }

    public void setRecordExisted(boolean recordExisted) {
        isRecordExisted = recordExisted;
    }

    public List<String> getStarNames() {
        return starNames;
    }

    public void setStarNames(List<String> starNames) {
        this.starNames = starNames;
    }

    public List<String> getRecordNames() {
        return recordNames;
    }

    public void setRecordNames(List<String> recordNames) {
        this.recordNames = recordNames;
    }
}
