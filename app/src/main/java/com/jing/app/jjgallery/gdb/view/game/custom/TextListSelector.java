package com.jing.app.jjgallery.gdb.view.game.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class TextListSelector {

    public interface OnListItemSelectListener {
        void onSelect(String text);
    }

    public void show(Context context, final String[]items, final OnListItemSelectListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(null)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onSelect(items[which]);
                        }
                    }
                })
                .show();
    }
}
