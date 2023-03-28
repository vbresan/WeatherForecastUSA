package biz.binarysolutions.weatherusa.components.location;

import android.location.Location;

/**
 * 
 *
 */
public interface LocationHandlerListener {

	void onLocationChanged(Location location);
	void onConnectionError();
}
