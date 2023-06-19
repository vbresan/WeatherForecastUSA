package biz.binarysolutions.weatherusa.components.forecast.workerthreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import biz.binarysolutions.weatherusa.components.forecast.ForecastDisplay;

/**
 * 
 *
 */
public class ForecastJSONParser extends Thread {
	
	private final String forecast;

	private final Vector<String> maximumTemperatures = new Vector<>();
	private final Vector<String> minimumTemperatures = new Vector<>();
	
	private final Vector<String> apparentTemperatures = new Vector<>();
	private final Vector<String> dewpointTemperatures = new Vector<>();
	
	private final Vector<String> icons   = new Vector<>();
	private final Vector<String> weather = new Vector<>();
	private final Vector<String> hazards = new Vector<>();

	private final ForecastDisplay display;

	/**
	 *
	 * @param date
	 */
	private void dispatchStartDate(String date) {

		try {
			Date startDate = DateFormat.getDateInstance().parse(date);
			display.onStartDateAvailable(startDate);
		} catch (ParseException e) {
			// TODO inform user about invalid date format?
		}
	}
	
	/**
	 * 
	 */
	private void dispatchMaximumTemperatures() {
		display.onMaximumTemperaturesAvailable(maximumTemperatures);
	}
	
	/**
	 * 
	 */
	private void dispatchMinimumTemperatures() {
		display.onMinimumTemperaturesAvailable(minimumTemperatures);
	}
	
	/**
	 * 
	 */
	private void dispatchApparentTemperatures() {
		display.onApparentTemperaturesAvailable(apparentTemperatures);
	}
	
	/**
	 * 
	 */
	private void dispatchDewpointTemperatures() {
		display.onDewpointTemperaturesAvailable(dewpointTemperatures);
	}
	
	/**
	 * 
	 */
	private void dispatchIcons() {
		display.onIconsAvailable(icons);
	}
	
	/**
	 * 
	 */
	private void dispatchWeather() {
		display.onWeatherAvailable(weather);
	}
	
	/**
	 * 
	 */
	private void dispatchHazards() {
		display.onHazardsAvailable(hazards);
	}
	
	/**
	 * 
	 * @param vector
	 * @param array
	 * @throws JSONException
	 */
	private void setVector(Vector<String> vector, JSONArray array) 
		throws JSONException {
		
		for (int i = 0, length = array.length(); i < length; i++) {
			vector.add(array.getString(i));
		}		
	}

	/**
	 *
	 * @param forecast
	 * @param display
	 */
	public ForecastJSONParser(String forecast, ForecastDisplay display) {
		this.forecast = forecast;
		this.display  = display;
	}
	
	/**
	 * 
	 * @param jsonObject
	 */
	private String parseStartDate(JSONObject jsonObject) {

		String date = "";
		
		try {
			date = jsonObject.getString("startDate");
		} catch (JSONException e) {
			// do nothing
		}
		
		return date;
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @param arrayName 
	 * @param destination 
	 */
	private void parseArray
		(
				JSONObject     jsonObject, 
				String         arrayName, 
				Vector<String> destination
		) {
		
		try {
			setVector(destination, jsonObject.getJSONArray(arrayName));
		} catch (JSONException e) {
			// do nothing
		}
	}
	
	/**
	 * 
	 */
	public void run() {
		
		try {
			
			JSONObject jsonObject = new JSONObject(forecast);
			
			String date = parseStartDate(jsonObject);
			dispatchStartDate(date);
			
			parseArray(jsonObject, "maximumTemperatures", maximumTemperatures);
			dispatchMaximumTemperatures();
			
			parseArray(jsonObject, "minimumTemperatures", minimumTemperatures);
			dispatchMinimumTemperatures();
			
			parseArray(jsonObject, "apparentTemperatures", apparentTemperatures);
			dispatchApparentTemperatures();
			
			parseArray(jsonObject, "dewpointTemperatures", dewpointTemperatures);
			dispatchDewpointTemperatures();			
			
			parseArray(jsonObject, "icons", icons);
			dispatchIcons();

			parseArray(jsonObject, "weather", weather);
			dispatchWeather();

			parseArray(jsonObject, "hazards", hazards);
			dispatchHazards();
			
		} catch(JSONException e) {
			//TODO: do nothing?
		}
	}
}
