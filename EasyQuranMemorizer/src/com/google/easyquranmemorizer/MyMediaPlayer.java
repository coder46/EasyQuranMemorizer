package com.google.easyquranmemorizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.easyquranmemorizer.R;
import com.google.easyquranmemorizerhelper.CONSTANT;
import com.google.easyquranmemorizerhelper.ExternalStorage;
import com.google.easyquranmemorizerhelper.HelperFunction;
import com.google.easyquranmemorizerhelper.MyDialogBuilder;
import com.google.easyquranmemorizerhelper.MyPreferenceHandler;
import com.google.easyquranmemorizerhelper.ReciterDataManger;
import com.google.easyquranmemorizerhelper.Surah;

/**
 * This class if the heart of the Application and most difficult to understand.
 * Anyway I have tried my best to write readable code.
 * 
 * @author Shoaib Khan
 * 
 */
public class MyMediaPlayer extends Activity {

	// public TextView startTimeField, endTimeField;
	private ImageButton playButton, next, prev, repeat;
	private SeekBar seekbar;
	private TextView surahText;
	private MediaPlayer mediaPlayer;

	// These variables are used for seeking
	private double startTime = 0, finalTime = 0;

	private Handler myHandler = new Handler();;

	// When looping is switched on, it sets to true.
	boolean looping = false;

	// Points to current playing verse
	private Integer verseNumber = 0;

	// True when media player is playing otherwise false.
	private boolean isPlaying = false;

	/*
	 * This runs after each 100 ms and updates the seekbar.
	 */
	private Runnable updateTime = new Runnable() {
		@Override
		public void run() {
			try {
				if (mediaPlayer != null) {

					startTime = mediaPlayer.getCurrentPosition();
					seekbar.setProgress((int) startTime);
					myHandler.postDelayed(this, 100);
				}
			} catch (Exception e) {
			}
		}
	};

	private Spinner reciterSpinner, startVerseSpinner, endVerseSpinner;

	// List of all reciter whose recitation is present for current surah.
	private List<String> downloadedReciterList;

	// Points to current surah.
	private Surah surah;

	// Stores the selected Author Name. User selects authorName from
	// reciterSpinner
	private String selectedAuthorName;

	// This stores the starting verse number selected from startVerseSpinner
	private int startingVerse = 0;

	// IT points to endVerse selected from endVerseSpinner
	private int endVerse;

	private TextView headingView, selectReciterTextView;
	private View viewLine;
	private ScrollView outerScrollView;
	private static final String START_VERSE = "Select Start Verse";
	private static final String END_VERSE = "Select End Verse";
	private ImageButton resetButton;
	private TextView translatioView;

	private ArrayList<Integer> verseCount;
	// Translation text for each verse stored as a list.
	private ArrayList<String> translatedTextList;
	private PowerManager.WakeLock mWakeLock;
	int previousTranslationLanguage;

	int prevTheme = -1;

