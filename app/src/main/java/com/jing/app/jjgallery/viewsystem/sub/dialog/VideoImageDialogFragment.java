package com.jing.app.jjgallery.viewsystem.sub.dialog;

import android.graphics.Bitmap;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.publicview.TouchImageView;
import com.king.lib.saveas.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/20 11:16
 */
public class VideoImageDialogFragment extends BaseDialogFragmentV4 {

    @BindView(R.id.iv_image)
    TouchImageView ivImage;

    private Bitmap bitmap;
    private OnShowImageListener onShowImageListener;

    @Override
    protected int getLayoutResource() {
        return R.layout.dlg_ft_show_image;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 如果不设置充满全屏，左右两侧会有空隙
        setWidth(ScreenUtils.getScreenWidth(getActivity()));
        // 如果不设置充满全屏，alignParentBottom的空间的marginBottom不起作用
        setHeight(ScreenUtils.getScreenHeight(getActivity()));
        ivImage.setImageBitmap(bitmap);
    }

    @OnClick({R.id.tv_set_cover, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_set_cover:
                if (onShowImageListener != null) {
                    onShowImageListener.onSetAsCover();
                }
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    public void setOnShowImageListener(OnShowImageListener onShowImageListener) {
        this.onShowImageListener = onShowImageListener;
    }

    public interface OnShowImageListener {
        void onSetAsCover();
    }
}
