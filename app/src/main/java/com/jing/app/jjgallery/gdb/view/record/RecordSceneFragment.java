package com.jing.app.jjgallery.gdb.view.record;

import android.content.DialogInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.RecordSceneNameAdapter;
import com.jing.app.jjgallery.gdb.view.pub.HsvColorDialog;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.SceneBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.angle;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 16:12
 */
public class RecordSceneFragment extends GBaseFragment implements IRecordSceneView {

    private IRecordSceneHolder holder;

    @BindView(R.id.rv_scenes)
    RecyclerView rvScenes;

    private HsvColorDialog colorDialog;

    private RecordSceneNameAdapter sceneAdapter;

    private List<SceneBean> sceneList;

    private int curSortType;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (IRecordSceneHolder) holder;
        this.holder.getPresenter().setRecordSceneView(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_gdb_scene;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rvScenes.setLayoutManager(manager);

        curSortType = GdbConstants.SCENE_SORT_NAME;
        holder.getPresenter().loadRecordScenes();
    }

    @Override
    public void onScenesLoaded(List<SceneBean> data) {
        sceneList = data;
        sceneAdapter = new RecordSceneNameAdapter(data);
        sceneAdapter.setOnSceneItemClickListener(new RecordSceneNameAdapter.OnSceneItemClickListener() {
            @Override
            public void onSceneItemClick(String scene) {
                holder.onSelectScene(scene);
            }
        });
        rvScenes.setAdapter(sceneAdapter);
    }

    public void sortByName() {
        if (curSortType != GdbConstants.SCENE_SORT_NAME) {
            curSortType = GdbConstants.SCENE_SORT_NAME;
            holder.getPresenter().sortScenes(sceneList, curSortType);
            sceneAdapter.notifyDataSetChanged();
        }
    }

    public void sortByAvg() {
        if (curSortType != GdbConstants.SCENE_SORT_AVG) {
            curSortType = GdbConstants.SCENE_SORT_AVG;
            holder.getPresenter().sortScenes(sceneList, curSortType);
            sceneAdapter.notifyDataSetChanged();
        }
    }

    public void sortByNumber() {
        if (curSortType != GdbConstants.SCENE_SORT_NUMBER) {
            curSortType = GdbConstants.SCENE_SORT_NUMBER;
            holder.getPresenter().sortScenes(sceneList, curSortType);
            sceneAdapter.notifyDataSetChanged();
        }
    }

    public void sortByMax() {
        if (curSortType != GdbConstants.SCENE_SORT_MAX) {
            curSortType = GdbConstants.SCENE_SORT_MAX;
            holder.getPresenter().sortScenes(sceneList, curSortType);
            sceneAdapter.notifyDataSetChanged();
        }
    }

    public void editColor() {
        if (colorDialog == null) {
            colorDialog = new HsvColorDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    Map<String, Integer> map = (Map<String, Integer>) object;
                    SettingProperties.setGdbSceneHsvStart(map.get("start"));
                    SettingProperties.setGdbSceneHsvAngle(map.get("angle"));
                    return true;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {
                    data.put("start", SettingProperties.getGdbSceneHsvStart());
                    data.put("angle", SettingProperties.getGdbSceneHsvAngle());
                }
            });
        }
        colorDialog.setOnHsvColorListener(new HsvColorDialog.OnHsvColorListener() {
            @Override
            public void onPreviewHsvColor(int start, int angle) {
                sceneAdapter.updateBgColors(start, angle);
            }
        });
        colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sceneAdapter.updateBgColors(SettingProperties.getGdbSceneHsvStart(), SettingProperties.getGdbSceneHsvAngle());
            }
        });
        colorDialog.show();
    }
}
