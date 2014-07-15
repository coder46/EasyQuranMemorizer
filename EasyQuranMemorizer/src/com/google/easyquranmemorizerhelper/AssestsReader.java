package com.google.easyquranmemorizerhelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Pair;

/**
 * This class reads all the assests file present in the assests folder such as
 * Quranic text files and quran metadata . It also populate the surahIndexer
 * hashMap which maps each integer to the surah (of Surah Class) variable.
 * 
 * 
 * @author Shoaib Khan
 * 
 */
public class AssestsReader {

	private HashMap<Integer, Surah> surahIndexer;
	private Context myContext;

	public AssestsReader(Context context) {
		myContext = context;
		surahIndexer = new HashMap<Integer, Surah>();
		readQuranXml();
	}

	/**
	 * List of names of all Surahs.
	 * 
	 * @return
	 */
	public ArrayList<Surah> getSurahsList() {
		ArrayList<Surah> surahList = new ArrayList<Surah>();
		for (int surahNumber = 1; surahNumber <= 114; surahNumber++)
			surahList.add(surahIndexer.get(surahNumber));
		return surahList;
	}

	/**
	 * This function loads surah text by reading CONSTANT>SIMPLEQURNTEXTNAME and
	 * returns a hashmap which links Integer (i.e. surah number) to
	 * pair<String,List<String>>, the first item of pair is arabic name of surah
	 * and second item contains data of each verse in a list.
	 * 
	 * To understand this function first read the file
	 * CONSTANT.SIMPLEQURANTEXTNAME located in the assests folder.
	 * 
	 * @param fileName
	 * @return
	 */
	private HashMap<Integer, Pair<String, List<String>>> loadSurahText(
			String fileName) {
		// /reading surahs verse text

		InputStream is = null;
		try {
			HashMap<Integer, Pair<String, List<String>>> map = new HashMap<Integer, Pair<String, List<String>>>();
			AssetManager assetManager = myContext.getAssets();
			is = assetManager.open(CONSTANT.SIMPLEQURANTEXTNAME);
			DocumentBuilderFactory dbFactoryText = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilderText = dbFactoryText.newDocumentBuilder();
			org.w3c.dom.Document docText = dBuilderText.parse(is);
			docText.getDocumentElement().normalize();
			// System.out.println("Root element :"
			// + docText.getDocumentElement().getNodeName());
			NodeList nListText = docText.getElementsByTagName("sura");
			for (int temp = 0; temp < nListText.getLength(); temp++) {
				Node nSurahNode = nListText.item(temp);
				if (nSurahNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nSurahNode;
					String surahIndex = eElement.getAttribute("index");
					String arabicName = eElement.getAttribute("name");

					NodeList nVerseList = eElement.getElementsByTagName("aya");
					List<String> verseText = new ArrayList<String>();

					for (int j = 0; j < nVerseList.getLength(); j++) {
						Node verseNode = nVerseList.item(j);
						Element eElementVerse = (Element) verseNode;
						String text = eElementVerse.getAttribute("text");
						String verseIndex = eElementVerse.getAttribute("index");

						if (Integer.parseInt(verseIndex) != j + 1) {
							Log.w("erro", "doesnot match");
						}
						verseText.add(text);
					}
					map.put(Integer.parseInt(surahIndex),
							new Pair<String, List<String>>(arabicName,
									verseText));

				}

			}
			is.close();
			return map;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * This function populates the surahIndexer variable. It first loads the
	 * surahText by calling loadSurahText function. It then traverse the
	 * surahList.xml which is a metadata of Quran i.e. it contains all
	 * information about each surah such as surah name, english name, english
	 * translated name, no of verses...
	 * 
	 * Have a look at this file before proceeding for this function.
	 */
	private void readQuranXml() {
		InputStream is = null;
		try {

			// getting surahText and their arabic names
			HashMap<Integer, Pair<String, List<String>>> surahText = loadSurahText(CONSTANT.SIMPLEQURANTEXTNAME);

			
			AssetManager assetManager = myContext.getAssets();
			
			// /reading surahList.xml and getting surahs name and its details
			is = assetManager.open("surahList.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element :"
			// + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("surah");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String surahName = eElement
							.getElementsByTagName("surahName").item(0)
							.getTextContent();
					String surahEnglishName = eElement
							.getElementsByTagName("englishName").item(0)
							.getTextContent();
					String verseCount = eElement
							.getElementsByTagName("verseCount").item(0)
							.getTextContent();
					String type = eElement.getElementsByTagName("type").item(0)
							.getTextContent();
					Pair<String, List<String>> surahTextData = surahText
							.get(temp + 1);

					String arabicName = surahTextData.first;
					Surah tmp = new Surah(surahName, surahEnglishName, type,
							arabicName, Integer.parseInt(verseCount), temp + 1,
							surahTextData.second);
					surahIndexer.put(temp + 1, tmp);
				}

			}
		} catch (Exception e) {
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
		}
	}

	/*
	 * Returns Surah given its id, for example 1 for surah Al-fatiha
	 */
	public Surah getSurah(int surahNumber) {
		if (surahIndexer.containsKey(surahNumber)) {

			return surahIndexer.get(surahNumber);
		}
		return null;
	}

}
