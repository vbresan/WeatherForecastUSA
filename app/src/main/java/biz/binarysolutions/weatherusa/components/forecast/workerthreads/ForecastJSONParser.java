package biz.binarysolutions.weatherusa.components.forecast.workerthreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import biz.binarysolutions.weatherusa.util.StringUtil;

/**
 * 
 *
 */
public abstract class ForecastJSONParser extends Thread {
	
	private final FileInputStream fileInputStream;

	/**
	 *
	 * @return
	 */
	private String getForecast() {

		String forecast = "";

		try {
			forecast = StringUtil.getString(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			// do nothing
		}

		return forecast;
	}

	/**
	 *
	 * @param jsonObject
	 */
	private Date parseStartDate(JSONObject jsonObject) {

		Date date = null;

		try {
			String string = jsonObject.getString("startDate");
			date = DateFormat.getDateInstance().parse(string);
		} catch (JSONException | ParseException e) {
			// do nothing
		}

		return date;
	}

	/**
	 *
	 * @param jsonObject
	 * @param arrayName
	 */
	private Vector<String> getVector(JSONObject jsonObject, String arrayName) {

		Vector<String> vector = new Vector<>();

		try {
			JSONArray array = jsonObject.getJSONArray(arrayName);
			for (int i = 0, length = array.length(); i < length; i++) {
				vector.add(array.getString(i));
			}
		} catch (JSONException e) {
			// do nothing
		}

		return vector;
	}

	protected abstract void onStartDateAvailable(Date date);
	protected abstract void onMaximumTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onMinimumTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onApparentTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onDewpointTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onIconsAvailable(Vector<String> icons);
	protected abstract void onWeatherAvailable(Vector<String> weather);
	protected abstract void onHazardsAvailable(Vector<String> hazards);

	/**
	 *
	 * @param fileInputStream
	 */
	public ForecastJSONParser(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	/**
	 * 
	 */
	public void run() {
		
		try {
			
			JSONObject jsonObject = new JSONObject(getForecast());
			
			Date date = parseStartDate(jsonObject);
			onStartDateAvailable(date);

			Vector<String> array;

			array = getVector(jsonObject, "maximumTemperatures");
			onMaximumTemperaturesAvailable(array);

			array = getVector(jsonObject, "minimumTemperatures");
			onMinimumTemperaturesAvailable(array);

			array = getVector(jsonObject, "apparentTemperatures");
			onApparentTemperaturesAvailable(array);

			array = getVector(jsonObject, "dewpointTemperatures");
			onDewpointTemperaturesAvailable(array);

			array = getVector(jsonObject, "icons");
			onIconsAvailable(array);

			array = getVector(jsonObject, "weather");
			onWeatherAvailable(array);

			array = getVector(jsonObject, "hazards");
			onHazardsAvailable(array);
			
		} catch(JSONException e) {
			// do nothing
		}
	}
}
