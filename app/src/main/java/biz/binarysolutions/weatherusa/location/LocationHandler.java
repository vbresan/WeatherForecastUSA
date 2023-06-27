package biz.binarysolutions.weatherusa.location;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import biz.binarysolutions.weatherusa.MainActivity;
import biz.binarysolutions.weatherusa.preferences.Preferences;
import biz.binarysolutions.weatherusa.util.WeatherLocation;

/**
 *
 *
 */
public class LocationHandler extends DefaultLocationListener {

	private final LocationManager locationManager;
	private final MainActivity    activity;

	private Location location;
	private String   provider;

	/**
	 *
	 * @param permission
	 * @return
	 */
	private boolean hasPermission(String permission) {
		return ActivityCompat.checkSelfPermission(activity, permission) ==
				PackageManager.PERMISSION_GRANTED;
	}

	/**
	 *
	 */
	private void requestPermission() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}

		activity.requestPermissions(
			new String[]{ ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION }, 0);
	}

	/**
	 *
	 * @return
	 */
	private Location getLastKnownGPSLocation() {

		if (!hasPermission(ACCESS_FINE_LOCATION)) {
			requestPermission();
			return null;
		}

		//noinspection "MissingPermission"
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	/**
	 *
	 * @return
	 */
	private Location getLastKnownNetworkLocation() {

		if (!hasPermission(ACCESS_COARSE_LOCATION)) {
			requestPermission();
			return null;
		}

		//noinspection "MissingPermission"
		return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * 
	 * @param activity
	 */
	public LocationHandler(MainActivity activity) {
		this.activity        = activity;
		this.locationManager = (LocationManager)
			activity.getSystemService(Context.LOCATION_SERVICE);

		WeatherLocation location = Preferences.getLocation(activity);
		if (location.isValid()) {
			this.location = location;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Location getLastKnownLocation() {

		if (location != null) {
			return location;
		}

		location = getLastKnownGPSLocation();
		if (location != null) {
			return location;
		}

		location = getLastKnownNetworkLocation();
		return location;
	}

	/**
	 *
	 */
	public void requestLocationUpdate() {

		if (!hasPermission(ACCESS_FINE_LOCATION) && !hasPermission(ACCESS_COARSE_LOCATION)) {
			requestPermission();
			return;
		}

		//noinspection "MissingPermission"
		locationManager.requestSingleUpdate(provider, this, null);
	}

	/**
	 *
	 * @return
	 */
	public boolean hasProvider() {

		provider = locationManager.getBestProvider(new Criteria(), true);
		return provider != null;
	}

	@Override
	public void onLocationChanged(Location location) {

		Preferences.saveLocation(activity, location);
		activity.onLocationChanged(location);
	}
}
