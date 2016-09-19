package com.jing.app.jjgallery.bean.order;

import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;

/**
 * Created by Administrator on 2016/9/18.
 */
public class CasualKeyword extends Keyword {
    private SOrder order;

    public SOrder getOrder() {
        return order;
    }

    public void setOrder(SOrder order) {
        this.order = order;
    }
}
