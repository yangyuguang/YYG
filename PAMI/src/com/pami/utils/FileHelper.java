package com.pami.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

/**
 * 文件操作工具类
 * @author Administrator
 *
 */
public class FileHelper {

	/**
	 * 判断path路径下是否存在文件
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String path, String name) throws Exception {
		boolean status = false;
		if (!name.equals("")) {
			File newPath = new File(path + name);
			status = newPath.exists();
		}
		return status;
	}

	/**
	 * 判断path路径下是否存在文件
	 * @param path
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String filepath) throws Exception {
		boolean status = false;
		if (!TextUtils.isEmpty(filepath)) {
			File newPath = new File(filepath);
			status = newPath.exists();
		}
		return status;
	}

	/**
	 * 创建文件夹( 可以创建多级)
	 * @param dirName
	 */
	public static void creatDir(String dirName) throws Exception {
		File file = new File(dirName);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 获取文件返回的流
	 * @param filepath 文件路径
	 * @param fileName 文件名称
	 * @return
	 */
	public static InputStream getfileInputStream(String filepath, String fileName) throws Exception {
		File file = new File(filepath, fileName);
		if (file.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				return fis;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 将字符串写入文件当中
	 * @param path 文件路径
	 * @param FileName 文件名
	 * @param resource 需要写入的字符串
	 */
	public static void saveStringToFile(String path, String FileName, String resource) throws Exception {
		creatDir(path);
		File file = new File(path, FileName);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(resource.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件名读取缓存对象
	 * @param context 上下文
	 * @param fileName 文件名 如:userinfo.txt
	 * @return
	 * @throws Exception
	 */
	public static Object getEntity(Context context,String fileName) throws Exception {
		Object object = null;
		try {
			FileInputStream fs = context.openFileInput(fileName);
			ObjectInputStream in = new ObjectInputStream(fs);
			object = in.readObject();
			in.close();
			fs.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 保存数据到应用缓存 
	 * @param context 上下文对象
	 * @param object 需要保存的对象
	 * @param fileName 保存的文件名 如:userinfo.txt
	 */
	public static void saveEntity(Context context,Object object, String fileName) throws Exception {
		try {
			FileOutputStream fs;
			fs = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fs);
			out.writeObject(object);
			out.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件中的字符串
	 * @param path 文件路径
	 * @param FileName 文件名
	 * @return
	 */
	public static String getStringFromFile(String path, String FileName) throws Exception {
		File file = new File(path, FileName);
		if (checkFileExists(path, FileName)) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[512];
				int length = -1;
				while ((length = fileInputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, length);
				}
				outStream.close();
				fileInputStream.close();
				return outStream.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * 保存bitmap 到文件 （不压缩）
	 * @param path 文件路径
	 * @param Filename 文件名
	 * @param bitmap bitmap对象
	 */
	public static void saveBitmap(String path, String Filename, Bitmap bitmap) throws Exception {
		File file = new File(path, Filename);
		if (!checkFileExists(path, Filename)) {
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将InputStream 中的数据保存到 文件
	 * @param is InputStream对象
	 * @param filepath 文件路径
	 * @param fileName 文件名
	 */
	public static void saveInputStreamToFile(InputStream is, String filepath, String fileName) throws Exception {
		File file = new File(filepath, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = is.read(data)) != -1) {
				fos.write(data, 0, len);
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取 InputStream 中的数据
	 * @param inStream InputStream对象
	 * @return
	 */
	public static String readInStream(InputStream inStream) throws Exception {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	/**
	 * 将 InputStream中的数据转换成 byte[]
	 * @param is InputStream 对象
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream is) throws Exception {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte[] imgdata = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/**
	 * 复制文件
	 * @param sourePath 源文件path
	 * @param derPath 目标文件path
	 * @throws FileNotFoundException
	 */
	public static void copyFile(String sourePath, String derPath) throws Exception {
		File oldfile = new File(sourePath);
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(sourePath); // 读入原文件
			FileOutputStream fs = new FileOutputStream(derPath);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}
	}

	/**
	 * 保存网络图片 到 文件
	 * @param path
	 * @param Filename
	 * @param imagePath
	 */
	public static void saveBitmap(String path, String Filename, String imagePath)throws Exception {
		File file = new File(path, Filename);
		if (!checkFileExists(path, Filename)) {
				URL url = new URL(imagePath);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(6 * 1000); // 注意要设置超时，设置时间不要超过10秒，避免被android系统回收
				if (conn.getResponseCode() != 200)
					throw new RuntimeException("请求url失败");
				InputStream inSream = conn.getInputStream();
				// 把图片保存到项目的根目录
				readAsFile(inSream, file);
			
		}
	}

	/**
	 * 保存 InputStream 中的数据 到文件
	 * @param inSream InputStream对象
	 * @param file 文件
	 * @throws Exception
	 */
	private static void readAsFile(InputStream inSream, File file) throws Exception {
		FileOutputStream outStream = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inSream.close();
	}
}
