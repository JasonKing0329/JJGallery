package com.jing.app.jjgallery.gdb.view.update;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.http.bean.response.AppCheckBean;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.bean.DownloadDialogBean;
import com.jing.app.jjgallery.gdb.presenter.GdbUpdatePresenter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.download.DownloadDialogFragment;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.publicview.download.v4.DownloadDialogFragmentV4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class GdbUpdateManager implements IUpdateView {

    private GdbUpdatePresenter mPresenter;
    private Context mContext;
    private FragmentManager fragmentManagerV4;
    private android.app.FragmentManager fragmentManager;
    private GdbUpdateListener updateListener;

    private boolean isUpdating;
    private boolean isShowing;

    private boolean showMessageWarning;

    public GdbUpdateManager(Context context, GdbUpdateListener listener) {
        mContext = context;
        this.updateListener = listener;
        mPresenter = new GdbUpdatePresenter(this);
    }

    public void setFragmentManagerV4(FragmentManager fragmentManagerV4) {
        this.fragmentManagerV4 = fragmentManagerV4;
    }

    public void setFragmentManager(android.app.FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showMessageWarning() {
        showMessageWarning = true;
    }

    public void startCheck() {
        if (!TextUtils.isEmpty(SettingProperties.getGdbServerBaseUrl(mContext))) {
            // 检测更新，必须在配置过服务器以后
            mPresenter.checkGdbDatabase();
        }
        else {
            if (mContext instanceof ProgressProvider) {
                ((ProgressProvider) mContext).showToastLong(mContext.getString(R.string.server_not_conf), ProgressProvider.TOAST_WARNING);
            }
            else {
                DebugLog.e(mContext.getString(R.string.server_not_conf));
            }
            if (updateListener != null) {
                updateListener.onUpdateCancel();
            }
        }
    }
    @Override
    public void onGdbDatabaseFound(final AppCheckBean bean) {
        isShowing = true;
        String msg = String.format(mContext.getString(R.string.gdb_update_found), bean.getGdbDabaseVersion());
        new DefaultDialogManager().showOptionDialog(mContext, null, msg
                , mContext.getResources().getString(R.string.yes)
                , null
                , mContext.getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            isUpdating = true;
                            startDownloadNewApp(bean);
                        }
                    }
                }
                , new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (updateListener != null) {
                            if (!isUpdating) {
                                if (updateListener != null) {
                                    updateListener.onUpdateCancel();
                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onGdbDatabaseIsLatest() {
        if (updateListener != null) {
            updateListener.onUpdateCancel();
        }
        if (showMessageWarning) {
            ((ProgressProvider) mContext).showToastLong(mContext.getString(R.string.gdb_is_latest), ProgressProvider.TOAST_INFOR);
        }
    }

    @Override
    public void onServiceDisConnected() {
        DebugLog.e("服务器连接失败");
        if (updateListener != null) {
            updateListener.onUpdateCancel();
        }
        if (showMessageWarning) {
            ((ProgressProvider) mContext).showToastLong(mContext.getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public void onRequestError() {
        DebugLog.e("更新app失败");
        if (updateListener != null) {
            updateListener.onUpdateCancel();
        }
        if (showMessageWarning) {
            ((ProgressProvider) mContext).showToastLong(mContext.getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    private void startDownloadNewApp(AppCheckBean bean) {

        if (fragmentManager != null) {
            showDownloadDialogFragment(bean);
        }
        else if (fragmentManagerV4 != null) {
            showDownloadDialogFragmentV4(bean);
        }

    }

    private void showDownloadDialogFragment(AppCheckBean bean) {
        final DownloadDialogFragment dialog = new DownloadDialogFragment();

        DownloadDialogBean dialogBean = new DownloadDialogBean();
        dialogBean.setShowPreview(false);
        dialogBean.setSavePath(Configuration.APP_DIR_CONF);
        DownloadItem item = new DownloadItem();
        item.setFlag(Command.TYPE_GDB_DATABASE);
        item.setSize(bean.getGdbDabaseSize());
        item.setName(bean.getGdbDabaseName());
        List<DownloadItem> list = new ArrayList<>();
        list.add(item);
        dialogBean.setDownloadList(list);
        dialog.setDialogBean(dialogBean);
        dialog.setOnDownloadListener(new DownloadDialogFragment.OnDownloadListener() {
            @Override
            public void onDownloadFinish(DownloadItem item) {
                new File(ConfManager.GDB_DB_JOURNAL).delete();
                isUpdating = false;
                dialog.dismiss();
                if (updateListener != null) {
                    // 采用自动更新替代gdata.db的方法，因为jornal的存在，会使重新使用这个db出现问题。需要删掉这个文件。
                    updateListener.onUpdateFinish();
                }
            }

            @Override
            public void onDownloadFinish(List<DownloadItem> downloadList) {

            }
        });

        dialog.show(fragmentManager, "DownloadDialogFragment");
    }

    private void showDownloadDialogFragmentV4(AppCheckBean bean) {
        final DownloadDialogFragmentV4 dialog = new DownloadDialogFragmentV4();

        DownloadDialogBean dialogBean = new DownloadDialogBean();
        dialogBean.setShowPreview(false);
        dialogBean.setSavePath(Configuration.APP_DIR_CONF);
        DownloadItem item = new DownloadItem();
        item.setFlag(Command.TYPE_GDB_DATABASE);
        item.setSize(bean.getGdbDabaseSize());
        item.setName(bean.getGdbDabaseName());
        List<DownloadItem> list = new ArrayList<>();
        list.add(item);
        dialogBean.setDownloadList(list);
        dialog.setDialogBean(dialogBean);
        dialog.setOnDownloadListener(new DownloadDialogFragmentV4.OnDownloadListener() {
            @Override
            public void onDownloadFinish(DownloadItem item) {
                new File(ConfManager.GDB_DB_JOURNAL).delete();
                isUpdating = false;
                dialog.dismiss();
                if (updateListener != null) {
                    // 采用自动更新替代gdata.db的方法，因为jornal的存在，会使重新使用这个db出现问题。需要删掉这个文件。
                    updateListener.onUpdateFinish();
                }
            }

            @Override
            public void onDownloadFinish(List<DownloadItem> downloadList) {

            }
        });

        dialog.show(fragmentManagerV4, "DownloadDialogFragmentV4");
    }
}
