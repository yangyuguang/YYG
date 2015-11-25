package com.pami.http;



/**
 * Volley加载图片
 * @author Administrator
 *
 */
public class VolleyImageLoad {

	
	/**
	 * 利用Volley从网络获取图片
	 * @param imageView
	 * @param imageUrl
	 * @param defaultImageResId
	 * @param errorImageResId
	 * @param maxWidth
	 * @param maxHeight
	 * @throws FileNotFoundException 
	 */
//	public static void loadImageFromNetWork(ImageView imageView,String imageUrl,int defaultImageResId,int errorImageResId,int maxWidth,int maxHeight) throws Exception{
//		
//		if(TextUtils.isEmpty(imageUrl)){
//			throw new Exception("图片地址为空");
//		}
//		String lowerImageUrl = imageUrl.toLowerCase();
//		
//		if(!lowerImageUrl.startsWith("http://")){
//			throw new Exception("图片地址不是网络地址");
//		}
//		
//		ImageLoader loader = new ImageLoader(PMApplication.getInstance().getRequestQueue(), VolleyImageCache.getInstence());
//		ImageListener imageListener = ImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId);
//		loader.get(imageUrl, imageListener, maxWidth, maxHeight);
//	}
	
}
