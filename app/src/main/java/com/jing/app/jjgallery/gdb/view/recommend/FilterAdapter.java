package com.jing.app.jjgallery.gdb.view.recommend;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterBean;
import com.jing.app.jjgallery.util.DebugLog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {

    private List<FilterBean> mList;

    public FilterAdapter(List<FilterBean> mList) {
        this.mList = mList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindModel(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FilterBean filterBean;
        ViewGroup group;
        CheckBox check;
        EditText etMin;
        EditText etMax;
        ViewGroup groupInput;

        public Holder(View itemView) {
            super(itemView);
            check = (CheckBox) itemView.findViewById(R.id.filter_check);
            etMin = (EditText) itemView.findViewById(R.id.filter_input_min);
            etMax = (EditText) itemView.findViewById(R.id.filter_input_max);
            groupInput = (ViewGroup) itemView.findViewById(R.id.filter_input_group);
            group = (ViewGroup) itemView.findViewById(R.id.filter_group);
            group.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // group的click事件代替checkbox的check事件
            boolean checked = check.isChecked();
            checked = !checked;
            check.setChecked(checked);

            filterBean.setEnable(checked);
            // check了显示输入框
            groupInput.setVisibility(checked ? View.VISIBLE:View.INVISIBLE);
            // 如果uncheck了，要清零条件
            if (!checked) {
                filterBean.setMax(0);
                filterBean.setMin(0);
                etMax.setText("");
                etMin.setText("");
            }
        }

        public void bindModel(FilterBean filterBean) {
            this.filterBean = filterBean;
            check.setText(filterBean.getKeyword());
            check.setChecked(filterBean.isEnable());

            groupInput.setVisibility(filterBean.isEnable() ? View.VISIBLE:View.INVISIBLE);

            etMin.removeTextChangedListener(minWatcher);
            if (filterBean.getMin() > 0) {
                etMin.setText(String.valueOf(filterBean.getMin()));
            }
            else {
                etMin.setText("");
            }
            etMin.addTextChangedListener(minWatcher);

            etMax.removeTextChangedListener(maxWatcher);
            if (filterBean.getMax() > 0) {
                etMax.setText(String.valueOf(filterBean.getMax()));
            }
            else {
                etMax.setText("");
            }
            etMax.addTextChangedListener(maxWatcher);
        }
        TextWatcher minWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                DebugLog.e(s.toString());
                if (TextUtils.isEmpty(s.toString())) {
                    filterBean.setMin(0);
                }
                else {
                    filterBean.setMin(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher maxWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    filterBean.setMax(0);
                }
                else {
                    filterBean.setMax(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

}
