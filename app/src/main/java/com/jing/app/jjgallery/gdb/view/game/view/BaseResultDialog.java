package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 14:36
 */
public abstract class BaseResultDialog extends CustomDialog {

    private ListView lvTop;
    private ListView lvBottom;

    protected ResultAdapter topAdapter;
    protected ResultAdapter bottomAdapter;

    protected OnBattleResultListener onBattleResultListener;

    public BaseResultDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        requestSaveAction(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResultDatas();
            }
        });

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        loadAdapterDatas(map);
        lvTop.setAdapter(topAdapter);
        lvBottom.setAdapter(bottomAdapter);
    }

    protected abstract void loadAdapterDatas(HashMap<String, Object> map);

    protected abstract void createResultDatas();

    public void setOnBattleResultListener(OnBattleResultListener onBattleResultListener) {
        this.onBattleResultListener = onBattleResultListener;
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gdb_game_dlg_battle_result, null);
        lvTop = (ListView) view.findViewById(R.id.battle_result_top_list);
        lvBottom = (ListView) view.findViewById(R.id.battle_result_bottom_list);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    protected class ResultAdapter<T extends AbsRoundResultBean> extends BaseAdapter {

        private List<T> list;

        public ResultAdapter(List<T> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ResultHolder holder;
            if (convertView == null) {
                holder = new ResultHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_battle_result_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.battle_result_name);
                holder.tvScoreTotal = (TextView) convertView.findViewById(R.id.battle_result_score_total);
                holder.tvScoreRound = (TextView) convertView.findViewById(R.id.battle_result_score_round);
                convertView.setTag(holder);
            }
            else {
                holder = (ResultHolder) convertView.getTag();
            }
            holder.tvName.setText(list.get(position).name);
            holder.tvScoreTotal.setText(list.get(position).scoreTotal);
            holder.tvScoreRound.setText(list.get(position).scoreRound);
            return convertView;
        }
    }

    private class ResultHolder {
        TextView tvName;
        TextView tvScoreTotal;
        TextView tvScoreRound;
    }

    protected abstract class AbsRoundResultBean {
        String name;
        String scoreTotal;
        String scoreRound;
    }

    public interface OnBattleResultListener<T>{
        void onCreateBattleResultDatas(List<T> datas);
    }
}
