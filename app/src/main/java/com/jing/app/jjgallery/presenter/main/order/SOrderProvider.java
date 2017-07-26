package com.jing.app.jjgallery.presenter.main.order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.MoveController;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.SOrderDao;
import com.jing.app.jjgallery.service.data.impl.SOrderDaoImpl;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundSelector;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderChooserUpdate;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.sub.dialog.FolderDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 * 提供 添加至列表、设置封面、设置为背景、查看详细信息、移动至目录 统一接口
 */
public class SOrderProvider implements Handler.Callback {

    private final String TAG = "SOrderProvider";

    private Context mContext;
    private SOrderManager sOrderManager;
    private MoveController moveController;

    private String moveFolderPath;

    private SOrderProviderCallback mCallback;
    private ProgressDialog moveProgressDialog;

    public SOrderProvider(Context context, SOrderProviderCallback callback) {
        mContext = context;
        mCallback = callback;
        sOrderManager = new SOrderManager(null);
        moveController = new MoveController(mContext, this);
    }

    private void showProgress() {
        if (moveProgressDialog == null) {
            moveProgressDialog = new ProgressDialog(mContext);
        }
        moveProgressDialog.setMessage(mContext.getString(R.string.processing));
        moveProgressDialog.show();
    }

    private void dismissProgress() {
        moveProgressDialog.dismiss();
    }

