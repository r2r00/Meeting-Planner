package it.polito.meet;

public class Time {
	
	private int hour;
	private int minute;
	
	public Time(String time) {
		String[] elem = time.split(":");
		hour = Integer.parseInt(elem[0]);
		minute = Integer.parseInt(elem[1]);
	}
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
	@Override
	public String toString() {
		return String.format("%02d:%02d", hour, minute);
	}
	public int distance(Time other) {
		int cnt = 0, h = hour, m = minute;
		while (!(h == other.hour && m == other.minute)) {
			cnt++;
			m = m == 59 ? 0 : m + 1;
			h = m == 0 ? (h == 23 ? 0 : h + 1) : h;
		}
		return cnt;
	}
}