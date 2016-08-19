package com.jing.app.jjgallery.viewsystem.sub.book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.BookInforBean;
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

    private TextView inforView;

    protected BookPresenter mPresenter;
    private FlipAction flipAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new BookPresenter(this);
        flipAction = new FlipAction();

        View view = inflater.inflate(R.layout.page_book, null);
        inforView = (TextView) view.findViewById(R.id.book_float_info);
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
    public void onDatasReady(List<List<ImageValue>> list, final BookInforBean inforBean) {

        bookPageAdapter = new BookPageAdapter(getActivity(), list);
        bookPageAdapter.setOnImageClickListener(bookImageListener);

        flipView.setAdapter(bookPageAdapter);
        // 第一页预览翻页效果
        flipView.peakNext(false);

        if (inforBean != null) {
            StringBuffer buffer = new StringBuffer("total:  ");
            buffer.append(inforBean.getTotal()).append("\n");
            buffer.append("widthQueue:  ").append(inforBean.getWidthQueue()).append("\n");
            buffer.append("heightQueue:  ").append(inforBean.getHeightQueue()).append("\n");
            buffer.append("middleQueue:  ").append(inforBean.getMiddleQueue());
            if (list.size() > 0) {
                buffer.append("\n").append("pageNumber:[").append(list.get(0).size());
                for (int i = 1; i < list.size(); i ++) {
                    buffer.append(",").append(list.get(i).size());
                }
                buffer.append("]");
            }
            inforView.setText(buffer.toString());
//            inforView.setVisibility(View.VISIBLE);
            inforView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inforView.startAnimation(getInforDisappearAnim());
                    inforView.setVisibility(View.GONE);
                }
            });
        }

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

    public void showInforView() {
        if (inforView.getVisibility() != View.VISIBLE) {
            inforView.startAnimation(getInforAppearAnim());
            inforView.setVisibility(View.VISIBLE);
        }
    }

    private Animation getInforAppearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
            , Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(500);
        return anim;
    }

    private Animation getInforDisappearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        anim.setDuration(500);
        return anim;
    }

    private class FlipAction implements FlipView.OnFlipListener, FlipView.OnOverFlipListener {

        @Override
        public void onFlippedToPage(FlipView v, int position, long id) {

        }

        @Override
        public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

        }
    }
}
