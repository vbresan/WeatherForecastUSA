package biz.binarysolutions.weatherusa.components.forecast;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.ForecastUpdater;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.ForecastJSONParserListener;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.ForecastXMLParserListener;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.WeatherIconSetterListener;
import biz.binarysolutions.weatherusa.util.DateUtil;

/**
 * 
 *
 */
class ForecastDisplay implements 
	ForecastXMLParserListener, ForecastJSONParserListener, 
	WeatherIconSetterListener {
	
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
			activity.findViewById(R.id.TextViewWeatherBoxTitle01),
			activity.findViewById(R.id.TextViewWeatherBoxTitle02),
			activity.findViewById(R.id.TextViewWeatherBoxTitle03),
			activity.findViewById(R.id.TextViewWeatherBoxTitle04),
			activity.findViewById(R.id.TextViewWeatherBoxTitle05),
			activity.findViewById(R.id.TextViewWeatherBoxTitle06),
			activity.findViewById(R.id.TextViewWeatherBoxTitle07),
		};
		
		weatherBox = new LinearLayout[] {
			activity.findViewById(R.id.LinearLayoutWeatherBox01),
			activity.findViewById(R.id.LinearLayoutWeatherBox02),
			activity.findViewById(R.id.LinearLayoutWeatherBox03),
			activity.findViewById(R.id.LinearLayoutWeatherBox04),
			activity.findViewById(R.id.LinearLayoutWeatherBox05),
			activity.findViewById(R.id.LinearLayoutWeatherBox06),
			activity.findViewById(R.id.LinearLayoutWeatherBox07),
		};
		
		high = new TextView[] {
			activity.findViewById(R.id.TextViewHigh01),
			activity.findViewById(R.id.TextViewHigh02),
			activity.findViewById(R.id.TextViewHigh03),
			activity.findViewById(R.id.TextViewHigh04),
			activity.findViewById(R.id.TextViewHigh05),
			activity.findViewById(R.id.TextViewHigh06),
			activity.findViewById(R.id.TextViewHigh07),
		};
		
		highValue = new TextView[] {
			activity.findViewById(R.id.TextViewHighValue01),
			activity.findViewById(R.id.TextViewHighValue02),
			activity.findViewById(R.id.TextViewHighValue03),
			activity.findViewById(R.id.TextViewHighValue04),
			activity.findViewById(R.id.TextViewHighValue05),
			activity.findViewById(R.id.TextViewHighValue06),
			activity.findViewById(R.id.TextViewHighValue07),
		};
		
		low = new TextView[] {
			activity.findViewById(R.id.TextViewLow01),
			activity.findViewById(R.id.TextViewLow02),
			activity.findViewById(R.id.TextViewLow03),
			activity.findViewById(R.id.TextViewLow04),
			activity.findViewById(R.id.TextViewLow05),
			activity.findViewById(R.id.TextViewLow06),
			activity.findViewById(R.id.TextViewLow07),
		};
		
		lowValue = new TextView[] {
			activity.findViewById(R.id.TextViewLowValue01),
			activity.findViewById(R.id.TextViewLowValue02),
			activity.findViewById(R.id.TextViewLowValue03),
			activity.findViewById(R.id.TextViewLowValue04),
			activity.findViewById(R.id.TextViewLowValue05),
			activity.findViewById(R.id.TextViewLowValue06),
			activity.findViewById(R.id.TextViewLowValue07),
		};
		
		apparent = new TextView[] {
			activity.findViewById(R.id.TextViewApparent01),
			activity.findViewById(R.id.TextViewApparent02),
			activity.findViewById(R.id.TextViewApparent03),
			activity.findViewById(R.id.TextViewApparent04),
			activity.findViewById(R.id.TextViewApparent05),
			activity.findViewById(R.id.TextViewApparent06),
			activity.findViewById(R.id.TextViewApparent07),
		};
			
		apparentValue = new TextView[] {
			activity.findViewById(R.id.TextViewApparentValue01),
			activity.findViewById(R.id.TextViewApparentValue02),
			activity.findViewById(R.id.TextViewApparentValue03),
			activity.findViewById(R.id.TextViewApparentValue04),
			activity.findViewById(R.id.TextViewApparentValue05),
			activity.findViewById(R.id.TextViewApparentValue06),
			activity.findViewById(R.id.TextViewApparentValue07),
		};
		
		dew = new TextView[] {
			activity.findViewById(R.id.TextViewDew01),
			activity.findViewById(R.id.TextViewDew02),
			activity.findViewById(R.id.TextViewDew03),
			activity.findViewById(R.id.TextViewDew04),
			activity.findViewById(R.id.TextViewDew05),
			activity.findViewById(R.id.TextViewDew06),
			activity.findViewById(R.id.TextViewDew07),
		};
			
		dewValue = new TextView[] {
			activity.findViewById(R.id.TextViewDewValue01),
			activity.findViewById(R.id.TextViewDewValue02),
			activity.findViewById(R.id.TextViewDewValue03),
			activity.findViewById(R.id.TextViewDewValue04),
			activity.findViewById(R.id.TextViewDewValue05),
			activity.findViewById(R.id.TextViewDewValue06),
			activity.findViewById(R.id.TextViewDewValue07),
		};		
		
		weather = new TextView[] {
			activity.findViewById(R.id.TextViewWeather01),
			activity.findViewById(R.id.TextViewWeather02),
			activity.findViewById(R.id.TextViewWeather03),
			activity.findViewById(R.id.TextViewWeather04),
			activity.findViewById(R.id.TextViewWeather05),
			activity.findViewById(R.id.TextViewWeather06),
			activity.findViewById(R.id.TextViewWeather07),
		};
		
		hazard = new TextView[] {
			activity.findViewById(R.id.TextViewHazard01),
			activity.findViewById(R.id.TextViewHazard02),
			activity.findViewById(R.id.TextViewHazard03),
			activity.findViewById(R.id.TextViewHazard04),
			activity.findViewById(R.id.TextViewHazard05),
			activity.findViewById(R.id.TextViewHazard06),
			activity.findViewById(R.id.TextViewHazard07),
		};
		
		icon = new ImageView[] {
			activity.findViewById(R.id.ImageViewWeatherIcon01),
			activity.findViewById(R.id.ImageViewWeatherIcon02),
			activity.findViewById(R.id.ImageViewWeatherIcon03),
			activity.findViewById(R.id.ImageViewWeatherIcon04),
			activity.findViewById(R.id.ImageViewWeatherIcon05),
			activity.findViewById(R.id.ImageViewWeatherIcon06),
			activity.findViewById(R.id.ImageViewWeatherIcon07),
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
		
		TextView textView = activity.findViewById(R.id.TextViewForecastDates);
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

	@Override
	public void onStartDateAvailable(Date startDate) {
		
		this.startDate = startDate;
		Date endDate = DateUtil.addDays(startDate, DateUtil.DAYS_IN_WEEK - 1);
		displayForecastRange(startDate, endDate);
		displayWeatherBoxTitles(startDate);
	}
	
	@Override
	public void onMaximumTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, high, highValue);
	}

	@Override
	public void onMinimumTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, low, lowValue);
	}

	@Override
	public void onApparentTemperaturesAvailable(TimelinedData temperatures) {
		onTemperaturesAvailable(temperatures, apparent, apparentValue);
	}

	@Override
	public void onDewpointTemperaturesAvailable(TimelinedData temperatures) {
		onTemperaturesAvailable(temperatures, dew, dewValue);
	}

	@Override
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
				//TODO: clear this
				//new WeatherIconSetter(icon[i], url, activity, this).start();
			}
			
			date = DateUtil.addDays(midDay, i + 1);
		}
	}

	@Override
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

	@Override
	public void onApparentTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, apparent, apparentValue);
		
	}

	@Override
	public void onDewpointTemperaturesAvailable(Vector<String> temperatures) {
		onTemperaturesAvailable(temperatures, dew, dewValue);
	}

	@Override
	public void onWeatherAvailable(Vector<String> weather) {
		
		for (int i = 0, length = weather.size(); i < length; i++) {

			String value = weather.elementAt(i);
			if (value.length() > 0) {
				this.weather[i].setText(value);
			}
		}
	}

	@Override
	public void onHazardsAvailable(Vector<String> hazard) {
		
		for (int i = 0, length = hazard.size(); i < length; i++) {

			String value = hazard.elementAt(i);
			if (value.length() > 0) {
				this.hazard[i].setText(value);
				weatherBox[i].setBackgroundResource(R.drawable.weather_box_hazard);
			}
		}
	}

	@Override
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

	@Override
	public void onIconAvailable(ImageView imageView, Drawable icon) {
		imageView.setImageDrawable(icon);
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
	public JSONObject toJSONObject() {
		
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
	
		return json;
	}
}
