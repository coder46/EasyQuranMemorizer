package com.google.easyquranmemorizerhelper;
/**
 * 
 * @author Shoaib Khan
 *
 */
public class HelperFunction {

	
	/**
	 * 
	 * This function converts number to three character String
	 * @param number
	 * @return String
	 */
	public static  String convertNumberTo3LengthString(int number) {
		String answer = "";
		String tmp = "";
		while (number > 0) {
			tmp += number % 10;
			number /= 10;
		}
		for (int i = 0; i < 3 - tmp.length(); i++)
			answer += "0";
		for (int i = 0; i < tmp.length(); i++) {
			answer += tmp.charAt(tmp.length() - 1 - i);
		}
		return answer;

	}
	
	
}
