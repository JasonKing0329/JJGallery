package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.StretchListView;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 15:26
 */
public class BattleItemAdapter {

    private List<RoundHolder> itemViewList;

    private OnBattleItemListener onBattleItemListener;

    private List<List<BattleBean>> roundList;

    private LinearLayout llCardsContainer;

    private BattleDetailData battleDetailData;

    /**
     * the battle item just created
     */
    private ItemHolder toAddItemHolder;

    public BattleItemAdapter(BattleDetailData data, LinearLayout llCardsContainer, List<BattleBean> battleList) {
        this.llCardsContainer = llCardsContainer;
        this.battleDetailData = data;
        itemViewList = new ArrayList<>();
        roundList = new ArrayList<>();
        if (battleList != null) {
            Map<Integer, List<BattleBean>> roundMap = new HashMap<>();
            for (int i = 0; i < battleList.size(); i ++) {
                int round = battleList.get(i).getRound();
                List<BattleBean> list = roundMap.get(round);
                if (list == null) {
                    list = new ArrayList<>();
                    roundMap.put(round, list);
                }
                list.add(battleList.get(i));
            }

            int rounds = roundMap.keySet().size();
            for (int i = 0; i < rounds; i ++) {
                roundList.add(roundMap.get(i + 1));
                addBattleCard(llCardsContainer, i + 1, roundMap.get(i + 1));
            }
        }
    }

    public List<List<BattleBean>> getRoundList() {
        return roundList;
    }

    public void setOnBattleItemListener(OnBattleItemListener onBattleItemListener) {
        this.onBattleItemListener = onBattleItemListener;
    }

    private void addBattleCard(LinearLayout llCardsContainer, int round,  List<BattleBean> battleList) {
        RoundHolder holder = new RoundHolder(round, battleList);
        itemViewList.add(holder);
        llCardsContainer.addView(holder.getView());
    }

    public void addNewRound() {
        List<BattleBean> list = new ArrayList<>();
        addBattleCard(llCardsContainer, roundList.size() + 1, list);
    }

    /**
     * insert player to current focus battle item
     * @param bean
     */
    public void addPlayerToFocusItem(PlayerBean bean) {
        if (bean.getTopCoachId() == battleDetailData.getCoach().getId()) {
//            toAddItemHolder
        }
        else {

        }
    }

    private class RoundHolder implements View.OnClickListener {
        View view;
        StretchListView listview;
        TextView titleView;
        ItemAdapter adapter;
        ImageView ivSetting;
        ImageView ivSave;
        ImageView ivAdd;
        ImageView ivEdit;
        List<BattleBean> battleList;
        /**
         * round的下标是从1开始的，从list中获取位置时要注意
         */
        private int round;

        private RoundHolder(int round, List<BattleBean> battleList) {
            this.round = round;
            this.battleList = battleList;
            view = LayoutInflater.from(llCardsContainer.getContext()).inflate(R.layout.adapter_gdb_game_battle_round, llCardsContainer, false);
            listview = (StretchListView) view.findViewById(R.id.battle_round_list);
            titleView = (TextView) view.findViewById(R.id.battle_round_title);
            ivSave = (ImageView) view.findViewById(R.id.battle_round_save);
            ivSetting = (ImageView) view.findViewById(R.id.battle_round_setting);
            ivAdd = (ImageView) view.findViewById(R.id.battle_round_add);
            ivEdit = (ImageView) view.findViewById(R.id.battle_round_edit);
            ivSave.setTag(this);
            ivSave.setOnClickListener(this);
            ivSetting.setTag(this);
            ivSetting.setOnClickListener(this);
            ivAdd.setTag(this);
            ivAdd.setOnClickListener(this);
            ivEdit.setTag(this);
            ivEdit.setOnClickListener(this);

            titleView.setText("Round " + round);
            adapter = new ItemAdapter(battleList);
            listview.setAdapter(adapter);
        }

        public View getView() {
            return view;
        }

        @Override
        public void onClick(View v) {
            if (v == ivSetting) {

            }
            else if (v == ivSave) {
                adapter.setEditMode(false);
            }
            else if (v == ivEdit) {
                adapter.setEditMode(true);
            }
            else if (v == ivAdd) {
                adapter.setEditMode(true);
                addNewItem();
            }
        }

        private void addNewItem() {
            BattleBean bean = new BattleBean();
            bean.setSeasonId(battleDetailData.getSeason().getId());
            bean.setRound(round);
            bean.setCoachId(battleDetailData.getCoach().getId());
            battleList.add(bean);
            adapter.notifyDataSetChanged();
        }

    }

