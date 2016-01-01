package com.qualimony.ka;

import java.util.Calendar;

public class KaCalendar extends Calendar {
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Calendar#computeTime()
	 * fields: GYMWDHmsS
	 *         012345678
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -371563684378629540L;
	public static final int KA_GEN = 0;
	public static final int KA_YEAR = 1;
	public static final int KA_MONTH = 2;
	public static final int KA_WEEK = 3;
	public static final int KA_DAY = 4;
	public static final int HOUR = 5;
	public static final int MINUTE = 6;
	public static final int SECOND = 7;
	public static final int MILLISECOND = 8;
	
	//private static final long EPOCH_DIFFERENCE = -251481348000000L; //6000 BC
	//private static final long EPOCH_DIFFERENCE = -12274740000000L; //1581 AD
	
	//After gregorian change
	//private static final long EPOCH_DIFFERENCE = -251477287200000L; //6000 BC
	private static final long EPOCH_DIFFERENCE = -377484170400000L; //-377707687200000L; //10000 BC
	//private static final long EPOCH_DIFFERENCE = -12275604000000L; //1581 AD
	
	//private static final long EPOCH_DIFFERENCE = -12212532000000L; //1583 AD
	
	//private static final long EPOCH_DIFFERENCE = -4922877600000L; //1814 AD
	//private static final long EPOCH_DIFFERENCE = -3881498400000L; //1847 AD
	//private static final long EPOCH_DIFFERENCE = -2840119200000L; //1880 AD
	//private static final long EPOCH_DIFFERENCE = -1798740000000L; //1913 AD
	//private static final long EPOCH_DIFFERENCE = -757360800000L; //1946 AD
	//private static final long EPOCH_DIFFERENCE = -694288800000L; //1948 AD
	//private static final long EPOCH_DIFFERENCE = 1072936800000L; //2004 AD
	//private static final long EPOCH_DIFFERENCE = 1167631200000L; //2007 AD
	//private static final long EPOCH_DIFFERENCE = 1199167200000L; //2008 AD
	//private static final long EPOCH_DIFFERENCE = 1230789600000L; //2009 AD
	//private static final long EPOCH_DIFFERENCE = 1293861600000L; //2011 AD
	
	private static final long MILLIS_IN_SECOND = 1000;
	private static final long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;
	private static final long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;
	private static final long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
	private static final long MILLIS_IN_WEEK = 7 * MILLIS_IN_DAY;
	private static final long MILLIS_IN_MONTH = 4 * MILLIS_IN_WEEK;
	private static final long MILLIS_IN_REGULAR_YEAR = 365 * MILLIS_IN_DAY;
	private static final long MILLIS_IN_LEAP_YEAR = MILLIS_IN_REGULAR_YEAR + MILLIS_IN_DAY;
	private static final long MILLIS_IN_4_GROUP = 3 * MILLIS_IN_REGULAR_YEAR + MILLIS_IN_LEAP_YEAR;
	private static final long MILLIS_IN_5_GROUP = 4 * MILLIS_IN_REGULAR_YEAR + MILLIS_IN_LEAP_YEAR;
	private static final long MILLIS_IN_GEN = 8 * MILLIS_IN_LEAP_YEAR + 25 * MILLIS_IN_REGULAR_YEAR;

	private boolean leapYear = false;
	
	@Override
	protected void computeTime() {
		long lTime = (fields[KA_GEN] - 1) * MILLIS_IN_GEN;
		
		int leapDays = 0;
		if(fields[KA_YEAR] == 33)
			leapDays = 7;
		else
			leapDays = (fields[KA_YEAR] - 1) / 4;
		lTime += (fields[KA_YEAR] - 1) * MILLIS_IN_REGULAR_YEAR + leapDays * MILLIS_IN_DAY;
		
		lTime+= (fields[KA_MONTH] - 1) * MILLIS_IN_MONTH;
		
		if(fields[KA_MONTH] == 14) {
			//do nothing
		} else {
			lTime+= (fields[KA_WEEK] - 1) * MILLIS_IN_WEEK;
		}
		
		if(fields[KA_MONTH] == 14) {
			computeLeapYear();
			if(leapYear)
				lTime+= (fields[KA_DAY] - 8) * MILLIS_IN_DAY;
			else
				; //do nothing - because a full day has not yet elapsed within this month
		} else {
			lTime+= (fields[KA_DAY] - 1) * MILLIS_IN_DAY;
		}
		
		lTime+= fields[HOUR] * MILLIS_IN_HOUR;
		
		lTime+= fields[MINUTE] * MILLIS_IN_MINUTE;
		
		lTime+= fields[SECOND] * MILLIS_IN_SECOND;
		
		lTime+= fields[MILLISECOND];
		
		this.time = lTime + EPOCH_DIFFERENCE;
	}

