package com.intelligencencu.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * 仅仅是将toast封装了下，也没啥
 * @author root
 *
 */
public class ToastUntil {
	/*
	 * showShortToast
	 * @描述: 短时间显示文本提示框
	 * */
	public static void showShortToast(Context context, String text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/*
	 * * showLongToast
	 * @描述: 长时间显示文本提示框
	 * */
	public static void showLongToast(Context context,String text){
		Toast tost = Toast.makeText(context, text, Toast.LENGTH_LONG);
		tost.show();
	}
}
