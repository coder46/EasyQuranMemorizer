package com.google.easyquranmemorizer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.easyquranmemorizer.R;
import com.google.easyquranmemorizerhelper.AssestsReader;
import com.google.easyquranmemorizerhelper.CONSTANT;
import com.google.easyquranmemorizerhelper.CustomAdapter;
import com.google.easyquranmemorizerhelper.Logger;
import com.google.easyquranmemorizerhelper.MyDialogBuilder;
import com.google.easyquranmemorizerhelper.MyPreferenceHandler;
import com.google.easyquranmemorizerhelper.ReciterDataManger;
import com.google.easyquranmemorizerhelper.Surah;

/**
 * @author Shoaib Khan
 * 
 */
public class HomePage extends Activity {

	private ListView loadedSurahListView;
	ArrayList<Pair<String, String>> adapterList;
	private HomePage CustomListView;
	private CustomAdapter adapter;
	private ArrayList<Surah> surahList;
	LinearLayout layout;
	// for maintaining the versecount of each surah, It will be used my
	// mediaPlayer to locate the particular verse in the translated file. For
	// more details view translated files in the files
	private ArrayList<Integer> surahVerseCount;
	private int prevTheme;

	/**
	 * This function initializes all the variables such as views, buttons,
	 * listsView ...
	 */
	private void initiliazeVariables() {
		layout = (LinearLayout) findViewById(R.id.home_page_outer_layout);

		AssestsReader reader = new AssestsReader(getApplicationContext());
		surahList = reader.getSurahsList();
		adapterList = new ArrayList<Pair<String, String>>();
		surahVerseCount = new ArrayList<Integer>();

		for (Surah surah : surahList) {
			String first = surah.getSurahNumber() + ". " + surah.getName();
			adapterList.add(new Pair<String, String>(first, surah
					.getArabicName()));

			surahVerseCount.add(surah.getVerseCount());
		}
		CustomListView = this;
		loadedSurahListView = (ListView) findViewById(R.id.home_page_surahs_list);

	}

	@Override
	protected void onResume() {
		int theme = MyPreferenceHandler.getTheme(getApplicationContext());
		// change the theme if the choice of theme has been changed. This may
		// triggerd when user returns to homepage after reviewing settings
		if (theme != prevTheme) {
			switch (theme) {
			case MyPreferenceHandler.DARK_THEME:
				layout.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_BACKGROUND_COLOUR));
				loadedSurahListView.setDivider(new ColorDrawable(Color
						.parseColor(CONSTANT.BLACK_DIVIDER)));
				break;
			case MyPreferenceHandler.MUSHAF_THEME:
				layout.setBackgroundColor(Color
						.parseColor(CONSTANT.MUSHAF_BACKGROUND_COLOUR));
				loadedSurahListView.setDivider(new ColorDrawable(Color
						.parseColor(CONSTANT.WHITE_DIVIDER)));

				break;
			case MyPreferenceHandler.WHITE_THEME:
				layout.setBackgroundColor(Color
						.parseColor(CONSTANT.WHITE_BACKGROUND_COLOUR));
				loadedSurahListView.setDivider(new ColorDrawable(Color
						.parseColor(CONSTANT.WHITE_DIVIDER)));

				break;
			}
			loadedSurahListView.setDividerHeight(1);
			Resources res = getResources();
			adapter = new CustomAdapter(CustomListView, surahList, res);
			loadedSurahListView.setAdapter(adapter);
			loadedSurahListView.setSelection(MyPreferenceHandler
					.getSurahPositionPreference(getApplicationContext()));

		}
		super.onResume();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		prevTheme = -1;

		initiliazeVariables();
		// MyPreferenceHandler.setSurahPositionPreference(getApplicationContext(),
		// 0);
	}

	/*
	 * Called when list item is called.
	 */
	public void onItemClick(int mPosition) {
		int position = loadedSurahListView.getFirstVisiblePosition();
		MyPreferenceHandler.setSurahPositionPreference(HomePage.this, position);

		ReciterDataManger manager = new ReciterDataManger(
				getApplicationContext());
		Surah surah = surahList.get(mPosition);
		
		//If surah contains atleast one audio , then play otherwise throw a toast message
		if (manager.getDownloadedReciterList(surah).size() > 0) {
			Intent intent = new Intent(HomePage.this, MyMediaPlayer.class);
			intent.putExtra("surah", surah);
			intent.putExtra("surahVerseCount", surahVerseCount);
			startActivity(intent);
		} else {

			Logger.makeToast(
					HomePage.this,
					"Surah "
							+ surah.getName()
							+ " does not contain single audio\n Please Download atleast one recitation audio");
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			break;
		case R.id.menu_about_us:
			MyDialogBuilder.buildAboutUsDialog(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
