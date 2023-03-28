package biz.binarysolutions.weatherusa.components.preferences;

import android.content.SharedPreferences;
import android.location.Location;
import biz.binarysolutions.weatherusa.components.location.LocationHandler;
import biz.binarysolutions.weatherusa.util.location.LocationGetter;

/**
 * TODO: separate thread?
 *
 */
public class Preferences {
	
	private static Location location;

	/**
	 * 
	 * @param sharedPreferences 
	 * @param locationHandler
	 */
	public static void load
		(
			SharedPreferences sharedPreferences, 
			LocationHandler   locationHandler
		) {

		double lat = sharedPreferences.getFloat("latitude", 0);
		double lon = sharedPreferences.getFloat("longitude", 0);
		
		if (lat != 0 && lon != 0) {
			location = LocationGetter.getLocation(lat, lon);
			locationHandler.setLocation(location);
		}		
	}

	/**
	 * @param locationHandler 
	 * @param sharedPreferences 
	 * 
	 */
	public static void save
		(
			SharedPreferences sharedPreferences, 
			LocationHandler   locationHandler
		) {
		
		Location newLocation = locationHandler.getLastKnownLocation();
		if (newLocation != null && !newLocation.equals(location)) {
			
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    
		    editor.putFloat("latitude",  (float) newLocation.getLatitude());
		    editor.putFloat("longitude", (float) newLocation.getLongitude());

		    editor.commit();		
		}
	}

}
