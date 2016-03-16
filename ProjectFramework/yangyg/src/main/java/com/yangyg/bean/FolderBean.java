package com.yangyg.bean;

/**
 * Created by yangyuguang on 15/9/20.
 */
public class FolderBean {


    private String dir;//当前文件夹的路径
    private String firstImgPath;
    private String name;
    private int count;

    public String getDir() {

        return dir;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf+1);
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
