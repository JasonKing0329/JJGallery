package com.jing.app.jjgallery.config;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class PreferenceKey {

    /**
     * configuration
     */
    // 是否第一次登录APP
    public static final String PREF_APP_INITED = "pref_app_inited";
    public static final String PREF_VERSION = "pref_version";

    /**
     * public
     */
    // circle progress background
    public static final String PREF_BG_PROGRESS = "pref_bg_progress";

    /**
     * setting
     */
    // 主界面
    public static final String PREF_HOME_VIEW = "pref_general_list_home";
    // 启用指纹验证
    public static final String PREF_SAFETY_FP = "pref_safety_fingerprint";
    // 文件管理器默认打开视图
    public static final String PREF_FM_VIEW = "pref_filemanager_view";
    // 文件管理器--显示原文件名
    public static final String PREF_FM_ORIGIN = "pref_filemanager_origin";
    // SOrder默认打开视图
    public static final String PREF_SORDER_VIEW = "pref_sorder_view";
    // TimeLine默认打开视图
    public static final String PREF_TIMELINE_VIEW = "pref_timeline_view";
    // 随便看看数量
    public static final String PREF_CASUAL_NUMBER = "pref_casual_number";
    // book翻页模式
    public static final String PREF_BOOK_SWITCH = "pref_book_switch_mode";

    /**
     * home
     */
    // 左侧菜单竖屏背景
    public static final String PREF_SLIDING_BK_LEFT = "pref_sliding_bk_left";
    // 右侧菜单竖屏背景
    public static final String PREF_SLIDING_BK_RIGHT = "pref_sliding_bk_right";
    // 左侧菜单横屏背景
    public static final String PREF_SLIDING_BK_LEFT_LAND = "pref_sliding_bk_left_land";
    // 右侧菜单横屏背景
    public static final String PREF_SLIDING_BK_RIGHT_LAND = "pref_sliding_bk_right_land";
    // 左侧菜单circle imageview
    public static final String PREF_SLIDING_CIRCLE = "pref_sliding_circle";

    /**
     * file manager
     */
    // 文件管理器Index page竖屏背景
    public static final String PREF_BG_FM_INDEX = "pref_bg_fm_index";
    // 文件管理器Index page横屏背景
    public static final String PREF_BG_FM_INDEX_LAND = "pref_bg_fm_index_land";
    // 点击index page item打开
    public static final String PREF_FM_INDEX_ITEM_OPEN = "pref_fm_index_item_open";

    /**
     * sorder
     */
    // SOrder Index page竖屏背景
    public static final String PREF_BG_SORDER_INDEX = "pref_bg_sorder_index";
    // SOrder Index page横屏背景
    public static final String PREF_BG_SORDER_INDEX_LAND = "pref_bg_sorder_index_land";
    // Grid page默认排序类型
    public static final String PREF_SORDER_GRID_ORDER = "pref_sorder_grid_order";
    // 翻页模式每页数量
    public static final String PREF_SORDER_PAGE_MODE = "pref_sorder_page_mode";
    // 翻页模式每页数量
    public static final String PREF_SORDER_PAGE_NUM = "pref_sorder_page_number";
    // 封面类型
    public static final String PREF_SORDER_COVER_MODE = "pref_sorder_cover_mode";
    // 层叠封面张数
    public static final String PREF_SORDER_CASCADE_NUM = "pref_sorder_cascade_number";
    // 点击grid page item打开
    public static final String PREF_SORDER_GRID_ITEM_OPEN = "pref_sorder_grid_item_open";
    // 点击index page item打开
    public static final String PREF_SORDER_INDEX_ITEM_OPEN = "pref_sorder_index_item_open";
    // 访问量页面展示的top个数
    public static final String PREF_SORDER_CARD_TOP_NUMBER = "pref_sorder_card_top_number";

    /**
     * surf
     */
    // Surf自动播放模式
    public static final String PREF_SURF_PLAY_MODE = "setting_auto_play_mode";
    // Surf自动播放切换速度
    public static final String PREF_SURF_PLAY_SPEED = "setting_auto_play_speed";

    /**
     * waterfall
     */
    // 瀑布流竖屏列数
    public static final String PREF_WATERFALL_COL = "pref_waterfall_column";
    // 瀑布流竖屏列数
    public static final String PREF_WATERFALL_COL_LAND = "pref_waterfall_land_column";

    /**
     * gdb
     */
    // gdb record list排序方式
    public static final String PREF_GDB_RECORD_ORDER = "pref_gdb_record_order";
    // gdb star record排序方式
    public static final String PREF_GDB_STAR_RECORD_ORDER = "pref_gdb_star_record_order";

    /**
     * http
     */
    // server url
    public static final String PREF_HTTP_SERVER = "pref_http_server";
    // 最大并发下载数量
    public static final String PREF_MAX_DOWNLOAD = "pref_http_download_max";
    // 检查更新
    public static final String PREF_CHECK_UPDATE = "pref_http_update";
}
