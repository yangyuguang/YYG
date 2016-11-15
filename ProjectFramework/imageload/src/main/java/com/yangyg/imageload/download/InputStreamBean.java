package com.yangyg.imageload.download;

import java.io.InputStream;

/**
 * Created by 杨裕光 on 16/11/9.
 */
public class InputStreamBean {

    private ImageSourceType imageSourceType = null;
    private InputStream inputStream = null;
    private int total = 0;

    public ImageSourceType getImageSourceType() {
        return imageSourceType;
    }

    public void setImageSourceType(ImageSourceType imageSourceType) {
        this.imageSourceType = imageSourceType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "InputStreamBean{" +
                "imageSourceType=" + imageSourceType +
                ", inputStream=" + inputStream +
                ", total=" + total +
                '}';
    }
}