	@Override
	protected void computeFields() {
		long kaTime = this.time - EPOCH_DIFFERENCE;
		
		//GEN
		fields[KA_GEN] = (int)(kaTime / MILLIS_IN_GEN) + 1;
		long millisInMyGen = kaTime % MILLIS_IN_GEN;
		
		//YEAR... this is a bit complicated
		int group = (int)(millisInMyGen / MILLIS_IN_4_GROUP);
		boolean isExtendedGroup = (group >= 7);
		if(group > 7)
			group = 7;
		int yearInGroup = 0;
		long millisInMyGroup = 0;
		long millisInMyYear = 0;
		if(isExtendedGroup) {
			//millisInMyGroup = millisInMyGen % MILLIS_IN_5_GROUP;
			millisInMyGroup = millisInMyGen - (7 * MILLIS_IN_4_GROUP);
			yearInGroup = (int)(millisInMyGroup / MILLIS_IN_REGULAR_YEAR);
			leapYear = (yearInGroup >= 4);
			if(yearInGroup > 4)
				yearInGroup = 4;
			fields[KA_YEAR] = 28 + yearInGroup + 1;
			if(leapYear)
				millisInMyYear = millisInMyGroup - (4 * MILLIS_IN_REGULAR_YEAR);
			else
				millisInMyYear = millisInMyGroup % MILLIS_IN_REGULAR_YEAR;
		} else {
			millisInMyGroup = millisInMyGen % MILLIS_IN_4_GROUP;
			yearInGroup = (int)(millisInMyGroup / MILLIS_IN_REGULAR_YEAR);
			leapYear = (yearInGroup >= 3);
			if(yearInGroup > 3)
				yearInGroup = 3;
			fields[KA_YEAR] = group * 4 + yearInGroup + 1;
			if(leapYear)
				millisInMyYear = millisInMyGroup - (3 * MILLIS_IN_REGULAR_YEAR);
			else
				millisInMyYear = millisInMyGroup % MILLIS_IN_REGULAR_YEAR;
		}
		
		//MONTH
		fields[KA_MONTH] = (int)(millisInMyYear / MILLIS_IN_MONTH) + 1;
		long millisInMyMonth = 0;
		if(fields[KA_MONTH] == 14)
			millisInMyMonth = millisInMyYear - (13 * MILLIS_IN_MONTH);
		else
			millisInMyMonth = millisInMyYear % MILLIS_IN_MONTH;
		
		//WEEK
		long millisInMyWeek = 0;
		if(fields[KA_MONTH] == 14) {
			fields[KA_WEEK] = 1;
			millisInMyWeek = millisInMyMonth;
		} else {
			fields[KA_WEEK] = (int)(millisInMyMonth / MILLIS_IN_WEEK) + 1;
			millisInMyWeek = millisInMyMonth % MILLIS_IN_WEEK;
		}
		
		//DAY
		fields[KA_DAY] = (int)(millisInMyWeek / MILLIS_IN_DAY) + 1;
		if(fields[KA_MONTH] == 14) {
			//these are "special" days
			if(leapYear)
				fields[KA_DAY]+= 7;
			else
				fields[KA_DAY] = 9;
		}
		long millisInMyDay = millisInMyWeek % MILLIS_IN_DAY;
		
		//HOUR
		fields[HOUR] = (int)(millisInMyDay / MILLIS_IN_HOUR); //do not add 1
		long millisInMyHour = millisInMyDay % MILLIS_IN_HOUR;
		
		//MINUTE
		fields[MINUTE] = (int)(millisInMyHour / MILLIS_IN_MINUTE); //do not add 1
		long millisInMyMinute = millisInMyHour % MILLIS_IN_MINUTE;
		
		//SECOND
		fields[SECOND] = (int)(millisInMyMinute / MILLIS_IN_SECOND); //do not add 1
		long millisInMySecond = millisInMyMinute % MILLIS_IN_SECOND;
		
		//MILLISECOND
		fields[MILLISECOND] = (int)millisInMySecond;
	}
	
	@Override
	public void add(int field, int amount) {
		addInternal(field, amount, true);

		//Adjust in case we landed in the year-end
		if(fields[KA_MONTH] == 14) {
			if(fields[KA_WEEK] != 1)
				fields[KA_WEEK] = 1;
			if(fields[KA_DAY] != 8 || fields[KA_DAY] != 9) {
				fields[KA_DAY] = 9;
			}
		} else {
			if(fields[KA_DAY] > 7) {
				if(amount > 0)
					fields[KA_DAY] = 7;
				else if(amount < 0)
					fields[KA_DAY] = 1;
			}
		}

		this.computeTime();
	}

