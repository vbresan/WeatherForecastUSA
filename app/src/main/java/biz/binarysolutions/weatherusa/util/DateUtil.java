package biz.binarysolutions.weatherusa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 *
 */
public class DateUtil {
	
	public static final int DAYS_IN_WEEK = 7;
	
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}	

	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		
		return calendar.getTime();
	}

	/**
	 * 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date date, int hours) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		
		return calendar.getTime();
	}

	/**
	 * 
	 * @param timestamp
	 * @param format
	 * @return
	 * @throws ParseException 
	 */
	public static Date parse(String timestamp, String format) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(timestamp);
	}
}
