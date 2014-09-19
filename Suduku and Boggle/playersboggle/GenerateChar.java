package edu.neu.madcourse.binbinlu.playersboggle;

import java.util.Random;

public class GenerateChar {
	// the weights of appearance of each character in the dictionary is distributed in feild of 100
//	private int[] weights = {84, 103, 147, 180, 293, 305, 330, 357, 449, 451, 
//			459, 515, 546, 620, 694, 727, 729, 801, 886, 955, 993, 1002, 1009, 1012, 1032, 1036};
	
	private int[] weights = {0, 84, 103, 147, 180, 293, 305, 330, 357, 449, 451, 
			459, 515, 546, 620, 694, 727, 729, 801, 886, 955, 993, 1002, 1009, 1012, 1032, 1036};
	private char[] gridChar;
	public GenerateChar(int diff) {
		switch (diff) {
		//easy
		case 2:
			gridChar=new char[6*6];
			break;
		//medium
		case 1:
			gridChar=new char[5*5];
			break;
		//hard
		case 0:
			gridChar=new char[4*4];
			break;
		default:
			break;
		}
	}
	public char[] getGrid() {
		int size=gridChar.length;
		for (int i=0; i<size; i++) {
			gridChar[i]=genChar();
		}
		return gridChar;
	}
	public char genChar() {
		char randomChar=' ';
		Random rand = new Random(System.nanoTime());
		int num=rand.nextInt(1036);
		for(int i=0; i<26; i++) {
			if ((num>=weights[i])&&(num<weights[i+1])) {
				randomChar=(char)(i+'a');
				break;
			}
		}
		return randomChar;
	}	
}


	
	
	
	

