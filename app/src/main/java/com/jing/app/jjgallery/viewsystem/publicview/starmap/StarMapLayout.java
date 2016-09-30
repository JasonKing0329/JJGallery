package com.jing.app.jjgallery.viewsystem.publicview.starmap;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.util.DensityUtil;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/22.
 * 随机位置思路
 * 1. 将layout的实际区域（除开padding）等分成大小一样的区域
 * 等分规则：
 * 每一个区域的大小是maxItemSize(item's max size) + SPACE_FREE(限制偏移随机距离)
 * 按照这个大小分为row行，col列
 * 2. 将item随机分布在各个区域中，为提高item的位置自由度，让item在自己的区域内可随机偏移SPACE_FREE的距离
 * 3. 为进一步提高位置的自由度，可以遍历整个星图，随机对占有区域进行如下操作：
 *   （1）检测该区域上下左右四个方向的“墙壁”（比如从top方向遍历，当碰到第一个同样为占有区域或者是边界的区域，称之为“墙壁”）
 *   （2）计算当前区域与“墙壁”在top方向的距离，然后随机一个距离范围内的值让当前区域向“墙壁”靠近
 * 4. 可以对第3步做1-n次的循环操作，来进一步修正item的位置
 */
public class StarMapLayout extends FrameLayout implements StarMapObserver, ViewTreeObserver.OnGlobalLayoutListener
    , View.OnClickListener{

    public interface OnItemClickListener {
        void onItemClick(View view, Keyword keyword);
    }

    public class AdapterNotSetException extends RuntimeException {

    }

    private final boolean DEBUG = true;
    private StarMapBaseAdapter mAdapter;

    private class Region{
        Point point;
        View view;
        LayoutParams params;
        int viewPosition;
        int imageSize;
    }
    public static final int IDX_X = 0;
    public static final int IDX_Y = 1;
    public static final int IDX_TXT_LENGTH = 2;
    public static final int IDX_DIS_Y = 3;
    /** 由外至内的动画。 */
    public static final int ANIMATION_IN = 1;
    /** 由内至外的动画。 */
    public static final int ANIMATION_OUT = 2;
    /** 位移动画类型：从外围移动到坐标点。 */
    public static final int OUTSIDE_TO_LOCATION = 1;
    /** 位移动画类型：从坐标点移动到外围。 */
    public static final int LOCATION_TO_OUTSIDE = 2;
    /** 位移动画类型：从中心点移动到坐标点。 */
    public static final int CENTER_TO_LOCATION = 3;
    /** 位移动画类型：从坐标点移动到中心点。 */
    public static final int LOCATION_TO_CENTER = 4;
    public static final long ANIM_DURATION = 800l;
    private static Interpolator interpolator;
    private static AlphaAnimation animAlpha2Opaque;
    private static AlphaAnimation animAlpha2Transparent;
    private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large, animScaleZero2Normal,
            animScaleNormal2Zero;

    /**
     * goToShow()中被赋值为true，标识开发人员触发其开始动画显示。<br/>
     * 本标识的作用是防止在填充keywrods未完成的过程中获取到width和height后提前启动动画。<br/>
     * 在show()方法中其被赋值为false。<br/>
     * 真正能够动画显示的另一必要条件：width 和 height不为0。<br/>
     */
    private boolean enableShow;
    /**
     * see ANIMATION_IN
     * see ANIMATION_OUT
     * see OUTSIDE_TO_LOCATION
     * see LOCATION_TO_OUTSIDE
     * see LOCATION_TO_CENTER
     * see CENTER_TO_LOCATION
     * */
    private int txtAnimInType, txtAnimOutType;
    /** 最近一次启动动画显示的时间。 */
    private long lastStartAnimationTime;
    /** 动画运行时间。 */
    private long animDuration;

    /**
     * 允许的最大item个数，需要根据行列数来动态控制，最大不超过MAX_ITEM的2/3
     */
    private int MAXITEM = 15;
    /**
     * item的随机偏移空间
     */
    private int SPACE_FREE;
    /**
     * item中图片的最大大小
     */
    private int maxItemSize;
    /**
     * item中图片的最小大小
     */
    private int minItemSize;
    /**
     * 星图中每一个区域的大小
     */
    private int pointWidth, pointHeight;

    /**
     * 星图的位置坐标
     */
    private Region[][] mapRegions;

    /**
     * 星图的行列数
     */
    private int row, col;

    /**
     * layout的总大小，包括padding
     */
    private int width, height;

    private Random random;

    /**
     * item的单击事件
     */
    private OnItemClickListener onItemClickListener;

    public StarMapLayout(Context context) {
        super(context);
        init();
    }

    public StarMapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lastStartAnimationTime = 0l;
        animDuration = ANIM_DURATION;
        random = new Random();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        interpolator = AnimationUtils.loadInterpolator(getContext(), android.R.anim.decelerate_interpolator);
        animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);
        animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);
        animScaleLarge2Normal = new ScaleAnimation(2, 1, 2, 1);
        animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);
        animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);
        animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);

        random = new Random();
        maxItemSize = getContext().getResources().getDimensionPixelSize(R.dimen.starmap_max_item_size);
        minItemSize = getContext().getResources().getDimensionPixelSize(R.dimen.starmap_min_item_size);
        if (DisplayHelper.isTabModel(getContext())) {
            SPACE_FREE = DensityUtil.dip2px(getContext(), 30);
        }
        else {
            SPACE_FREE = DensityUtil.dip2px(getContext(), 20);
        }

    }

    @Override
    public void onGlobalLayout() {

    }

    public void setAdapter(StarMapBaseAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setObserver(this);

        width = getWidth();
        height = getHeight();
        // 构造星图区域
        initMapPoints();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 获取星图中item的最大个数，在setAdapter之后调用
     * MAXITEM是根据行列数来动态控制的
     * 而行列数是取决于adapter中定义的布局大小
     * @return 不能超过最大item个数的2/3
     */
    public int getMaxItem() throws AdapterNotSetException {
        if (mAdapter == null) {
            throw new AdapterNotSetException();
        }
        return MAXITEM * 2 / 3;
    }

    /**
     * 构造星图区域
     */
    private void initMapPoints() {
        pointWidth = maxItemSize + SPACE_FREE;
        int realWidth = (getWidth() - getPaddingLeft() - getPaddingRight());
        int realHeight = (getHeight() - getPaddingTop() - getPaddingBottom());
        pointHeight = pointWidth + mAdapter.getTextHeight();
        // 确认星图的行列数
        col = realWidth / pointWidth;
        row = realHeight / pointHeight;
        MAXITEM = row * col;
        // 余下空白的地方补作padding
        int xPadding = (realWidth - col * pointWidth) / 2;
        int yPadding = (realHeight - row * pointHeight) / 2;

        // 构造星图中的区域
        mapRegions = new Region[row][];

        if (DEBUG) {
            DebugLog.d("realWidth=" + realWidth + ", realHeight=" + realHeight);
            DebugLog.d("row=" + row + ", col=" + col);
            DebugLog.d("xPadding=" + xPadding + ", yPadding=" + yPadding);
            DebugLog.d("pointWidth=" + pointWidth);
            DebugLog.d("pointHeight=" + pointHeight);
        }

        for (int i = 0; i < row; i ++) {
            mapRegions[i] = new Region[col];
            for (int j = 0; j < col; j ++) {
                mapRegions[i][j] = new Region();
                mapRegions[i][j].point = new Point();
                mapRegions[i][j].point.set(xPadding + j * pointWidth, yPadding + i * pointHeight);
                if (DEBUG) {
                    DebugLog.d("[" + i + "][" + j + "], " + mapRegions[i][j].point.x + ", " + mapRegions[i][j].point.y);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            int position = (int) v.getTag();
            onItemClickListener.onItemClick(v, mAdapter.getKeyword(position));
        }
    }

    @Override
    public void onDataSetChanged() {
        // 不用onDataSetChanged来刷新，主要是涉及到动画的切换问题，用外部的goToShow来主动刷新
    }

    private void refreshView() {
        // 每次执行的时候要清空之前的view和params
        removeAllViews();
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j++) {
                mapRegions[i][j].view = null;
                mapRegions[i][j].params = null;
                mapRegions[i][j].viewPosition = 0;
                mapRegions[i][j].imageSize = 0;
            }
        }

        int count = mAdapter.getCount();
        // 确定随机区域
        List<Integer> positions = new ArrayList<>();
        for (int n = 0; n < row * col; n ++) {
            positions.add(n);
        }
        Collections.shuffle(positions);

        // 将item部署到前count个对应的区域内
        for (int i = 0; i < count; i ++) {
            View child = mAdapter.createView(i, null);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            arrangeChildView(i, child, params, positions.get(i));
        }

        // 第一次修正view的位置
        modifyChildPosition();

        // 添加view，并执行动画
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j++) {
                if (mapRegions[i][j].view != null) {
                    // 添加为child
                    addView(mapRegions[i][j].view, mapRegions[i][j].params);
                    // 给item绑定数据
                    mAdapter.bindViewData(mapRegions[i][j].viewPosition, mapRegions[i][j].view, mapRegions[i][j].imageSize);
                    // 注册listener
                    mapRegions[i][j].view.setTag(mapRegions[i][j].viewPosition);
                    mapRegions[i][j].view.setOnClickListener(this);

                    // 动画
                    int[] iXY = new int[] { mapRegions[i][j].params.leftMargin, mapRegions[i][j].params.topMargin, mapRegions[i][j].params.width };
                    AnimationSet animSet = getAnimationSet(iXY, width >> 1, width >> 1, txtAnimInType);
                    mapRegions[i][j].view.startAnimation(animSet);
                }
            }
        }
    }

    /**
     * 设置item第一次的随机区域
     * @param position
     * @param child child item
     * @param params 为child item设置layout params
     * @param targetPosition 在星图阵列中的序列
     */
    private void arrangeChildView(int position, View child, LayoutParams params, int targetPosition) {
        int x = targetPosition / col;
        int y = targetPosition % col;
//        child.setTag(R.id.starmap_child_tag_region, mapRegions[x][y]);
        mapRegions[x][y].view = child;
        mapRegions[x][y].params = params;
        mapRegions[x][y].viewPosition = position;

        // 随机大小
        int size = minItemSize + Math.abs(random.nextInt()) % (maxItemSize - minItemSize);
        params.width = size;
        params.height = size + mAdapter.getTextHeight();
        mapRegions[x][y].imageSize = size;

        // 偏移位置
        int xOffset = random.nextInt(SPACE_FREE / 2);
        if (random.nextInt(2) == 1) {
            xOffset = -xOffset;
        }
        int yOffset = random.nextInt(SPACE_FREE / 2);
        if (random.nextInt(2) == 1) {
            yOffset = -yOffset;
        }
        params.leftMargin = mapRegions[x][y].point.x + xOffset;
        params.topMargin = mapRegions[x][y].point.y + yOffset;
        if (DEBUG) {
            DebugLog.d("x=" + x + ", y=" + y);
            DebugLog.d("size=" + size);
            DebugLog.d("xOffset=" + xOffset + ", yOffset=" + yOffset);
            DebugLog.d("leftMargin=" + params.leftMargin + ", topMargin=" + params.topMargin);
        }
    }

    private void modifyChildPosition() {
        //TODO v2.9.1 这一期先不做
    }

    /**
     * 开始动画显示。<br/>
     * 之前已经存在的TextView将会显示退出动画。<br/>
     *
     * @return 正常显示动画返回true；反之为false。返回false原因如下：<br/>
     *         1.时间上不允许，受lastStartAnimationTime的制约；<br/>
     *         2.未获取到width和height的值。<br/>
     */
    @Override
    public void goToShow(int animType) {
        if (System.currentTimeMillis() - lastStartAnimationTime > animDuration) {
            enableShow = true;
            if (animType == ANIMATION_IN) {
                txtAnimInType = OUTSIDE_TO_LOCATION;
                txtAnimOutType = LOCATION_TO_CENTER;
            } else if (animType == ANIMATION_OUT) {
                txtAnimInType = CENTER_TO_LOCATION;
                txtAnimOutType = LOCATION_TO_OUTSIDE;
            }
            disapper();

            refreshView();
        }
    }

    public void rubKeywords() {
        mAdapter.clear();
    }

    public void reset() {
        mAdapter = null;
        mapRegions = null;
        MAXITEM = 15;
        row = 0;
        col = 0;
        width = 0;
        height = 0;
        pointWidth = 0;
        pointHeight = 0;
    }

    private void disapper() {
        int size = getChildCount();
        for (int i = size - 1; i >= 0; i--) {
            final View view = getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                removeView(view);
                continue;
            }
            LayoutParams layParams = (LayoutParams) view.getLayoutParams();
            // Log.d("ANDROID_LAB", txt.getText() + " leftM=" +
            // layParams.leftMargin + " topM=" + layParams.topMargin
            // + " width=" + txt.getWidth());
            int[] xy = new int[] { layParams.leftMargin, layParams.topMargin, layParams.width };
            AnimationSet animSet = getAnimationSet(xy, (width >> 1), (height >> 1), txtAnimOutType);
            view.startAnimation(animSet);
            animSet.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.setOnClickListener(null);
                    view.setClickable(false);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    public AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter, int type) {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(interpolator);
        if (type == OUTSIDE_TO_LOCATION) {
            animSet.addAnimation(animAlpha2Opaque);
            animSet.addAnimation(animScaleLarge2Normal);
            TranslateAnimation translate = new TranslateAnimation(
                    (xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, (xy[IDX_Y] - yCenter) << 1, 0);
            animSet.addAnimation(translate);
        } else if (type == LOCATION_TO_OUTSIDE) {
            animSet.addAnimation(animAlpha2Transparent);
            animSet.addAnimation(animScaleNormal2Large);
            TranslateAnimation translate = new TranslateAnimation(0,
                    (xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, (xy[IDX_Y] - yCenter) << 1);
            animSet.addAnimation(translate);
        } else if (type == LOCATION_TO_CENTER) {
            animSet.addAnimation(animAlpha2Transparent);
            animSet.addAnimation(animScaleNormal2Zero);
            TranslateAnimation translate = new TranslateAnimation(0, (-xy[IDX_X] + xCenter), 0, (-xy[IDX_Y] + yCenter));
            animSet.addAnimation(translate);
        } else if (type == CENTER_TO_LOCATION) {
            animSet.addAnimation(animAlpha2Opaque);
            animSet.addAnimation(animScaleZero2Normal);
            TranslateAnimation translate = new TranslateAnimation((-xy[IDX_X] + xCenter), 0, (-xy[IDX_Y] + yCenter), 0);
            animSet.addAnimation(translate);
        }
        animSet.setDuration(animDuration);
        return animSet;
    }

}
