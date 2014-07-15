package com.google.easyquranmemorizerhelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.easyquranmemorizer.R;


/**
 * This class handles all the dialog used in this application execpt one Theme switching dialog.
 * @author Shoaib Khan
 *
 */
public class MyDialogBuilder {

	public static AlertDialog.Builder getBuilder(Context context) {
		AlertDialog.Builder builder = null;
		int theme = MyPreferenceHandler.getTheme(context);
		if (theme == MyPreferenceHandler.DARK_THEME) {
			builder = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_DARK);

		} else {
			builder = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);

		}
		return builder;

	}


	//This function searches for element in the list and returns the index.
	//It can also be change to list.indexOf(Object).
	
	private static int searchPosition(ArrayList<String> list, int element) {
		
		for (int pos = 0; pos < list.size(); pos++) {
			if (Integer.parseInt(list.get(pos).trim()) == element)
				return pos;
		}
		return 0;
	}

	public static void buildArabicFontDialog(final Context myContext) {

		ArrayList<String> items = new ArrayList<String>();

		items.add("Simple");
		items.add("Uthmani");

		int checkedItem = MyPreferenceHandler.getArabicFont(myContext);
		// Where we track the selected items
		AlertDialog.Builder builder = getBuilder(myContext);
		// Set the dialog title
		builder.setTitle("Arabic Font")
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
									MyPreferenceHandler.setArabicFont(
											myContext,
											MyPreferenceHandler.SIMPLE);
								else
									MyPreferenceHandler.setArabicFont(
											myContext,
											MyPreferenceHandler.UTHMANI);

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

	public static void buildTranslationFontDialog(final Context myContext) {

		ArrayList<String> items = new ArrayList<String>();

		items.add("English (Saheeh International)");
		items.add("Hindi (Muhammad Farooq & Ahmed) ");
		items.add("Indonesian 	(Bahasa Indonesia)");
		items.add("Malysian (Basmeih)");
		int checkedItem = MyPreferenceHandler.getTranslatedLanguage(myContext);
		// Where we track the selected items
		AlertDialog.Builder builder = getBuilder(myContext);
		// Set the dialog title
		builder.setTitle("Language")
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
									MyPreferenceHandler.setTranslatedLanguage(
											myContext,
											MyPreferenceHandler.ENGLISH);
								else if (which == 1)
									MyPreferenceHandler.setTranslatedLanguage(
											myContext,
											MyPreferenceHandler.HINDI);
								else if (which == 2)
									MyPreferenceHandler.setTranslatedLanguage(
											myContext,
											MyPreferenceHandler.INDONESIAN);
								else
									MyPreferenceHandler.setTranslatedLanguage(
											myContext,
											MyPreferenceHandler.MALAYSIAN);

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

		builder.show();

	}

	
	public static void buildTranslationFontSizeDialog(final Context myContext) {

		final ArrayList<String> items = new ArrayList<String>();
		items.add("14");
		items.add("16");
		items.add("18");
		items.add("20");
		items.add("22");
		items.add("24");

		int checkedItem = searchPosition(items,
				MyPreferenceHandler.getTranslatedFontSize(myContext));
		// Where we track the selected items
		AlertDialog.Builder builder = getBuilder(myContext);
		// Set the dialog title
		builder.setTitle("Translation Font Size")
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

								MyPreferenceHandler.setTranslatedFontSize(
										myContext, Integer.parseInt(items.get(
												which).trim()));
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

	public static void buildArbicFontSizeDialog(final Context myContext) {

		final ArrayList<String> items = new ArrayList<String>();
		items.add("24");
		items.add("26");
		items.add("28");
		items.add("30");
		items.add("32");
		items.add("34");
		items.add("36");
		items.add("38");
		items.add("40");

		int checkedItem = searchPosition(items,
				MyPreferenceHandler.getArabicFontSize(myContext));
		// Where we track the selected items
		AlertDialog.Builder builder = getBuilder(myContext);
		// Set the dialog title
		builder.setTitle("Arabic Font Size")
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

								MyPreferenceHandler.setArabicFontSize(
										myContext, Integer.parseInt(items.get(
												which).trim()));
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

	public static void buildDeleteSurah(final Context myContext,
			final Surah mySurah) {

		ReciterDataManger manager = new ReciterDataManger(myContext);
		ArrayList<String> reciterList = manager
				.getDownloadedReciterList(mySurah);

		if (reciterList.size() == 0) {
			Logger.makeToast(myContext, mySurah.getName()
					+ " does not have any audio file");
			return;
		}

		AlertDialog.Builder builderSingle = getBuilder(myContext);
		// builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select reciter to delete:  "
				+ mySurah.getName());
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				myContext, android.R.layout.select_dialog_singlechoice) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = (TextView) super.getView(position,
						convertView, parent);

				int theme = MyPreferenceHandler.getTheme(myContext);
				if (theme == MyPreferenceHandler.DARK_THEME) {
					textView.setTextColor(Color.WHITE);
				} else
					textView.setTextColor(Color.BLACK);

				return textView;
			}
		};
		for (String string : reciterList) {
			arrayAdapter.add(string);
		}
		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String reciter = arrayAdapter.getItem(which);

						deleteFiles(reciter);
					}

					private void deleteFiles(String reciter) {

						File path =new File( MyPreferenceHandler.getLocationPreference(myContext)+"/"+CONSTANT.AUDIODIRECTORYLOCATION
										+ "/"+ String.valueOf(mySurah.getSurahNumber())+ "/"+ reciter);
						File[] all = path.listFiles();
						int count = 0;
						for (File file : all) {

							if (file.delete())
								count++;
						}
						if (count == 0)
							Logger.makeToast(myContext,
									"Files couldn't be deleted");
						else if (count < all.length)
							Logger.makeToast(myContext,
									"Some Files couldn't be deleted");
						else
							Logger.makeToast(myContext,
									"All Files Have been successfully deleted");

					}
				});
		builderSingle.show();
	}

	public static void buildDownloadDialog(final Context myContext,
			final Surah mySurah) {
		ReciterDataManger manager = new ReciterDataManger(myContext);
		ArrayList<String> reciterList = manager	
				.getNotDownloadedReciterList(mySurah);
		AlertDialog.Builder builderSingle =getBuilder(myContext);

//		AlertDialog.Builder builderSingle = new AlertDialog.Builder(myContext);
		// builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select Reciter to download "
				+ mySurah.getName());
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				myContext, android.R.layout.select_dialog_singlechoice) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = (TextView) super.getView(position,
						convertView, parent);

				int theme = MyPreferenceHandler.getTheme(myContext);
				if (theme == MyPreferenceHandler.DARK_THEME) {
					textView.setTextColor(Color.WHITE);
				} else
					textView.setTextColor(Color.BLACK);

				return textView;
			}
		};
		for (String string : reciterList) {
			arrayAdapter.add(string);
		}
		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String reciter = arrayAdapter.getItem(which);

						DownloadManager manager = new DownloadManager(myContext);
						manager.downloadSurah(reciter, mySurah,
								mySurah.getVerseCount());
					}
				});
		builderSingle.show();
	}

	public static void buildRepeatDialog(Context context) {

		AlertDialog.Builder builderSingle = getBuilder(context);
		builderSingle.setCancelable(false);
		builderSingle.setMessage(CONSTANT.REPEAT_MESSAGE);
		builderSingle.setNegativeButton("Got It",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builderSingle.show();
	}

	public static void buildRestartDialog(Context context) {
		AlertDialog.Builder builderSingle = getBuilder(context);
		builderSingle.setCancelable(false);
		builderSingle.setMessage(CONSTANT.RESTART_MESSAGE);
		builderSingle.setNegativeButton("Got It",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builderSingle.show();
	}

	public static void buildAboutUsDialog(final Context context) {

		int theme = MyPreferenceHandler.getTheme(context);

		// AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		AlertDialog.Builder builderSingle = getBuilder(context);

		builderSingle.setTitle(R.string.app_name);
		if (theme == MyPreferenceHandler.DARK_THEME) {
			builderSingle.setIcon(context.getResources().getDrawable(
					R.drawable.ic_action_about));

		} else
			builderSingle.setIcon(context.getResources().getDrawable(
					R.drawable.ic_action_about_light));

		final SpannableString s = new SpannableString(CONSTANT.ABOUTUSMOD);
		Linkify.addLinks(s, Linkify.ALL);

		builderSingle.setMessage(s);
		builderSingle.setCancelable(true);
		if (!MyPreferenceHandler.isRateClicked(context)) {
			builderSingle.setPositiveButton("Rate Us",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							context.startActivity(new Intent(
									Intent.ACTION_VIEW, Uri
											.parse("market://details?id="
													+ context.getPackageName())));
							dialog.dismiss();
							MyPreferenceHandler.setRateClicked(context, true);
						}
					});
		}
		final Dialog dialog = builderSingle.create();
		dialog.show();
		((TextView) dialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public static void buildStorageOptionDialog(final Context context) {
		final ArrayList<String> list = new ArrayList<String>();
		Map<String, File> externalLocations = ExternalStorage
				.getAllStorageLocations();
		File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
		list.add(sdCard.getAbsolutePath());

		if (externalLocations.containsKey(ExternalStorage.EXTERNAL_SD_CARD))
			list.add(externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD)
					.getAbsolutePath());

		String checkedItem = MyPreferenceHandler.getLocationPreference(context);
		int checkedPos = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(checkedItem))
				checkedPos = i;

		}
		// Where we track the selected items
		AlertDialog.Builder builder = getBuilder(context);
		// Set the dialog title
		builder.setTitle("Storage Option")
		// Specify the list array, the items to be selected by default (null for
		// none),
		// and the listener through which to receive callbacks when items are
		// selected
				.setSingleChoiceItems(
						list.toArray(new CharSequence[list.size()]),
						checkedPos, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								MyPreferenceHandler.setLocationPreference(
										context, list.get(which));
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

		builder.show();

	}
}
