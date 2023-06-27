package biz.binarysolutions.weatherusa.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * 
 *
 */
class DefaultLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		// do nothing
	}

	@Override
	public void onProviderDisabled(String provider) {
		// do nothing
	}

	@Override
	public void onProviderEnabled(String provider) {
		// do nothing
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// do nothing
	}
}
