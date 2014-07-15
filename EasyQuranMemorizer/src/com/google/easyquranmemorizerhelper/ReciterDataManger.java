package com.google.easyquranmemorizerhelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * This class manages the list of all reciters and urls from which audio can be
 * downloaded.
 * 
 * @author Shoaib Khan
 * 
 */
public class ReciterDataManger {

	private Context myContext;
	private LinkedHashMap<String, String> reciterUrlList;

	public ReciterDataManger(Context myContext) {
		this.myContext = myContext;

		reciterUrlList = new LinkedHashMap<String, String>();
		populateList();

	}

	public ArrayList<String> getReciterList() {
		ArrayList<String> reciterList = new ArrayList<String>();
		for (String string : reciterUrlList.keySet()) {
			reciterList.add(string);
		}
		return reciterList;
	}

	public HashMap<String, String> getReciterUrlList() {
		return reciterUrlList;
	}

	/**
	 * This function load the CONSTANT.RECITER_LIST file which contains
	 * recitername and url containing audio. It populate the hashMap
	 * reciterUrlList
	 */
	private void populateList() {
		AssetManager assetManager = myContext.getAssets();
		InputStream is;
		try {
			is = assetManager.open(CONSTANT.RECITER_LIST);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = in.readLine()) != null) {
				String tmp[] = line.split("===");
				String reciterName = tmp[0].trim();
				String reciterUrl = tmp[1].trim();
				reciterUrlList.put(reciterName, reciterUrl);
				// System.out.println(reciterName + "," + reciterUrl);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function searches for all the reciter whose recitation has already
	 * been downloaded for surah given as prameter. It scans the download
	 * location and determines the same.
	 * 
	 * @param surah
	 * @return
	 */
	public ArrayList<String> getDownloadedReciterList(final Surah surah) {
		ArrayList<String> authorList = new ArrayList<String>();
		ArrayList<String> pathList = ExternalStorage.getSdAndExternalStorage();

		// Searching for all possible audio it could be in externalStorage or sd
		// card.
		for (String string : pathList) {
			File path = new File(string + "/"
					+ CONSTANT.APPLICATIONROOTDIRECTORYLOCATION);
			if (!path.exists())
				continue;
			path = new File(string + "/" + CONSTANT.AUDIODIRECTORYLOCATION);

			// If Audio Directory does not exists then return;
			if (!path.exists())
				continue;

			path = new File(string + "/" + CONSTANT.AUDIODIRECTORYLOCATION
					+ "/" + surah.getSurahNumber());

			if (!path.exists() || path.isFile() || !path.canRead())
				continue;

			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {

				String reciterName = files[i].getName();
				if (files[i].isFile() || !files[i].canRead())
					continue;
				File[] tmp = files[i].listFiles(new FilenameFilter() {

					// This filters scans all the files and checks if all the
					// files are .mp3 and their count is equal to total verse of
					// the surah
					@Override
					public boolean accept(File file, String match) {
						String[] reTmp = match.split("\\.");
						if (reTmp == null || reTmp.length != 2
								|| !reTmp[1].equals("mp3"))
							return false;

						int number = Integer.parseInt(reTmp[0]);
						if (number < 0 || number > surah.getVerseCount())
							return false;

						return true;

					}
				});
				if (tmp != null && tmp.length == surah.getVerseCount())
					if (!authorList.contains(reciterName))
						authorList.add(reciterName);
			}
		}

		return authorList;

	}
	/**
	 * Returns Reciter List whose audio has not yet been downloaded.
	 * @param surah
	 * @return
	 */

	public ArrayList<String> getNotDownloadedReciterList(final Surah surah) {
		ArrayList<String> all = getReciterList();
		ArrayList<String> present = getDownloadedReciterList(surah);
		for (String string : present) {
			all.remove(string);
		}
		return all;
	}
}
