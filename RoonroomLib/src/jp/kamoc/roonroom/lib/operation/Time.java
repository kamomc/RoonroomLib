package jp.kamoc.roonroom.lib.operation;

class Time {
	public int h = 0;
	public int m = 0;

	public Time(int hour, int min) {
		if (hour >= 24) {
			return;
		}
		if (min >= 60) {
			return;
		}
		h = hour;
		m = min;
	}
}