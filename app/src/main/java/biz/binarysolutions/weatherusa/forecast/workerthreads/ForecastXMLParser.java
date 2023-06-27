package biz.binarysolutions.weatherusa.forecast.workerthreads;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import biz.binarysolutions.weatherusa.forecast.TimelinedData;
import biz.binarysolutions.weatherusa.util.DateUtil;
import biz.binarysolutions.weatherusa.util.StringUtil;

/**
 * 
 *
 */
public abstract class ForecastXMLParser extends Thread {
	
	private static final String TIMESTAMP_DATE = "yyyy-MM-dd";
	private static final String TIMESTAMP_FULL = "yyyy-MM-dd'T'HH:mm";
	
	private boolean isStartTimeDispached = false;
	
	private boolean inMaximumTemperature  = false;
	private boolean inMinimumTemperature  = false;
	private boolean inDewPointTemperature = false;
	private boolean inApparentTemperature = false;
	private boolean inWeatherConditions   = false;
	
	private final String forecast;

	private Vector<Date> lastTimeline;
	private final HashMap<String, Vector<Date>> timelines = new HashMap<>();
	
	private final Vector<String> maximumTemperatures  = new Vector<>();
	private final Vector<String> minimumTemperatures  = new Vector<>();
	
	private final StringBuffer currentWeather = new StringBuffer();
	private final StringBuffer currentHazard  = new StringBuffer();

	private final TimelinedData weather = new TimelinedData();
	private final TimelinedData hazards = new TimelinedData();

	private final TimelinedData apparentTemperatures = new TimelinedData();
	private final TimelinedData dewPointTemperatures = new TimelinedData();
	
	/**
	 * 
	 */
	private void resetTagFlags() {
		
		inMaximumTemperature  = false; 
		inMinimumTemperature  = false; 
		inDewPointTemperature = false; 
		inApparentTemperature = false;
	}

	/**
	 * 
	 * @param parser
	 */
	private void appendToCurrentWeather(XmlPullParser parser) {
		
		String additive = parser.getAttributeValue(null, "additive");
		if (additive != null) {
			currentWeather.append(additive);
			currentWeather.append(" ");
		}

		String weather = parser.getAttributeValue(null, "weather-type");
		if (weather != null) {
			currentWeather.append(StringUtil.capitalize(weather));
			currentWeather.append(" ");
		}
	}
	
	/**
	 * 
	 * @param parser
	 */
	private void appendToCurrentHazard(XmlPullParser parser) {
		
		String significance = parser.getAttributeValue(null, "significance");
		if (significance != null) {
			currentHazard.append(significance);
			currentHazard.append(":\n");
		}

		String phenomena = parser.getAttributeValue(null, "phenomena");
		if (phenomena != null) {
			currentHazard.append(phenomena);
			currentHazard.append("\n");
		}
	}	
	
	/**
	 * 
	 * @param parser
	 * @return
	 */
	private Vector<Date> getReferencedTimeline(XmlPullParser parser) {
		
		String timelineName = parser.getAttributeValue(null, "time-layout");
		return timelines.get(timelineName);
	}

	/**
	 * 
	 */
	private void parseStartTag(XmlPullParser parser) {

		String tagName = parser.getName();
		if (tagName.equals("hazards")) {
			hazards.setTimeline(getReferencedTimeline(parser));
		} else if (tagName.equals("value")) {
			
			if (inWeatherConditions) {
				appendToCurrentWeather(parser);
			}
			
		}  else if (tagName.equals("hazard")) {
			appendToCurrentHazard(parser);
		} else if (tagName.equals("temperature")) {
			
			String type = parser.getAttributeValue(null, "type");
			if (type.equals("maximum")) {
				inMaximumTemperature = true;
			} else if (type.equals("minimum")) {
				inMinimumTemperature = true; 
			} else if (type.equals("dew point")) {
				inDewPointTemperature = true;
				dewPointTemperatures.setTimeline(getReferencedTimeline(parser));
			} else if (type.equals("apparent")) {
				inApparentTemperature = true;
				apparentTemperatures.setTimeline(getReferencedTimeline(parser));
			}
			
		} else if (tagName.equals("time-layout")) {
			lastTimeline = new Vector<Date>();
		} else if (tagName.equals("weather")) {
			weather.setTimeline(getReferencedTimeline(parser));
		} else if (tagName.equals("weather-conditions")) {
			inWeatherConditions = true;
		}
	}

