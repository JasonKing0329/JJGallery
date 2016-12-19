package com.jing.app.jjgallery.gdb.model;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.model.main.file.FolderManager;

import java.util.List;

/**
 * Created by 景阳 on 2016/12/19.
 */

public class FileModel {

    public List<String> getStarFileList() {
        return new FolderManager().loadPathList(Configuration.GDB_IMG_STAR);
    }

    public List<String> getRecordFileList() {
        return new FolderManager().loadPathList(Configuration.GDB_IMG_RECORD);
    }
}
