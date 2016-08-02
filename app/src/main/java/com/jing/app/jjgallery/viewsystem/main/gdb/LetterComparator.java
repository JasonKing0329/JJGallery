package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.king.service.gdb.bean.Star;

import java.util.Comparator;

public class LetterComparator implements Comparator<Star> {

    @Override
    public int compare(Star l, Star r) {
        if (l == null || r == null) {
            return 0;
        }

        return l.getName().toLowerCase().compareTo(r.getName().toLowerCase());
    }
}