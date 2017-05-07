package com.jing.app.jjgallery.viewsystem.publicview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.ImageValueController;

import java.io.File;

public class DefaultDialogManager {

	public interface OnDialogActionListener {
		void onOk(String name);
	}

	public void openCreateFolderDialog(Context context, final OnDialogActionListener listener) {
		LinearLayout layout = new LinearLayout(context);
		layout.setPadding(40, 10, 40, 10);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		EditText edit = new EditText(context);
		edit.setLayoutParams(params);
		layout.addView(edit);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage(R.string.menu_file_create_folder);
		dialog.setView(layout);

		final EditText folderEdit = edit;
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String folderName = folderEdit.getText().toString();
				listener.onOk(folderName);
			}
		});
		dialog.setNegativeButton(R.string.cancel, null);
		dialog.show();
	}

	public void openInputDialog(Context context, final OnDialogActionListener listener) {
		LinearLayout layout = new LinearLayout(context);
		layout.setPadding(40, 10, 40, 10);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		EditText edit = new EditText(context);
		edit.setLayoutParams(params);
		layout.addView(edit);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage("input content");
		dialog.setView(layout);

		final EditText folderEdit = edit;
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String folderName = folderEdit.getText().toString();
				listener.onOk(folderName);
			}
		});
		dialog.setNegativeButton(R.string.cancel, null);
		dialog.show();
	}

	public void openSaveFileDialog(Context context, final OnDialogActionListener listener, String initText) {
		LinearLayout layout = new LinearLayout(context);
		layout.setPadding(40, 10, 40, 10);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		EditText edit = new EditText(context);
		edit.setLayoutParams(params);
		edit.setText(initText);
		layout.addView(edit);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage(R.string.save);
		dialog.setView(layout);

		final EditText folderEdit = edit;
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String folderName = folderEdit.getText().toString();
				listener.onOk(folderName);
			}
		});
		dialog.setNegativeButton(R.string.cancel, null);
		dialog.show();
	}

	public void openDetailDialog(Context context, File file) {

		String fileName = file.getName();
		String originName = null;
		if (EncryptUtil.isEncrypted(file)) {
			originName = EncryptUtil.getOriginName(file);
		}

		String msg;
		ImageValue value = new ImageValueController().queryImagePixel(file.getPath());
		String valueInfor = value == null ? "" : context.getResources().getString(R.string.spicture_details_wh) + "\n"
				+ value.getWidth() + " * " + value.getHeight();
		if (originName == null) {
			msg = valueInfor
					+ "\n" + context.getResources().getString(R.string.spicture_details_fname) + "\n" + fileName
					+ "\n" + context.getResources().getString(R.string.spicture_details_oname) + file.getPath();
		}
		else {
			msg = valueInfor
					+ "\n" + context.getResources().getString(R.string.spicture_details_fname) + "\n" + fileName
					+ "\n" + context.getResources().getString(R.string.spicture_details_oname) + "\n" + originName
					+ "\n" + context.getResources().getString(R.string.spicture_details_fpath) + file.getPath();
		}

		View view = LayoutInflater.from(context).inflate(R.layout.layout_detail_infor, null);
		//use EditText to implement copy/cut/select function on text
		EditText edit = (EditText) view.findViewById(R.id.detail_edit_infor);
		edit.setText(msg);

		new AlertDialog.Builder(context)
				.setTitle(R.string.spicture_details)
				.setView(view)
				.setPositiveButton(R.string.ok, null)
				.show();
	}

	public void showWarningActionDialog(Context context, String msg, String okText, String neutralText,
										final OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.warning);
		builder.setMessage(msg);
		builder.setPositiveButton(okText, listener);
		if (neutralText != null) {
			builder.setNeutralButton(neutralText, listener);
		}
		builder.setNegativeButton(R.string.cancel, listener);
		builder.show();
	}

	/**
	 *
	 * @param context
	 * @param msg
	 * @param positiveText
	 * @param neutralText can be null
	 * @param negativeText
	 * @param listener
	 */
	public void showWarningActionDialog(Context context, String msg, String positiveText
			, String neutralText, String negativeText, OnClickListener listener) {
		showOptionDialog(context, context.getString(R.string.warning), msg, positiveText, neutralText, negativeText, listener, null);
	}

	/**
	 *
	 * @param context
	 * @param msg
	 * @param positiveText
	 * @param neutralText can be null
	 * @param negativeText
	 * @param clickListener
	 * @param dismissListener
	 */
	public void showOptionDialog(Context context, String title, String msg, String positiveText
			, String neutralText, String negativeText, OnClickListener clickListener, DialogInterface.OnDismissListener dismissListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title == null) {
			title = context.getString(R.string.option);
		}
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(positiveText, clickListener);
		if (neutralText != null) {
			builder.setNeutralButton(neutralText, clickListener);
		}
		builder.setNegativeButton(negativeText, clickListener);
		builder.setOnDismissListener(dismissListener);
		builder.show();
	}

}
