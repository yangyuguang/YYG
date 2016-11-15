package com.yangyg.imageload;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 后进先出双向队列
 * Created by 杨裕光 on 16/11/10.
 */
public class FIFOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {


    @Override
    public boolean offer(T t) {
        return super.offerFirst(t);
    }

    @Override
    public T remove() {
        return super.removeFirst();
    }
}
