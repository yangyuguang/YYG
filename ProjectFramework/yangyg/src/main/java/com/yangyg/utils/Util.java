package com.yangyg.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Util {

	private static long lastClickTime;
	private static final double EARTH_RADIUS = 6378137;
	public static int isShowLoding = 0;
	private static Toast sToast = null;
	
	private static ArrayMap<String, Typeface> customFonts = new ArrayMap<String, Typeface>();

	public static int getIsShowLoding() {
		return isShowLoding;
	}

	public static void setIsShowLoding(int isShowLoding) {
		Util.isShowLoding = isShowLoding;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static final String TAG = "Util";



	/**
	 * 是否是手机号
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhone(String phoneNumber) {
		return regularVerify(phoneNumber, "^1[3|4|5|7|8][0-9]\\d{8}$");
	}

	/**
	 * 是否是邮箱
	 * 
	 * @return
	 */
	public static boolean isEmail(String email) {
		return regularVerify(email, "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");
	}


	public static boolean regularVerify(String value, String match) {
		Pattern p = Pattern.compile(match);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 判断字符串是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;

				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	/**
	 * 判断字符串是否是纯字母
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isPureChinese(String s) {

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断第一个字符是否是字母
	 * 
	 * @param fstrData
	 * @return
	 */
	public static boolean isFirstLetter(String fstrData) {
		char c = fstrData.charAt(0);
		return isLetter(c);
	}

	/**
	 * 判断字符是否是字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否全部是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		return regularVerify(str, "[0-9]*");
	}



	/**
	 * 匹配分行信息 1-10 汉字
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isBranchBank(String str) {
		return regularVerify(str, "[\u4E00-\u9FA5]{1,10}");

	}



	private static final String ID_CARD_PATTERN = "[0-9]{6}(1[9]|2[0])\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}(\\d|\\w)";

	public static boolean isIDCARD(String ID) {
		Pattern p = Pattern.compile(ID_CARD_PATTERN);
		Matcher m = p.matcher(ID);
		return m.matches();
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * 去除所有空格制表符 换行
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}



	/***
	 * 计算百分比
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	public static double myPercent(double y, double z) {
		String baifenbi = "";// 接受百分比的值
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		NumberFormat nf = NumberFormat.getPercentInstance(); // 注释掉的也是一种方法
		nf.setMinimumFractionDigits(2); // 保留到小数点后几位
		DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
		// 百分比格式，后面不足2位的用0补齐

		baifenbi = df1.format(fen);
		double temp = 0;
		try {
			temp = Double.parseDouble(baifenbi.replace("%", "").trim());
		} catch (Exception e) {
			// TODO: handle exception
			temp = 0;
		}
		return temp;
	}

	/***
	 * 计算百分比小数点后两位
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	public static String myPercentDecimal(double y, double z) {
		String baifenbiTemp = "";// 接受百分比的值
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		NumberFormat nf = NumberFormat.getPercentInstance(); // 注释掉的也是一种方法
		nf.setMinimumFractionDigits(2); // 保留到小数点后几位
		// 百分比格式，后面不足2位的用0补齐
		baifenbiTemp = nf.format(fen);

		return baifenbiTemp;
	}

	/***
	 * 计算百分比
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	public static String myPercentDecimalTwo(double y, double z) {
		String baifenbiTemp = "";// 接受百分比的值
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		NumberFormat nf = NumberFormat.getPercentInstance(); // 注释掉的也是一种方法
		nf.setMinimumFractionDigits(2); // 保留到小数点后几位
		// 百分比格式，后面不足2位的用0补齐
		baifenbiTemp = nf.format(fen);

		return baifenbiTemp;
	}

	public static BitmapFactory.Options options;



	// 四舍五入
	public static int StringToRounding(String value) {

		Double temp1 = 0.0;
		int temp2 = 0;
		try {
			temp1 = Double.parseDouble(value);
			temp2 = (int) Math.ceil(temp1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp2;
	}

	/**
	 * 加0
	 * 
	 * @param temp
	 * @return
	 */
	public static String addZeros(int temp) {
		// TODO Auto-generated method stub
		if (temp <= 0) {
			return "00";
		}
		if (temp < 10) {
			return "0" + temp;
		}
		return temp + "";
	}

	/**
	 * 生成一个15位的随机数订单号
	 * 
	 * @return
	 */
	public static String creatRequestId() {
		// TODO Auto-generated method stub
		String id = System.currentTimeMillis() + "";
		if (id.equals("") || id.length() < 12)
			id = "88888888888";

		if (id.length() > 15) {
			id = id.substring(0, 15);
		} else if (id.length() < 15) {
			int tmep = 15 - id.length();
			id = id + getRandomCodeStr(tmep);
		}
		return id;
	}

	/**
	 * 随机四位
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomCodeStr(Integer length) {
		Set<Integer> set = getRandomNumber(length);
		// 使用迭代器
		Iterator<Integer> iterator = set.iterator();
		// 临时记录数据
		String temp = "";
		while (iterator.hasNext()) {
			temp += iterator.next();
		}
		return temp;
	}

	/**
	 * 获取一个四位随机数，并且四位数不重复
	 * 
	 * @return Set<Integer>
	 */
	private static Set<Integer> getRandomNumber(Integer length) {
		// 使用SET以此保证写入的数据不重复
		Set<Integer> set = new HashSet<Integer>();
		// 随机数
		Random random = new Random();

		while (set.size() < length) {
			// nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
			// 和指定值（不包括）之间均匀分布的 int 值。
			set.add(random.nextInt(10));
		}
		return set;
	}

	/**
	 * 去零计算
	 * 
	 * @param temp1
	 * @return
	 */
	public static String stringProcessing(String temp1) {
		if (temp1.trim().equals("") || temp1.trim().equals("0.0") || Double.parseDouble(temp1.trim()) == 0) {
			return "0.00";
		} else {
			double amountCount;
			try {
				amountCount = Double.parseDouble(temp1);
			} catch (Exception e) {
				e.printStackTrace();
				amountCount = 0;
			}
			String temp = new java.text.DecimalFormat(".00").format(amountCount);
			if (temp.equals("0") || temp.equals("0.0") || temp.equals("0.00") || temp.equals(".00")
					|| temp.equals(".0")) {
				temp = "";
			}
			if (temp.indexOf(".") == 0) {
				temp = temp.replace(".", "0.");
			}
			return temp;
		}
	}

	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();

	}

	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();

	}

	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.multiply(b2).doubleValue();

	}

	private static final int DEF_DIV_SCALE = 10;

	public static double div(double d1, double d2) {

		return div(d1, d2, DEF_DIV_SCALE);

	}

	public static double div(double d1, double d2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 播放系统默认提示音
	 * 
	 * @param context
	 */
	public static void playRingtone(Context context) {
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}

	/**
	 * 播放系统默认震动
	 * 
	 * @param context
	 */
	public static void playVibrator(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
	}

	/**
	 * 判断字符串是否超长
	 * 一个中文算两个字符  字母算一个字符   符号和数字算一个字符
	 * @param result
	 *            字符串的长度
	 * @param max
	 *            最大长度
	 * @return true 没有超长 false 已经超长
	 */
	public static synchronized boolean strIsBeyond(String result, int max) {
		if (!TextUtils.isEmpty(result)) {
			int length = result.length();
			float is_beyond = 0;
			char[] cc = new char[1];
			int strLength = 0;
			String str = null;
			char result_item;
			for (int i = 0; i < length; i++) {
				result_item = result.charAt(i);
				cc[0] = result_item;
				str = new String(cc);
				strLength = str.getBytes().length;
				switch (strLength) {
				case 3: {// 一个中文
					is_beyond += 2;
					if (is_beyond > max) {
						return false;
					}
					break;
				}
				case 2: {// 一个字母
					is_beyond += 1;
					if (is_beyond > max) {
						return false;
					}
					break;
				}
				case 1: {// 一个符号 或者 数字
					is_beyond += 1;
					if (is_beyond > max) {
						return false;
					}
					break;
				}

				default:
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取字符串的长度
	 * 一个中文算两个字符  字母算一个字符   符号和数字算一个字符
	 * @param result
	 * @return
	 */
	public static int getStringLenght(String result){
		if (!TextUtils.isEmpty(result)) {
			int length = result.length();
			int is_beyond = 0;
			char[] cc = new char[1];
			int strLength = 0;
			String str = null;
			char result_item;
			for (int i = 0; i < length; i++) {
				result_item = result.charAt(i);
				cc[0] = result_item;
				str = new String(cc);
				strLength = str.getBytes().length;
				switch (strLength) {
				case 3: {// 一个中文
					is_beyond += 2;
					break;
				}
				case 2: {// 一个字母
					is_beyond += 1;
					break;
				}
				case 1: {// 一个符号 或者 数字
					is_beyond += 1;
					break;
				}

				default:
					break;
				}
			}
			return is_beyond;
		}
		
		return 0;
	}

	/**
	 * 防重复点击
	 * 
	 * @return true 表示两次点击时间小于500毫秒 false表示两次点击时间大于500毫秒
	 */
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 200) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void setToast(Toast toast) {
		if (sToast != null)
			sToast.cancel();
		sToast = toast;
	}

	public static void cancelToast(Toast toast) {
		if (toast != null)
			toast.cancel();
		toast = null;
	}
	
	/**
	 * 设置文字字体
	 * 如果要改变Button的字体 必须使用此方法
	 * @param context 上下文对象
	 * @param view 按钮
	 * @param text 文本
	 * @param path 字体文件路径
	 */
	public static void setTextTypeFace(Context context,TextView view,String text,String path){
		view.setText(text);
		setTextTypeFace(context, view, path);
	}
	
	/**
	 * 设置文字字体
	 * @param context 上下文对象
	 * @param view 文件控件（TextView RadioButton等）
	 * @param path 字体文件路径
	 */
	public static void setTextTypeFace(Context context,TextView view,String path){
		Typeface customFont = customFonts.get(path);
		if(customFont == null){
			customFont = Typeface.createFromAsset(context.getAssets(), path);
			customFonts.put(path, customFont);
		}
		view.setTypeface(customFont);
	}

}
