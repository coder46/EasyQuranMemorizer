package com.google.easyquranmemorizerhelper;

import java.io.Serializable;
import java.util.List;

/*
 * This Class contains all details about a particular Surah such as arabic name,
 *  english translated name, verse count , arabic text..
 */
/**
 * @author Shoaib Khan
 *
 */
public class Surah implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name, englishTranslatedName, type, arabicName;
	private int verseCount;
	private int surahNumber;
	private List<String> surahSimpleText;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurahText(int verseNumber) {
		if (verseNumber < surahSimpleText.size())
			return surahSimpleText.get(verseNumber);
		return null;
	}

	public Surah(String name, String englishTranslatedName, String type,
			String arabicName, int verseCount, int surahNumber,
			List<String> surahSimpleText) {
		super();
		this.name = name;
		this.englishTranslatedName = englishTranslatedName;
		this.type = type;
		this.arabicName = arabicName;
		this.verseCount = verseCount;
		this.surahNumber = surahNumber;
		this.surahSimpleText = surahSimpleText;
		// this.translatedText=tranHashMap;
	}

	public String getEnglishTranslatedName() {
		return englishTranslatedName;
	}

	public void setEnglishTranslatedName(String englishTranslatedName) {
		this.englishTranslatedName = englishTranslatedName;
	}

	public String getArabicName() {
		return arabicName;
	}

	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}

	public int getSurahNumber() {
		return surahNumber;
	}

	public void setSurahNumber(int surahNumber) {
		this.surahNumber = surahNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVerseCount() {
		return verseCount;
	}

	public void setVerseCount(int verseCount) {
		this.verseCount = verseCount;
	}

	public List<String> getSurahSimpleText() {
		return surahSimpleText;
	}

	public void setSurahSimpleText(List<String> surahSimpleText) {
		this.surahSimpleText = surahSimpleText;
	}

}
