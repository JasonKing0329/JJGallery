package com.jing.app.jjgallery.gdb.presenter.game;

import android.text.TextUtils;

import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/15 0015.
 * 保存coach对应的season列表，在CoachActivity的作用区间始终有效
 */

public class CoachSeasonManager {

    private List<SeasonBean> seasonList;
    private Map<Integer, List<SeasonBean>> coachSeasonMap;

    public CoachSeasonManager() {
        coachSeasonMap = new HashMap<>();
    }

    public void setSeasonList(List<SeasonBean> list) {
        seasonList = list;
    }

    /**
     * 第一次进行加载，此后从缓存里取
     * @param coachBean
     * @return
     */
    public List<SeasonBean> getCoachSeasonList(CoachBean coachBean) {
        List<SeasonBean> list = coachSeasonMap.get(coachBean.getId());
        if (list == null) {
            list = getCoachSeasons(coachBean);
        }
        return list;
    }

    /**
     * _seasonIds字段以“,”分隔开所有的season id
     * @param coachBean
     * @return
     */
    private List<SeasonBean> getCoachSeasons(CoachBean coachBean) {
        List<SeasonBean> list = new ArrayList<>();
        if (seasonList != null) {
            String seasons = coachBean.getSeasonIds();
            if (!TextUtils.isEmpty(seasons)) {
                String[] ids = seasons.split(",");
                for (int i = 0; i < seasonList.size(); i ++) {
                    for (int j = 0; j < ids.length; j ++) {
                        if (ids[j].equals(String.valueOf(seasonList.get(i).getId()))) {
                            list.add(seasonList.get(i));
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

}
