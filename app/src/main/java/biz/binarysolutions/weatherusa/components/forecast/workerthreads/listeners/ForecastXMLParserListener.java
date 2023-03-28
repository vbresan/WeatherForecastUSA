package biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners;

import java.util.Date;
import java.util.Vector;

import biz.binarysolutions.weatherusa.components.forecast.TimelinedData;

/**
 * 
 *
 */
public interface ForecastXMLParserListener {

	void onStartDateAvailable(Date startTime);

	void onMaximumTemperaturesAvailable(Vector<String> maximumTemperatures);
	void onMinimumTemperaturesAvailable(Vector<String> minimumTemperatures);

	void onWeatherAvailable(TimelinedData weatherSequence);
	void onHazardsAvailable(TimelinedData hazardsSequence);

	void onApparentTemperaturesAvailable(TimelinedData apparentTemperatures);
	void onDewpointTemperaturesAvailable(TimelinedData dewPointTemperatures);
}
