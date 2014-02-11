package com.monsieurchak.baidumapdemo.Util;

import android.text.format.Time;

public class Util {

	/**
	 * 通过前面补零的方法，将不够长度的数字转化成目标长度后以String的形式返回
	 * @param sourceDate 源数据
	 * @param formatLength 目标长度
	 * @return
	 */
	public static String frontCompWithZore(long sourceDate,int formatLength)  
	{  
		String newString = String.format("%0"+formatLength+"d", sourceDate);  
		return  newString;  
	} 
	
	/**
	 * 获取当前时间
	 * @return 当前时间String
	 */
	public static String getCurrentTime(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		return "" + hour + ":" + minute + ":" + second + "  ";
	}
	
	/**
	 * 根据所给的字符串获取前半部的数字，以Long返回
	 * 注意：源数据必须满足长度为22
	 * @param source
	 * @return
	 */
	public static long getFirst(String source){
		int len = source.length()/2;
		if (source.length()%2 != 0) {
			return 0;
		}
		byte [] src = source.getBytes();
		
		long first = 0;
		long cache = (long) 1E10;
		for (int i = 0; i < len; i++) {
			first = Long.valueOf((src[i]-48) * cache) + first;
			cache/=10;
		}
		return first;
	}
	
	/**
	 * 根据所给的字符串获取后半部的数字，以Long返回
	 * 注意：源数据必须满足长度为22
	 * @param source
	 * @return
	 */
	public static long getLast(String source){
		int len = source.length()/2;
		if (source.length()%2 != 0) {
			return 0;
		}
		byte [] src = source.getBytes();
		
		long first = 0;
		long cache = (long) 1E10;
		for (int i = len; i < len*2; i++) {
			first = Long.valueOf((src[i]-48) * cache) + first;
			cache/=10;
		}
		return first;
	}
}
