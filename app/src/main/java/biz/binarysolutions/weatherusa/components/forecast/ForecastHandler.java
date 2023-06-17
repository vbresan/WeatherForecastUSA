package biz.binarysolutions.weatherusa.components.forecast;

import android.app.Activity;
import android.location.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastSaver;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastJSONParser;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastXMLParser;
import biz.binarysolutions.weatherusa.util.StringUtil;

/**
 * 
 *
 */
public class ForecastHandler {
	
	private static final String FILE_NAME_JSON = "latest.json";
	
	private static final String FULL_PATH = "/data/data/biz.binarysolutions.weatherusa/files/";
	
	private Activity activity;
	private ForecastHandlerListener listener;
	
	private ForecastDisplay display;
	
	/**
	 * TODO: load forecast in separate thread?
	 * 
	 * @return
	 */
	private String loadForecast(String fileName) {
		
		String forecast = "";
		
		try {
			FileInputStream in = activity.openFileInput(fileName);
			forecast = StringUtil.getString(in);
			in.close();
		} catch (IOException e) {
			// do nothing
		}
		
		return forecast;
	}

	/**
	 *
	 * @param forecast
	 */
	private void onForecastAvailable(String forecast) {

		if (forecast != null && forecast.startsWith("<?xml")) {

			display.clear();
			new ForecastXMLParser(forecast, display) {
				@Override
				protected void onDone() {
					// must run on UI thread as that's where display update is enqueued
					activity.runOnUiThread(() -> {
						String json = display.toJSONString();
						new ForecastSaver(activity, FILE_NAME_JSON, json).start();
					});
				}
			}.start();

			listener.onForecastAvailable();
		} else {
			listener.onForecastUnavailable();
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public ForecastHandler
		(	
			Activity activity,
			ForecastHandlerListener listener
		) {
		
		this.activity = activity;
		this.listener = listener;
		
		this.display = new ForecastDisplay(activity);
	}

	/**
	 * 
	 * @return
	 */
	public Date getLastKnownForecastDate() {
	
		Date date = null;
	
		File file = new File(FULL_PATH + FILE_NAME_JSON);
		if (file.exists()) {
			date = new Date(file.lastModified());
		}
		
		return date;
	}

	/**
	 * 
	 * @return
	 */
	public void getLastKnownForecast() {
		new ForecastJSONParser(loadForecast(FILE_NAME_JSON), display).start();
	}

	/**
	 *
	 * @param location
	 */
	public void updateForecast(Location location) {
		
		if (location == null) {
			return;
		}

		new ForecastUpdater(location) {
			@Override
			protected void onResponseReceived(String response) {
				//TODO: once got rid of handler in ForecastXMLParser
				//	run it on it's native thread
				activity.runOnUiThread(() -> onForecastAvailable(response));
			}
		}.start();
	}
}