    /**
     * set as cover
     * @param imgPath target path must be valid file
     */
    public void openOrderChooserToSetCover(final String imgPath) {

        if (imgPath == null || !new File(imgPath).exists()) {
            return;
        }

		SOrderChooserUpdate chooser = new SOrderChooserUpdate(mContext, new CustomDialog.OnCustomDialogActionListener() {

			@Override
			public boolean onSave(Object object) {
				if (object != null) {
					SOrder order = (SOrder) object;
                    order.setCoverPath(imgPath);
                    String msg = null;
                    if (sOrderManager.updateOrder(order)) {
                        msg = mContext.getResources().getString(R.string.spicture_myorders_set_cover_ok);
                    }
                    else {
                        msg = mContext.getResources().getString(R.string.spicture_myorders_set_cover_fail);
                    }
                    if (order.getName() != null) {
                        msg = msg.replace("%s", order.getName());
                    }
                    ((ProgressProvider) mContext).showToastLong(msg, ProgressProvider.TOAST_SUCCESS);
				}
				return true;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {

			}

			@Override
			public boolean onCancel() {
				return false;
			}
		});
		chooser.setTitle(mContext.getResources().getString(R.string.set_as_cover));
		chooser.show();
    }

    public void openOrderChooserToAddItem(final List<String> pathList) {

        SOrderChooserUpdate chooser = new SOrderChooserUpdate(mContext, new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {
                if (object != null) {
                    final SOrder order = (SOrder) object;
                    for (String path : pathList) {
                        final String itemPath = path;
                        if (itemPath != null) {
                            if (sOrderManager.isOrderItemExist(itemPath, order.getId())) {
                                String title = mContext.getResources().getString(R.string.spicture_myorders_item_exist);
                                title = String.format(title, order.getName());
                                new AlertDialog.Builder(mContext)
                                        .setMessage(title)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                addToOrder(itemPath, order, true);
//                                                mCallback.onAddToOrderSuccess();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, null)
                                        .show();
                            }
                            else {
                                addToOrder(itemPath, order, false);
                            }
                        }
                        else {
                            ((ProgressProvider) mContext).showToastLong(mContext.getString(R.string.login_pwd_error), ProgressProvider.TOAST_ERROR);
                        }
                    }
                    mCallback.onAddToOrderFinished();
                }
                return true;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {

            }

            @Override
            public boolean onCancel() {
                return false;
            }
        });
        chooser.setTitle(mContext.getResources().getString(R.string.add_to_order));
        chooser.show();
    }

    private void addToOrder(String path, SOrder order, boolean showResult) {
		String msg = null;
		if (sOrderManager.addItemToOrder(path, order)) {
			msg = mContext.getResources().getString(R.string.spicture_myorders_add_ok);
		}
		else {
			msg = mContext.getResources().getString(R.string.spicture_myorders_add_fail);
		}
		if (order.getName() != null) {
			msg = msg.replace("%s", order.getName());
		}
		if (showResult) {
            ((ProgressProvider) mContext).showToastLong(msg, ProgressProvider.TOAST_INFOR);
		}
    }

    /**
     * move to folder
     * @param pathList
     */
    public void openFolderDialogToMoveFiles(final List<String> pathList) {
        FolderDialog folderDialog = new FolderDialog(mContext,
                new CustomDialog.OnCustomDialogActionListener() {

                    @Override
                    public boolean onSave(Object object) {
                        File targetFile = (File) object;
                        moveFolderPath = targetFile.getPath();
                        moveController.showProgress();
                        new MoveTask().execute(pathList, targetFile);
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        data.put(Constants.KEY_FOLDERDLG_ROOT, Configuration.APP_DIR_IMG);
                    }

                    @Override
                    public boolean onCancel() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                });
        folderDialog.setTitle(mContext.getResources().getString(R.string.move_to_folder));
        folderDialog.show();
    }

    /**
     * view detail
     * @param path
     */
    public void viewDetails(String path) {
        File file = new File(path);
        if (file != null && file.exists()) {
            new DefaultDialogManager().openDetailDialog(mContext, file);
        }
    }

    public void deleteItemFromFolder(final List<String> pathList) {
        String msg = mContext.getResources().getString(R.string.thumb_folder_warning_delete);
        msg = msg.replace("%d", "" + pathList.size());
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgress();
                        new DeleteFromFolderTask().execute(pathList);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void deleteItemFromOrder(final SOrder mCurrentOrder, final List<Integer> selectedIndex) {
        String msg = mContext.getResources().getString(R.string.thumb_folder_warning_delete);
        msg = msg.replace("%d", "" + selectedIndex.size());
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgress();
                        new DeleteFromOrderTask().execute(mCurrentOrder, selectedIndex);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.STATUS_MOVE_FILE_DONE:
                Log.d(TAG, "handleMessage STATUS_MOVE_FILE_DONE");
                moveController.updateProgress();
                break;
            case Constants.STATUS_MOVE_FILE_FINISH:
                Log.d(TAG, "handleMessage STATUS_MOVE_FILE_FINISH");
                moveController.updateProgress();
                moveController.cancleProgress(true);
                mCallback.onMoveFinish(moveFolderPath);
//                refreshGridView();
//                if (gridAdapterProvider.isActionMode()) {
//                    gridAdapterProvider.showActionMode(false);
//                    gridAdapterProvider.refresh(false);
//                }
                break;
            case Constants.STATUS_MOVE_FILE_UNSUPORT:
                Log.d(TAG, "handleMessage STATUS_MOVE_FILE_UNSUPORT");
                Bundle bundle = msg.getData();
                boolean isFinish = bundle.getBoolean(Constants.KEY_MOVETO_UNSUPPORT_FINISH);
                String src = bundle.getString(Constants.KEY_MOVETO_UNSUPPORT_SRC);
                String error = mContext.getResources().getString(R.string.move_src_to_src) + "\n" + src;

                moveController.addError(error);
                if (isFinish) {
                    moveController.cancleProgress(false);
                }
                break;
        }
        return true;
    }

    public void openBackgroundSelector(final String imagePath) {
        new BackgroundSelector(mContext, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put("imagePath", imagePath);
            }
        }).show();
    }

    private class MoveTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Object... params) {
            List<String> pathList = (List<String>) params[0];
            File targetFile = (File) params[1];
            moveController.moveToFolder(pathList, targetFile, moveController.getHandler());
            return null;
        }
    }

    /**
     * 仅仅从列表中删除
     */
    private class DeleteFromOrderTask extends AsyncTask<Object, Integer, Integer> {
        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            dismissProgress();
            mCallback.onDeleteFinished(count);
        }

        @Override
        protected Integer doInBackground(Object... params) {
            SOrder order = (SOrder) params[0];
            List<Integer> posList = (List<Integer>) params[1];
            int index = 0;
            //从后往前删，否则易出数组越界异常
            for (int i = posList.size() - 1; i > -1; i --) {
                index = posList.get(i);
                sOrderManager.deleteItemFromOrder(index, order);
                publishProgress(index);
                order.getImgPathIdList().remove(index);
                order.getImgPathList().remove(index);
            }
            return posList.size();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mCallback.onDeleteIndex(values[0]);
            super.onProgressUpdate(values);
        }
    }

    /**
     * 除了从磁盘删除外，还有删除数据库中关联的记录
     */
    private class DeleteFromFolderTask extends AsyncTask<List<String>, Integer, Integer> {
        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            dismissProgress();
            mCallback.onDeleteFinished(count);
        }

        @Override
        protected Integer doInBackground(List<String>... params) {
            List<String> pathList = params[0];
            try {
                SqlConnection.getInstance().connect(DBInfor.DB_PATH);
                SOrderDao dao = new SOrderDaoImpl();

                for (int i = 0; i < pathList.size(); i ++) {
                    String path = pathList.get(i);
                    File file = new File(path);
                    if (EncryptUtil.isEncrypted(file)) {
                        file.delete();
                        dao.deleteItemFromAllOrders(path, SqlConnection.getInstance().getConnection());
                        Log.i("FileEncryption", "delete file " + file.getPath());

                        path = path.replace(EncryptUtil.getFileExtra(), EncryptUtil.getNameExtra());
                        file = new File(path);
                        file.delete();
                        Log.i("FileEncryption", "delete file " + file.getPath());

                    }
                    else {
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SqlConnection.getInstance().close();
            }
            return pathList.size();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mCallback.onDeleteIndex(values[0]);
            super.onProgressUpdate(values);
        }
    }
}
