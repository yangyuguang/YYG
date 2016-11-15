package com.yangyg.imageload;

import android.content.Context;
import android.util.Log;

import com.yangyg.imageload.cache.LruMemoryCache;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片下载相关的配置
 * 1、是否缓存到内存(默认是)  是否缓存到本地(默认是)  内存缓存空间大小   本地缓存空间大小
 * 2、执行下载任务的Executors
 * Created by 杨裕光 on 16/11/9.
 */
public class ImageLoaderConfiger {

    private int corePoolSize = 3;

    private int maximumPoolSize = 3;
    private int keepAliveTime = 0;

    ExecutorService mNetworkExecutor = null;
    ExecutorService mFileExecutor = null;
    //加载图片的顺序
    private QueueProcessingType queueProcessingType = QueueProcessingType.FIFO;
    /**
     * 存储图片
     */
    LruMemoryCache lruMemoryCache = null;

    DisplayImageOption mDefauleDisplayImageOption = null;

    String defaultDiskCachePath = null;

    Context mContext = null;

    ImageLoaderConfiger(Context context) {
        this.mContext = context.getApplicationContext();
        defaultDiskCachePath = mContext.getFilesDir().getAbsolutePath() + File.separator + "appCache" + File.separator + "imageCache";
        Log.e("imageDownload", "缓存的图片地址：" + defaultDiskCachePath);
        init(Thread.NORM_PRIORITY - 2, "ImageDownloadThread:");

    }

    private void init(int threadPriority, String threadNamePrefix) {

        mDefauleDisplayImageOption = DisplayImageOption.createSimple();

        lruMemoryCache = new LruMemoryCache();

        mNetworkExecutor = createSimpleExecutorService(threadPriority, threadNamePrefix);
        mFileExecutor = createSimpleExecutorService(threadPriority, threadNamePrefix);
    }

    private ExecutorService createSimpleExecutorService(int threadPriority, String threadNamePrefix) {
        BlockingQueue<Runnable> workQueue = null;
        if (queueProcessingType == QueueProcessingType.FIFO) {
            workQueue = new FIFOLinkedBlockingDeque<Runnable>();
        } else {
            workQueue = new LinkedBlockingQueue<Runnable>();
        }
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, new DefaultThreadFactory(threadPriority, threadNamePrefix));

    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final ThreadGroup threadGroup;
        private int threadPriority = 0;
        private String threadNamePrefix = null;

        /**
         * 创建线程工厂
         *
         * @param threadPriority   线程优先级
         * @param threadNamePrefix 线程名称的前缀
         */
        public DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
            threadGroup = Thread.currentThread().getThreadGroup();
            this.threadPriority = threadPriority;
            this.threadNamePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(threadGroup, r, threadNamePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                //如果是守护线程 则设置daemon为false
                t.setDaemon(false);
            }
            t.setPriority(threadPriority);

            Log.e("imageDownload", "创建线程   NAME:" + t.getName() + "   ID:" + t.getId());

            return t;
        }
    }

    public String getDefaultDiskCachePath(){
        return this.defaultDiskCachePath;
    }

    public LruMemoryCache getLruMemoryCache(){
        return this.lruMemoryCache;
    }
}