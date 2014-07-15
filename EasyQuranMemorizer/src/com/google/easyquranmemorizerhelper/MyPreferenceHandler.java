package com.google.easyquranmemorizerhelper;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @author Shoaib Khan
 *
 */
public class MyPreferenceHandler {
	
	// My Preference name
	private  final static String NAME = "pbuh";

	
	
	/// Preference key for tracking the last clicked location in the home page
	private final static String POSITION_SURAH="surah_position";
	
	
	//Keys for tracking for first time button clicked.
	private final static String RESTART_BUTTON_CICKED="isRestartButtonClicked";
	private final static String REPEAT_BUTTON_CICKED="isRepeatButtonClicked";
	private static String RATE_CLICKED="isRateUsclicked";

	
	//Stroage Location Preference key
	private final static String LOCATION="storage_location";
	
	
	//DEFAULT ARABIC FONT SIZE AND TRANSLATED FONT SIZE
	private final static int DEFAULT_ARABIC_FONT_SIZE=28;
	private final static int DEFAULT_TRANSLATION_FONT_SIZE=18;
	
	
	///Keys for refering to 
	private  final static String TRANSLATION = "translation_language";
	private  final static String  TRANSLATIONFONTSIZE = "translation_font_size";
	private final static String SHOWTRANSLATION = "show_translation";
	private  final static String THEME= "theme";
	private  final static String ARABICFONT = "arabic_font";
	private final static String ARABICFONTSIZE = "arabic_font_size";
	private final static String KEEPSCREENON = "keep_screen_on";
	
	
	// arabic font type
	public static final int SIMPLE = 0;
	public  static final int UTHMANI = 1;

	// translated Language
	public static final int ENGLISH = 0;
	public static final int HINDI = 1;
	public static final int INDONESIAN = 2;
	public static final int MALAYSIAN=3;
	// theme type
	public static final int WHITE_THEME = 0;
	public static final int DARK_THEME = 1;
	public static final int MUSHAF_THEME = 2;



	public static boolean isTranslationAllowed(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference
				.getBoolean(SHOWTRANSLATION, false);
	}

	public static void setTranslationAllowed(Context context,
			boolean translationAllowed) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(SHOWTRANSLATION,
				translationAllowed);
		editor.commit();
	}

	public static int getTranslatedLanguage(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getInt(TRANSLATION, ENGLISH);
	}

	public static void setTranslatedLanguage(Context context, int translatedLanguage) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(TRANSLATION, translatedLanguage);
		editor.commit();
	}

	public static int getTheme(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getInt(THEME, MUSHAF_THEME);
	}

	public static void setTheme(Context context, int themeArg) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(THEME, themeArg);
		editor.commit();
	}

	public static int getArabicFontSize(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getInt(ARABICFONTSIZE, DEFAULT_ARABIC_FONT_SIZE);
	}

	public static void setArabicFontSize(Context context, int arabicFontSizeArg) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(ARABICFONTSIZE, arabicFontSizeArg);
		editor.commit();
	}

	public static int getTranslatedFontSize(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getInt(TRANSLATIONFONTSIZE, DEFAULT_TRANSLATION_FONT_SIZE);
	}

	public static void setTranslatedFontSize(Context context, int translatedFontSizeArg) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(TRANSLATIONFONTSIZE,
				translatedFontSizeArg);
		editor.commit();
	}

	public static int getArabicFont(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getInt(ARABICFONT, SIMPLE);
	}

	public static void setArabicFont(Context context, int val) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(ARABICFONT, val);
		editor.commit();
	}

	public static boolean isKeepScreenOn(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getBoolean(KEEPSCREENON, false);
	}

	public static void setKeepScreenOn(Context context, boolean keepScreenOnArg) {
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(KEEPSCREENON, keepScreenOnArg);
		editor.commit();
	}

	public static void setRepeatButtonValue(Context context,boolean state)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(REPEAT_BUTTON_CICKED, state);
		editor.commit();
	}
	public static void setRestartButtonValue(Context context,boolean state)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(RESTART_BUTTON_CICKED, state);
		editor.commit();
	}
	
	public static boolean getRepeatButtonValue(Context context)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getBoolean(REPEAT_BUTTON_CICKED, true);
	}
	public static boolean getRestartButtonValue(Context context)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return preference.getBoolean(RESTART_BUTTON_CICKED, true);
	}
	
	
	public static String getLocationPreference(Context context)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		ArrayList<String> list=ExternalStorage.getSdAndExternalStorage();
		if(list.size()==1)
			return preference.getString(LOCATION, list.get(0));
		else
			return preference.getString(LOCATION, list.get(1));

			
	}
	public static void setLocationPreference(Context context,String location)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString(LOCATION, location);
		editor.commit();
	}
	//Get All Possible Arabic Font Array

	public static boolean isRateClicked(Context context)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return  preference.getBoolean(RATE_CLICKED, false);	
	}
	public static void setRateClicked(Context context,boolean state)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(RATE_CLICKED, state);
		editor.commit();
	}
	public static int getSurahPositionPreference(Context context)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		return  preference.getInt(POSITION_SURAH, 0);

			
	}
	public static void setSurahPositionPreference(Context context,int location)
	{
		SharedPreferences preference = context.getSharedPreferences(
				NAME, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt(POSITION_SURAH, location);
		editor.commit();
	}
	//Get All Possible Arabic Font Array
}
