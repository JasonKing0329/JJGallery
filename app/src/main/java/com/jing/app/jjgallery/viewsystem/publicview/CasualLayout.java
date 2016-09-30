package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.OnKeywordClickListener;

import java.util.LinkedList;
import java.util.Random;

@Deprecated
public class CasualLayout extends FrameLayout implements OnGlobalLayoutListener {

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
    public static int MAX = 15;
    public static final int TEXT_SIZE_MAX = 32;
    public static final int TEXT_SIZE_MIN = 20;
    private OnKeywordClickListener onKeywordClickListener;
    private static Interpolator interpolator;
    private static AlphaAnimation animAlpha2Opaque;
    private static AlphaAnimation animAlpha2Transparent;
    private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large, animScaleZero2Normal,
            animScaleNormal2Zero;

    private int width, height;
    /**
     * go2Show()中被赋值为true，标识开发人员触发其开始动画显示。<br/>
     * 本标识的作用是防止在填充keywrods未完成的过程中获取到width和height后提前启动动画。<br/>
     * 在show()方法中其被赋值为false。<br/>
     * 真正能够动画显示的另一必要条件：width 和 height不为0。<br/>
     */
    private boolean enableShow;
    private Random random;
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

    private CasualBaseAdapter mAdapter;

    public CasualLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CasualLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CasualLayout(Context context) {
        super(context);
        init();
    }

    public void setKeywordsNumber(int num) {
        MAX = num;
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
    }

    public void setAdapter(CasualBaseAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setCasualLayout(this);
    }

    public long getDuration() {
        return animDuration;
    }

    public void setDuration(long duration) {
        animDuration = duration;
    }

    /**
     * 开始动画显示。<br/>
     * 之前已经存在的TextView将会显示退出动画。<br/>
     *
     * @return 正常显示动画返回true；反之为false。返回false原因如下：<br/>
     *         1.时间上不允许，受lastStartAnimationTime的制约；<br/>
     *         2.未获取到width和height的值。<br/>
     */
    public boolean go2Show(int animType) {
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
            boolean result = show();
            return result;
        }
        return false;
    }

    private void disapper() {
        int size = getChildCount();
        for (int i = size - 1; i >= 0; i--) {
            final View txt = getChildAt(i);
            if (txt.getVisibility() == View.GONE) {
                removeView(txt);
                continue;
            }
            LayoutParams layParams = (LayoutParams) txt.getLayoutParams();
            // Log.d("ANDROID_LAB", txt.getText() + " leftM=" +
            // layParams.leftMargin + " topM=" + layParams.topMargin
            // + " width=" + txt.getWidth());
            int[] xy = new int[] { layParams.leftMargin, layParams.topMargin, txt.getWidth() };
            AnimationSet animSet = getAnimationSet(xy, (width >> 1), (height >> 1), txtAnimOutType);
            txt.startAnimation(animSet);
            animSet.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    txt.setOnClickListener(null);
                    txt.setClickable(false);
                    txt.setVisibility(View.GONE);
                }
            });
        }
    }

    // 占用屏幕点阵的方式，不太好，容易产生几个item都在一条水平线/垂直线的情况，效果不好
