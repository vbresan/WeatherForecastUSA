package biz.binarysolutions.weatherusa.components.forecast;

import java.util.Date;
import java.util.Vector;

/**
 * 
 *
 */
public class TimelinedData {

	private Vector<Date> timeline;
	private final Vector<String> data  = new Vector<>();
	private final Vector<String> icons = new Vector<>();

	/**
	 * 
	 * @param timeline
	 */
	public void setTimeline(Vector<Date> timeline) {
		this.timeline = timeline;
	}

	/**
	 * 
	 * @param data
	 */
	public void addData(String data) {
		this.data.add(data);
	}

	/**
	 * 
	 * @param icon
	 */
	public void addIcon(String icon) {
		icons.add(icon);
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public int indexBefore(Date date) {
		
		for (int i = timeline.size() - 1; i >=0 ; i--) {
			
			if (timeline.elementAt(i).compareTo(date) < 0) {
				
				String event = data.elementAt(i);
				if (event.length() > 0) {
					return i;
				}
			}
		}		
		
		return -1;
	}

	/**
	 * TODO: implement better search (maybe with TreeMap?)
	 * 
	 * @param date
	 * @return
	 */
	public int indexAfter(Date date) {
		
		for (int i = 0; i < timeline.size(); i++) {
			
			if (timeline.elementAt(i).compareTo(date) >= 0) {
				
				String event = data.elementAt(i);
				if (event.length() > 0) {
					return i;
				}
			}
		}
		
		return -1;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public String dataAt(int index) {
		return data.elementAt(index);
	}

	/**
	 * TODO: some data (e.g. hazards) don't have an icon - check for out of bounds!
	 * 
	 * @param index
	 * @return
	 */
	public String iconAt(int index) {
		return icons.elementAt(index);
	}

}