	private void addInternal(int field, int amount, boolean direct) {
		if(amount == 0)
			return;
		//int newValue = 0;
		//int parentAdd = 0;
		switch(field) {
		case KA_GEN:
			fields[KA_GEN]+= amount;
			break;
		case KA_YEAR:
			int newYear = fields[KA_YEAR] - 1 + amount;
			int genDiff = newYear / 33;
			newYear = newYear % 33;
			if(newYear < 0) {
				newYear = 33 + newYear;
				genDiff-= 1;
			}
			fields[KA_YEAR] = newYear + 1;
			addInternal(KA_GEN, genDiff, false);
			break;
		case KA_MONTH:
			int newMonth = fields[KA_MONTH];
			/*
			if(newMonth == 14) {
				if(amount > 0) {
					newMonth = 13;
				}
				else if(amount < 0)
					newMonth = 14;
				if(direct) {
					fields[KA_WEEK] = 1;
					fields[KA_DAY] = 1;
				}
			}
			*/
			newMonth = newMonth - 1 + amount;
			int yearDiff = newMonth / 14;
			newMonth = newMonth % 14;
			if(newMonth < 0) {
				newMonth = 14 + newMonth;
				yearDiff-= 1;
			}
			fields[KA_MONTH] = newMonth + 1;
			addInternal(KA_YEAR, yearDiff, false);
			break;
		case KA_WEEK:
			/*
			if(fields[KA_MONTH] == 14) {
				if(amount > 0) {
					fields[KA_MONTH] = 13;
					fields[KA_WEEK] = 4;
				} else if(amount < 0) {
					fields[KA_MONTH] = 14;
					fields[KA_WEEK] = 1;
				}
				fields[KA_DAY] = 1;
			}
			*/
			if(fields[KA_MONTH] == 14) {
				if(amount > 0) {
					fields[KA_WEEK] = 4;
				} else if(amount < 0) {
					fields[KA_WEEK] = 1;
				}
			}
			int newWeek = fields[KA_WEEK] - 1 + amount;
			int monthDiff = newWeek / 4;
			newWeek = newWeek % 4;
			if(newWeek < 0) {
				newWeek = 4 + newWeek;
				monthDiff-= 1;
			}
			fields[KA_WEEK] = newWeek + 1;
			addInternal(KA_MONTH, monthDiff, false);
			break;
		case KA_DAY:
			autoAdd(amount, MILLIS_IN_DAY);
			break;
		case HOUR:
			autoAdd(amount, MILLIS_IN_DAY);
			break;
		case MINUTE:
			autoAdd(amount, MILLIS_IN_DAY);
			break;
		case SECOND:
			autoAdd(amount, MILLIS_IN_SECOND);
			break;
		case MILLISECOND:
			autoAdd(amount, 1);
			break;
		}
	}
	
	private void autoAdd(int amount, long multiplier) {
		if(!isTimeSet)
			computeTime();
		time+= amount * multiplier;
		computeFields();
	}

	@Override
	public void roll(int field, boolean up) {
		switch(field) {
		case KA_GEN:
			if(up && fields[KA_GEN] != Integer.MAX_VALUE)
				fields[KA_GEN]++;
			else if(!up && fields[KA_GEN] != Integer.MIN_VALUE)
				fields[KA_GEN]--;
			break;
		case KA_YEAR:
			if(up) {
				if(fields[KA_YEAR] == 33)
					fields[KA_YEAR] = 1;
				else
					fields[KA_YEAR]++;
			} else {
				if(fields[KA_YEAR] == 1)
					fields[KA_YEAR] = 33;
				else
					fields[KA_YEAR]--;
			}
			yearRollAdjust();
			break;
		case KA_MONTH:
			if(up) {
				if(fields[KA_MONTH] == 14 || fields[KA_MONTH] == 13)
					fields[KA_MONTH] = 1;
				else
					fields[KA_MONTH]++;
			} else {
				if(fields[KA_MONTH] == 1)
					fields[KA_MONTH] = 13;
				else
					fields[KA_MONTH]--;
			}
			//monthRollAdjust();
			break;
		case KA_WEEK:
			if(fields[KA_MONTH] == 14)
				;//do nothing
			else {
				if(up) {
					if(fields[KA_WEEK] == 4)
						fields[KA_WEEK] = 1;
					else
						fields[KA_WEEK]++;
				} else {
					if(fields[KA_WEEK] == 1)
						fields[KA_WEEK] = 4;
					else
						fields[KA_WEEK]--;
				}
			}
			//weekRollAdjust();
			break;
		case KA_DAY:
			computeLeapYear();
			if(up) {
				if(fields[KA_DAY] == 7)
					fields[KA_DAY] = 1;
				else
					fields[KA_DAY]++;
			} else {
				if(fields[KA_DAY] == 1)
					fields[KA_DAY] = 7;
				else
					fields[KA_DAY]--;
			}
			
			if(fields[KA_DAY] == 8)
				fields[KA_DAY] = 9;
			else if(fields[KA_DAY] == 9) {
				if(leapYear)
					fields[KA_DAY] = 8;
				else
					;//stay with 9
			}
			break;
		case HOUR:
			if(up) {
				if(fields[HOUR] == 23)
					fields[HOUR] = 0;
				else
					fields[HOUR]++;
			} else {
				if(fields[HOUR] == 0)
					fields[HOUR] = 23;
				else
					fields[HOUR]--;
			}
			break;
		case MINUTE:
			if(up) {
				if(fields[MINUTE] == 59)
					fields[MINUTE] = 0;
				else
					fields[MINUTE]++;
			} else {
				if(fields[MINUTE] == 0)
					fields[MINUTE] = 59;
				else
					fields[MINUTE]--;
			}
			break;
		case SECOND:
			if(up) {
				if(fields[SECOND] == 59)
					fields[SECOND] = 0;
				else
					fields[SECOND]++;
			} else {
				if(fields[SECOND] == 0)
					fields[SECOND] = 59;
				else
					fields[SECOND]--;
			}
			break;
		case MILLISECOND:
			if(up) {
				if(fields[MILLISECOND] == 999)
					fields[MILLISECOND] = 0;
				else
					fields[MILLISECOND]++;
			} else {
				if(fields[MILLISECOND] == 0)
					fields[MILLISECOND] = 999;
				else
					fields[MILLISECOND]--;
			}
		}
	}
	
