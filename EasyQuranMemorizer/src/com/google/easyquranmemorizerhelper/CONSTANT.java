package com.google.easyquranmemorizerhelper;
/**
 * 
 * @author Shoaib Khan
 *
 *This File contains most of the constant variable used in the project.
 */
public class CONSTANT {

	public static final String APPLICATIONNAME = "SurahMemorizer";
	public static final String APPLICATIONROOTDIRECTORYLOCATION = APPLICATIONNAME;
	public static final String AUDIODIRECTORYNAME = "Audio";
	public static final String AUDIODIRECTORYLOCATION = APPLICATIONROOTDIRECTORYLOCATION
			+ "/" + AUDIODIRECTORYNAME;

	// Arabic Text files
	public static final String SIMPLEQURANTEXTNAME = "quran-simple.xml";

	// Translation Files

	public static final String ENGLISHTRANSLATED = "shahih_translation.sahih";
	public static final String MALAYSIANTRANSLATED = "ms.basmeih";
	public static final String INDONESIANTRANSLATED = "id.indonesian";
	public static final String HINDITRANSLATED = "hi.farooq";

	public static final String BISMILLAH = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيم";
	public static final String RECITER_LIST = "reciterList.txt";
	public static final String DOWNLOAD_SUCCESSFUL = "Download SuccessFul";

	// Background colors
	public static final String MUSHAF_BACKGROUND_COLOUR = "#F9FAE1";
	public static final String WHITE_BACKGROUND_COLOUR = "#FFFFFF";
	public static final String BLACK_BACKGROUND_COLOUR = "#000000";

	// Popup message when repeat and restart button are clicked for first time
	public static final String REPEAT_MESSAGE = "This button keeps on repeating the current verse until you press it again";
	public static final String RESTART_MESSAGE = "This button restart the current playing verse from the selected starting verse";

	// Divider line background colour. You can observe this line between
	// surahsList and media player and settings actiity
	public static final String WHITE_DIVIDER = "#D8D8D8";
	public static final String BLACK_DIVIDER = "#202020";

	// About us message
	public static final String ABOUTUSMOD = "Easy Quran Memorizer is a free, open source Quran "
			+ "application for Android. "
			+ "The audio used are from http://www.everyayah.com, "
			+ "while Quran Text and Quran Translation files are from http://www.tanzil.net."
			+ "\nIf it has helped you in memorizing quran, "
			+ "please remember to pray to Allah(swt) for forgiveness of all muslim"
			+ ".If you come across any suggestion then please drop a mail to "
			+ "skkshoaib@gmail.com";

}