	/*
	 * This function has been overridden to tackle the changes in the setting
	 * which would can reflect here too such as hiding translaton bar or
	 * changing the theme or changing the text size or changing the translation
	 * size or keeping screen on.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		// acquireWakeLock if KeepScreenOn is selected by user
		if (MyPreferenceHandler.isKeepScreenOn(getApplicationContext())) {
			acquireWakeLock();
		} else {
			releaseWakeLock();
		}

		// Hiding TranslationView if selected
		if (!MyPreferenceHandler.isTranslationAllowed(getApplicationContext())) {
			translatioView.setVisibility(View.GONE);
		} else {
			if (previousTranslationLanguage != MyPreferenceHandler
					.getTranslatedLanguage(getApplicationContext())) {
				previousTranslationLanguage = MyPreferenceHandler
						.getTranslatedLanguage(getApplicationContext());
				loadTranslationData();
			}
			translatioView.setVisibility(View.VISIBLE);

		}
		surahText.setTextSize(MyPreferenceHandler
				.getArabicFontSize(getApplicationContext()));
		if (MyPreferenceHandler.isTranslationAllowed(getApplicationContext())) {

			int size = MyPreferenceHandler
					.getTranslatedFontSize(getApplicationContext());

			translatioView.setTextSize(size);

			translatioView.setText(translatedTextList.get(verseNumber));
		}

		// setting media player theme
		int theme = MyPreferenceHandler.getTheme(getApplicationContext());
		if (theme != prevTheme) {
			if (theme == MyPreferenceHandler.DARK_THEME) {
				outerScrollView.setBackgroundColor(Color
						.parseColor(CONSTANT.BLACK_BACKGROUND_COLOUR));
				headingView.setTextColor(Color.WHITE);
				selectReciterTextView.setTextColor(Color.WHITE);
				surahText.setBackgroundResource(R.drawable.text_border_dark);
				surahText.setTextColor(Color.WHITE);
				translatioView.setTextColor(Color.WHITE);
				translatioView
						.setBackgroundResource(R.drawable.text_border_dark);
				viewLine.setBackgroundColor(Color.WHITE);
				// mySpinner.setBackground(R.drawable.myspinner_black_background));
				// startVerseSpinner.setBackground(R.drawable.myspinner_black_background));
				// endVerseSpinner.setBackground(R.drawable.myspinner_black_background));
				//
				reciterSpinner
						.setBackgroundResource(R.drawable.myspinner_background);
				startVerseSpinner
						.setBackgroundResource(R.drawable.myspinner_background);
				endVerseSpinner
						.setBackgroundResource(R.drawable.myspinner_background);
			} else {
				if (theme == MyPreferenceHandler.WHITE_THEME) {
					reciterSpinner
							.setBackgroundResource(R.drawable.myspinner_background);
					startVerseSpinner
							.setBackgroundResource(R.drawable.myspinner_background);
					endVerseSpinner
							.setBackgroundResource(R.drawable.myspinner_background);
					outerScrollView.setBackgroundColor(Color
							.parseColor(CONSTANT.WHITE_BACKGROUND_COLOUR));

				} else {
					reciterSpinner
							.setBackgroundResource(R.drawable.myspinner_mushaf_background);
					startVerseSpinner
							.setBackgroundResource(R.drawable.myspinner_mushaf_background);
					endVerseSpinner
							.setBackgroundResource(R.drawable.myspinner_mushaf_background);
					outerScrollView.setBackgroundColor(Color
							.parseColor(CONSTANT.MUSHAF_BACKGROUND_COLOUR));

				}
				viewLine.setBackgroundColor(Color.BLACK);

				// mySpinner.setBackgroundColor(Color.WHITE);
				// mySpinner.setPopupBackgroundResource(R.drawable.white_background);
				// startVerseSpinner.setBackgroundColor(Color.WHITE);
				surahText.setTextColor(Color.BLACK);
				translatioView.setTextColor(Color.BLACK);
				headingView.setTextColor(Color.BLACK);
				selectReciterTextView.setTextColor(Color.BLACK);
				surahText.setBackgroundResource(R.drawable.text_border_white);
				translatioView
						.setBackgroundResource(R.drawable.text_border_white);
			}

		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mediaPlayer.stop();
		// mediaPlayer.release();
		// mediaPlayer.release();
	}

	@Override
	protected void onDestroy() {
		// release mediaPlayer if not already destroyed and release wakelock too
		if (mediaPlayer != null)
			mediaPlayer.release();
		releaseWakeLock();
		super.onDestroy();

	}

	/*
	 * acquireWakeLock if not held
	 */
	private void acquireWakeLock() {

		if (!this.mWakeLock.isHeld())
			this.mWakeLock.acquire();
	}

	/*
	 * release wakeLock if held
	 */
	private void releaseWakeLock() {
		if (this.mWakeLock.isHeld())
			this.mWakeLock.release();
	}

