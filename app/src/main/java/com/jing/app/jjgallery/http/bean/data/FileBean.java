package com.jing.app.jjgallery.http.bean.data;

public class FileBean {

    private String name;

    private String extra;

    private String path;

    private boolean isFolder;

    /**
     * server端不赋值，client端记录parent
     */
    private FileBean parentBean;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public FileBean getParentBean() {
        return parentBean;
    }

    public void setParentBean(FileBean parentBean) {
        this.parentBean = parentBean;
    }
}
