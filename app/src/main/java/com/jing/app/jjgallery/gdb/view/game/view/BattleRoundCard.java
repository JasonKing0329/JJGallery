package com.jing.app.jjgallery.gdb.view.game.view;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.StretchListView;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class BattleRoundCard implements View.OnClickListener {
    private View view;
    private StretchListView listview;
    private TextView titleView;
    private ItemAdapter adapter;
    private ImageView ivSetting;
    private ImageView ivAdd;
    private ImageView ivEdit;
    private ImageView ivBack;
    private ImageView ivExpand;
    private ViewGroup groupNormal;
    private ViewGroup groupEdit;
    private List<BattleBean> battleList;
    public int round;
    private OnBattleItemListener onBattleItemListener;
    private IBattleRoundProvider roundProvider;

    private SceneDialog sceneDialog;

    private BattleBean selectSceneBean;

    public BattleRoundCard(ViewGroup container, int round, List<BattleBean> battleList
            , IBattleRoundProvider provider) {
        this.round = round;
        this.battleList = battleList;
        this.roundProvider = provider;
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.adapter_gdb_game_battle_round, container, false);
        listview = (StretchListView) view.findViewById(R.id.battle_round_list);
        titleView = (TextView) view.findViewById(R.id.battle_round_title);
        ivSetting = (ImageView) view.findViewById(R.id.battle_round_setting);
        ivAdd = (ImageView) view.findViewById(R.id.battle_round_add);
        ivEdit = (ImageView) view.findViewById(R.id.battle_round_edit);
        ivBack = (ImageView) view.findViewById(R.id.battle_round_back);
        ivExpand = (ImageView) view.findViewById(R.id.battle_round_expand);
        groupNormal = (ViewGroup) view.findViewById(R.id.battle_round_action_normal);
        groupEdit = (ViewGroup) view.findViewById(R.id.battle_round_action_edit);
        ivSetting.setTag(this);
        ivSetting.setOnClickListener(this);
        ivAdd.setTag(this);
        ivAdd.setOnClickListener(this);
        ivEdit.setTag(this);
        ivEdit.setOnClickListener(this);
        ivBack.setTag(this);
        ivBack.setOnClickListener(this);
        ivExpand.setOnClickListener(this);
        // 默认展开状态
        ivExpand.setTag(true);

        titleView.setText("Round " + round);
        adapter = new ItemAdapter(battleList);
        listview.setAdapter(adapter);

        sceneDialog = new SceneDialog(container.getContext(), new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                String scene = (String) object;
                selectSceneBean.setScene(scene);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {

            }
        });
        sceneDialog.setTitle("Scene");
    }

    public View getView() {
        return view;
    }

    public void setOnBattleItemListener(OnBattleItemListener onBattleItemListener) {
        this.onBattleItemListener = onBattleItemListener;
    }

    @Override
    public void onClick(View v) {
        if (v == ivSetting) {

        }
        else if (v == ivEdit) {
            editCard();
        }
        else if (v == ivAdd) {
            addNewItem();
            editCard();
        }
        else if (v == ivBack) {
            cancelEditCard();
        }
        else if (v == ivExpand) {
            boolean isExpand = (boolean) ivExpand.getTag();
            ivExpand.setTag(!isExpand);
            if (isExpand) {
                ivExpand.setImageResource(R.drawable.ic_expand_more_purple_3f3a71_36dp);
                listview.setVisibility(View.GONE);
                groupNormal.setVisibility(View.GONE);
            }
            else {
                ivExpand.setImageResource(R.drawable.ic_expand_less_purple_3f3a71_36dp);
                listview.setVisibility(View.VISIBLE);
                groupNormal.setVisibility(View.VISIBLE);
            }
        }
    }

    private void cancelEditCard() {
        adapter.setEditMode(false);
        groupEdit.setVisibility(View.GONE);
        groupNormal.setVisibility(View.VISIBLE);
        ivExpand.setVisibility(View.VISIBLE);
        roundProvider.onCardEditCancel(round);
    }

    private void editCard() {
        adapter.setEditMode(true);
        groupEdit.setVisibility(View.VISIBLE);
        groupNormal.setVisibility(View.GONE);
        ivExpand.setVisibility(View.GONE);
        roundProvider.onCardRequestEdit(round);
    }

    private void addNewItem() {
        BattleBean bean = new BattleBean();
        bean.setSeasonId(roundProvider.getBattleDetailData().getSeason().getId());
        bean.setRound(round);
        bean.setCoachId(roundProvider.getBattleDetailData().getCoach().getId());
        bean.setScore("");
        bean.setBottomPlayerId(-1);
        bean.setTopPlayerId(-1);
        battleList.add(bean);
        adapter.setSelection(battleList.size() - 1);
    }

    public void setTopPlayer(PlayerBean topPlayer) {
        if (adapter.hasSelection()) {
            BattleBean bean = battleList.get(adapter.getSelection());
            bean.setTopPlayerId(topPlayer.getId());
            adapter.notifyDataSetChanged();
        }
    }

    public void setBottomPlayer(PlayerBean bottomPlayer) {
        if (adapter.hasSelection()) {
            BattleBean bean = battleList.get(adapter.getSelection());
            bean.setBottomPlayerId(bottomPlayer.getId());
            adapter.notifyDataSetChanged();
        }
    }

    private class ItemAdapter extends BaseAdapter implements View.OnClickListener {

        private List<BattleBean> list;
        private boolean editMode;

        private int selection = -1;

        private ItemAdapter(List<BattleBean> list) {
            this.list = list;
        }

        public int getSelection() {
            return selection;
        }

        public void setSelection(int selection) {
            this.selection = selection;
        }

        public boolean hasSelection() {
            return selection >= 0 && selection < list.size();
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
                holder.group = (ViewGroup) convertView.findViewById(R.id.battle_item_group);
                holder.scene = (TextView) convertView.findViewById(R.id.battle_item_scene);
                holder.tvBottom = (TextView) convertView.findViewById(R.id.battle_item_bottom_name);
                holder.tvTop = (TextView) convertView.findViewById(R.id.battle_item_top_name);
                holder.ivBottom = (ImageView) convertView.findViewById(R.id.battle_item_bottom_image);
                holder.ivTop = (ImageView) convertView.findViewById(R.id.battle_item_top_image);
                holder.score = (EditText) convertView.findViewById(R.id.battle_item_score);
                holder.ivDone = (ImageView) convertView.findViewById(R.id.battle_item_done);
                holder.ivRemove = (ImageView) convertView.findViewById(R.id.battle_item_remove);
            }
            else {
                holder = (ItemHolder) convertView.getTag();
            }

            holder.position = position;
            BattleBean bean = list.get(position);

            // 监听事件
            holder.scene.setTag(holder);
            holder.scene.setOnClickListener(this);
            holder.ivDone.setTag(holder);
            holder.ivDone.setOnClickListener(this);
            holder.ivRemove.setTag(holder);
            holder.ivRemove.setOnClickListener(this);
            holder.group.setTag(holder);
            holder.group.setOnClickListener(this);

            // score
            holder.score.removeTextChangedListener(holder.scoreWatcher);
            holder.score.setText(bean.getScore());
            if (holder.scoreWatcher == null) {
                holder.scoreWatcher = new ScoreWatcher();
            }
            holder.scoreWatcher.setBattlebean(bean);
            holder.score.addTextChangedListener(holder.scoreWatcher);

            // scene
            if (TextUtils.isEmpty(bean.getScene())) {
                holder.scene.setText("Scene");
            }
            else {
                holder.scene.setText(bean.getScene());
            }

            // player image and name
            if (bean.getTopPlayerId() != -1) {
                PlayerBean top = onBattleItemListener.getPlayerBean(bean.getTopPlayerId());
                holder.tvTop.setText(top.getName());
                holder.ivTop.setVisibility(View.VISIBLE);
                SImageLoader.getInstance().displayImage(onBattleItemListener.getPlayerImage(top.getName()), holder.ivTop);
            }
            else {
                holder.tvTop.setText("");
                holder.ivTop.setImageResource(R.drawable.default_cover);
                holder.ivTop.setVisibility(View.INVISIBLE);
            }
            if (bean.getBottomPlayerId() != -1) {
                PlayerBean bottom = onBattleItemListener.getPlayerBean(bean.getBottomPlayerId());
                holder.tvBottom.setText(bottom.getName());
                holder.ivBottom.setVisibility(View.VISIBLE);
                SImageLoader.getInstance().displayImage(onBattleItemListener.getPlayerImage(bottom.getName()), holder.ivBottom);
            }
            else {
                holder.tvBottom.setText("");
                holder.ivBottom.setImageResource(R.drawable.default_cover);
                holder.ivBottom.setVisibility(View.INVISIBLE);
            }

            // 非编辑模式下，score/scene不可修改，修改按钮不显示，group不可被选中且背景颜色正常
            holder.score.setEnabled(false);
            holder.group.setEnabled(false);
            holder.group.setBackgroundColor(parent.getContext().getResources().getColor(R.color.transparent));
            holder.scene.setOnClickListener(null);
            holder.ivDone.setVisibility(View.GONE);
            holder.ivRemove.setVisibility(View.GONE);

            // 编辑模式下，默认显示删除按钮，group可被选中
            if (editMode) {
                holder.ivRemove.setVisibility(View.VISIBLE);
                holder.group.setEnabled(true);
                // 若是选中Item，隐藏删除按钮，显示编辑完成按钮，score/scene允许编辑，group可被选中且背景颜色为选中状态
                if (position == selection) {
                    holder.score.setEnabled(true);
                    holder.scene.setOnClickListener(this);
                    holder.group.setBackgroundColor(Color.rgb(0xdd, 0xdd, 0xdd));
                    holder.ivDone.setVisibility(View.VISIBLE);
                    holder.ivRemove.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        public void setEditMode(boolean editMode) {
            this.editMode = editMode;
            if (!editMode) {
                selection = -1;
            }
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            ItemHolder holder = (ItemHolder) v.getTag();
            BattleBean bean = list.get(holder.position);
            switch (v.getId()) {
                case R.id.battle_item_group:
                    if (editMode) {
                        selection = holder.position;
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.battle_item_scene:
                    selectSceneBean = bean;
                    sceneDialog.show();
                    break;
                case R.id.battle_item_done:
                    bean.setScene(holder.scene.getText().toString());
                    onBattleItemListener.onAddBattleBean(bean);
                    roundProvider.getBattleDetailData().getBattleList().add(bean);
                    selection = -1;
                    cancelEditCard();
                    break;
                case R.id.battle_item_remove:
                    battleList.remove(holder.position);
                    onBattleItemListener.onRemoveBattleBean(bean);
                    roundProvider.getBattleDetailData().getBattleList().remove(bean);
                    cancelEditCard();
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    private class ScoreWatcher implements TextWatcher {

        private BattleBean battlebean;

        public void setBattlebean(BattleBean battlebean) {
            this.battlebean = battlebean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            battlebean.setScore(String.valueOf(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private class ItemHolder {
        ViewGroup group;
        EditText score;
        TextView scene;
        ImageView ivTop;
        ImageView ivBottom;
        TextView tvTop;
        TextView tvBottom;
        ImageView ivRemove;
        ImageView ivDone;
        int position;
        ScoreWatcher scoreWatcher;
    }

}
