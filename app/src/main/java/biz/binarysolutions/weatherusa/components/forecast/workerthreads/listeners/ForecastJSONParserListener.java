package biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners;

import java.util.Date;
import java.util.Vector;

/**
 * 
 *
 */
public interface ForecastJSONParserListener {

	void onStartDateAvailable(Date startTime);

	void onMaximumTemperaturesAvailable(Vector<String> temperatures);
	void onMinimumTemperaturesAvailable(Vector<String> temperatures);
	
	void onApparentTemperaturesAvailable(Vector<String> temperatures);
	void onDewpointTemperaturesAvailable(Vector<String> temperatures);	

	void onWeatherAvailable(Vector<String> weather);
	void onHazardsAvailable(Vector<String> hazard);
	
	void onIconsAvailable(Vector<String> icons);
}
