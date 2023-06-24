package biz.binarysolutions.weatherusa.components.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import biz.binarysolutions.weatherusa.components.location.LocationHandler;
import biz.binarysolutions.weatherusa.util.location.LocationGetter;

/**
 *
 */
public class Preferences {

	private static final String PREFERENCES_NAME = "preferences";
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
	@SuppressLint("ApplySharedPref")
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

	/**
	 * @param context
	 * @param isGPS
     * @param zip
     */
	@SuppressLint("ApplySharedPref")
	public static void save(Context context, boolean isGPS, String zip) {

		SharedPreferences.Editor editor =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			).edit();

		editor.putBoolean("isGPS", isGPS);
		editor.putString("zip", zip.length() == 5? zip : "");

		editor.commit();
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isGPS(Context context) {

		SharedPreferences preferences =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			);

		return preferences.getBoolean("isGPS", false);
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static String getZIP(Context context) {

		SharedPreferences preferences =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			);

		return preferences.getString("zip", "");
	}
}
