package biz.binarysolutions.weatherusa.forecast;

import static biz.binarysolutions.weatherusa.MainActivity.ZIP_LENGTH;

import android.content.ContextWrapper;
import android.location.Location;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import biz.binarysolutions.weatherusa.MainActivity;
import biz.binarysolutions.weatherusa.forecast.workerthreads.ForecastJSONParser;
import biz.binarysolutions.weatherusa.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.forecast.workerthreads.ForecastXMLParser;

/**
 * 
 *
 */
public class ForecastHandler {
	
	private static final String FILE_NAME = "latest.json";

	private final MainActivity    activity;
	private final ForecastDisplay display;
	
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
			activity.onForecastUnavailable();
			return;
		}

		display.clear();

		new ForecastXMLParser(forecast) {
			@Override
			public void onStartDateAvailable(Date date) {
				activity.runOnUiThread(() ->
					display.onStartDateAvailable(date)
				);
			}

			@Override
			protected void onMaximumTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onMaximumTemperaturesAvailable(temperatures)
				);
			}

			@Override
			protected void onMinimumTemperaturesAvailable(Vector<String> temperatures) {
				activity.runOnUiThread(() ->
					display.onMinimumTemperaturesAvailable(temperatures)
				);
			}

			@Override
			protected void onDewpointTemperaturesAvailable(TimelinedData temperatures) {
				activity.runOnUiThread(() ->
					display.onDewpointTemperaturesAvailable(temperatures)
				);
			}

			@Override
			protected void onApparentTemperaturesAvailable(TimelinedData temperatures) {
				activity.runOnUiThread(() ->
					display.onApparentTemperaturesAvailable(temperatures)
				);
			}

			@Override
			protected void onWeatherAvailable(TimelinedData weather) {
				activity.runOnUiThread(() ->
					display.onWeatherAvailable(weather)
				);
			}

			@Override
			protected void onHazardsAvailable(TimelinedData hazards) {
				activity.runOnUiThread(() ->
					display.onHazardsAvailable(hazards)
				);
			}

			@Override
			protected void onDone() {
				activity.runOnUiThread(() ->
					saveForecast(display.toJSONString())
				);
			}
		}.start();

		activity.onForecastAvailable();
	}

	/**
	 *
	 */
	public ForecastHandler(MainActivity activity) {
		this.activity = activity;
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

	/**
	 *
	 * @param zip
	 */
	public void updateForecast(String zip) {

		if (TextUtils.isEmpty(zip) || zip.length() != ZIP_LENGTH) {
			return;
		}

		new ForecastUpdater(zip) {
			@Override
			protected void onResponseReceived(String response) {
				activity.runOnUiThread(() -> onForecastAvailable(response));
			}
		}.start();
	}
}
