package com.google.easyquranmemorizerhelper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
/**
 * 
 * @author Shoaib Khan
 *
 */
public class Logger {

	public static void makeToast(Context context,String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	public static void logMessage(String message)
	{
		Log.v("logMSG",message);
	}
}
