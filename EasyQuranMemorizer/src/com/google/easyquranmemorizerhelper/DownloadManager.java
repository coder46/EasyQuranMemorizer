package com.google.easyquranmemorizerhelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Pair;

/**
 * 
 * @author Shoaib Khan
 * 
 */
public class DownloadManager {

	private HashMap<String, String> reciterUrlList;
	private Context myContext;
	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;

	public DownloadManager(Context myContext) {
		this.myContext = myContext;
		ReciterDataManger reciterDataManger = new ReciterDataManger(myContext);
		reciterUrlList = reciterDataManger.getReciterUrlList();
		// instantiate it within the onCreate method
		initializeProgress();

	}

	/**
	 * Initializes the progress bar displayed while downloading .. It also sets
	 * the downloading theme according to theme selected in the settngs.
	 */
	private void initializeProgress() {
		int theme = MyPreferenceHandler.getTheme(myContext);
		if (theme == MyPreferenceHandler.DARK_THEME) {
			mProgressDialog = new ProgressDialog(myContext,
					AlertDialog.THEME_HOLO_DARK);

		} else {
			mProgressDialog = new ProgressDialog(myContext,
					AlertDialog.THEME_HOLO_LIGHT);

		}
		mProgressDialog.setTitle("In Progress...");
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);

	}

	/**
	 * Initialize directories for downloading. For example surah Al-fatiha will
	 * be stored as [External Location selected by
	 * user]/SurahMemorizer/Audio/1/reciterName/*.mp3 This function create
	 * directories if does not exists. In the above case it will first create
	 * SurahMemorizer directory if not exists, then Audio if not exists and so
	 * on
	 * 
	 * @param reciter
	 * @param surahNumber
	 */
	private void initializeDirectoriesForDownloading(String reciter,
			int surahNumber) {

		String pathLoc = MyPreferenceHandler.getLocationPreference(myContext)
				+ "/" + CONSTANT.APPLICATIONROOTDIRECTORYLOCATION;
		File path = new File(pathLoc);
		if (!path.exists())
			path.mkdir();

		pathLoc = MyPreferenceHandler.getLocationPreference(myContext) + "/"
				+ CONSTANT.AUDIODIRECTORYLOCATION;
		path = new File(pathLoc);
		if (!path.exists())
			path.mkdir();

		path = new File(pathLoc + "/" + String.valueOf(surahNumber));
		if (!path.exists())
			path.mkdir();
		path = new File(pathLoc + "/" + String.valueOf(surahNumber) + "/"
				+ reciter);

		path.mkdir();
	}

	public boolean downloadSurah(String reciter, Surah surah, int totalVerse) {

		// Return false if reciter is not in the reciterUrlList.
		// Even though this case will not happen
		if (!reciterUrlList.containsKey(reciter))
			return false;

		// return false if not connected to Internet
		ConnectivityManager connMgr = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
			Logger.makeToast(myContext, "Not Connected To Internet");
			return false;
		}

		initializeDirectoriesForDownloading(reciter, surah.getSurahNumber());

		Pair<ArrayList<Pair<String, String>>, Integer> UrlListWithNames = new Pair<ArrayList<Pair<String, String>>, Integer>(
				new ArrayList<Pair<String, String>>(), totalVerse);

		boolean previousDownload = false;
		for (int i = 1; i <= totalVerse; i++) {

			/**
			 * Since file name on the server side are stored as 001001.mp3 where
			 * first three digit represent surah number while latter represtnt
			 * verse number In the above case, it is 1st verse of surah
			 * Al-fatiha
			 */
			String fileName = HelperFunction.convertNumberTo3LengthString(surah
					.getSurahNumber())
					+ HelperFunction.convertNumberTo3LengthString(i) + ".mp3";

			String fileLocation = MyPreferenceHandler
					.getLocationPreference(myContext)
					+ "/"
					+ CONSTANT.AUDIODIRECTORYLOCATION
					+ "/"
					+ String.valueOf(surah.getSurahNumber())
					+ "/"
					+ reciter
					+ "/"
					+ HelperFunction.convertNumberTo3LengthString(i)
					+ ".mp3";

			/**
			 * Forward only if file not exists. If a particular verse exist then
			 * skip it for downloading
			 */
			if (!new File(fileLocation).exists()) {
				String url = reciterUrlList.get(reciter) + "/" + fileName;
				UrlListWithNames.first.add(new Pair<String, String>(url,
						fileLocation));
				// Log.d("debug", "url is " + url + " file name is " +
				// fileName);
			} else {
				previousDownload = true;
			}

			// System.out.println(url);
		}
		if (previousDownload)
			mProgressDialog.setTitle("Resuming previous download "
					+ surah.getName());
		else
			mProgressDialog.setTitle("Downloading " + surah.getName());

		final MyAsyncTask async = new MyAsyncTask();
		async.execute(UrlListWithNames);

		return true;
	}

	/**
	 * This Task asynchronously downloads all the required file and updates the
	 * progress bar.
	 * 
	 * @author Shoaib Khan
	 * 
	 */
	private class MyAsyncTask
			extends
			AsyncTask<Pair<ArrayList<Pair<String, String>>, Integer>, Integer, String> {

		private int downloadedItems;
		private String ErrorMsg = "";
		private PowerManager.WakeLock mWakeLock;
		private int noOfUrls;
		private boolean running = true;

		private long downloadingStartTime;
		private long downloadingEndTime;
		private float downloadSpeed;
		private int lenghtOfFile;

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);

			// Logger.logMessage("speed is "+downloadSpeed);

			// size in mb
			float size = (float) lenghtOfFile / (float) 1000000;
			// rounding it off to two digits
			size = (float) Math.round(size * 100) / (float) 100;
			// rounding downloadSpeed to two digits
			downloadSpeed = (float) Math.round(downloadSpeed * 100)
					/ (float) 100;
			mProgressDialog.setMessage("Loading " + (downloadedItems + 1) + "/"
					+ noOfUrls + "  Speed: " + String.valueOf(downloadSpeed)
					+ "KB/s" + "  Size: " + String.valueOf(size) + "MB");
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			running = false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) myContext
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// cancel(true);
							running = false;
							dialog.dismiss();
						}
					});
			mProgressDialog.show();
		}

		/**
		 * This function downloads the given urlStr and returns it as byte array
		 * 
		 * @param urlStr
		 * @return
		 */
		private byte[] downloadFile(String urlStr) {
			ConnectivityManager connMgr = (ConnectivityManager) myContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			// If network connectivity is lost in between
			if (networkInfo == null || !networkInfo.isConnected()) {
				ErrorMsg = "Not Connected To Internet";
				return null;
			}
			BufferedInputStream bis = null;
			ByteArrayOutputStream outputBytes = null;
			try {
				URL url = new URL(urlStr);
				URLConnection connection = url.openConnection();
				lenghtOfFile = connection.getContentLength();
				bis = new BufferedInputStream(url.openStream());
				outputBytes = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int publishBytes = 0;
				int bytesRead = 0;
				while ((bytesRead = bis.read(buffer)) > 0) {
					if (!running) {
						ErrorMsg = "Downloading Cancelled";
						return null;
					}
					publishBytes += bytesRead;

					downloadingEndTime = System.nanoTime();
					downloadSpeed = ((float) publishBytes * 1000000)
							/ (downloadingEndTime - downloadingStartTime);

					outputBytes.write(buffer, 0, bytesRead);

					// publish the progress in the progress bar
					publishProgress((publishBytes * 100) / lenghtOfFile);
				}
				return outputBytes.toByteArray();

			} catch (MalformedURLException e) {
				ErrorMsg = "MalFormed URL";
				e.printStackTrace();
			} catch (IOException e) {
				ErrorMsg = "Download Failed";
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				// File closing handling
				if (bis != null)
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (outputBytes != null) {
					try {
						outputBytes.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;

		}

		/**
		 * This function create writes result bytes into the fileLocation
		 * 
		 * @param fileLocation
		 * @param result
		 * @return
		 */
		private boolean createFile(String fileLocation, byte[] result) {

			File path = new File(fileLocation);
			FileOutputStream out = null;
			boolean done = false;
			try {
				out = new FileOutputStream(path.getAbsoluteFile());
				out.write(result);
				// System.out.println("Done!");
				done = true;
			} catch (IOException e) {

				ErrorMsg = "Failed to save file";
				e.printStackTrace();

			} finally {
				if (out != null)
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			return done;

		}

		/**
		 * Executed after task is done
		 */
		@Override
		protected void onPostExecute(String result) {

			mWakeLock.release();
			mProgressDialog.dismiss();
			if (result == CONSTANT.DOWNLOAD_SUCCESSFUL) {
				Logger.makeToast(myContext, "Download SuccessFul");
			} else {
				Logger.makeToast(myContext, result);

			}
		}

		/**
		 * This function is called after onPreExecute. This function handles the
		 * downloading to downloadFile function and then handles the creation of
		 * file createFile
		 */
		@Override
		protected String doInBackground(
				Pair<ArrayList<Pair<String, String>>, Integer>... arg) {
			downloadedItems = arg[0].second - arg[0].first.size();
			noOfUrls = arg[0].second;
			for (Pair<String, String> pair : arg[0].first) {
				if (!running) {
					return "Downloading Cancelled";
				}
				downloadingStartTime = System.nanoTime();
				byte[] result = downloadFile(pair.first);
				downloadingEndTime = System.nanoTime();
				//

				if (result != null) {

					downloadingStartTime = System.nanoTime();
					boolean createResult = createFile(pair.second, result);
					downloadingEndTime = System.nanoTime();
					// Logger.logMessage("Time for exceuting file creation "
					// + (downloadingEndTime - downloadingStartTime));

					if (createResult) {
						// Logger.logMessage("Download SuccessFul");

						downloadedItems++;
					} else {
						// Logger.logMessage(ErrorMsg);
						return ErrorMsg;
					}

				} else {
					// Logger.logMessage(ErrorMsg);
					return ErrorMsg;
				}

			}

			return CONSTANT.DOWNLOAD_SUCCESSFUL;
		}

	}
}
