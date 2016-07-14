package com.jing.app.jjgallery.viewsystem.main.order;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.viewsystem.sub.key.AbsKeyAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public class SOrderIndexAdapter extends AbsKeyAdapter {

    private List<SOrder> mList;
    public SOrderIndexAdapter(KeywordsFlow keywordsFlow, List<SOrder> list) {
        super(keywordsFlow);
        mList = list;
    }

    @Override
    protected List<Keyword> createKeywordList() {
        List<Keyword> list = new ArrayList<>();
        Keyword keyword = null;
        for (int i = 0; i < mList.size(); i ++) {
            keyword = new Keyword();
            keyword.setDisplayName(mList.get(i).getName());
            keyword.setObject(mList.get(i));
            list.add(keyword);
        }
        return list;
    }
}