    private class ItemAdapter extends BaseAdapter implements View.OnClickListener {

        private List<BattleBean> list;
        private List<ItemHolder> holderList;
        private boolean editMode;

        private ItemAdapter(List<BattleBean> list) {
            this.list = list;
            holderList = new ArrayList<>();
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
            ItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_battle_item, parent, false);
                holder = new ItemHolder();
                convertView.setTag(holder);
                holder.scene = (TextView) convertView.findViewById(R.id.battle_item_scene);
                holder.tvBottom = (TextView) convertView.findViewById(R.id.battle_item_bottom_name);
                holder.tvTop = (TextView) convertView.findViewById(R.id.battle_item_top_name);
                holder.ivBottom = (ImageView) convertView.findViewById(R.id.battle_item_bottom_image);
                holder.ivTop = (ImageView) convertView.findViewById(R.id.battle_item_top_image);
                holder.score = (EditText) convertView.findViewById(R.id.battle_item_score);
                holder.ivDone = (ImageView) convertView.findViewById(R.id.battle_item_done);
                holder.ivRemove = (ImageView) convertView.findViewById(R.id.battle_item_remove);
                holderList.add(holder);
            }
            else {
                holder = (ItemHolder) convertView.getTag();
            }

            holder.position = position;

            BattleBean bean = list.get(position);
            holder.score.setText(bean.getScore());
            if (TextUtils.isEmpty(bean.getScene())) {
                holder.scene.setText("Scene");
            }
            else {
                holder.scene.setText(bean.getScene());
            }
            PlayerBean top = onBattleItemListener.getPlayerBean(bean.getTopPlayerId());
            PlayerBean bottom = onBattleItemListener.getPlayerBean(bean.getBottomPlayerId());
            holder.tvTop.setText(top.getName());
            holder.tvBottom.setText(bottom.getName());
            SImageLoader.getInstance().displayImage(onBattleItemListener.getPlayerImage(top.getName()), holder.ivTop);
            SImageLoader.getInstance().displayImage(onBattleItemListener.getPlayerImage(bottom.getName()), holder.ivBottom);


            // 非编辑模式下不允许输入score和选择场景，隐藏删除按钮
            if (editMode) {
                holder.ivRemove.setVisibility(View.VISIBLE);
                holder.score.setEnabled(true);
                holder.scene.setEnabled(true);
            }
            else {
                holder.ivRemove.setVisibility(View.GONE);
                holder.score.setEnabled(false);
                holder.scene.setEnabled(false);
            }

            // 编辑模式下，如果是新增item显示done按钮，如果是已添加item显示删除按钮，新增/已添加通过id是否为-1判断。
            if (editMode && bean.getId() == -1) {
                holder.ivDone.setVisibility(View.VISIBLE);
                holder.ivRemove.setVisibility(View.GONE);
            }
            else {
                holder.ivDone.setVisibility(View.GONE);
                holder.ivRemove.setVisibility(View.VISIBLE);
            }

            // 监听事件
            holder.scene.setTag(holder);
            holder.scene.setOnClickListener(this);
            holder.ivDone.setTag(holder);
            holder.ivDone.setOnClickListener(this);
            holder.ivRemove.setTag(holder);
            holder.ivRemove.setOnClickListener(this);
            return convertView;
        }

        public void setEditMode(boolean editMode) {
            this.editMode = editMode;
        }

        @Override
        public void onClick(View v) {
            ItemHolder holder = (ItemHolder) v.getTag();
            BattleBean bean = list.get(holder.position);
            switch (v.getId()) {
                case R.id.battle_item_scene:
                    break;
                case R.id.battle_item_done:
                    onBattleItemListener.onAddBattleBean(bean);
                    break;
                case R.id.battle_item_remove:
                    onBattleItemListener.onRemoveBattleBean(bean);
                    break;
            }
        }
    }

    private class ItemHolder {
        EditText score;
        TextView scene;
        ImageView ivTop;
        ImageView ivBottom;
        TextView tvTop;
        TextView tvBottom;
        ImageView ivRemove;
        ImageView ivDone;
        int position;
    }

    public interface OnBattleItemListener {
        String getPlayerImage(String name);
        PlayerBean getPlayerBean(int playerId);
        void onAddBattleBean(BattleBean bean);
        void onRemoveBattleBean(BattleBean bean);
    }
}
