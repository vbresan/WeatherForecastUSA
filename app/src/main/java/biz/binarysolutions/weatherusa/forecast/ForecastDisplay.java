package biz.binarysolutions.weatherusa.forecast;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

import biz.binarysolutions.weatherusa.R;
import biz.binarysolutions.weatherusa.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.util.DateUtil;

/**
 * 
 *
 */
class ForecastDisplay {
	
	private final Activity activity;
	
	private Date startDate;
	
	private TextView[] weatherBoxTitle;
	private LinearLayout[] weatherBox;
	
	private TextView[] high;
	private TextView[] highValue;
	
	private TextView[] low;
	private TextView[] lowValue;
	
	private TextView[] apparent;
	private TextView[] apparentValue;
	
	private TextView[] dew;
	private TextView[] dewValue;	
	
	private ImageView[] icon;
	private TextView[] weather;
	
	private TextView[] hazard;
	
	private final String[] iconURL = new String[DateUtil.DAYS_IN_WEEK];
	
	
	
	/**
	 * 
	 */
	private void setTextViewArrays() {
		
		weatherBoxTitle = new TextView[] {
			activity.findViewById(R.id.textViewWeatherBoxTitle01),
			activity.findViewById(R.id.textViewWeatherBoxTitle02),
			activity.findViewById(R.id.textViewWeatherBoxTitle03),
			activity.findViewById(R.id.textViewWeatherBoxTitle04),
			activity.findViewById(R.id.textViewWeatherBoxTitle05),
			activity.findViewById(R.id.textViewWeatherBoxTitle06),
			activity.findViewById(R.id.textViewWeatherBoxTitle07),
		};
		
		weatherBox = new LinearLayout[] {
			activity.findViewById(R.id.linearLayoutWeatherBox01),
			activity.findViewById(R.id.linearLayoutWeatherBox02),
			activity.findViewById(R.id.linearLayoutWeatherBox03),
			activity.findViewById(R.id.linearLayoutWeatherBox04),
			activity.findViewById(R.id.linearLayoutWeatherBox05),
			activity.findViewById(R.id.linearLayoutWeatherBox06),
			activity.findViewById(R.id.linearLayoutWeatherBox07),
		};
		
		high = new TextView[] {
			activity.findViewById(R.id.textViewHigh01),
			activity.findViewById(R.id.textViewHigh02),
			activity.findViewById(R.id.textViewHigh03),
			activity.findViewById(R.id.textViewHigh04),
			activity.findViewById(R.id.textViewHigh05),
			activity.findViewById(R.id.textViewHigh06),
			activity.findViewById(R.id.textViewHigh07),
		};
		
		highValue = new TextView[] {
			activity.findViewById(R.id.textViewHighValue01),
			activity.findViewById(R.id.textViewHighValue02),
			activity.findViewById(R.id.textViewHighValue03),
			activity.findViewById(R.id.textViewHighValue04),
			activity.findViewById(R.id.textViewHighValue05),
			activity.findViewById(R.id.textViewHighValue06),
			activity.findViewById(R.id.textViewHighValue07),
		};
		
		low = new TextView[] {
			activity.findViewById(R.id.textViewLow01),
			activity.findViewById(R.id.textViewLow02),
			activity.findViewById(R.id.textViewLow03),
			activity.findViewById(R.id.textViewLow04),
			activity.findViewById(R.id.textViewLow05),
			activity.findViewById(R.id.textViewLow06),
			activity.findViewById(R.id.textViewLow07),
		};
		
		lowValue = new TextView[] {
			activity.findViewById(R.id.textViewLowValue01),
			activity.findViewById(R.id.textViewLowValue02),
			activity.findViewById(R.id.textViewLowValue03),
			activity.findViewById(R.id.textViewLowValue04),
			activity.findViewById(R.id.textViewLowValue05),
			activity.findViewById(R.id.textViewLowValue06),
			activity.findViewById(R.id.textViewLowValue07),
		};
		
		apparent = new TextView[] {
			activity.findViewById(R.id.textViewApparent01),
			activity.findViewById(R.id.textViewApparent02),
			activity.findViewById(R.id.textViewApparent03),
			activity.findViewById(R.id.textViewApparent04),
			activity.findViewById(R.id.textViewApparent05),
			activity.findViewById(R.id.textViewApparent06),
			activity.findViewById(R.id.textViewApparent07),
		};
			
		apparentValue = new TextView[] {
			activity.findViewById(R.id.textViewApparentValue01),
			activity.findViewById(R.id.textViewApparentValue02),
			activity.findViewById(R.id.textViewApparentValue03),
			activity.findViewById(R.id.textViewApparentValue04),
			activity.findViewById(R.id.textViewApparentValue05),
			activity.findViewById(R.id.textViewApparentValue06),
			activity.findViewById(R.id.textViewApparentValue07),
		};
		
		dew = new TextView[] {
			activity.findViewById(R.id.textViewDew01),
			activity.findViewById(R.id.textViewDew02),
			activity.findViewById(R.id.textViewDew03),
			activity.findViewById(R.id.textViewDew04),
			activity.findViewById(R.id.textViewDew05),
			activity.findViewById(R.id.textViewDew06),
			activity.findViewById(R.id.textViewDew07),
		};
			
		dewValue = new TextView[] {
			activity.findViewById(R.id.textViewDewValue01),
			activity.findViewById(R.id.textViewDewValue02),
			activity.findViewById(R.id.textViewDewValue03),
			activity.findViewById(R.id.textViewDewValue04),
			activity.findViewById(R.id.textViewDewValue05),
			activity.findViewById(R.id.textViewDewValue06),
			activity.findViewById(R.id.textViewDewValue07),
		};		
		
		weather = new TextView[] {
			activity.findViewById(R.id.textViewWeather01),
			activity.findViewById(R.id.textViewWeather02),
			activity.findViewById(R.id.textViewWeather03),
			activity.findViewById(R.id.textViewWeather04),
			activity.findViewById(R.id.textViewWeather05),
			activity.findViewById(R.id.textViewWeather06),
			activity.findViewById(R.id.textViewWeather07),
		};
		
		hazard = new TextView[] {
			activity.findViewById(R.id.textViewHazard01),
			activity.findViewById(R.id.textViewHazard02),
			activity.findViewById(R.id.textViewHazard03),
			activity.findViewById(R.id.textViewHazard04),
			activity.findViewById(R.id.textViewHazard05),
			activity.findViewById(R.id.textViewHazard06),
			activity.findViewById(R.id.textViewHazard07),
		};
		
		icon = new ImageView[] {
			activity.findViewById(R.id.imageViewWeatherIcon01),
			activity.findViewById(R.id.imageViewWeatherIcon02),
			activity.findViewById(R.id.imageViewWeatherIcon03),
			activity.findViewById(R.id.imageViewWeatherIcon04),
			activity.findViewById(R.id.imageViewWeatherIcon05),
			activity.findViewById(R.id.imageViewWeatherIcon06),
			activity.findViewById(R.id.imageViewWeatherIcon07),
		};
	}
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private void displayForecastRange(Date startDate, Date endDate) {
		
		String text;

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
		
		if (startDate.getMonth() == endDate.getMonth()) {
			int endDay = DateUtil.getDayOfMonth(endDate);
			text = sdf.format(startDate) + " - " + endDay;
		} else {
			text = sdf.format(startDate) + " - " + sdf.format(endDate);
		}
		
		TextView textView = activity.findViewById(R.id.textViewForecastDates);
		textView.setText(text);		
	}
	
