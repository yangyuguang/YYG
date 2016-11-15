package com.yangyg.simpleimagedownload;

/**
 * Created by 杨裕光 on 16/11/11.
 */
public class ImageBean {
    private String url;
    private String title;

    public ImageBean(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
