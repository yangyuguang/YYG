package com.yangyg.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yangyg.YYGApplication;
import com.yangyg.bean.ImageBeanHolder;
import com.yangyg.bean.ImageSize;


/**
 * 图片加载类
 * Created by 杨裕光 on 15/9/17.
 */
public class ImageLoader {

    private static ImageLoader instance;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 默认是一个线程
     */
    private static final int DEFULT_THREAD_COUNT = 1;
    private Type mType = Type.LIFO;

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    public enum Type {
        FIFO, LIFO
    }

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool = null;

    public static ImageLoader getInstance() {

        return getInstance(DEFULT_THREAD_COUNT, Type.LIFO);
    }

    public static ImageLoader getInstance(int mThreadCount, Type type) {

        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader(DEFULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }

        return instance;
    }

    private ImageLoader(int mThreadCount, Type type) {
        init(mThreadCount, type);
    }

    /**
     * 初始化操作
     *
     * @param mThreadCount
     * @param type
     */
    private void init(int mThreadCount, Type type) {

        /**
         *
         */
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池中取出一个任务执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(mThreadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(mThreadCount);
    }

    /**
     * 从线程池中获取任务
     *
     * @return
     */
    private Runnable getTask() {

        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }

    /**
     * 根据path加载图片
     *
     * @param imageView
     * @param path      图片的路径
     */
    public void loadImage(final ImageView imageView, final String path) throws Exception {
        imageView.setTag(path);//

        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到的图片  为ImageView回调设置图片

                    ImageBeanHolder bean = (ImageBeanHolder) msg.obj;
                    Bitmap bm = bean.bitmap;
                    String path = bean.path;
                    ImageView imageView1 = bean.imageView;
                    //将path与getTag存储路径进行比较
                    if (imageView1.getTag().toString().equals(path)) {
                        imageView1.setImageBitmap(bm);
                    }

                }
            };
        }

        Bitmap bm = getBitmapFromLruCache(path);
        if (bm == null) {

            addTasks(new Runnable() {
                @Override
                public void run() {
                    try {
                        //TODO 加载图片
                        //图片压缩
                        //获得图片需要显示的大小
                        ImageSize imageSize = getImageViewSize(imageView);
                        //压缩图片
                        Bitmap bm = BitmapUtils.equalRatioCompressImage(path, imageSize.width, imageSize.height);
                        //把图片加入到缓存
                        addBitmapToLruCache(path, bm);

                        //显示图片
                        refreashBitmap(path, imageView, bm);
                        mSemaphoreThreadPool.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            //显示图片
            refreashBitmap(path, imageView, bm);
        }

    }

    /**
     * 利用UIHandler显示图片
     *
     * @param path
     * @param imageView
     * @param bm
     */
    private void refreashBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImageBeanHolder bean = new ImageBeanHolder();
        bean.path = path;
        bean.bitmap = bm;
        bean.imageView = imageView;
        message.obj = bean;
        message.what = 0;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入缓存
     *
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null) {
                YYGApplication.getInstance().getmImageLoaderCache().put(path, bm);
            }
        }
    }

    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView) throws Exception {
        ImageSize imageSize = new ImageSize();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        DisplayMetrics dm = imageView.getContext().getResources().getDisplayMetrics();

        int width = imageView.getWidth();

        if (width <= 0) {
            width = lp.width;
        }

        if (width <= 0) {
            width = getImagViewFieldValue(imageView, "mMaxWidth");
        }

        if (width <= 0) {
            width = dm.widthPixels;
        }


        int height = imageView.getHeight();

        if (height <= 0) {
            height = lp.height;
        }

        if (height <= 0) {
            height = getImagViewFieldValue(imageView, "mMaxHeight");//检查最大值
        }

        if (height <= 0) {
            height = dm.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    /**
     * 通过反射机制获取属性
     *
     * @param object 属性对象
     * @param fieldName 属性名称
     * @return
     */
    private static int getImagViewFieldValue(Object object, String fieldName) throws Exception {

        int value = 0;

        Field field = ImageView.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        int fieldValue = field.getInt(object);

        if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
            value = fieldValue;
        }

        return value;
    }

    private synchronized void addTasks(Runnable runnable) throws Exception {
        mTaskQueue.add(runnable);

        if (mPoolThreadHandler == null) {
            mSemaphorePoolThreadHandler.acquire();
        }

        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 根据路径从缓存中获取图片
     *
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return YYGApplication.getInstance().getmImageLoaderCache().get(path);
    }

}
