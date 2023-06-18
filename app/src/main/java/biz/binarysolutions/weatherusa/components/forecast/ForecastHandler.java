package biz.binarysolutions.weatherusa.components.forecast;

import android.app.Activity;
import android.content.ContextWrapper;
import android.location.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastJSONParser;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers.ForecastXMLParser;
import biz.binarysolutions.weatherusa.util.StringUtil;

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
	 * TODO: load forecast in separate thread?
	 * 
	 * @return
	 */
	private String loadForecast() {
		
		String forecast = "";
		
		try {
			FileInputStream in = activity.openFileInput(FILE_NAME);
			forecast = StringUtil.getString(in);
			in.close();
		} catch (IOException e) {
			// do nothing
		}
		
		return forecast;
	}

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

		if (forecast != null && forecast.startsWith("<?xml")) {

			display.clear();
			new ForecastXMLParser(forecast, display) {
				@Override
				protected void onDone() {
					/* must run on UI thread as that's where display updates
						are enqueued */
					activity.runOnUiThread(
						() -> saveForecast(display.toJSONString())
					);
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
		new ForecastJSONParser(loadForecast(), display).start();
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
