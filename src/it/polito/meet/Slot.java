package it.polito.meet;

public class Slot {
	
	private String meetingId;
	private String date;
	private Time startTime;
	private Time endTime;
	
	public Slot(String meetingId, String date, Time startTime, Time endTime) {
		this.meetingId = meetingId;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	
	public String getMeetingId() {
		return meetingId;
	}

	public String getDate() {
		return date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	
	@Override
	public boolean equals(Object o) {
		Slot other = (Slot) o;
		if (this.meetingId == other.meetingId && this.date == other.date
				&& this.startTime.distance(other.startTime) == 0 && this.endTime.distance(other.endTime) == 0) return true;
		return false;
	}
	
	@Override
	public String toString() {
		String[] elem = date.split("-");
		if (elem[1].length() != 2) {
			elem[1] = "0" + elem[1];
		}
		if (elem[2].length() != 2) {
			elem[2] = "0" + elem[2];
		}
		
		return elem[0]+"-"+elem[1]+"-"+elem[2] + "T" + startTime.toString() + "-" + endTime.toString();
	}
}
