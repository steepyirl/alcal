package com.qualimony.ka;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class KaDateFormat extends DateFormat {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4699588020938655946L;
	private String pattern = "G-y-M-W-E H:mm:ss.SSS";
	
	public KaDateFormat(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo,
			FieldPosition fieldPosition) {
		KaCalendar cal = new KaCalendar();
		cal.setTime(date);
		//Gen
		String modPat = pattern;
		modPat = formatField(modPat, cal.get(KaCalendar.KA_GEN), 'G');
		//Year
		modPat = formatField(modPat, cal.get(KaCalendar.KA_YEAR), 'y');
		//Month
		modPat = formatMonth(modPat, cal.get(KaCalendar.KA_MONTH));
		//Week
		modPat = formatWeek(modPat, cal.get(KaCalendar.KA_MONTH), cal.get(KaCalendar.KA_WEEK));
		//Day
		modPat = formatDay(modPat, cal.get(KaCalendar.KA_DAY));
		//Hour
		modPat = formatField(modPat, cal.get(KaCalendar.HOUR), 'H');
		//Minute
		modPat = formatField(modPat, cal.get(KaCalendar.MINUTE), 'm');
		//Second
		modPat = formatField(modPat, cal.get(KaCalendar.SECOND), 's');
		//Milli
		modPat = formatField(modPat, cal.get(KaCalendar.MILLISECOND), 'S');
		
		if(toAppendTo != null)
			toAppendTo.append(modPat);
		return toAppendTo;
	}
	
	private String formatMonth(String modPat, int value) {
		int index = modPat.indexOf('M');
		if(index == -1)
			return modPat;
		String replacement = "";
		switch(value) {
		case 1:
			replacement = "\u2651";
			break;
		case 2:
			replacement = "\u2652";
			break;
		case 3:
			replacement = "\u2653";
			break;
		case 4:
			replacement = "\u2648";
			break;
		case 5:
			replacement = "\u2649";
			break;
		case 6:
			replacement = "\u264a";
			break;
		case 7:
			replacement = "\u264b";
			break;
		case 8:
			replacement = "\u264c";
			break;
		case 9:
			replacement = "\u264d";
			break;
		case 10:
			replacement = "\u264e";
			break;
		case 11:
			replacement = "\u264f";
			break;
		case 12:
			replacement = "\u26ce";
			break;
		case 13:
			replacement = "\u2650";
			break;
		case 14:
			replacement = "\u25ef";
			break;
		}
		modPat = modPat.substring(0, index) + replacement + modPat.substring(index+1);
		return modPat;
	}
	
	private String formatWeek(String modPat, int monthValue, int weekValue) {
		int index = modPat.indexOf('W');
		if(index == -1)
			return modPat;
		if(monthValue == 14){
			modPat = modPat.substring(0, index) + modPat.substring(index+1);
		} else {
			char c = '0';
			c += weekValue;
			modPat = modPat.substring(0, index) + c + modPat.substring(index + 1);
		}
		return modPat;
	}
	
	private String formatDay(String modPat, int value) {
		int index = modPat.indexOf('E');
		if(index == -1)
			return modPat;
		String replacement = "";
		switch(value) {
		case 1:
			replacement = "\u263d";
			break;
		case 2:
			replacement = "\u2642";
			break;
		case 3:
			replacement = "\u263f";
			break;
		case 4:
			replacement = "\u2643";
			break;
		case 5:
			replacement = "\u2640";
			break;
		case 6:
			replacement = "\u2644";
			break;
		case 7:
			replacement = "\u2609";
			break;
		case 8:
			replacement = "\u2645";
			break;
		case 9:
			replacement = "\u2646";
			break;
		}
		modPat = modPat.substring(0, index) + replacement + modPat.substring(index+1);
		return modPat;
	}
	
	private String formatField(String modPat, int value, char c) {
		String replacement = "";
		int iFirstIndex = modPat.indexOf(c);
		if(iFirstIndex == -1)
			return modPat;
		int iLastIndex = modPat.lastIndexOf(c);
		int length = iLastIndex - iFirstIndex + 1;
		if(length == 1)
			replacement = String.valueOf(value);
		else {
			value = value % (int)(Math.pow(10, length));
			for(int i = length-1; i > 0; i--) {
				replacement+= String.valueOf(value / (int)(Math.pow(10, i)));
				value = value % ((int)Math.pow(10, i));
			}
			replacement+= String.valueOf(value);
		}
		modPat = modPat.substring(0, iFirstIndex) + replacement + modPat.substring(iLastIndex+1);
		return modPat;
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		// TODO Auto-generated method stub
		return null;
	}

}