	/**
	 * 
	 * @param parser
	 * @param currentText
	 */
	private void parseEndTag(XmlPullParser parser, String currentText) {
		
		String tagName = parser.getName();
		if (tagName.equals("conditions-icon")) {
			onWeatherAvailable(weather);
		} else if (tagName.equals("hazard-conditions")) {
			
			hazards.addData(currentHazard.toString().trim());
			currentHazard.setLength(0);
			
		} else if (tagName.equals("hazards")) {
			onHazardsAvailable(hazards);
		}  else if (tagName.equals("icon-link")) {
			weather.addIcon(currentText);
		} else if (tagName.equals("start-valid-time")) {
			
			if (!isStartTimeDispached) {
				try {
					Date startDate = DateUtil.parse(currentText, TIMESTAMP_DATE);
					onStartDateAvailable(startDate);
				} catch (ParseException e) {
					// do nothing
				}
				isStartTimeDispached = true;
			}
			
			try {
				Date date = DateUtil.parse(currentText, TIMESTAMP_FULL);
				lastTimeline.add(date);
			} catch (ParseException e) {
				//TODO do nothing?
			}
			
		} else if (tagName.equals("value")) {
			
			if (inMaximumTemperature) {
				maximumTemperatures.add(currentText);
			} else if (inMinimumTemperature) {
				minimumTemperatures.add(currentText);
			} else if (inDewPointTemperature) {
				dewPointTemperatures.addData(currentText);
			} else if (inApparentTemperature) {
				apparentTemperatures.addData(currentText);
			}
			
		} else if (tagName.equals("temperature")) {
			
			if (inMaximumTemperature) {
				onMaximumTemperaturesAvailable(maximumTemperatures);
			} else if (inMinimumTemperature) {
				onMinimumTemperaturesAvailable(minimumTemperatures);
			} else if (inDewPointTemperature) {
				onDewpointTemperaturesAvailable(dewPointTemperatures);
			} else if (inApparentTemperature) {
				onApparentTemperaturesAvailable(apparentTemperatures);
			}
			
			resetTagFlags();
			
		} else if (tagName.equals("layout-key")) {
			timelines.put(currentText, lastTimeline);
		} else if (tagName.equals("weather-conditions")) {
			
			inWeatherConditions = false;
			weather.addData(currentWeather.toString().trim());
			currentWeather.setLength(0);
			
		} 
	}

	/**
	 * @param parser 
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 * 
	 */
	private void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		String currentText = "";
		int    eventType   = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			
			if (eventType == XmlPullParser.START_TAG) {
				parseStartTag(parser);
			} else if (eventType == XmlPullParser.END_TAG) {
				parseEndTag(parser, currentText.trim());
				currentText = "";
			} else if (eventType == XmlPullParser.TEXT) {
				currentText += parser.getText();
			}
			
			eventType = parser.next();
		}		
	}

	protected abstract void onStartDateAvailable(Date date);
	protected abstract void onMaximumTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onMinimumTemperaturesAvailable(Vector<String> temperatures);
	protected abstract void onDewpointTemperaturesAvailable(TimelinedData temperatures);
	protected abstract void onApparentTemperaturesAvailable(TimelinedData temperatures);
	protected abstract void onWeatherAvailable(TimelinedData weather);
	protected abstract void onHazardsAvailable(TimelinedData hazards);
	protected abstract void onDone();

	/**
	 *
	 * @param forecast
	 */
	public ForecastXMLParser(String forecast) {
		this.forecast = forecast;
	}

	/**
	 * 
	 */
	public void run() {
		
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(StringUtil.getInputStream(forecast), "UTF-8");
			parse(parser);
			onDone();
		} catch (Exception e) {
			// TODO inform user about the error?
		} 
	}
}
