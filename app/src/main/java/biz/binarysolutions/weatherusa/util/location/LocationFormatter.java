package biz.binarysolutions.weatherusa.util.location;

import java.text.DecimalFormat;

import android.location.Location;

/**
 * 
 *
 */
public class LocationFormatter {
	
	private static final DecimalFormat df = new DecimalFormat("###.00");
	
    /**
	 * 
	 * @param latitude
	 * @return
	 */
	private static String getFormattedLatitude(double latitude) {
	
		StringBuffer  sb = new StringBuffer();
		
		if (latitude < 0) {
			sb.append(df.format(Math.abs(latitude)));
			sb.append("°S");
		} else {
			sb.append(df.format(latitude));
			sb.append("°N");			
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @param longitude
	 * @return
	 */
	private static String getFormattedLongitude(double longitude) {
	
		StringBuffer  sb = new StringBuffer();
		
		if (longitude < 0) {
			sb.append(df.format(Math.abs(longitude)));
			sb.append("°W");
		} else {
			sb.append(df.format(longitude));
			sb.append("°E");			
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public static String format(Location location) {
		
		return new StringBuffer()
					.append(getFormattedLatitude(location.getLatitude()))
					.append(" ")
					.append(getFormattedLongitude(location.getLongitude()))
					.toString();
	}	
}
