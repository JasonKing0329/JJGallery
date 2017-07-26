package com.jing.app.jjgallery.gdb.view.pub;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述: hsv color编辑器
 * 提供h值的范围编辑
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/13 11:51
 */
public class HsvColorDialog extends CustomDialog {

    @BindView(R.id.et_start)
    EditText etStart;
    @BindView(R.id.et_angle)
    EditText etAngle;

    private OnHsvColorListener onHsvColorListener;

    public HsvColorDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    public void show() {
        super.show();
        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        etStart.setText(String.valueOf(map.get("start")));
        etAngle.setText(String.valueOf(map.get("angle")));
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_hsv_color, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        requestSaveAction(true);
        requestCancelAction(true);
        setTitle("Color");
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveIcon) {
            String start = etStart.getText().toString().trim();
            if (TextUtils.isEmpty(start)) {
                return;
            }
            String angle = etAngle.getText().toString().trim();
            if (TextUtils.isEmpty(angle)) {
                return;
            }
            Map<String, Integer> map = new HashMap<>();
            try {

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            map.put("start", Integer.parseInt(start));
            map.put("angle", Integer.parseInt(angle));
            actionListener.onSave(map);
        }
        super.onClick(view);
    }

    @OnClick(R.id.btn_preview)
    public void onViewClicked() {
        if (onHsvColorListener != null) {
            String start = etStart.getText().toString().trim();
            String angle = etAngle.getText().toString().trim();
            if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(angle)) {
                try {
                    onHsvColorListener.onPreviewHsvColor(Integer.parseInt(start), Integer.parseInt(angle));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnHsvColorListener(OnHsvColorListener onHsvColorListener) {
        this.onHsvColorListener = onHsvColorListener;
    }

    public interface OnHsvColorListener {
        void onPreviewHsvColor(int start, int angle);
    }
}
