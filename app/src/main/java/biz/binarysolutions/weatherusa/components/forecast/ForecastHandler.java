package biz.binarysolutions.weatherusa.components.forecast;

import android.app.Activity;
import android.content.ContextWrapper;
import android.location.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastJSONParser;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastXMLParser;

/**
 * 
 *
 */
public class ForecastHandler {
	
	private static final String FILE_NAME = "latest.json";

	private final Activity                activity;
	private final ForecastHandlerListener listener;
	private final ForecastDisplay         display;
	
	/**
	 *
	 * @param content
	 */
	private void saveForecast(String content) {

		new Thread() {
			@Override
			public void run() {
				try {
					FileOutputStream out = activity.openFileOutput(
						FILE_NAME,
						ContextWrapper.MODE_PRIVATE
					);

					out.write(content.getBytes());
					out.close();

				} catch (IOException e) {
					// do nothing
				}
			}
		}.start();
	}

	/**
	 *
	 * @param forecast
	 */
	private void onForecastAvailable(String forecast) {

		if (forecast == null || !forecast.startsWith("<?xml")) {
			listener.onForecastUnavailable();
			return;
		}

		display.clear();

		new ForecastXMLParser(forecast, display) {
			@Override
			protected void onDone() {
				activity.runOnUiThread(() ->
					saveForecast(display.toJSONString())
				);
			}
		}.start();

		listener.onForecastAvailable();
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

		String path = activity.getFilesDir().getPath();
		File file = new File(path, FILE_NAME);
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

		FileInputStream fileInputStream;
		try {
			fileInputStream = activity.openFileInput(FILE_NAME);
		} catch (FileNotFoundException e) {
			return;
		}

		new ForecastJSONParser(fileInputStream) {

			@Override
			public void onStartDateAvailable(Date date) {
				activity.runOnUiThread(() ->
					display.onStartDateAvailable(date)
				);
			}

			@Override
			public void onMaximumTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onMaximumTemperaturesAvailable(temperatures)
				);
			}

			@Override
			public void onMinimumTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onMinimumTemperaturesAvailable(temperatures)
				);
			}

			@Override
			public void onApparentTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onApparentTemperaturesAvailable(temperatures)
				);
			}

			@Override
			public void onDewpointTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onDewpointTemperaturesAvailable(temperatures)
				);
			}

			@Override
			public void onIconsAvailable(Vector<String> icons) {
				activity.runOnUiThread(() ->
					display.onIconsAvailable(icons)
				);
			}

			@Override
			public void onWeatherAvailable(Vector<String> weather) {
				activity.runOnUiThread(() ->
					display.onWeatherAvailable(weather)
				);
			}

			@Override
			public void onHazardsAvailable(Vector<String> hazards) {
				activity.runOnUiThread(() ->
					display.onHazardsAvailable(hazards)
				);
			}

		}.start();
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
				activity.runOnUiThread(() -> onForecastAvailable(response));
			}
		}.start();
	}
}
