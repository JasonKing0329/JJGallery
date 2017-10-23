package com.jing.app.jjgallery.presenter.main.filesystem;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jing.app.jjgallery.bean.filesystem.FilePageItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.ImageValueController;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.R.attr.path;

public class FileListController {

	private final String TAG = "FileListController";

	private Context mContext;
	private Handler handler;
	private String currentPath;
	private FileChangeListener fileChangeListener;
	private ImageValueController imageValueController;

	private List<FilePageItem> filePageItemList;

	public static final int FILE_TYPE_ALL = 0;
	public static final int FILE_TYPE_ENCRYPTED = 1;
	public static final int FILE_TYPE_UNENCRYPTED = 2;
	private int currentType = FILE_TYPE_ALL;

	private boolean isFindAll, isFindEncrypted, isFindUnEncrypted;
	public static final int SORT_BY_NONE = 0;
	public static final int SORT_BY_NAME = 1;
	public static final int SORT_BY_DATE = 2;
	private int sortMode;
	private boolean sortNameDesc, sortDateDesc;

	private TraceNode traceNode;

	/**
	 * in this structure, folder trace tree only record its parent and one child
	 * if access another child, it will replace the previous child
	 * @author Yang Jing
	 *
	 */
	private class TraceNode {
		TraceNode parent;
		TraceNode child;
		int scrollPosition;
		String path;

		public TraceNode(int position) {
			scrollPosition = position;
		}
	}

	public FileListController(Context context, Handler handler, FileChangeListener listener) {
		this.handler = handler;
		mContext = context;
		fileChangeListener = listener;
		imageValueController = new ImageValueController();
		currentPath = Configuration.APP_DIR_IMG;
		isFindAll = true;
		sortMode = SORT_BY_NONE;

		traceNode = new TraceNode(0);
		traceNode.path = currentPath;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String path) {
		this.currentPath = path;
	}

	public List<FilePageItem> getFilePageItemList() {
		return filePageItemList;
	}

	public int getFileType() {
		return currentType;
	}

	public void setSortMode(int mode, boolean decrease) {
		sortMode = mode;
		if (sortMode == SORT_BY_NAME) {
			sortNameDesc = decrease;
		}
		else if (sortMode == SORT_BY_DATE) {
			sortDateDesc = decrease;
		}
	}

	public void setFindAll(boolean isFindAll) {
		this.isFindAll = isFindAll;
	}

	public void setFindEncrypted(boolean isFindEncrypted) {
		this.isFindEncrypted = isFindEncrypted;
	}

	public void setFindUnEncrypted(boolean isFindUnEncrypted) {
		this.isFindUnEncrypted = isFindUnEncrypted;
	}

	public boolean isRootFolder() {
		return currentPath.equals(Configuration.APP_DIR_IMG);
	}

	public boolean isEncryptedFile(File file) {
		if (file.exists()) {
			return EncryptUtil.isEncrypted(file);
		}
		return false;
	}

	public boolean isEncrypted(String path) {
		File file = new File(path);
		if (file.exists()) {
			return EncryptUtil.isEncrypted(file);
		}
		return false;
	}

	public boolean createFolder(File file) {
		if (!file.exists()) {
			Log.i(TAG, "createFolder");
			file.mkdir();
			return true;
		}
		return false;
	}

	public void encryptCurFolder() {
		new Thread() {

			public void run() {
				File files[] = new File(currentPath).listFiles();
				for (File file:files) {
					if (!file.isDirectory()) {
						EncryptUtil.encryptFile(file);
					}
				}
				Message message = new Message();
				message.what = FILE_TYPE_ENCRYPTED;
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", true);
				message.setData(bundle);
				handler.sendMessage(message);
			}
		}.start();
	}

	public boolean encryptFile(int position) {
		final File file = filePageItemList.get(position).getFile();
		if (file.exists() && !file.isDirectory()) {
			new Thread() {

				public void run() {
					boolean result = EncryptUtil.encryptFile(file)
							== null ? false:true;
					Log.i(TAG, " encryptFile " + result + " " + file.getPath());
					Message message = new Message();
					message.what = FILE_TYPE_ENCRYPTED;
					Bundle bundle = new Bundle();
					bundle.putBoolean("result", result);
					message.setData(bundle);
					handler.sendMessage(message);
				}
			}.start();
		}
		return false;
	}

