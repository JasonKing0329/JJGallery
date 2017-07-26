package com.jing.app.jjgallery.viewsystem.sub.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/25 15:28
 */
public class AlertDialogFragment extends DialogFragment {

    private String title;

    private String message;

    private String positiveText;

    private String negativeText;

    private String neutralText;

    private DialogInterface.OnClickListener positiveListener;

    private DialogInterface.OnClickListener negativeListener;

    private DialogInterface.OnClickListener neutralListener;

    private DialogInterface.OnDismissListener dismissListener;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    public void setNeutralText(String neutralText) {
        this.neutralText = neutralText;
    }

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(DialogInterface.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    public void setNeutralListener(DialogInterface.OnClickListener neutralListener) {
        this.neutralListener = neutralListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message);
        if (positiveText != null) {
            builder.setPositiveButton(positiveText, positiveListener);
        }
        if (negativeText != null) {
            builder.setNegativeButton(negativeText, negativeListener);
        }
        if (neutralText != null) {
            builder.setNeutralButton(neutralText, neutralListener);
        }
        builder.setOnDismissListener(dismissListener);
        return builder.create();
    }
}
