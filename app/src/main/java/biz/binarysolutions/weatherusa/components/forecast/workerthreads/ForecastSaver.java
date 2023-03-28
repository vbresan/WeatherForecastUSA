package biz.binarysolutions.weatherusa.components.forecast.workerthreads;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContextWrapper;

/**
 * 
 *
 */
public class ForecastSaver extends Thread {

	private ContextWrapper contextWrapper;
	
	private String fileName;
	private String forecast;

	/**
	 * 
	 * @param contextWrapper
	 * @param fileName
	 * @param forecast
	 */
	public ForecastSaver
		(
			ContextWrapper contextWrapper, 
			String fileName,
			String forecast
		) {
		super();

		this.contextWrapper = contextWrapper;
		this.fileName = fileName;
		this.forecast = forecast;
	}
	
	/**
	 * 
	 */
	public void run() {
		
		try {
			
			FileOutputStream out = contextWrapper.openFileOutput(
					fileName, 
					ContextWrapper.MODE_PRIVATE
				);
			
			out.write(forecast.getBytes());
			out.close();
			
		} catch (IOException e) {
			// do nothing
		}		
	}

}
