package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class SceneDialog extends CustomDialog implements AdapterView.OnItemClickListener
    , AdapterView.OnItemLongClickListener{

    private GridView gridScene;
    private SceneAdapter sceneAdapter;
    private List<String> sceneList;

    public SceneDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        sceneList = new ArrayList<>();
        requestCancelAction(true);
        sceneAdapter = new SceneAdapter();
        gridScene.setAdapter(sceneAdapter);
        String scene = SettingProperties.getGdbGameScenes(context);
        try {
            String[] scenes = scene.split(",");
            for (String sc:scenes) {
                sceneList.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gdb_game_dlg_scene, null);
        gridScene = (GridView) view.findViewById(R.id.scene_grid);
        gridScene.setOnItemClickListener(this);
        gridScene.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gdb_game_scene_actionbar, null);
        view.findViewById(R.id.scene_dlg_add).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.scene_dlg_add:
                new DefaultDialogManager().openInputDialog(getContext(), new DefaultDialogManager.OnDialogActionListener() {
                    @Override
                    public void onOk(String name) {
                        sceneList.add(name);
                        saveScenes();
                        sceneAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    private void saveScenes() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < sceneList.size(); i ++) {
            if (i == 0) {
                buffer.append(sceneList.get(i));
            }
            else {
                buffer.append(",").append(sceneList.get(i));
            }
        }
        SettingProperties.saveGdbGameScenes(getContext(), buffer.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!sceneAdapter.isSelectionMode()) {
            String scene = sceneList.get(position);
            actionListener.onSave(scene);
            dismiss();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        sceneAdapter.setSelectionMode(!sceneAdapter.isSelectionMode());
        sceneAdapter.notifyDataSetChanged();
        return true;
    }

    private class SceneAdapter extends BaseAdapter implements View.OnClickListener {

        private boolean selectionMode;

        public void setSelectionMode(boolean selectionMode) {
            this.selectionMode = selectionMode;
        }

        public boolean isSelectionMode() {
            return selectionMode;
        }

        @Override
        public int getCount() {
            return sceneList.size();
        }

        @Override
        public Object getItem(int position) {
            return sceneList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SceneHolder holder;
            if (convertView == null) {
                holder = new SceneHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_scene, parent, false);
                holder.scene = (TextView) convertView.findViewById(R.id.scene_name);
                holder.delete = (ImageView) convertView.findViewById(R.id.scene_delete);
                convertView.setTag(holder);
            }
            else {
                holder = (SceneHolder) convertView.getTag();
            }
            holder.scene.setText(sceneList.get(position));
            holder.delete.setVisibility(selectionMode ? View.VISIBLE:View.GONE);
            holder.delete.setTag(position);
            holder.delete.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            sceneList.remove(position);
            saveScenes();
            notifyDataSetChanged();
        }
    }

    private class SceneHolder {
        TextView scene;
        ImageView delete;
    }
}
