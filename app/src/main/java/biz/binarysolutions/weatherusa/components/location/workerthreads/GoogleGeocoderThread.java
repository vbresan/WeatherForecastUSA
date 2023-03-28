package biz.binarysolutions.weatherusa.components.location.workerthreads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import biz.binarysolutions.weatherusa.components.MessageStatus;
import biz.binarysolutions.weatherusa.components.location.LocationHandler;
import biz.binarysolutions.weatherusa.util.InternetUtil;

/**
 * 
 *
 */
public class GoogleGeocoderThread extends Thread {
	
	private static final String URI = "https://geocode-x.appspot.com/json";
	
	private final Handler handler;
	private final String  zip;

	/**
	 *
	 * @return
	 */
	private String getGeocoderResponse() {
		return InternetUtil.getGetResponse(URI + "?address=" + zip);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	private Bundle getLocationBundle(JSONObject json) {
		
		Bundle bundle = null;
		
		try {
			
			JSONObject coordinates = json
				.getJSONArray("results")
				.getJSONObject(0)
				.getJSONObject("geometry")
				.getJSONObject("location");
			
			double latitude  = coordinates.getDouble("lat");
			double longitude = coordinates.getDouble("lng");
			
			bundle = new Bundle();
			bundle.putDouble("latitude",  latitude);
			bundle.putDouble("longitude", longitude);
			
		} catch (JSONException e) {
			// do nothing
		}
	
		return bundle;
	}

	/**
	 * 
	 * @param locationHandler
	 * @param zip
	 */
	public GoogleGeocoderThread(LocationHandler locationHandler, String zip) {
		
		this.handler = new GoogleGeocoderHandler(locationHandler);
		this.zip     = zip;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		
		Message message = Message.obtain();
		Bundle  bundle  = null;
		
		message.what = MessageStatus.OK;
		
		try {
			
			String     response = getGeocoderResponse();
			JSONObject json     = new JSONObject(response);
			
			bundle = getLocationBundle(json); 
			
		} catch (JSONException e) {
			// do nothing
		}
		
		message.setData(bundle);
		handler.sendMessage(message);
	}

}
