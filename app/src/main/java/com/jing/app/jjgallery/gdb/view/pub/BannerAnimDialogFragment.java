package com.jing.app.jjgallery.gdb.view.pub;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DraggableDialogFragmentV4;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/21 14:14
 */
public class BannerAnimDialogFragment extends DraggableDialogFragmentV4 {

    private EditFragment ftContent;
    private OnAnimSettingListener onAnimSettingListener;

    @Override
    protected void onClickSave() {
        ftContent.onSave();
        super.onClickSave();
    }

    @Override
    protected View getToolbarView(ViewGroup groupToolbar) {
        requestCloseAction();
        requestSaveAction();
        setTitle(getContext().getString(R.string.gdb_nav_setting));
        return null;
    }

    @Override
    protected Fragment getContentViewFragment() {
        ftContent = new EditFragment();
        ftContent.setOnAnimSettingListener(onAnimSettingListener);
        return ftContent;
    }

    public void setOnAnimSettingListener(OnAnimSettingListener onAnimSettingListener) {
        this.onAnimSettingListener = onAnimSettingListener;
    }

    public static class EditFragment extends ContentFragmentV4 {

        @BindView(R.id.rb_random)
        RadioButton rbRandom;
        @BindView(R.id.rb_fixed)
        RadioButton rbFixed;
        @BindView(R.id.et_time)
        EditText etTime;

        private OnAnimSettingListener onAnimSettingListener;

        public static final String[] ANIM_TYPES = new String[]{
                "defaultEffect", "alpha", "rotate", "cube", "flip", "accordion", "zoomFade",
                "fade", "zoomCenter", "zoomStack", "stack", "depth", "zoom", "zoomOut"/*, "parallax"*/
        };

        @Override
        protected void bindChildFragmentHolder(IFragmentHolder holder) {

        }

        @Override
        protected int getLayoutRes() {
            return R.layout.dlg_gdb_star_list_nav_setting;
        }

        @Override
        protected void initView(View view) {
            ButterKnife.bind(this, view);
            rbFixed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAnimationSelector();
                }
            });
            rbRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        onAnimSettingListener.onRandomAnim(true);
                    }
                }
            });
            rbFixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        onAnimSettingListener.onRandomAnim(false);
                    }
                }
            });

            boolean isRandom = onAnimSettingListener.isRandomAnim();
            if (isRandom) {
                rbRandom.setChecked(true);
                rbFixed.setText("Fixed");
            }
            else {
                rbFixed.setChecked(true);
                try {
                    rbFixed.setText(formatFixedText(ANIM_TYPES[onAnimSettingListener.getAnimType()]));
                } catch (Exception e) {
                    e.printStackTrace();
                    rbFixed.setText(formatFixedText(ANIM_TYPES[0]));
                }
            }

            etTime.setText(String.valueOf(onAnimSettingListener.getAnimTime()));
        }

        public void setOnAnimSettingListener(OnAnimSettingListener onAnimSettingListener) {
            this.onAnimSettingListener = onAnimSettingListener;
        }

        private String formatFixedText(String type) {
            return "Fixed (" + type + ")";
        }

        private void showAnimationSelector() {
            new AlertDialog.Builder(getContext())
                    .setTitle(null)
                    .setItems(ANIM_TYPES, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rbFixed.setText(formatFixedText(ANIM_TYPES[which]));
                            onAnimSettingListener.onSaveAnimType(which);
                        }
                    }).show();
        }


        public void onSave() {
            int time;
            try {
                time = Integer.parseInt(etTime.getText().toString());
            } catch (Exception e) {
                time = 0;
            }
            // 至少2S
            if (time < 2000) {
                time = 2000;
            }
            onAnimSettingListener.onSaveAnimTime(time);
            onAnimSettingListener.onParamsSaved();
        }
    }

    public interface OnAnimSettingListener {
        boolean isRandomAnim();
        void onRandomAnim(boolean random);
        int getAnimType();
        void onSaveAnimType(int type);
        int getAnimTime();
        void onSaveAnimTime(int time);
        void onParamsSaved();
    }
}
