package biz.binarysolutions.weatherusa.components.preferences;

import static biz.binarysolutions.weatherusa.MainActivity.ZIP_LENGTH;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.annotation.NonNull;

import biz.binarysolutions.weatherusa.util.location.LocationGetter;

/**
 *
 */
public class Preferences {

	private static final String PREFERENCES_NAME = "preferences";

	private static Boolean  isGPS;
	private static String   zip;
	private static Location location;

	/**
	 *
	 * @param context
	 * @return
	 */
	public static Location getLocation(Context context) {

		if (location != null) {
			return location;
		}

		SharedPreferences preferences =
			context.getApplicationContext().getSharedPreferences(
					PREFERENCES_NAME,
					Context.MODE_PRIVATE
			);

		double latitude  = preferences.getFloat("latitude", 0);
		double longitude = preferences.getFloat("longitude", 0);
		
		location = LocationGetter.getLocation(latitude, longitude);
		return location;
	}

	/**
	 *
	 * @param context
	 * @param location
	 */
	public static void saveLocation
		(
			@NonNull Context  context,
			@NonNull Location location
		) {

		Preferences.location = location;

		SharedPreferences.Editor editor =
			context.getApplicationContext().getSharedPreferences(
					PREFERENCES_NAME,
					Context.MODE_PRIVATE
			).edit();
		
		editor.putFloat("latitude",  (float) location.getLatitude());
		editor.putFloat("longitude", (float) location.getLongitude());

		editor.apply();
	}

	/**
	 * @param context
	 * @param isGPS
     * @param zip
     */
	public static void saveLocationPreferences
		(
			Context context,
			boolean isGPS,
			String  zip
		) {

		Preferences.isGPS = isGPS;
		Preferences.zip   = zip;

		SharedPreferences.Editor editor =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			).edit();

		editor.putBoolean("isGPS", isGPS);
		editor.putString("zip", zip.length() == ZIP_LENGTH? zip : "");

		editor.apply();
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isGPS(Context context) {

		if (isGPS != null) {
			return isGPS;
		}

		SharedPreferences preferences =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			);

		isGPS = preferences.getBoolean("isGPS", false);
		return isGPS;
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static String getZIP(Context context) {

		if (zip != null) {
			return zip;
		}

		SharedPreferences preferences =
			context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME,
				Context.MODE_PRIVATE
			);

		zip = preferences.getString("zip", "");
		return zip;
	}
}
