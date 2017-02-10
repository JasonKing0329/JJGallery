package com.jing.app.jjgallery.gdb.presenter.game;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.game.GameProvider;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class GamePresenter {

    protected GameProvider gameProvider;
    protected GDBProvider gdbProvider;

    /**
     * 由于player总是处于动态更新图片的，所以通过缓存全局player的方式获取图片地址
     */
    private Map<String, String> starImageMap;

    public GamePresenter() {
        gameProvider = new GameProvider(DBInfor.GDB_GAME_DB_PATH);
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public List<SeasonBean> getSeasonList() {
        return gameProvider.getSeasonList();
    }

    public SeasonBean getSeasonById(int id) {
        return gameProvider.getSeasonById(id);
    }

    public boolean saveSeason(SeasonBean seasonBean) {
        return gameProvider.updateSeason(seasonBean);
    }

    public List<CoachBean> getCoachList() {
        return gameProvider.getCoachList();
    }

    public CoachBean getCoachById(int id) {
        return gameProvider.getCoachById(id);
    }

    public void saveCoach(CoachBean coachBean) {
        gameProvider.updateCoach(coachBean);
    }

    protected void loadPlayerImageMap() {
        starImageMap = new HashMap<>();
        List<String> pathList = new FolderManager().loadPathList(Configuration.GDB_IMG_STAR);
        Encrypter encrypter = EncrypterFactory.create();
        for (String path:pathList) {
            String name = encrypter.decipherOriginName(new File(path));
            String preName = name.substring(0, name.lastIndexOf("."));
            starImageMap.put(preName, path);
        }
    }

    public String getStarImage(String starName) {
        return starImageMap.get(starName);
    }

    public void deleteSeason(SeasonBean bean) {
        gameProvider.deleteSeason(bean.getId());
    }

    public Star queryStarByName(String name) {
        return gdbProvider.queryStarByName(name);
    }

}
