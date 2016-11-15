package com.yangyg.imageload.download;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 杨裕光 on 16/11/9.
 */
public class ImageDownload {

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 0;
    private int readTimeout = 0;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private static final int BUFFER_SIZE = 32 * 1024;

    public ImageDownload(int connectionTimeout,int readTimeout){
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }
    public ImageDownload(){
    }
    /**
     * 从网络中获取InputStream对象
     * @param url 图片地址
     * @return InputStream对象
     * @throws IOException
     */
    public InputStreamBean getInputStringFromNetwork(String url)throws IOException{


        HttpURLConnection connection = createConnection(url);

        int responseCode = connection.getResponseCode();
        Log.i("imageDownload","请求图片返回的responseCode:"+responseCode);

        if(responseCode != 200){
            return null;
        }

        InputStreamBean bean = new InputStreamBean();
        bean.setTotal(connection.getContentLength());
        bean.setInputStream(connection.getInputStream());
        bean.setImageSourceType(ImageSourceType.HTTP);
        return bean;
    }

    /**
     * 根据url 创建HttpURLConnection连接
     * @param url
     * @return
     * @throws IOException
     */
    public HttpURLConnection createConnection(String url)throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(Uri.encode(url, ALLOWED_URI_CHARS)).openConnection();
        if(connection == null){
            throw new IllegalArgumentException(String.format("HttpRULConnection is null from %s",url));
        }

        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        connection.setRequestMethod("GET");

        return connection;
    }

    public InputStreamBean getInputStringFromFilePath(String filePath)throws IOException{
        File file = new File(filePath);
        if(file.exists()){
            return this.getInputStringFromFile(file);
        }
        return null;
    }

    public InputStreamBean getInputStringFromFile(File file)throws IOException{
        InputStreamBean bean = new InputStreamBean();
        BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
        bean.setInputStream(imageStream);
        bean.setTotal(imageStream.available());
        bean.setImageSourceType(ImageSourceType.FILE);
        return bean;
    }

}