	public void decipherCurFolder() {
		new Thread() {

			public void run() {
				File files[] = new File(currentPath).listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory();
					}
				});
				for (File file:files) {
					EncryptUtil.decipherFile(file, null);
				}
				Message message = new Message();
				message.what = FILE_TYPE_UNENCRYPTED;
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", true);
				message.setData(bundle);
				handler.sendMessage(message);
			}
		}.start();
	}

	public boolean decipherFile(int position) {
		final File file = filePageItemList.get(position).getFile();
		if (file.exists() && !file.isDirectory()) {
			new Thread() {

				public void run() {
					boolean result = EncryptUtil.decipherFile(file, null);
					Log.i(TAG, " decipherFile " + result + " " + file.getPath());
					Message message = new Message();
					message.what = FILE_TYPE_UNENCRYPTED;
					Bundle bundle = new Bundle();
					bundle.putBoolean("result", result);
					message.setData(bundle);
					handler.sendMessage(message);
				}
			}.start();
		}
		return false;
	}

	public void findEncryptedFile() {
		Log.i(TAG, "findEncryptedFile");
		File[] files = new File(currentPath).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return EncryptUtil.isEncrypted(file) || file.isDirectory();
			}

		});
		createPageItems(files);
		currentType = FILE_TYPE_ENCRYPTED;
	}

	public void createPageItems(File[] files) {
		if (files.length > 0) {
			boolean showOriginName = SettingProperties.isShowFileOriginMode(mContext);
			filePageItemList = new ArrayList<FilePageItem>();
			FilePageItem item = null;
			for (File f:files) {
				item = new FilePageItem();
				item.setFile(f);
				item.setDate(f.lastModified());

				if (!f.isDirectory()) {
					// 解析原文件名
					if (EncryptUtil.isEncrypted(f)) {
						item.setOriginName(EncryptUtil.getOriginName(f));
					}
					if (showOriginName && item.getOriginName() != null) {
						item.setDisplayName(item.getOriginName());
					}
					else {
						item.setDisplayName(f.getName());
					}

					// gdb目录下的文件太多，解析ImageValue会耗时太长，在tab s上时长很长
					// gdb文件不关注image大小，不解析这个参数
					if (!f.getPath().startsWith(Configuration.GDB_IMG)) {
						// 解析图片大小
						ImageValue value = imageValueController.queryImagePixel(f.getPath());
						item.setImageValue(value);
					}

				}
				else {
					item.setDisplayName(f.getName());
				}

				filePageItemList.add(item);
			}
		}
		else {
			filePageItemList.clear();
		}
	}

	/**
	 * 判断加载当前目录的内容用同步还是异步
	 * 异步的条件：
	 * 目录内容全是加密文件
	 * 文件个数大于20
	 * @param path
	 * @return
     */
	private boolean needLoadAsync(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length > 20*2) {// 加密文件是由jfe和jne两个文件构成
				for (File f:files) {
					if (!f.isDirectory()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void findUnEncryptedFile() {
		Log.i(TAG, "findUnEncryptedFile");
		File[] files = new File(currentPath).listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File file, String name) {

				return !EncryptUtil.isEncrypted(file);
			}
		});
		createPageItems(files);
		currentType = FILE_TYPE_UNENCRYPTED;
	}

	public void findAllFile() {
		Log.i(TAG, "findAllFile");
		File[] files = new File(currentPath).listFiles(new FilenameFilter() {//过滤掉name的encrypt文件

			@Override
			public boolean accept(File arg0, String name) {

				return !name.endsWith(EncryptUtil.getNameExtra());
			}
		});
		createPageItems(files);
		currentType = FILE_TYPE_ALL;

	}

	private Handler findFileHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (sortMode == SORT_BY_NAME) {
				sortByName(sortNameDesc);
			}
			else if (sortMode == SORT_BY_DATE) {
				sortByTime(sortDateDesc);
			}
			else {
				fileChangeListener.onFindFileFinish();
			}
			((ProgressProvider) mContext).dismissProgressCycler();
			super.handleMessage(msg);
		}
	};

	/**
	 * 加载目录改为区分字目录形式
	 * 若字目录是全文件夹，同步加载
	 * 若字目录全是加密文件，异步加载
	 */
	public void findFile() {
		// 子目录都是加密文件且大于20，需要解析原文件名，异步加载
		if (needLoadAsync(currentPath)) {
			((ProgressProvider) mContext).showProgressCycler();
			new Thread() {
				public void run() {
					startFindFile();
					findFileHandler.sendEmptyMessage(1);
				}
			}.start();
		}
		// 子目录都是文件夹（不用解析原文件名），直接同步加载
		else {
			startFindFile();
			if (sortMode == SORT_BY_NAME) {
				sortByName(sortNameDesc);
			}
			else if (sortMode == SORT_BY_DATE) {
				sortByTime(sortDateDesc);
			}
			else {
				fileChangeListener.onFindFileFinish();
			}
		}
	}

	private void startFindFile() {
		if (isFindAll) {
			findAllFile();
		}
		else if (isFindEncrypted) {
			findEncryptedFile();
		}
		else if (isFindUnEncrypted) {
			findUnEncryptedFile();
		}
	}

	public void findParent() {
		File file = new File(currentPath);
		file = file.getParentFile();
		currentPath = file.getPath();
		if (file.getPath().equals(Configuration.APP_DIR_IMG)) {
			fileChangeListener.onBackToRoot();
			findFile();
		}
		else {
			findFile();
		}
	}

	public void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] sub = file.listFiles();
			for (int i = 0; i < sub.length; i ++) {
				sub[i].delete();
			}
		}
		file.delete();
	}

	public void sortByTime(final boolean decrease) {
		if (filePageItemList != null) {
			Collections.sort(filePageItemList, new Comparator<FilePageItem>() {

				@Override
				/**
				 * compare 需要用-1,0,1表示大小关系，不能用<0,>0判断
				 * @param lhs
				 * @param rhs
				 * @return -1, 0, 1
				 */
				public int compare(FilePageItem lhs, FilePageItem rhs) {

					long result = 0;
					if (decrease) {
						result = rhs.getDate() - lhs.getDate();
					}
					else {
						result = lhs.getDate() - rhs.getDate();
					}

					if (result < 0) {
						return - 1;
					}
					if (result == 0) {
						return 0;
					}
					else {
						return 1;
					}
				}
			});
			fileChangeListener.onFindFileFinish();
		}
	}

	public void sortByName(final boolean decrease) {
		if (filePageItemList != null) {
			Collections.sort(filePageItemList, new Comparator<FilePageItem>() {

				@Override
				public int compare(FilePageItem lhs, FilePageItem rhs) {

					if (decrease) {
						return rhs.getDisplayName().toLowerCase(Locale.getDefault()).compareTo(lhs.getDisplayName().toLowerCase());
					}
					else {
						return lhs.getDisplayName().toLowerCase(Locale.getDefault()).compareTo(rhs.getDisplayName().toLowerCase());
					}
				}
			});
			fileChangeListener.onFindFileFinish();
		}
	}

	public int getScrollPosition() {
		return traceNode.scrollPosition;
	}

	/**
	 * call this after setCurrentPath
	 * @param position
	 */
	public void updateParentPosition(int position) {
		traceNode.scrollPosition = position;
		if (traceNode.child == null || !currentPath.equals(traceNode.child.path)) {
			TraceNode node = new TraceNode(0);
			node.path = currentPath;
			traceNode.child = node;
			node.parent = traceNode;
		}
		traceNode = traceNode.child;
	}

	/**
	 * call this after setCurrentPath
	 * @param position
	 */
	public void updateChildPosition(int position) {
		if (traceNode.parent != null) {
			traceNode.scrollPosition = position;
			traceNode = traceNode.parent;
		}
	}
}
