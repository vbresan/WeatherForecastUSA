package biz.binarysolutions.weatherusa.components.location;

import android.location.Location;

/**
 * 
 *
 */
public interface LocationHandlerListener {

	public void onLocationChanged(Location location);
	public void onConnectionError();
}
