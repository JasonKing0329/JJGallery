package com.jing.app.jjgallery.gdb.view.record;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.util.DensityUtil;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JingYang on 2016/8/23 0023.
 * Description:
 */
public class SortDialog extends CustomDialog implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private RadioButton ascButton, descButton;
    private CheckBox cbDeprecated;
    private int textPadding;
    private int focusColor;

    private ItemAdapter itemAdapter;

    private SortItem[] items = new SortItem[] {
            new SortItem("None", PreferenceValue.GDB_SR_ORDERBY_NONE)
            , new SortItem("Name", PreferenceValue.GDB_SR_ORDERBY_NAME)
            , new SortItem("Date", PreferenceValue.GDB_SR_ORDERBY_DATE)
            , new SortItem("Score", PreferenceValue.GDB_SR_ORDERBY_SCORE)
            , new SortItem("FK", PreferenceValue.GDB_SR_ORDERBY_FK)
            , new SortItem("Cum", PreferenceValue.GDB_SR_ORDERBY_CUM)
            , new SortItem("Star1", PreferenceValue.GDB_SR_ORDERBY_STAR1)
            , new SortItem("StarC1", PreferenceValue.GDB_SR_ORDERBY_STARCC1)
            , new SortItem("Bjob", PreferenceValue.GDB_SR_ORDERBY_BJOB)
            , new SortItem("Star2", PreferenceValue.GDB_SR_ORDERBY_STAR2)
            , new SortItem("StarC2", PreferenceValue.GDB_SR_ORDERBY_STARCC2)
            , new SortItem("Bareback", PreferenceValue.GDB_SR_ORDERBY_BAREBACK)
            , new SortItem("ScoreFeel", PreferenceValue.GDB_SR_ORDERBY_SCOREFEEL)
            , new SortItem("Story", PreferenceValue.GDB_SR_ORDERBY_STORY)
            , new SortItem("Foreplay", PreferenceValue.GDB_SR_ORDERBY_FOREPLAY)
            , new SortItem("Rim", PreferenceValue.GDB_SR_ORDERBY_RIM)
            , new SortItem("Rhythm", PreferenceValue.GDB_SR_ORDERBY_RHYTHM)
            , new SortItem("Scene", PreferenceValue.GDB_SR_ORDERBY_SCENE)
            , new SortItem("CShow", PreferenceValue.GDB_SR_ORDERBY_CSHOW)
            , new SortItem("Special", PreferenceValue.GDB_SR_ORDERBY_SPECIAL)
            , new SortItem("HD Level", PreferenceValue.GDB_SR_ORDERBY_HD)
            , new SortItem("FK-sit face", PreferenceValue.GDB_SR_ORDERBY_FK1)
            , new SortItem("FK-sit back", PreferenceValue.GDB_SR_ORDERBY_FK2)
            , new SortItem("FK-stand face", PreferenceValue.GDB_SR_ORDERBY_FK3)
            , new SortItem("FK-stand back", PreferenceValue.GDB_SR_ORDERBY_FK4)
            , new SortItem("FK-side", PreferenceValue.GDB_SR_ORDERBY_FK5)
            , new SortItem("FK-special", PreferenceValue.GDB_SR_ORDERBY_FK6)
            , new SortItem("ScoreBasic", PreferenceValue.GDB_SR_ORDERBY_SCORE_BASIC)
            , new SortItem("ScoreExtra", PreferenceValue.GDB_SR_ORDERBY_SCORE_EXTRA)
            , new SortItem("Star", PreferenceValue.GDB_SR_ORDERBY_STAR)
            , new SortItem("StarC", PreferenceValue.GDB_SR_ORDERBY_STARC)
    };

    public SortDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        applyLightThemeStyle();
        setTitle(R.string.gdb_sort_title);
        textPadding = DensityUtil.dip2px(getContext(), 20);
        focusColor = context.getResources().getColor(R.color.actionbar_bk_orange);
        requestOkAction(true);
        requestCancelAction(true);

        initData();
    }

    private void initData() {
        itemAdapter = new ItemAdapter();

        HashMap<String, Object> data = new HashMap<>();
        actionListener.onLoadData(data);
        // 初始化升序/降序
        boolean desc = (boolean) data.get("desc");
        if (!desc) {
            ascButton.setChecked(true);
        }

        // 初始化当前排序类型
        int sortMode = (int) data.get("sortMode");
        for (int i = 0; i < items.length; i ++) {
            if (sortMode == items[i].value) {
                itemAdapter.setSelection(i);
                break;
            }
        }

        gridView.setAdapter(itemAdapter);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_gdb_sort, null);
        gridView = (GridView) view.findViewById(R.id.gdb_sort_grid);
        ascButton = (RadioButton) view.findViewById(R.id.gdb_sort_asc);
        descButton = (RadioButton) view.findViewById(R.id.gdb_sort_desc);
        cbDeprecated = (CheckBox) view.findViewById(R.id.cb_deprecated);
        gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemAdapter.setSelection(position);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == saveIcon) {
            Map<String, Object> map = new HashMap<>();
            map.put("desc", descButton.isChecked());
            map.put("sortMode", items[itemAdapter.getSelectedIndex()].value);
            map.put("include_deprecated", cbDeprecated.isChecked());
            actionListener.onSave(map);
        }
        super.onClick(view);
    }

    private class SortItem {
        String name;
        int value;
        public SortItem(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private class ItemAdapter extends BaseAdapter {

        private int selectedIndex;

        public void setSelection(int position) {
            selectedIndex = position;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getContext());
            textView.setText(items[position].name);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, textPadding, 0, textPadding);
            if (position == selectedIndex) {
                textView.setBackgroundColor(focusColor);
            }
            else {
                textView.setBackground(null);
            }
            return textView;
        }
    }
}
