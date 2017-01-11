package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;

/**
 * Created by 景阳 on 2017/1/11.
 * thumb仅作为选择器时，folder/order模式的选择界面，只需要实例化一次
 */

public class ThumbTypeFragment extends Fragment implements View.OnClickListener {

    private IThumbSelector thumbSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_thumb_select_type, container, false);
        contentView.findViewById(R.id.thumb_select_folder).setOnClickListener(this);
        contentView.findViewById(R.id.thumb_select_order).setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thumbSelector = (IThumbSelector) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumb_select_folder:
                thumbSelector.onSelectFolderThumb();
                break;
            case R.id.thumb_select_order:
                thumbSelector.onSelectOrderThumb();
                break;
        }
    }
}