	/**
	 * 
	 * @param date
	 */
	private void displayWeatherBoxTitles(Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		for (int i = 0; i < weatherBoxTitle.length; i++) {
			
			String text = sdf.format(calendar.getTime()).toUpperCase();
			weatherBoxTitle[i].setText(text);
			calendar.add(Calendar.DATE, 1);
		}
	}
	
	/**
	 * 
	 * @param weather
	 * @return
	 */
	private String formatWeather(String weather) {
		
		weather = weather.replaceAll("Thunderstorms", "Thunderst.");
		weather = weather.replace(" ", "\n");
		
		return weather; 
	}

	/**
	 *
	 * @param index
	 * @param uniqueHazards
	 */
	private void displayUniqueHazards
		(
				int index, 
				LinkedHashSet<String> uniqueHazards
		) {
		
		StringBuffer sb = new StringBuffer();
		
		Iterator<String> iterator = uniqueHazards.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			sb.append(string);
		}
		
		hazard[index].setText(sb.toString());
		weatherBox[index].setBackgroundResource(R.drawable.weather_box_hazard);
	}

	/**
	 * 
	 * @param hazardsSequence
	 * @param from
	 * @param to
	 * @return
	 */
	private LinkedHashSet<String> getUniqueHazards
		(
				TimelinedData hazardsSequence, 
				int from, 
				int to
		) {
	
		LinkedHashSet<String> uniqueHazards = new LinkedHashSet<>();
	
		for (int i = from; i <= to; i++) {
			
			String hazard = hazardsSequence.dataAt(i);
			if (hazard != null) {
				uniqueHazards.add(hazard);
			}
		}
		
		return uniqueHazards;
	}

	/**
	 * 
	 * @param temperatures
	 * @param titles
	 * @param values
	 */
	private void onTemperaturesAvailable
		(
				Vector<String> temperatures, 
				TextView[]     titles, 
				TextView[]     values
		) {
		
		int length = Math.min(
				temperatures.size(), Math.min(titles.length, values.length));
		
		for (int i = 0; i < length; i++) {
	
			String value = temperatures.elementAt(i);
			if (value.length() > 0) {
				titles[i].setVisibility(View.VISIBLE);
				values[i].setText(value);
			}
		}		
	}

	/**
	 * 
	 * @param temperatures
	 * @param titles
	 * @param values
	 */
	private void onTemperaturesAvailable
		(
				TimelinedData temperatures,
				TextView[]    titles, 
				TextView[]    values
		) {
	
		Date midDay = DateUtil.addHours(startDate, 12);
		Date now    = new Date();
		Date date   = now.after(midDay) ? now : midDay;
		
		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {
			
			int index = temperatures.indexAfter(date);
			if (index >= 0) {
			
				String value = temperatures.dataAt(index);
				titles[i].setVisibility(View.VISIBLE);
				values[i].setText(value);
			}
			
			date = DateUtil.addDays(midDay, i + 1);
		}
	}

	/**
	 * 
	 * @param array 
	 * @return
	 */
	private JSONArray toJSONArray(TextView[] array) {
		
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {
			jsonArray.put(array[i].getText().toString());
		}
		
		return jsonArray;
	}
	
	/**
	 * 
	 * @param array 
	 * @return
	 */
	private JSONArray toJSONArray(String[] array) {
		
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {
			jsonArray.put(array[i]);
		}
		
		return jsonArray;
	}

	/**
	 *
	 * @param url
	 * @return
	 */
	private GlideUrl getGlideUrl(String url) {

		return new GlideUrl(
			url.replace("http://", "https://"),
			new LazyHeaders.Builder()
				.addHeader("User-Agent", ForecastUpdater.USER_AGENT)
				.build()
		);
	}

	/**
	 * 
	 * @param activity
	 */
	public ForecastDisplay(Activity activity) {
		this.activity = activity;
		setTextViewArrays();
	}

	/**
	 *
	 * @param startDate
	 */
	public void onStartDateAvailable(Date startDate) {

		this.startDate = startDate;
		Date endDate = DateUtil.addDays(startDate, DateUtil.DAYS_IN_WEEK - 1);
		displayForecastRange(startDate, endDate);
		displayWeatherBoxTitles(startDate);
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onMaximumTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, high, highValue);
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onMinimumTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, low, lowValue);
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onApparentTemperaturesAvailable(TimelinedData temperatures) {
		onTemperaturesAvailable(temperatures, apparent, apparentValue);
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onDewpointTemperaturesAvailable(TimelinedData temperatures) {
		onTemperaturesAvailable(temperatures, dew, dewValue);
	}

	/**
	 *
	 * @param weatherSequence
	 */
	public void onWeatherAvailable(TimelinedData weatherSequence) {

		Date midDay = DateUtil.addHours(startDate, 12);
		Date now    = new Date();
		Date date   = now.after(midDay) ? now : midDay;

		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {

			int index = weatherSequence.indexAfter(date);
			if (index >= 0) {

				String event = weatherSequence.dataAt(index);
				weather[i].setText(formatWeather(event));

				String url = weatherSequence.iconAt(index);
				iconURL[i] = url;

				GlideUrl glideUrl = getGlideUrl(iconURL[i]);
				Glide.with(icon[i]).load(glideUrl).into(icon[i]);
			}

			date = DateUtil.addDays(midDay, i + 1);
		}
	}

	/**
	 *
	 * @param hazardsSequence
	 */
	public void onHazardsAvailable(TimelinedData hazardsSequence) {

		Date start = startDate;
		Date end   = DateUtil.addDays(start, 1);
		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {

			int from = hazardsSequence.indexAfter(start);
			int to   = hazardsSequence.indexBefore(end);

			if (from != -1) {
				displayUniqueHazards(i, getUniqueHazards(hazardsSequence, from, to));
			}

			start = end;
			end   = DateUtil.addDays(start, 1);
		}
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onApparentTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, apparent, apparentValue);
	}

	/**
	 *
	 * @param temperatures
	 */
	public void onDewpointTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, dew, dewValue);
	}

	/**
	 *
	 * @param weather
	 */
	public void onWeatherAvailable(Vector<String> weather) {

		for (int i = 0, length = weather.size(); i < length; i++) {

			String value = weather.elementAt(i);
			if (value.length() > 0) {
				this.weather[i].setText(value);
			}
		}
	}

	/**
	 *
	 * @param hazard
	 */
	public void onHazardsAvailable(Vector<String> hazard) {

		for (int i = 0, length = hazard.size(); i < length; i++) {

			String value = hazard.elementAt(i);
			if (value.length() > 0) {
				this.hazard[i].setText(value);
				weatherBox[i].setBackgroundResource(R.drawable.weather_box_hazard);
			}
		}
	}

	/**
	 *
	 * @param icons
	 */
	public void onIconsAvailable(Vector<String> icons) {

		for (int i = 0, length = icons.size(); i < length; i++) {

			String url = icons.elementAt(i);
			if (url.length() > 0) {

				iconURL[i] = url;
				GlideUrl glideUrl = getGlideUrl(iconURL[i]);
				Glide.with(icon[i]).load(glideUrl).into(icon[i]);
			}
		}
	}

	/**
	 * 
	 */
	public void clear() {

		for (int i = 0; i < DateUtil.DAYS_IN_WEEK; i++) {
			
			lowValue[i].setText("");
			low[i].setVisibility(View.INVISIBLE);
			
			highValue[i].setText("");
			high[i].setVisibility(View.INVISIBLE);
			
			apparentValue[i].setText("");
			apparent[i].setVisibility(View.INVISIBLE);
			
			dewValue[i].setText("");
			dew[i].setVisibility(View.INVISIBLE);			
			
			weather[i].setText("");
			icon[i].setImageDrawable(null);
			iconURL[i] = null;
			
			hazard[i].setText("");
			weatherBox[i].setBackgroundResource(R.drawable.weather_box);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String toJSONString() {
		
		JSONObject json = new JSONObject();

		try {
			
			String date = "";
			if (startDate != null) {
				date = DateFormat.getDateInstance().format(startDate);
			}
			
			json.put("startDate", date);
			json.put("maximumTemperatures", toJSONArray(highValue));
			json.put("minimumTemperatures", toJSONArray(lowValue));
			json.put("apparentTemperatures", toJSONArray(apparentValue));
			json.put("dewpointTemperatures", toJSONArray(dewValue));
			json.put("icons", toJSONArray(iconURL));
			json.put("weather", toJSONArray(weather));
			json.put("hazards", toJSONArray(hazard));
			
		} catch (JSONException e) {
			// do nothing
		}
	
		return json.toString();
	}
}
