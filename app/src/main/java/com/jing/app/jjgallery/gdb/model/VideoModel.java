package com.jing.app.jjgallery.gdb.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore.Video;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.bean.VideoData;
import com.jing.app.jjgallery.util.FormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述: loadVideos in GHomeActivity, clear int GHomeActivity.onDestroy
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/18 13:53
 */
public class VideoModel {

    String[] columns = new String[] {
            Video.Media.DATA,
            Video.Media._ID,
            Video.Media.TITLE,
            Video.Media.DISPLAY_NAME,
            Video.Media.SIZE,
            Video.Media.DURATION,
            Video.Media.DATE_ADDED,
            Video.Media.MIME_TYPE,
            Video.Media.WIDTH,
            Video.Media.HEIGHT
    };

    private static Map<String, String> map;

    public static void loadVideos(Context context) {
        map = new HashMap<>();
        File file = new File(Configuration.getGdbVideoDir(context));
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f:files) {
                try {
                    String name = f.getName().substring(0, f.getName().lastIndexOf('.'));
                    map.put(name, f.getPath());
                } catch (Exception e) {

                }
            }
        }
    }

    public static void clear() {
        map = null;
    }

    public static String getVideoPath(String name) {
        if (map == null) {
            return null;
        }
        return map.get(name);
    }

    public VideoModel() {

    }

    /**
     * create thumb nail mode image, to save the memory
     * @param src
     * @param width
     * @param height
     * @return
     */
    private Bitmap convertToThumbnail(Bitmap src, int width, int height) {
        Bitmap bitmap = null;

        Matrix matrix = new Matrix();
        float dx = (float) width / (float) src.getWidth();
        float dy = (float) height / (float) src.getHeight();
        matrix.postScale(dx, dy);
        try {
            bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        } catch (Exception e) {
            bitmap = null;
        }
        src.recycle();
        return bitmap;
    }

    /**
     * load image in videos
     * @param filePath
     * @param minus 毫秒数
     * @param width
     * @param height
     * @return
     */
    public Bitmap getVideoFrame(String filePath, int minus, int width, int height) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        // 第一个参数是毫秒数再乘以1000
        Bitmap bitmap = retriever.getFrameAtTime(minus * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        bitmap = convertToThumbnail(bitmap, width, height);
        retriever.release();
        return bitmap;
    }

    public VideoData queryVideoDataByPath(Context context, String path) {
        VideoData data = null;
        Cursor cursor = context.getContentResolver().query(
                Video.Media.EXTERNAL_CONTENT_URI,
                columns, Video.Media.DATA + " = ?", new String[]{path}, null);
        if (cursor.moveToNext()) {
            data = getVideoDataFromCursor(cursor);
        }
        cursor.close();
        return data;
    }

    private VideoData getVideoDataFromCursor(Cursor cursor) {
        VideoData data = new VideoData();
        data.setId(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media._ID)));
        data.setName(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.DISPLAY_NAME)));
        data.setPath(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.DATA)));
        data.setSizeLong(cursor.getLong(cursor.getColumnIndexOrThrow(Video.Media.SIZE)));
        data.setSize(FormatUtil.formatSize(data.getSizeLong()));
        data.setDurationInt(cursor.getInt(cursor.getColumnIndexOrThrow(Video.Media.DURATION)));
        data.setDuration(FormatUtil.formatTime(data.getDurationInt()));
        try {
            data.setDateAdded(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.DATE_ADDED))));
        } catch (Exception e) {
            data.setDateAdded(0);
        }
        try {
            data.setWidth(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.WIDTH))));
        } catch (Exception e) {
            data.setWidth(0);
        }
        try {
            data.setHeight(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.HEIGHT))));
        } catch (Exception e) {
            data.setHeight(0);
        }
        data.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.MIME_TYPE)));
        return data;
    }

    public List<Integer> createVideoFrames(VideoData videoData, int total) {
        List<Integer> timeList = new ArrayList<>();
        int duration = videoData.getDurationInt();
        int step = duration / total;

        for (int i = 0; i < total; i ++) {
            timeList.add(i * step);
        }

        Random random = new Random();
        for (int i = 0; i < total; i ++) {
            int start = timeList.get(i);
            int end = duration;
            if (i < total - 1) {
                end = timeList.get(i + 1) - 1000;
            }
            int time = start + Math.abs(random.nextInt()) % (end - start);
            timeList.set(i, time);
        }
        return timeList;
    }

    /**
     *
     * @param videoData
     * @param timeList 创建缩略图的时间帧
     * @param updateNum 每加载几张通知一次更新
     * @param width 缩略图width
     * @param height 缩略图height
     * @param callback
     */
    public void createThumbnails(final VideoData videoData, final List<Integer> timeList, final int updateNum
        , final int width, final int height, final VideoThumbCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<Bitmap>>() {
            @Override
            public void call(Subscriber<? super List<Bitmap>> subscriber) {
                List<Bitmap> list = null;
                for (int i = 0; i < timeList.size(); i ++) {
                    if (i % updateNum == 0) {
                        list = new ArrayList<>();
                    }
                    list.add(getVideoFrame(videoData.getPath(), timeList.get(i), width, height));
                    if (i % updateNum == updateNum - 1) {
                        subscriber.onNext(list);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Bitmap>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Bitmap> bitmaps) {
                        callback.onThumbnailCreated(bitmaps);
                    }
                });
    }
}
