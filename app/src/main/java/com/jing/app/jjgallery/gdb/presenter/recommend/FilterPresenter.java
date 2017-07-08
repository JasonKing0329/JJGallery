package com.jing.app.jjgallery.gdb.presenter.recommend;

import android.content.Context;

import com.google.gson.Gson;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterBean;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.gdb.GdbConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/3 0003.
 * the filter handle process
 */

public class FilterPresenter {

    public FilterModel getFilters(Context context) {
        String json = SettingProperties.getGdbFilterModel(context);
        DebugLog.e(json);
        Gson gson = new Gson();
        try {
            FilterModel modle = gson.fromJson(json, FilterModel.class);
            if (modle == null) {
                return createFilters(context);
            }
            return modle;
        } catch (Exception e) {
            e.printStackTrace();
            return createFilters(context);
        }
    }
    public void saveFilters(Context context, FilterModel modle) {
        Gson gson = new Gson();
        String data = gson.toJson(modle);
        DebugLog.e(data);
        SettingProperties.saveGdbFilterModel(context, data);
    }
    private FilterModel createFilters(Context context) {
        String[] keys = new String[] {
                GdbConstants.FILTER_KEY_SCORE, GdbConstants.FILTER_KEY_SCORE_FEEL,
                GdbConstants.FILTER_KEY_SCORE_BASIC, GdbConstants.FILTER_KEY_SCORE_EXTRA,
                GdbConstants.FILTER_KEY_SCORE_CUM, GdbConstants.FILTER_KEY_SCORE_FK,
                GdbConstants.FILTER_KEY_SCORE_STAR1, GdbConstants.FILTER_KEY_SCORE_STAR2,
                GdbConstants.FILTER_KEY_SCORE_STAR,
                GdbConstants.FILTER_KEY_SCORE_BJOB, GdbConstants.FILTER_KEY_SCORE_BAREBACK,
                GdbConstants.FILTER_KEY_SCORE_STORY, GdbConstants.FILTER_KEY_SCORE_RHYTHM,
                GdbConstants.FILTER_KEY_SCORE_SCECE, GdbConstants.FILTER_KEY_SCORE_RIM,
                GdbConstants.FILTER_KEY_SCORE_STARCC1, GdbConstants.FILTER_KEY_SCORE_STARCC2,
                GdbConstants.FILTER_KEY_SCORE_CSHOW, GdbConstants.FILTER_KEY_SCORE_SPECIAL,
                GdbConstants.FILTER_KEY_SCORE_FOREPLAY, GdbConstants.FILTER_KEY_SCORE_FK_SIT_FACE,
                GdbConstants.FILTER_KEY_SCORE_FK_SIT_BACK, GdbConstants.FILTER_KEY_SCORE_FK_STAND_FACE,
                GdbConstants.FILTER_KEY_SCORE_FK_STAND_BACK, GdbConstants.FILTER_KEY_SCORE_FK_SIDE,
                GdbConstants.FILTER_KEY_SCORE_FK_SPECIAL, GdbConstants.FILTER_KEY_SCORE_DEPRECATED
        };
        List<FilterBean> list = new ArrayList<>();
        FilterModel modle = new FilterModel();
        modle.setSupportNR(false);
        modle.setList(list);
        for (int i = 0; i < keys.length; i ++) {
            FilterBean bean = new FilterBean();
            bean.setKeyword(keys[i]);
            list.add(bean);
        }
        saveFilters(context, modle);
        return modle;
    }
}