	/*
	 * This function traverse the translation file located in the assests folder
	 * and populate the translatedTextList variable which stores translation
	 * data for each verse as list.
	 */
	private void loadTranslationData() {
		translatedTextList = new ArrayList<String>();
		int language = MyPreferenceHandler
				.getTranslatedLanguage(getApplicationContext());

		String fileName = "";
		switch (language) {
		case MyPreferenceHandler.ENGLISH:
			fileName = CONSTANT.ENGLISHTRANSLATED;
			break;
		case MyPreferenceHandler.MALAYSIAN:
			fileName = CONSTANT.MALAYSIANTRANSLATED;
			break;
		case MyPreferenceHandler.INDONESIAN:
			fileName = CONSTANT.INDONESIANTRANSLATED;
			break;
		case MyPreferenceHandler.HINDI:
			fileName = CONSTANT.HINDITRANSLATED;
			break;
		}
		int surahNumber = surah.getSurahNumber();

		/**
		 * These two variables are used to fastly load the translation data.
		 * verseCountTillCrntSurahNumber has the count of no of verse before
		 * this surah.verseCountAfterCrntSurahNumber has the verse count till
		 * the next surah. These two variables pin point to actual line number
		 * in the translation file which should be processed further.
		 */
		int verseCountTillCrntSurahNumber = 0, verseCountAfterCrntSurahNumber = 0, i;

		for (i = 1; i < surahNumber; i++)
			verseCountTillCrntSurahNumber += verseCount.get(i - 1);
		verseCountAfterCrntSurahNumber = verseCountTillCrntSurahNumber
				+ verseCount.get(i - 1);

		AssetManager assetManager = this.getAssets();
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = assetManager.open(fileName);
			reader = new BufferedReader(new InputStreamReader(is));
			for (i = 0; i < verseCountAfterCrntSurahNumber; i++) {
				String content = reader.readLine();
				if (i < verseCountTillCrntSurahNumber)
					continue;
				String tmp[] = content.trim().split("\\|");
				translatedTextList.add(tmp[2]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * Sets looping if not or vice versa and change the repeat icon accordingly
	 */
	private void toggleLooping() {
		if (looping) {
			looping = false;
			repeat.setImageResource(R.drawable.ic_action_repeat);

		} else {
			looping = true;
			repeat.setImageResource(R.drawable.ic_action_forward);

		}
	}

	/*
	 * Click Listener
	 */
	public void myClickListener(View v) {
		switch (v.getId()) {
		case R.id.media_player_next:
			playNext();
			break;

		case R.id.media_player_play:
			if (!isPlaying) {
				play();
			} else {
				isPlaying = false;
				playButton.setImageResource(android.R.drawable.ic_media_play);

				mediaPlayer.pause();
			}
			break;
		case R.id.media_player_prev:
			playPrev();
			break;
		case R.id.media_player_restart:
			resetVerse();
			play();
			if (MyPreferenceHandler
					.getRestartButtonValue(getApplicationContext())) {
				MyPreferenceHandler.setRestartButtonValue(
						getApplicationContext(), false);
				MyDialogBuilder.buildRestartDialog(this);
			}
			break;
		case R.id.media_player_repeat:
			toggleLooping();
			mediaPlayer.setLooping(looping);

			if (MyPreferenceHandler
					.getRepeatButtonValue(getApplicationContext())) {
				MyPreferenceHandler.setRepeatButtonValue(
						getApplicationContext(), false);
				MyDialogBuilder.buildRepeatDialog(this);
			}
			break;
		}
	}

	private void playPrev() {

		// PlayOnly if the prev verse (verseNumber -1) is less than
		// startingVerse selected by user.
		if (verseNumber - 1 >= startingVerse) {

			try {
				setVerseData(verseNumber - 1);
				decrementVerse();
				play();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*
	 * increment the current playing verse, it also toggles the state of button.
	 * State changes in the button were previously visible but it does not work
	 * now(Becuase of custom icon background ). I guess we would have to change
	 * the background of button when it is disabled by setEnabled(false).
	 * Anyway, it does not afflict the functionality and can be solved on later
	 * stage
	 */
	private void incrementVerse() {
		if (!prev.isEnabled())
			prev.setEnabled(true);

		if (verseNumber == endVerse)
			return;
		else if (verseNumber == endVerse - 1) {
			next.setEnabled(false);
		}
		verseNumber++;

	}

	/*
	 * similiar to above
	 */
	private void decrementVerse() {
		if (!next.isEnabled())
			next.setEnabled(true);
		if (verseNumber == startingVerse)
			return;
		else if (verseNumber == startingVerse + 1) {
			prev.setEnabled(false);
		}
		verseNumber--;

	}

	/*
	 * plays the next verse similiar to playPrev
	 */
	private void playNext() {
		if (verseNumber + 1 <= endVerse) {

			try {
				setVerseData(verseNumber + 1);
				incrementVerse();
				play();
			} catch (IllegalArgumentException | IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// If it has crossed the endVerse limit then reset the player to
			// startingVerse.
			resetVerse();
			play();
		}

	}

	private void play() {
		isPlaying = true;
		// Show pause button when playing
		playButton.setImageResource(android.R.drawable.ic_media_pause);

		mediaPlayer.start();
		finalTime = mediaPlayer.getDuration();
		startTime = mediaPlayer.getCurrentPosition();
		seekbar.setMax((int) finalTime);

		seekbar.setProgress((int) startTime);
		myHandler.postDelayed(updateTime, 100);

	}

	/*
	 * reset verseNumber and disable prev button (since it cannot move further
	 * backward).
	 */
	private void resetVerse() {
		verseNumber = startingVerse;
		if (!next.isEnabled())
			next.setEnabled(true);
		prev.setEnabled(false);
		setVerseData(verseNumber);
	}

	/*
	 * returns the fileLocation of the given verse.
	 */
	private String getVerseAudioPath(int verseNumber) {

		ArrayList<String> list = ExternalStorage.getSdAndExternalStorage();
		for (String string : list) {
			String path = string
					+ "/"
					+ CONSTANT.AUDIODIRECTORYLOCATION
					+ "/"
					+ surah.getSurahNumber()
					+ "/"
					+ selectedAuthorName
					+ "/"
					+ HelperFunction
							.convertNumberTo3LengthString(verseNumber + 1)
					+ ".mp3";
			if (new File(path).exists())
				return path;
		}
		return null;

	}

	/*
	 * It reset the mediaPlayer and sets it data source to current verse audio
	 * File. It also sets the surahText and translated text if shown.
	 */
	private void setVerseData(int verseNumber) {

		try {

			String audioFile = getVerseAudioPath(verseNumber);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(audioFile);
			mediaPlayer.prepare();
			String textData = surah.getSurahText(verseNumber);
			surahText.setTypeface(Typeface.createFromAsset(getAssets(),
					"Roboto-Regular.ttf"));
			surahText.setText(textData);

			surahText.setTextSize(MyPreferenceHandler
					.getArabicFontSize(getApplicationContext()));
			if (MyPreferenceHandler
					.isTranslationAllowed(getApplicationContext())
					&& translatedTextList != null
					&& translatedTextList.size() == surah.getVerseCount()) {

				int size = MyPreferenceHandler
						.getTranslatedFontSize(getApplicationContext());

				translatioView.setTextSize(size);

				translatioView.setText(translatedTextList.get(verseNumber));
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initializeReciterSpinner()

	{

		ReciterDataManger manager = new ReciterDataManger(
				getApplicationContext());
		downloadedReciterList = manager.getDownloadedReciterList(surah);

		// setting reciter spinner list
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, downloadedReciterList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		reciterSpinner.setAdapter(dataAdapter);
		reciterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				selectedAuthorName = downloadedReciterList.get(position);
				// if (selectedAuthorName != CONSTANT.AUDIO_NOT_FOUND) {
				// resetVerse();
				// play();
				// }
				resetVerse();
				play();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	private void initiliazeVaraibles() throws IllegalArgumentException,
			IllegalStateException, IOException {
		surahText = (TextView) findViewById(R.id.media_player_text_view);
		next = (ImageButton) findViewById(R.id.media_player_next);
		playButton = (ImageButton) findViewById(R.id.media_player_play);
		prev = (ImageButton) findViewById(R.id.media_player_prev);
		repeat = (ImageButton) findViewById(R.id.media_player_repeat);
		resetButton = (ImageButton) findViewById(R.id.media_player_restart);
		translatioView = (TextView) findViewById(R.id.media_player_translation_view);
		seekbar = (SeekBar) findViewById(R.id.media_player_seek);
		startVerseSpinner = (Spinner) findViewById(R.id.media_player_start_verse_spinner);
		endVerseSpinner = (Spinner) findViewById(R.id.media_player_end_verse_spinner);
		reciterSpinner = (Spinner) findViewById(R.id.media_player_author_list);
		headingView = (TextView) findViewById(R.id.media_player_heading);
		outerScrollView = (ScrollView) findViewById(R.id.media_player_outer_scroll_view);
		viewLine = findViewById(R.id.media_player_view);
		selectReciterTextView = (TextView) findViewById(R.id.media_player_select_reciter_text_view);
		// initializeWakeLock
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"My Tag");

		// ///setting heading text
		String text = surah.getSurahNumber() + ". " + surah.getName() + "\n"
				+ "( " + surah.getEnglishTranslatedName() + " )" + "\n\n"
				+ CONSTANT.BISMILLAH;
		headingView.setText(text);

		// previous translation language to keep track of
		previousTranslationLanguage = MyPreferenceHandler
				.getTranslatedLanguage(getApplicationContext());
		loadTranslationData();

		initializeReciterSpinner();

		// setting MediaPlayer and instantiating data
		mediaPlayer = new MediaPlayer();
		endVerse = surah.getVerseCount() - 1;

		selectedAuthorName = downloadedReciterList.get(0);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			// this will run on the completion of each verse
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (!looping)
					playNext();
				else {
					setVerseData(verseNumber);

					mediaPlayer.setLooping(looping);
					play();
				}
			}
		});
		resetVerse();

		initializeSpinnerAndSeekbar();

		// setSurahImage(imageFile);
		// play();

	}

	private void initializeSpinnerAndSeekbar() {
		// setting startVerse spinner
		List<String> startVerseList = new ArrayList<String>();
		startVerseList.add(START_VERSE);
		for (int i = 1; i <= endVerse + 1; i++)
			startVerseList.add(String.valueOf(i));
		ArrayAdapter<String> startVerseAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, startVerseList);
		startVerseAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		startVerseSpinner.setAdapter(startVerseAdapter);
		startVerseSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (position == 0) {
							startingVerse = 0;
						} else {
							if (position - 1 <= endVerse)
								startingVerse = position - 1;
							else {
								Toast.makeText(
										getApplicationContext(),
										"Starting Verse should not be more than End verse",
										Toast.LENGTH_LONG).show();
								startVerseSpinner
										.setSelection(startingVerse + 1);
								return;
							}
						}
						resetVerse();
						play();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		// setting endVerse Spinner
		List<String> endVerseList = new ArrayList<String>();
		endVerseList.add(END_VERSE);
		for (int i = 1; i <= endVerse + 1; i++)
			endVerseList.add(String.valueOf(i));
		ArrayAdapter<String> endVerseAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, endVerseList);
		endVerseAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		endVerseSpinner.setAdapter(endVerseAdapter);
		endVerseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {

					endVerse = surah.getVerseCount() - 1;
				} else {
					if (position - 1 >= startingVerse) {
						endVerse = position - 1;
					} else {
						Toast.makeText(
								getApplicationContext(),
								"End Verse should not be less than starting verse",
								Toast.LENGTH_LONG).show();
						endVerseSpinner.setSelection(endVerse + 1);

						return;
					}
				}
				if (endVerse <= verseNumber) {
					resetVerse();
					play();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// setting seekbar and it's listener
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public boolean userTouch = false;

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

				userTouch = false;
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				userTouch = true;
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {

				if (userTouch)
					mediaPlayer.seekTo(progress);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player);
		Intent intent = getIntent();

		surah = (Surah) intent.getSerializableExtra("surah");
		Bundle bundle = intent.getExtras();
		if (bundle != null)
			verseCount = bundle.getIntegerArrayList("surahVerseCount");

		try {

			// getActionBar().setDisplayHomeAsUpEnabled(true);

			initiliazeVaraibles();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
