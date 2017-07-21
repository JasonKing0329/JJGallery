package com.jing.app.jjgallery.gdb.view.star;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/13 17:21
 */
public class StarListPagerAdapter extends FragmentPagerAdapter {

    private List<StarListFragment> fragmentList;
    private List<String> titleList;

    public StarListPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    public void addFragment(StarListFragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    public void updateTitle(int index, String title) {
        titleList.set(index, title);
    }

    @Override
    public StarListFragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
