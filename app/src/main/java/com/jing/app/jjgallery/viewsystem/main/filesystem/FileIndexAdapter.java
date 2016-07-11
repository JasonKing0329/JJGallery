package com.jing.app.jjgallery.viewsystem.main.filesystem;

import com.jing.app.jjgallery.viewsystem.sub.key.AbsKeyAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public class FileIndexAdapter extends AbsKeyAdapter {

    private List<String> mPathList;
    public FileIndexAdapter(KeywordsFlow keywordsFlow, List<String> list) {
        super(keywordsFlow);
        mPathList = list;
    }

    @Override
    protected List<Keyword> createKeywordList() {
        List<Keyword> list = new ArrayList<>();
        Keyword keyword = null;
        for (int i = 0; i < mPathList.size(); i ++) {
            keyword = new Keyword();
            String[] array = mPathList.get(i).split("/");
            keyword.setDisplayName(array[array.length - 1]);
            keyword.setObject(mPathList.get(i));
            list.add(keyword);
        }
        return list;
    }
}
