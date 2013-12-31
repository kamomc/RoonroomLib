package jp.kamoc.roonroom.lib.operation;

import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.constants.RRL;

public class CleaningSchedule {
	private static final int DEFAULT_DAYS = 7;

	private Map<RRL.DAY, Time> schedule = new HashMap<RRL.DAY, Time>();

	private int days = DEFAULT_DAYS;

	public CleaningSchedule() {
		for (RRL.DAY day : RRL.DAY.values()) {
			schedule.put(day, new Time(0, 0));
		}
	}

	public void addSchedule(RRL.DAY day, int hour, int min) {
		schedule.put(day, new Time(hour, min));
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Map<RRL.DAY, Time> getSchedule() {
		return schedule;
	}
}
