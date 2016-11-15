package com.yangyg.imageload.download;

import java.util.Locale;

/**
 * Created by 杨裕光 on 16/11/15.
 */
public enum ImageSourceType {

    HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");

    private String imageSourceType;
    private String uriPrefix;

    ImageSourceType(String imageSourceType){
        this.imageSourceType = imageSourceType;
        this.uriPrefix = imageSourceType + "://";
    }

    public static ImageSourceType ofUri(String uri) {
        if (uri != null) {
            for (ImageSourceType s : values()) {
                if (s.belongsTo(uri)) {
                    return s;
                }
            }
        }
        return UNKNOWN;
    }


    private boolean belongsTo(String uri) {
        return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
    }

    public String getPrefix(){
        return this.uriPrefix;
    }

    /**
     * 获取不同类型的图片地址
     * @param path
     * @return
     */
    public String wrap(String path) {
        return uriPrefix + path;
    }

    public String crop(String uri){
        if (!belongsTo(uri)) {
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, imageSourceType));
        }
        return uri.substring(uriPrefix.length());
    }

}
