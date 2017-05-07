package com.jing.app.jjgallery.model.main.file;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FolderManager {

	private FileCallback mCallback;

	public void setFileCallback(FileCallback callback) {
		mCallback = callback;
	}

	public List<File> loadList(String rootPath) {
		List<File> list = null;
		File file = new File(rootPath);
		File[] files = file.listFiles();
		if (files.length > 0) {
			list = new ArrayList<File>();
			for (File f:files) {
				list.add(f);
			}
		}
		return list;
	}

	public List<String> loadPathList(String path) {
		List<String> list = new ArrayList<>();
		File[] files = new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return EncryptUtil.isEncrypted(file) || file.isDirectory();
			}

		});
		for (File f:files) {
			list.add(f.getPath());
		}
		return list;
	}

	public List<FileBean> loadFileBeanList(String path) {
		List<FileBean> list = new ArrayList<>();
		File[] files = new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return EncryptUtil.isEncrypted(file) || file.isDirectory();
			}

		});
		FileBean bean = null;
		for (File f:files) {
			bean = new FileBean();
			bean.setPath(f.getPath());
			list.add(bean);
		}
		return list;
	}

	public List<File> addSubFolders(File file) {
		List<File> list = null;
		File[] subFile = file.listFiles();
		if (file.isDirectory()) {
			if (subFile.length > 0) {
				list = new ArrayList<File>();
				for (File f:subFile) {
					if (f.isDirectory()) {
						list.add(f);
					}
				}
			}
		}
		if (list != null && list.size() == 0) {
			list = null;
		}
		if (list != null) {
			Collections.sort(list, new Comparator<File>() {

				@Override
				public int compare(File f1, File f2) {

					return f1.getName().compareTo(f2.getName());
				}
			});
		}
		return list;
	}

	public boolean hasFolderChild(File file) {
		File[] subFile = file.listFiles();
		if (file.isDirectory()) {
			if (subFile.length > 0) {
				for (File f:subFile) {
					if (f.isDirectory()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * collect all deepest folder in list
	 * @param targetList
	 * @param file
	 * @return
	 */
	private boolean collectFolders(List<File> targetList, File file) {
		boolean hasChildFolder = false;
		File[] child = file.listFiles();
		for (int i = 0; i < child.length; i ++) {
			if (child[i].isDirectory()) {
				hasChildFolder = collectFolders(targetList, child[i]);
			}
		}
		if (!hasChildFolder) {
			targetList.add(file);
			return true;
		}
		return false;
	}

	/**
	 * collect all deepest folder in one list
	 * apply in thumbfolder folder list and bookview keyword flow
	 * @return
	 */
	public List<File> collectAllFolders() {
		List<File> folderList = new ArrayList<File>();
		File file = new File(Configuration.APP_DIR_IMG);
		collectFolders(folderList, file);
		return folderList;
	}
	
	/**
	 * 
	 * @param parent parent folder path
	 * @param folderName target folder name to create
	 * @return if null, folder already exist
	 */
	public File createFolder(String parent, String folderName) {
		if (parent == null) {
			parent = Configuration.APP_DIR_IMG;
		}
		
		File file = new File(parent + "/" + folderName);
		if (file.exists()) {
			return null;
		}
		
		file.mkdir();
		return file;
	}

	/**
	 * 加载所有的有内容的目录（IndexPage)
	 * 异步方法，FileCallback -> onLoadAllFolders回调
	 */
	public void loadAllFolders() {
		new LoadTask().execute();
	}

	private class LoadTask extends AsyncTask<Void, Void, List<String>> {
		@Override
		protected List<String> doInBackground(Void... params) {
			List<String> list = new ArrayList<>();
			List<File> fileList = new FolderManager().collectAllFolders();;
			for (int i = 0; i < fileList.size(); i ++) {
				list.add(fileList.get(i).getPath());
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<String> list) {
			mCallback.onLoadAllFolders(list);
			super.onPostExecute(list);
		}
	}
}
