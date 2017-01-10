package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.util.DebugLog;

/**
 * Created by 景阳 on 2017/1/10.
 */

public abstract class GameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getContentView(), container, false);
        Bundle bundle = getArguments();
        initView(contentView, bundle);
        return contentView;
    }

    protected abstract int getContentView();

    protected abstract void initView(View contentView, Bundle bundle);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachActivity(context);
    }

    protected abstract void onAttachActivity(Context context);
}