	private void computeLeapYear() {
		if(fields[KA_YEAR] == 33)
			leapYear = true;
		else if(fields[KA_YEAR] == 32)
			leapYear = false;
		else
			leapYear = ((fields[KA_YEAR] % 4) == 0);
	}
	
	private void yearRollAdjust() {
		computeLeapYear();
		if(!leapYear && fields[KA_DAY] == 8)
			fields[KA_DAY] = 9;
	}
	
	/*
	private void monthRollAdjust() {
		computeLeapYear();
		if(fields[KA_MONTH] == 14) {
			fields[KA_WEEK] = 1;
			if(fields[KA_DAY] <= 7) {
				fields[KA_DAY] = 9;
			}
		} else {
			if(fields[KA_DAY] > 7) {
				fields[KA_DAY] = 1;
			}
		}
	}
	
	
	private void weekRollAdjust() {
		if(fields[KA_MONTH] == 14)
			fields[KA_WEEK] = 1;
	}
	*/

	@Override
	public int getMinimum(int field) {
		switch(field) {
		case KA_GEN:
			return Integer.MIN_VALUE;
		case KA_YEAR:
			return 0;
		case KA_MONTH:
			return 1;
		case KA_WEEK:
			return 1;
		case KA_DAY:
			return 1;
		case HOUR:
			return 0;
		case MINUTE:
			return 0;
		case SECOND:
			return 0;
		case MILLISECOND:
			return 0;
		}
		return Integer.MIN_VALUE;
	}

	@Override
	public int getMaximum(int field) {
		switch(field) {
		case KA_GEN:
			return Integer.MAX_VALUE;
		case KA_YEAR:
			return 33;
		case KA_MONTH:
			return 14;
		case KA_WEEK:
			return 4;
		case KA_DAY:
			return 9;
		case HOUR:
			return 23;
		case MINUTE:
			return 59;
		case SECOND:
			return 59;
		case MILLISECOND:
			return 999;
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public int getGreatestMinimum(int field) {
		switch(field){
		case KA_DAY:
			return 9;
		default:
			return getMinimum(field);
		}
	}

	@Override
	public int getLeastMaximum(int field) {
		switch(field){
		case KA_WEEK:
			return 1;
		case KA_DAY:
			return 7;
		default:
			return getMaximum(field);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Calendar#getActualMinimum(int)
	 */
	@Override
	public int getActualMinimum(int field) {
		if(field == KA_DAY && fields[KA_MONTH] == 14) {
			if(leapYear)
				return 8;
			else
				return 9;
		} else
			return getMinimum(field);
	}

	/* (non-Javadoc)
	 * @see java.util.Calendar#getActualMaximum(int)
	 */
	@Override
	public int getActualMaximum(int field) {
		switch(field) {
		case KA_WEEK:
			if(fields[KA_MONTH] == 14)
				return 1;
			else
				return getMaximum(field);
		case KA_DAY:
			if(fields[KA_MONTH] == 14)
				return getMaximum(field);
			else
				return 7;
		default:
			return getMaximum(field);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Calendar#set(int, int)
	 */
	@Override
	public void set(int field, int value) {
		// TODO Auto-generated method stub
		super.set(field, value);
	}

	public boolean isLeapYear() {
		return leapYear;
	}

}
