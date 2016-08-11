package com.jing.app.jjgallery.viewsystem.sub.book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.sub.BookPresenter;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;

import java.util.List;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * Created by JingYang on 2016/7/28 0028.
 * Description:
 */
public abstract class BookFragment extends Fragment implements IBookView {

    private BookPageAdapter bookPageAdapter;

    private ShowImageDialog showImageDialog;
    protected FlipView flipView;

    protected BookPresenter mPresenter;
    private FlipAction flipAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new BookPresenter(this);
        flipAction = new FlipAction();

        View view = inflater.inflate(R.layout.page_book, null);
        flipView = (FlipView) view.findViewById(R.id.book_flip_view);
        if (SettingProperties.getBookSwitchMode(getActivity()).equals(PreferenceValue.VALUE_BOOK_SWITCH_VER)) {
            flipView.setIsVertical(true);
        }
        else {
            flipView.setIsVertical(false);
        }
        flipView.setOnFlipListener(flipAction);
        flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        flipView.setEmptyView(getActivity().findViewById(R.id.book_empty));
        flipView.setOnOverFlipListener(flipAction);

        initFlipView();
        return view;
    }

    private void initFlipView() {
        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).showProgressCycler();
        }
        loadDatas();
    }

    protected abstract void loadDatas();

    @Override
    public void onDatasReady(List<List<ImageValue>> list) {

        bookPageAdapter = new BookPageAdapter(getActivity(), list);
        bookPageAdapter.setOnImageClickListener(bookImageListener);

        flipView.setAdapter(bookPageAdapter);
        // 第一页预览翻页效果
        flipView.peakNext(false);

        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }

    View.OnClickListener bookImageListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (showImageDialog == null) {
                showImageDialog = new ShowImageDialog(getActivity(), null, 0);
            }
            ImageValue value = (ImageValue) view.getTag(R.id.book_item_position);
            showImageDialog.setImagePath(value.getPath());
            showImageDialog.show();
        }
    };

    private class FlipAction implements FlipView.OnFlipListener, FlipView.OnOverFlipListener {

        @Override
        public void onFlippedToPage(FlipView v, int position, long id) {

        }

        @Override
        public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

        }
    }
}