//    private boolean show() {
//        if (mAdapter == null) {
//            return false;
//        }
//
//        if (width > 0 && height > 0 && enableShow) {
//            // 生成屏幕矩阵
//            int itemWidth = mAdapter.getItemWidth(0);
//            int itemHeight = mAdapter.getItemHeight(0);
//            int row = width / itemWidth;
//            int col = height / itemHeight;
//            int totalScreen = row * col;
//
//            enableShow = false;
//            lastStartAnimationTime = System.currentTimeMillis();
//            //找到中心点
//            int xCenter = width >> 1, yCenter = height >> 1;
//            //关键字的个数。
//            int count = mAdapter.getCount();
//            for (int i = 0; i < count; i++) {
//                View view = mAdapter.getView(i);
//                final Keyword keyword = mAdapter.getKeyword(i);
//                view.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onKeywordClickListener != null && v.getTag() != null) {
//                            onKeywordClickListener.onKeywordClick(v, keyword);
//                        }
//                    }
//                });
//
//                int pointIndex = Math.abs(random.nextInt()) % totalScreen;
//                int[] offset = new int[3];
//                offset[IDX_X] = (pointIndex % col) * itemWidth;// col
//                offset[IDX_Y] = (pointIndex / col) * itemHeight;// row
//                offset[IDX_TXT_LENGTH] = itemWidth;
//
//                Log.d("ANDROID_LAB", "--------------------------width=" + width +
//                        " height=" + height + "  offsetX=" + offset[IDX_X]
//                        + " offsetY=" + offset[IDX_Y] + "---------------------------");
//                LayoutParams layParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//                        LayoutParams.WRAP_CONTENT);
//                layParams.gravity = Gravity.LEFT | Gravity.TOP;
//                layParams.leftMargin = offset[IDX_X];
//                layParams.topMargin = offset[IDX_Y];
//                addView(view, layParams);
//                // 动画
//                AnimationSet animSet = getAnimationSet(offset, xCenter, yCenter, txtAnimInType);
//                view.startAnimation(animSet);
//            }
//        }
//        return false;
//    }

    private boolean show() {
        if (mAdapter == null) {
            return false;
        }

        if (width > 0 && height > 0 && enableShow) {
            enableShow = false;
            lastStartAnimationTime = System.currentTimeMillis();
            //找到中心点
            int xCenter = width >> 1, yCenter = height >> 1;
            //关键字的个数。
            int size = mAdapter.getCount();
            int xItem = width / size, yItem = height / size;
            Log.d("ANDROID_LAB", "--------------------------width=" + width +
                    " height=" + height + "  xItem=" + xItem
                    + " yItem=" + yItem + "---------------------------");
            LinkedList<Integer> listX = new LinkedList<>(), listY = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                // 准备随机候选数，分别对应x/y轴位置
                listX.add(i * xItem);
                listY.add(i * yItem + (yItem >> 2));
                Log.e("Search", "ListX:"+(i * xItem)+"#listY:"+(i * yItem + (yItem >> 2)));
            }
            // TextView[] txtArr = new TextView[size];
            LinkedList<View> listTxtTop = new LinkedList<>();
            LinkedList<View> listTxtBottom = new LinkedList<>();
            for (int i = 0; i < size; i++) {

                View view = mAdapter.getView(i);
                final Keyword keyword = mAdapter.getKeyword(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onKeywordClickListener != null && v.getTag() != null) {
                            onKeywordClickListener.onKeywordClick(v, keyword);
                        }
                    }
                });

                // 随机位置，糙值
                int xy[] = randomXY(random, listX, listY, xItem);
                int strWidth = mAdapter.getItemWidth(i);
                xy[IDX_TXT_LENGTH] = strWidth;
                // 第一次修正:修正x坐标
                if (xy[IDX_X] + strWidth > width - (xItem >> 1)) {
                    int baseX = width - strWidth;
                    // 减少文本右边缘一样的概率
                    xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);
                } else if (xy[IDX_X] == 0) {
                    // 减少文本左边缘一样的概率
                    xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
                }
                xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);
                view.setTag(xy);
                if (xy[IDX_Y] > yCenter) {
                    listTxtBottom.add(view);
                } else {
                    listTxtTop.add(view);
                }
            }
            attach2Screen(listTxtTop, xCenter, yCenter, yItem);
            attach2Screen(listTxtBottom, xCenter, yCenter, yItem);
            return true;
        }
        return false;
    }

    /** 修正TextView的Y坐标将将其添加到容器上。 */
    private void attach2Screen(LinkedList<View> listTxt, int xCenter, int yCenter, int yItem) {
        int size = listTxt.size();
        sortXYList(listTxt, size);
        for (int i = 0; i < size; i++) {
            View txt = listTxt.get(i);
            int[] iXY = (int[]) txt.getTag();
            // Log.d("ANDROID_LAB", "fix[  " + txt.getText() + "  ] x:" +
            // iXY[IDX_X] + " y:" + iXY[IDX_Y] + " r2="
            // + iXY[IDX_DIS_Y]);
            // 第二次修正:修正y坐标
            int yDistance = iXY[IDX_Y] - yCenter;
            // 对于最靠近中心点的，其值不会大于yItem<br/>
            // 对于可以一路下降到中心点的，则该值也是其应调整的大小<br/>
            int yMove = Math.abs(yDistance);
            inner: for (int k = i - 1; k >= 0; k--) {
                int[] kXY = (int[]) listTxt.get(k).getTag();
                int startX = kXY[IDX_X];
                int endX = startX + kXY[IDX_TXT_LENGTH];
                // y轴以中心点为分隔线，在同一侧
                if (yDistance * (kXY[IDX_Y] - yCenter) > 0) {
                    // Log.d("ANDROID_LAB", "compare:" +
                    // listTxt.get(k).getText());
                    if (isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X] + iXY[IDX_TXT_LENGTH])) {
                        int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);
                        if (tmpMove > yItem) {
                            yMove = tmpMove;
                        } else if (yMove > 0) {
                            // 取消默认值。
                            yMove = 0;
                        }
                        // Log.d("ANDROID_LAB", "break");
                        break inner;
                    }
                }
            }
            // Log.d("ANDROID_LAB", txt.getText() + " yMove=" + yMove);
            if (yMove > yItem) {
                int maxMove = yMove - yItem;
                int randomMove = random.nextInt(maxMove);
                int realMove = Math.max(randomMove, maxMove >> 1) * yDistance / Math.abs(yDistance);
                iXY[IDX_Y] = iXY[IDX_Y] - realMove;
                iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);
                // 已经调整过前i个需要再次排序
                sortXYList(listTxt, i + 1);
            }
            LayoutParams layParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layParams.gravity = Gravity.LEFT | Gravity.TOP;
            layParams.leftMargin = iXY[IDX_X];
            layParams.topMargin = iXY[IDX_Y];
            addView(txt, layParams);
            // 动画
            AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter, txtAnimInType);
            txt.startAnimation(animSet);
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

    /**
     * 根据与中心点的距离由近到远进行冒泡排序。
     *
     * @param endIdx
     *            起始位置。
     * @param endIdx
     *            待排序的数组。
     *
     */
    private void sortXYList(LinkedList<View> listTxt, int endIdx) {
        for (int i = 0; i < endIdx; i++) {
            for (int k = i + 1; k < endIdx; k++) {
                if (((int[]) listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[]) listTxt.get(i).getTag())[IDX_DIS_Y]) {
                    View iTmp = listTxt.get(i);
                    View kTmp = listTxt.get(k);
                    listTxt.set(i, kTmp);
                    listTxt.set(k, iTmp);
                }
            }
        }
    }

    /** A线段与B线段所代表的直线在X轴映射上是否有交集。 */
    private boolean isXMixed(int startA, int endA, int startB, int endB) {
        boolean result = false;
        if (startB >= startA && startB <= endA) {
            result = true;
        } else if (endB >= startA && endB <= endA) {
            result = true;
        } else if (startA >= startB && startA <= endB) {
            result = true;
        } else if (endA >= startB && endA <= endB) {
            result = true;
        }
        return result;
    }

    private int[] randomXY(Random ran, LinkedList<Integer> listX, LinkedList<Integer> listY, int xItem) {
        int[] arr = new int[4];
        arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
        arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
        return arr;
    }

    public void onGlobalLayout() {
        int tmpW = getWidth();
        int tmpH = getHeight();
        if (width != tmpW || height != tmpH) {
            width = tmpW;
            height = tmpH;
            show();
        }
    }

    public void rubKeywords() {
        mAdapter.clear();
    }

    /** 直接清除所有的TextView。在清除之前不会显示动画。 */
    public void rubAllViews() {
        removeAllViews();
    }

    public void setOnKeywordClickListener(OnKeywordClickListener listener) {
        onKeywordClickListener = listener;
    }

    public void notifyDataSetChanged() {
    }

    // public void onDraw(Canvas canvas) {  
    // super.onDraw(canvas);  
    // Paint p = new Paint();  
    // p.setColor(Color.BLACK);  
    // canvas.drawCircle((width >> 1) - 2, (height >> 1) - 2, 4, p);  
    // p.setColor(Color.RED);  
    // }  
}
