package com.jing.app.jjgallery.service.image.lru2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.ISImageLoader;
import com.jing.app.jjgallery.service.image.lru.ImageSizeUtil;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 图片加载类
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 * 这个ImageLoader应用在TimeLine视图中，其滑动过程更顺滑，不会出现ImageView闪回其他item的情况
 */
public class ImageLoader implements ISImageLoader {

    private ImageShowManager imageManager;

    private int defaultResId = -1;

    public ImageLoader(Context context) {
        imageManager = ImageShowManager.from(context);
    }

    @Override
    public void setDefaultImgRes(int resId) {
        defaultResId = resId;
    }

    @Override
    public void removeCache(String path) {
        imageManager.deleteInMemery(path);
    }

    @Override
    public void removeCache() {

    }

    @Override
    public void displayImage(String path, ImageView imageView) {
        // 首先检测是否已经有线程在加载同样的资源（如果则取消较早的），避免出现重复加载
        if (cancelPotentialLoad(path, imageView)) {
            AsyncLoadImageTask task = new AsyncLoadImageTask(imageView);
            if (defaultResId != -1) {
                imageView.setImageResource(defaultResId);
            }
            imageView.setTag(R.id.tag_lru2_imageloader_task, new ImageTask(task));
            task.execute(path);
        }
    }

    /**
     * 判断当前的imageview是否在加载相同的资源
     *
     * @param url
     * @param imageview
     * @return
     */
    private boolean cancelPotentialLoad(String url, ImageView imageview) {

        AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
        if (loadImageTask != null) {
            String bitmapUrl = loadImageTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                loadImageTask.cancel(true);
            } else {
                // 相同的url已经在加载中.
                return false;
            }
        }
        return true;
    }

    /**
     * 负责加载图片的异步线程
     *
     * @author Administrator
     *
     */
    class AsyncLoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private String url = null;

        public AsyncLoadImageTask(ImageView imageview) {
            super();
            imageViewReference = new WeakReference<ImageView>(imageview);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            /**
             * 具体的获取bitmap的部分，流程： 从内存缓冲区获取，如果没有向硬盘缓冲区获取，如果没有从sd卡/网络获取
             */

            Bitmap bitmap = null;
            this.url = params[0];
            if (url == null) {
                return null;
            }

            // 从内存缓存区域读取
            bitmap = imageManager.getBitmapFromMemory(url);
            if (bitmap != null) {
                Log.d("dqq", "return by 内存");
                return bitmap;
            }
            // 从硬盘缓存区域中读取
            bitmap = imageManager.getBitmapFormDisk(url);
            if (bitmap != null) {
                imageManager.putBitmapToMemery(url, bitmap);
                Log.d("dqq", "return by 硬盘");
                return bitmap;
            }

            // 没有缓存则从原始位置读取
            ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageViewReference.get());
            bitmap = decodeSampledBitmapFromPath(url, imageSize.width, imageSize.height);
            imageManager.putBitmapToMemery(url, bitmap);
            imageManager.putBitmapToDisk(url, bitmap);
            Log.d("dqq", "return by 原始读取");
            return bitmap;
        }

        /**
         * 根据图片需要显示的宽和高对图片进行压缩
         *
         * @param path
         * @param width
         * @param height
         * @return
         */
        protected Bitmap decodeSampledBitmapFromPath(String path, int width,
                                                     int height)
        {
            Bitmap bitmap = null;
            byte[] datas = EncryptUtil.getEncrypter().decipherToByteArray(new File(path));
            if (datas != null) {
                // 获得图片的宽和高，并不把图片加载到内存中
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
//		BitmapFactory.decodeFile(path, options);

                options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
                        width, height);

                // 使用获得到的InSampleSize再次解析图片
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
//		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap resultBitmap) {
            if (isCancelled()) {
                // 被取消了
                resultBitmap = null;
            }
            if (imageViewReference != null) {
                ImageView imageview = imageViewReference.get();
                AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
                if (this == loadImageTask) {
                    imageview.setImageDrawable(null);
                    imageview.setImageBitmap(resultBitmap);
                }

            }

            super.onPostExecute(resultBitmap);
        }
    }

    /**
     * 根据imageview，获得正在为此imageview异步加载数据的函数
     *
     * @param imageview
     * @return
     */
    private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview) {
        if (imageview != null) {
            ImageTask task = (ImageTask) imageview.getTag(R.id.tag_lru2_imageloader_task);
            if (task != null) {
                return task.getLoadImageTask();
            }
        }
        return null;
    }

    public static class ImageTask {
        private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

        public ImageTask(AsyncLoadImageTask loadImageTask) {
            loadImageTaskReference = new WeakReference<>(
                    loadImageTask);
        }

        public AsyncLoadImageTask getLoadImageTask() {
            return loadImageTaskReference.get();
        }
    }

}
