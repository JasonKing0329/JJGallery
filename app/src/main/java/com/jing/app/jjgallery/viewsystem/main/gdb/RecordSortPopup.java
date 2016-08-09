package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceValue;

/**
 * Created by JingYang on 2016/8/9 0009.
 * Description:
 */
public class RecordSortPopup {

    public interface SortCallback {
        void onSortModeSelected(int sortMode, boolean refresh);
    }

    private SortCallback mCallback;

    public void showSortPopup(Context context, View anchor, SortCallback callback) {
        mCallback = callback;
        PopupMenu menu = new PopupMenu(context, anchor);
        menu.getMenuInflater().inflate(R.menu.sort_gdb_record_list, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(sortListener);
    }

    PopupMenu.OnMenuItemClickListener sortListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int sortMode;
            boolean refresh = true;
            switch (item.getItemId()) {
                case R.id.menu_by_date:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_DATE;
                    break;
                case R.id.menu_by_name:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_NAME;
                    break;
                case R.id.menu_by_score:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_SCORE;
                    break;
                case R.id.menu_by_fk:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_FK;
                    break;
                case R.id.menu_by_cum:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_CUM;
                    break;
                case R.id.menu_by_bjob:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_BJOB;
                    break;
                case R.id.menu_by_star1:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STAR1;
                    break;
                case R.id.menu_by_star2:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STAR2;
                    break;
                case R.id.menu_by_starcc1:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STARCC1;
                    break;
                case R.id.menu_by_starcc2:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STARCC2;
                    break;

                case R.id.menu_by_none:
                default:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_NONE;
                    refresh = false;
                    break;
            }

            mCallback.onSortModeSelected(sortMode, refresh);
            return true;
        }
    };
}
