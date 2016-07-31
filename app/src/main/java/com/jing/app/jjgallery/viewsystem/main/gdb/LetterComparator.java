package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.king.service.gdb.bean.Star;

import java.util.Comparator;

public class LetterComparator implements Comparator<Star> {

    @Override
    public int compare(Star l, Star r) {
        if (l == null || r == null) {
            return 0;
        }

        String lhsSortLetters = l.getName().substring(0, 1).toUpperCase();
        String rhsSortLetters = r.getName().substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}