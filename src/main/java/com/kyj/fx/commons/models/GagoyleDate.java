/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.model
 *	작성일   : 2016. 2. 19.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.models;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.kyj.fx.commons.utils.DateUtil;

/**
 * @author KYJ
 *
 */
public class GagoyleDate {

	private TimeZone timeZone = TimeZone.getDefault();

	private int year;

	private int month;

	private int day;

	public GagoyleDate(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);

		this.year = instance.get(Calendar.YEAR);
		this.month = instance.get(Calendar.MONTH);
		this.day = instance.get(Calendar.DATE);
	}

	public GagoyleDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public GagoyleDate(TimeZone timeZone, int year, int month, int day) {
		this.timeZone = timeZone;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String getYearString() {
		return String.valueOf(year);
	}

	public String getMonthString() {
		return String.valueOf(month);
	}

	public String getDayString() {
		return String.valueOf(day);
	}

	/**
	 * Convert Date Class.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @return
	 */
	public Date toDate() {
		Calendar instance = Calendar.getInstance();
		instance.set(year, month, day);
		return instance.getTime();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @return
	 */
	public String toDateString() {
		return toDateString(DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @param pattern
	 * @return
	 */
	public String toDateString(String pattern) {
		return DateUtil.getDateAsStr(toDate(), pattern);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GagoyleDate [timeZone=" + timeZone + ", year=" + year + ", month=" + month + ", day=" + day + "]";
	}

}
