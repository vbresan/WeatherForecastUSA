package biz.binarysolutions.weatherusa.util.location;

import android.location.Location;
import android.location.LocationManager;

/**
 * 
 *
 */
public class LocationGetter {
	
	private static Location LOCATION = null;

	/**
	 * 
	 * @param lm
	 * @return
	 */
	private static Location getLastKnownGPSLocation(LocationManager lm) {
		
		Location location = null;
		
		try {
			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			// do nothing
		}
		
		return location;
	}

	/**
	 * 
	 * @param lm
	 * @return
	 */
	private static Location getLastKnownNetworkLocation(LocationManager lm) {
		
		Location location = null;
		
		try {
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			// do nothing
		}
		
		return location;
	}

	/**
	 * 
	 * @param location
	 */
	public static void setLastKnownLocation(Location location) {
		LOCATION = location;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Location getLastKnownLocation(LocationManager lm) {
		
		Location location = getLastKnownGPSLocation(lm);
		if (location != null) {
			return location;
		}
		
		location = getLastKnownNetworkLocation(lm);
		if (location != null) {
			return location;
		}
		
		return LOCATION;
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static Location getLocation(double latitude, double longitude) {
		
		Location location = new Location(LocationManager.GPS_PROVIDER);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setTime(System.currentTimeMillis());
		
		return location;
	}
	
}
