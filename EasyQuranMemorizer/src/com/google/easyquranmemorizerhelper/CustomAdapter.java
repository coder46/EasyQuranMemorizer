package com.google.easyquranmemorizerhelper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.easyquranmemorizer.HomePage;
import com.google.easyquranmemorizer.R;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements OnClickListener {

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList<Surah> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;

	/************* CustomAdapter Constructor *****************/
	public CustomAdapter(Activity a, ArrayList<Surah> d, Resources resLocal) {

		/********** Take passed values **********/
		activity = a;
		data = d;
		res = resLocal;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/******** What is the size of Passed Arraylist Size ************/
	@Override
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView leftText;
		public TextView rightText;
		public ImageView downloadButton;
		public ImageView deleteButton;
		public int position;

	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		// display.getSize(size);
		int PaddingWidth = 20, rightMarginWidth = 5, buttonWidth = 48;

		if (display.getWidth() < 480) {
			buttonWidth = (display.getWidth() * 48) / 680;
		}

		int extramargin;

		final DisplayMetrics metrics = activity.getResources()
				.getDisplayMetrics();
		float densityDpi = metrics.density;

		//I could have written a basic mathematical formula for calculating extramargin.
		//But in that case person reading the code will not understand it better.
		//Therefore I have splitted in small,hd,full... screen.
		
		if (densityDpi <=1) // for small screen
			extramargin = (int) (densityDpi * 20.0);
		
		else if(densityDpi<=2) // for hd screen
			extramargin = (int) (densityDpi * 35.0);
		
		else if(densityDpi<=3) // for more than hd screen
			extramargin = (int) (densityDpi * 55.0);
		
		else if(densityDpi<=4) //For full hd screen
			extramargin=(int) (densityDpi*70.0);
		
		else  // for ultra hd screen
			extramargin=(int ) (densityDpi*85.0);

		int availableWidth = display.getWidth() - PaddingWidth
				- rightMarginWidth - 2 * buttonWidth - extramargin;


		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.home_page_row, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.leftText = (TextView) vi
					.findViewById(R.id.home_page_left_textview);
			holder.rightText = (TextView) vi
					.findViewById(R.id.home_page_right_textview);

			holder.downloadButton = (ImageView) vi
					.findViewById(R.id.home_page_download_button);

			holder.deleteButton = (ImageView) vi
					.findViewById(R.id.home_page_delete_button);
			holder.deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					ViewHolder myHolder = (ViewHolder) view.getTag();
					MyDialogBuilder.buildDeleteSurah(activity,
							data.get(myHolder.position));

				}
			});
			holder.downloadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					ViewHolder myHolder = (ViewHolder) view.getTag();
					MyDialogBuilder.buildDownloadDialog(activity,
							data.get(myHolder.position));
				}
			});
			setTheme(holder);
			/************ Set holder with LayoutInflater ************/
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		/***** Get each Model object from Arraylist ********/
		Surah name = data.get(position);

		/************ Set Model values in Holder elements ***********/

		holder.leftText.setText(name.getSurahNumber() + ". " + name.getName());

		// holder.rightText.setTypeface(Typeface.createFromAsset(activity.getAssets(),
		// "TAHOMA.ttf"));
		// holder.rightText.setText(ArabicUtilities.reshape(name.getArabicName()));
		holder.rightText.setText(name.getArabicName());

		;
		holder.position = position;
		holder.rightText.setWidth(availableWidth / 2);
		holder.leftText.setWidth(availableWidth / 2);

		holder.downloadButton.setTag(holder);
		holder.deleteButton.setTag(holder);
		/******** Set Item Click Listner for LayoutInflater for each row *******/

		vi.setOnClickListener(new OnItemClickListener(position));
		return vi;
	}

	private void setTheme(ViewHolder holder) {

		int theme = MyPreferenceHandler.getTheme(activity);
		if (theme == MyPreferenceHandler.DARK_THEME) {
			holder.downloadButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_download_dark));
			holder.leftText.setTextColor(Color.WHITE);
			holder.rightText.setTextColor(Color.WHITE);
			holder.deleteButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_discard));

		} else {
			holder.downloadButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_download));
			holder.leftText.setTextColor(Color.BLACK);
			holder.rightText.setTextColor(Color.BLACK);
			holder.deleteButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_discard_light));
		}
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			HomePage sct = (HomePage) activity;

			/****
			 * Call onItemClick Method inside CustomListViewAndroidExample Class
			 * ( See Below )
			 ****/

			sct.onItemClick(mPosition);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
