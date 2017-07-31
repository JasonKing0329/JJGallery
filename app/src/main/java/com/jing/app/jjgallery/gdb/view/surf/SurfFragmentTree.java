package com.jing.app.jjgallery.gdb.view.surf;

/**
 * 描述: surf模式符合 parent-current-child 结构
 * 每一个节点最多仅有一个parent和child
 * level用于标记当前的树层次
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/31 13:50
 */
public class SurfFragmentTree {
    public SurfFragmentTree parent;
    public SurfFragmentTree child;
    public SurfFragment fragment;
    public int level;
}
