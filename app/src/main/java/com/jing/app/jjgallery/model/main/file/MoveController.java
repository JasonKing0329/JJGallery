package com.jing.app.jjgallery.model.main.file;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.file.FileIO;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MoveController {

	private final String TAG = "MoveController";
	private Context mContext;
	private Handler moveEventHandler;
	private ProgressDialog moveProgressDialog;
	private String moveProgressText;
	private int moveIndex;
	
	private Queue<String> errorQueue;
	
	public MoveController(Context context, Callback callback) {
		mContext = context;
		moveEventHandler = new Handler(callback);
		moveProgressText = context.getResources().getString(R.string.folderdlg_moving);
		errorQueue = new LinkedList<String>();
	}
	
	public void showProgress() {
		Log.d(TAG, "showProgress");
		errorQueue.clear();
		if (moveProgressDialog == null) {
			moveProgressDialog = new ProgressDialog(mContext);
		}
		moveIndex = 0;
		String text = String.format(moveProgressText, moveIndex);
		moveProgressDialog.setMessage(text);
		moveProgressDialog.show();
	}
	
	public void updateProgress() {
		Log.d(TAG, "updateProgress");
		moveIndex ++;
		String text = String.format(moveProgressText, moveIndex);
		moveProgressDialog.setMessage(text);
	}
	
	public void cancleProgress(boolean delay) {

		Log.d(TAG, "cancleProgress delay " + delay);
		if (delay) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					moveProgressDialog.cancel();
				}
			}, 1000);
		}
		else {
			moveProgressDialog.cancel();
		}
		
		showErrorQueue();
	}

	private void showErrorQueue() {
		if (errorQueue.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String error:errorQueue) {
				buffer.append(error).append("\n");
			}
			new AlertDialog.Builder(mContext)
			.setTitle(R.string.error_msg_title)
			.setMessage(buffer.toString())
			.setPositiveButton(R.string.ok, null)
			.show();
		}
	}

	public Handler getHandler() {

		return moveEventHandler;
	}

	public void moveToFolder(List<String> pathList, File targetFile, final Handler handler) {
		if (pathList != null && targetFile.exists() && targetFile.isDirectory()) {
			new SOrderManager(null).moveToFolder(pathList, targetFile, handler, new OnOrderItemMoveTrigger() {

				@Override
				public void onTrigger(String src, String target, boolean isAllFinish) {
					FileIO fileIO = new FileIO();
					Encrypter encrypter = EncrypterFactory.create();
					fileIO.moveFile(src, target);

					src = src.replace(encrypter.getFileExtra(), encrypter.getNameExtra());
					target = target.replace(encrypter.getFileExtra(), encrypter.getNameExtra());
					fileIO.moveFile(src, target);

					Message msg = new Message();
					msg.what = isAllFinish ? Constants.STATUS_MOVE_FILE_FINISH:Constants.STATUS_MOVE_FILE_DONE;
					handler.sendMessage(msg);
				}

				@Override
				public void onNotSupport(String src, boolean allFinish) {
					Message msg = new Message();
					msg.what = Constants.STATUS_MOVE_FILE_UNSUPORT;
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constants.KEY_MOVETO_UNSUPPORT_FINISH, allFinish);
					bundle.putString(Constants.KEY_MOVETO_UNSUPPORT_SRC, src);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			});
		}
	}

	public void addError(String error) {
		errorQueue.add(error);
	}
}
