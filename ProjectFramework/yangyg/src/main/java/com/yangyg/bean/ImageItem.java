package com.yangyg.bean;

public class ImageItem {
	
	public String imageId;
    public String imagePath;
    public String thumbnailPath;
    
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "imageId='" + imageId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                '}';
    }
}
