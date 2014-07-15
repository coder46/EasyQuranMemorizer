package com.google.easyquranmemorizer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.easyquranmemorizer.R;
import com.google.easyquranmemorizerhelper.CONSTANT;
import com.google.easyquranmemorizerhelper.MyDialogBuilder;
import com.google.easyquranmemorizerhelper.MyPreferenceHandler;
/**
 * 
 * @author Shoaib Khan
 *
 */
public class Settings extends Activity {

	TextView translationTextView;
	TextView appearanceTextView;
	ScrollView outerScrollView;
	TextView languageSelection;
	CheckBox showTranslation;
	TextView showTranslationTextView;
	TextView theme;
	// TextView arabicFont;
	TextView arabicFontSize;
	TextView translatedFontSize;
	CheckBox keepScreenOn;
	TextView KeepScreenOnTextView, other, storageOption;
	int prevSetting;

	View dark1, dark2, light1, light2, light3, light4, dark3;

	/*
	 * All the dialog are implemented in MyDialogBuilder.java apart from this
	 * dialog. This function needs to access to all view variables of this
	 * settings because it is required to change the theme of settings as soon
	 * as user changes theme. Passing all these variables would be inappropriat,
	 * therefore I have declared this function in this class itself
	 */
	private void buildThemeSwitchinDialog() {
		final Context myContext = this;
		ArrayList<String> items = new ArrayList<String>();

		items.add("Light");
		items.add("Dark");
		items.add("Creamy");

		int checkedItem = MyPreferenceHandler.getTheme(myContext);
		// Where we track the selected items
		AlertDialog.Builder builder = MyDialogBuilder.getBuilder(myContext);
		// Set the dialog title
		builder.setTitle("Theme")
		// Specify the list array, the items to be selected by default (null for
		// none),
		// and the listener through which to receive callbacks when items are
		// selected
				.setSingleChoiceItems(
						items.toArray(new CharSequence[items.size()]),
						checkedItem, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								if (which == 0)
									MyPreferenceHandler.setTheme(myContext,
											MyPreferenceHandler.WHITE_THEME);
								else if (which == 1)
									MyPreferenceHandler.setTheme(myContext,
											MyPreferenceHandler.DARK_THEME);
								else
									MyPreferenceHandler.setTheme(myContext,
											MyPreferenceHandler.MUSHAF_THEME);
								switchTheme();
								dialog.dismiss();

							}
						})
				// Set the action buttons
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		builder.create();
		builder.show();

	}

	//Switches the theme  such as Dark,Light and Creamy.
	private void switchTheme() {
		if (MyPreferenceHandler.getTheme(getApplicationContext()) != prevSetting) {
			prevSetting = MyPreferenceHandler.getTheme(getApplicationContext());
			int theme = MyPreferenceHandler.getTheme(getApplicationContext());
			if (theme == MyPreferenceHandler.DARK_THEME) {
				outerScrollView.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_BACKGROUND_COLOUR));
				translationTextView.setTextColor(Color.WHITE);
				appearanceTextView.setTextColor(Color.WHITE);
				languageSelection.setTextColor(Color.WHITE);
				this.theme.setTextColor(Color.WHITE);
				// arabicFont.setTextColor(Color.WHITE);
				arabicFontSize.setTextColor(Color.WHITE);
				translatedFontSize.setTextColor(Color.WHITE);
				KeepScreenOnTextView.setTextColor(Color.WHITE);
				showTranslationTextView.setTextColor(Color.WHITE);
				other.setTextColor(Color.WHITE);
				storageOption.setTextColor(Color.WHITE);
				light1.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				light2.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				light3.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				light4.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				dark1.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				dark2.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
				dark3.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_DIVIDER));
			} else {

				if (theme == MyPreferenceHandler.WHITE_THEME) {
					outerScrollView.setBackgroundColor(Color
							.parseColor(CONSTANT.WHITE_BACKGROUND_COLOUR));

				} else {
					outerScrollView.setBackgroundColor(Color
							.parseColor(CONSTANT.MUSHAF_BACKGROUND_COLOUR));

				}
				translationTextView.setTextColor(Color.BLACK);
				appearanceTextView.setTextColor(Color.BLACK);
				languageSelection.setTextColor(Color.BLACK);
				this.theme.setTextColor(Color.BLACK);
				// arabicFont.setTextColor(Color.BLACK);
				arabicFontSize.setTextColor(Color.BLACK);
				translatedFontSize.setTextColor(Color.BLACK);
				KeepScreenOnTextView.setTextColor(Color.BLACK);
				showTranslationTextView.setTextColor(Color.BLACK);
				other.setTextColor(Color.BLACK);
				storageOption.setTextColor(Color.BLACK);
				light1.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				light2.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				light3.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				light4.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				dark1.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				dark2.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
				dark3.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_DIVIDER));
			}
		}
	}

	public void myClickListener(View view) {
		switch (view.getId()) {

		// case R.id.settings_arabic_font:
		// buildArabicFontDialog();
		// break;
		case R.id.settings__storage_location:
			MyDialogBuilder.buildStorageOptionDialog(this);
			break;
		case R.id.settings_arabic_font_size:
			MyDialogBuilder.buildArbicFontSizeDialog(this);
			break;

		case R.id.settings_theme:
			buildThemeSwitchinDialog();
			break;
		case R.id.settings_translation_font_size:
			MyDialogBuilder.buildTranslationFontSizeDialog(this);
			break;
		case R.id.settings_translation_language:
			MyDialogBuilder.buildTranslationFontDialog(this);
			break;
		case R.id.settings_keep_screen_on:
			MyPreferenceHandler.setKeepScreenOn(getApplicationContext(),
					((CheckBox) view).isChecked());
			break;
		case R.id.settings_show_translation:
			MyPreferenceHandler.setTranslationAllowed(getApplicationContext(),
					((CheckBox) view).isChecked());

			break;
		case R.id.settings_keep_screen_on_textView:
			keepScreenOn.toggle();
			MyPreferenceHandler.setKeepScreenOn(getApplicationContext(),
					keepScreenOn.isChecked());

			break;
		case R.id.settings_show_translation_text_view:
			showTranslation.toggle();
			MyPreferenceHandler.setTranslationAllowed(getApplicationContext(),
					showTranslation.isChecked());
			break;
		}
	}

	private void initialize() {
		languageSelection = (TextView) findViewById(R.id.settings_translation_language);
		showTranslation = (CheckBox) findViewById(R.id.settings_show_translation);
		theme = (TextView) findViewById(R.id.settings_theme);
		// arabicFont = (TextView) findViewById(R.id.settings_arabic_fo);
		arabicFontSize = (TextView) findViewById(R.id.settings_arabic_font_size);
		translatedFontSize = (TextView) findViewById(R.id.settings_translation_font_size);
		keepScreenOn = (CheckBox) findViewById(R.id.settings_keep_screen_on);
		translationTextView = (TextView) findViewById(R.id.settings_translation_text_view);
		appearanceTextView = (TextView) findViewById(R.id.settings_appearance_text_view);
		outerScrollView = (ScrollView) findViewById(R.id.settings_outer_layout);
		KeepScreenOnTextView = (TextView) findViewById(R.id.settings_keep_screen_on_textView);
		showTranslationTextView = (TextView) findViewById(R.id.settings_show_translation_text_view);
		dark1 = findViewById(R.id.settings_view_dark_1);
		dark2 = findViewById(R.id.settings_view_dark2);
		light1 = findViewById(R.id.settings_view_light_1);
		light2 = findViewById(R.id.settings_view_light2);
		light3 = findViewById(R.id.settings_view_light_3);
		light4 = findViewById(R.id.settings_view_light_4);
		dark3 = findViewById(R.id.settings_view_dark_3);
		storageOption = (TextView) findViewById(R.id.settings__storage_location);
		other = (TextView) findViewById(R.id.settings_other_text_view);
		this.setTitle("Settings");
		keepScreenOn.setChecked(MyPreferenceHandler
				.isKeepScreenOn(getApplicationContext()));
		showTranslation.setChecked(MyPreferenceHandler
				.isTranslationAllowed(getApplicationContext()));

	}

	@Override
	protected void onResume() {

		switchTheme();
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		prevSetting = -1;
		initialize();
	}

}
