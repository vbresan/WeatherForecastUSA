package biz.binarysolutions.weatherusa.components.forecast.workerthreads.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.ForecastJSONParserListener;

/**
 * 
 *
 */
public class ForecastJSONParser extends Thread {
	
	/**
	 * 
	 *
	 */
	private String forecast;
	private Date startDate;

	private Vector<String> maximumTemperatures = new Vector<String>();
	private Vector<String> minimumTemperatures = new Vector<String>();
	
	private Vector<String> apparentTemperatures = new Vector<String>();
	private Vector<String> dewpointTemperatures = new Vector<String>();
	
	private Vector<String> icons   = new Vector<String>();
	private Vector<String> weather = new Vector<String>();
	private Vector<String> hazards = new Vector<String>();	
	
	
	private ForecastJSONParserListener listener;

	/**
	 * 
	 */
	private Handler handler = new Handler() {
		
		public void handleMessage(Message message) {
			
			switch (message.what) {
			
			case MessageCodes.START_TIME:
				listener.onStartDateAvailable(startDate);
				break;
				
			case MessageCodes.MAXIMUM_TEMPERATURES:
				listener.onMaximumTemperaturesAvailable(maximumTemperatures);
				break;
				
			case MessageCodes.MINIMUM_TEMPERATURES:
				listener.onMinimumTemperaturesAvailable(minimumTemperatures);
				break;
				
			case MessageCodes.APPARENT_TEMPERATURES:
				listener.onApparentTemperaturesAvailable(apparentTemperatures);
				break;
				
			case MessageCodes.DEWPOINT_TEMPERATURES:
				listener.onDewpointTemperaturesAvailable(dewpointTemperatures);
				break;
				
			case MessageCodes.ICONS:
				listener.onIconsAvailable(icons);
				break;				
				
			case MessageCodes.WEATHER:
				listener.onWeatherAvailable(weather);
				break;
				
			case MessageCodes.HAZARDS:
				listener.onHazardsAvailable(hazards);

			default:
				break;
			}
		}
	};

	/**
	 *
	 * @param date
	 */
	private void dispatchStartDate(String date) {
		
		
		try {
			startDate = DateFormat.getDateInstance().parse(date);
			handler.sendEmptyMessage(MessageCodes.START_TIME);
		} catch (ParseException e) {
			// TODO inform user about invalid date format?
		}
	}
	
	/**
	 * 
	 */
	private void dispatchMaximumTemperatures() {
		handler.sendEmptyMessage(MessageCodes.MAXIMUM_TEMPERATURES);
	}
	
	/**
	 * 
	 */
	private void dispatchMinimumTemperatures() {
		handler.sendEmptyMessage(MessageCodes.MINIMUM_TEMPERATURES);
	}
	
	/**
	 * 
	 */
	private void dispatchApparentTemperatures() {
		handler.sendEmptyMessage(MessageCodes.APPARENT_TEMPERATURES);
	}
	
	/**
	 * 
	 */
	private void dispatchDewpointTemperatures() {
		handler.sendEmptyMessage(MessageCodes.DEWPOINT_TEMPERATURES);
	}
	
	/**
	 * 
	 */
	private void dispatchIcons() {
		handler.sendEmptyMessage(MessageCodes.ICONS);
	}	
	
	/**
	 * 
	 */
	private void dispatchWeather() {
		handler.sendEmptyMessage(MessageCodes.WEATHER);
	}
	
	/**
	 * 
	 */
	private void dispatchHazards() {
		handler.sendEmptyMessage(MessageCodes.HAZARDS);
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
	 */
	public ForecastJSONParser
		(
			String forecast, 
			ForecastJSONParserListener listener
		) {
		super();
		
		this.forecast = forecast;
		this.listener = listener;
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
