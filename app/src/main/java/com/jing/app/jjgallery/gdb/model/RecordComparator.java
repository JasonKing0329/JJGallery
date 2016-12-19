package com.jing.app.jjgallery.gdb.model;

import com.jing.app.jjgallery.config.PreferenceValue;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.Comparator;

/**
 * Created by 景阳 on 2016/12/19.
 * the comparator of record sort
 */

public class RecordComparator implements Comparator<Record> {

    private int sortMode;
    private boolean desc;
    public RecordComparator(int sortMode, boolean desc) {
        this.sortMode = sortMode;
        this.desc = desc;
    }

    @Override
    public int compare(Record lhs, Record rhs) {
        RecordOneVOne left = null;
        RecordOneVOne right = null;
        if (lhs instanceof RecordOneVOne) {
            left = (RecordOneVOne) lhs;
        }
        if (rhs instanceof RecordOneVOne) {
            right = (RecordOneVOne) rhs;
        }

        int result = 0;
        switch (sortMode) {
            case PreferenceValue.GDB_SR_ORDERBY_NAME:// asc
                if (desc) {
                    result = rhs.getName().toLowerCase().compareTo(lhs.getName().toLowerCase());
                }
                else {
                    result = lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_DATE:
                if (desc) {
                    long lr = rhs.getLastModifyTime() - lhs.getLastModifyTime();
                    // 直接把负数long型强制转化为int会有问题，所以还是要通过判断来返回真正的带符号的整型
                    if (lr > 0) {
                        return 1;
                    }
                    else if (lr < 0) {
                        return -1;
                    }
                    else {
                        return 0;
                    }
                }
                else {
                    result = (int) (lhs.getLastModifyTime() - rhs.getLastModifyTime());
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCORE:
                if (desc) {
                    result = rhs.getScore() - lhs.getScore();
                }
                else {
                    result = lhs.getScore() - rhs.getScore();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK:
                if (desc) {
                    result = right.getScoreFk() - left.getScoreFk();
                }
                else {
                    result = left.getScoreFk() - right.getScoreFk();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_CUM:
                if (desc) {
                    result = right.getScoreCum() - left.getScoreCum();
                }
                else {
                    result = left.getScoreCum() - right.getScoreCum();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_BJOB:
                if (desc) {
                    result = right.getScoreBJob() - left.getScoreBJob();
                }
                else {
                    result = left.getScoreBJob() - right.getScoreBJob();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR1:
                if (desc) {
                    result = right.getScoreStar1() - left.getScoreStar1();
                }
                else {
                    result = left.getScoreStar1() - right.getScoreStar1();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR2:
                if (desc) {
                    result = right.getScoreStar2() - left.getScoreStar2();
                }
                else {
                    result = left.getScoreStar2() - right.getScoreStar2();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARCC1:
                if (desc) {
                    result = right.getScoreStarC1() - left.getScoreStarC1();
                }
                else {
                    result = left.getScoreStarC1() - right.getScoreStarC1();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARCC2:
                if (desc) {
                    result = right.getScoreStarC2() - left.getScoreStarC2();
                }
                else {
                    result = left.getScoreStarC2() - right.getScoreStarC2();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_BAREBACK:
                if (desc) {
                    result = right.getScoreNoCond() - left.getScoreNoCond();
                }
                else {
                    result = left.getScoreNoCond() - right.getScoreNoCond();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCOREFEEL:
                if (desc) {
                    result = right.getScoreFeel() - left.getScoreFeel();
                }
                else {
                    result = left.getScoreFeel() - right.getScoreFeel();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STORY:
                if (desc) {
                    result = right.getScoreStory() - left.getScoreStory();
                }
                else {
                    result = left.getScoreStory() - right.getScoreStory();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FOREPLAY:
                if (desc) {
                    result = right.getScoreForePlay() - left.getScoreForePlay();
                }
                else {
                    result = left.getScoreForePlay() - right.getScoreForePlay();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_RIM:
                if (desc) {
                    result = right.getScoreRim() - left.getScoreRim();
                }
                else {
                    result = left.getScoreRim() - right.getScoreRim();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_RHYTHM:
                if (desc) {
                    result = right.getScoreRhythm() - left.getScoreRhythm();
                }
                else {
                    result = left.getScoreRhythm() - right.getScoreRhythm();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCENE:
                if (desc) {
                    result = right.getScoreScene() - left.getScoreScene();
                }
                else {
                    result = left.getScoreScene() - right.getScoreScene();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_CSHOW:
                if (desc) {
                    result = right.getScoreCShow() - left.getScoreCShow();
                }
                else {
                    result = left.getScoreCShow() - right.getScoreCShow();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SPECIAL:
                if (desc) {
                    result = right.getScoreSpeicial() - left.getScoreSpeicial();
                }
                else {
                    result = left.getScoreSpeicial() - right.getScoreSpeicial();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_HD:
                if (desc) {
                    result = rhs.getHDLevel() - lhs.getHDLevel();
                }
                else {
                    result = lhs.getHDLevel() - rhs.getHDLevel();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK1:
                if (desc) {
                    result = right.getScoreFkType1() - left.getScoreFkType1();
                }
                else {
                    result = left.getScoreFkType1() - right.getScoreFkType1();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK2:
                if (desc) {
                    result = right.getScoreFkType2() - left.getScoreFkType2();
                }
                else {
                    result = left.getScoreFkType2() - right.getScoreFkType2();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK3:
                if (desc) {
                    result = right.getScoreFkType3() - left.getScoreFkType3();
                }
                else {
                    result = left.getScoreFkType3() - right.getScoreFkType3();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK4:
                if (desc) {
                    result = right.getScoreFkType4() - left.getScoreFkType4();
                }
                else {
                    result = left.getScoreFkType4() - right.getScoreFkType4();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK5:
                if (desc) {
                    result = right.getScoreFkType5() - left.getScoreFkType5();
                }
                else {
                    result = left.getScoreFkType5() - right.getScoreFkType5();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK6:
                if (desc) {
                    result = right.getScoreFkType6() - left.getScoreFkType6();
                }
                else {
                    result = left.getScoreFkType6() - right.getScoreFkType6();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCORE_BASIC:
                if (desc) {
                    result = right.getScoreBasic() - left.getScoreBasic();
                }
                else {
                    result = left.getScoreBasic() - right.getScoreBasic();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCORE_EXTRA:
                if (desc) {
                    result = right.getScoreExtra() - left.getScoreExtra();
                }
                else {
                    result = left.getScoreExtra() - right.getScoreExtra();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR:
                if (desc) {
                    result = right.getScoreStar() - left.getScoreStar();
                }
                else {
                    result = left.getScoreStar() - right.getScoreStar();
                }
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARC:
                if (desc) {
                    result = right.getScoreStarC() - left.getScoreStarC();
                }
                else {
                    result = left.getScoreStarC() - right.getScoreStarC();
                }
                break;
        }
        return result;
    }
}
