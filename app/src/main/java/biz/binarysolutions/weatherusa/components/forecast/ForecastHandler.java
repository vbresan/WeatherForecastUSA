package biz.binarysolutions.weatherusa.components.forecast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import biz.binarysolutions.weatherusa.R;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastSaver;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.ForecastUpdaterListener;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastJSONParser;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastXMLParser;
import biz.binarysolutions.weatherusa.util.StringUtil;

/**
 * 
 *
 */
public class ForecastHandler implements ForecastUpdaterListener { 
	
	private static final String FILE_NAME_XML  = "latest.xml";
	private static final String FILE_NAME_JSON = "latest.json";
	
	private static final String FULL_PATH = "/data/data/biz.binarysolutions.weatherusa/files/";
	
	private Activity activity;
	private ForecastHandlerListener listener;
	
	private ForecastDisplay display;
	
	private boolean isForecastUpdated = false;
	
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
	
		File file = new File(FULL_PATH + FILE_NAME_XML);
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
	 * @param context 
	 * @param location 
	 * 
	 */
	public void updateForecast(Location location) {
		
		if (location == null) {
			return;
		}
		
		ProgressDialog dialog = ProgressDialog.show(
			activity, "", activity.getString(R.string.GettingForecast));
		
		display.clear();
		new ForecastUpdater(location, dialog, this).start();
		isForecastUpdated = true;
	}

	/**
	 * 
	 */
	public void saveForecast() {
		
		if (isForecastUpdated) {
			String json = display.toJSONObject().toString();
			new ForecastSaver(activity, FILE_NAME_JSON, json).start();
		}
	}

	@Override
	public void onForecastAvailable(String forecast) {
		
		if (forecast != null && forecast.startsWith("<?xml")) {
			new ForecastSaver(activity, FILE_NAME_XML, forecast).start();
			new ForecastXMLParser(forecast, display).start();
			listener.onForecastAvailable(forecast);
		} else {
			listener.onForecastUnavailable();
		}
	}

	@Override
	public void onConnectionError() {
		listener.onConnectionError();
	}
}
