package it.polito.meet;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.time.*;


public class MeetServer {

	/**
	 * adds a set of meeting categories to the list of categories
	 * The method can be invoked multiple times.
	 * Possible duplicates are ignored.
	 * 
	 * @param categories the meeting categories
	 */
	
	private TreeSet<String> catTree;
	private TreeMap<String, Meeting> meetings;
	private HashSet<Slot> slotsSet;
	private HashSet<Preference> prefSet;
	private int nextId=0;

	
	public MeetServer() {
		catTree = new TreeSet<>();
		meetings = new TreeMap<>();
		slotsSet = new HashSet<>();
		prefSet = new HashSet<>();
		nextId= 1;
		
	}

	
	
	public void addCategories(String... categories) {
		
		for (String s: categories) {
			catTree.add(s);
		}
	}

	/**
	 * retrieves the list of available categories
	 * 
	 * @return list of categories
	 */
	public Collection<String> getCategories() {
		return catTree.stream().collect(Collectors.toList());
	}
	
	
	/**
	 * adds a new meeting with a given category
	 * 
	 * @param title		title of meeting
	 * @param topic	    subject of meeting
	 * @param category  category of the meeting
	 * @return a unique id of the meeting
	 * @throws Exception 
	 */
	public String addMeeting(String title, String topic, String category) throws MeetException {
		if (!catTree.contains(category)) throw new MeetException("Category not found");
		
		String idToStr = String.valueOf(nextId++);
		meetings.put(idToStr, new Meeting(idToStr, title, topic, category));
		
		return idToStr;
	}

	/**
	 * retrieves the list of meetings with the given category
	 * 
	 * @param category 	required category
	 * @return list of meeting ids
	 */
	public Collection<String> getMeetings(String category) {
		return meetings.values().stream().filter(m -> m.getCategory() == category).map(Meeting::getId).collect(Collectors.toList());
		}

	/**
	 * retrieves the title of the meeting with the given id
	 * 
	 * @param meetingId  id of the meeting 
	 * @return the title
	 */
	public String getMeetingTitle(String meetingId) {
		return meetings.get(meetingId).getTitle();

	}

	/**
	 * retrieves the topic of the meeting with the given id
	 * 
	 * @param meetingId  id of the meeting 
	 * @return the topic of the meeting
	 */
	public String getMeetingTopic(String meetingId) {
		return meetings.get(meetingId).getTopic();
	}

	// R2
	
	/**
	 * Add a new option slot for a meeting as a date and a start and end time.
	 * The slot must not overlap with an existing slot for the same meeting.
	 * 
	 * @param meetingId	id of the meeting
	 * @param date		date of slot
	 * @param start		start time
	 * @param end		end time
	 * @return the length in hours of the slot
	 * @throws MeetException in case of slot overlap or wrong meeting id
	 */
	public double addOption(String meetingId, String date, String start, String end) throws MeetException {
		if (!meetings.containsKey(meetingId)) throw new MeetException("Meeting not found");
		
		Time startTime = new Time(start);
		Time endTime = new Time(end);
		LocalTime startTimeFromSlot, endTimeFromSlot, sTime, eTime;
		sTime = LocalTime.of(startTime.getHour(), startTime.getMinute());
		eTime = LocalTime.of(endTime.getHour(), endTime.getMinute());
		for (Slot sl : slotsSet) {
			if (sl.getMeetingId() == meetingId && sl.getDate() == date) {
				startTimeFromSlot = LocalTime.of(sl.getStartTime().getHour(), sl.getStartTime().getMinute());
				endTimeFromSlot = LocalTime.of(sl.getEndTime().getHour(), sl.getEndTime().getMinute());

				if ((sTime.compareTo(endTimeFromSlot) < 0 && sTime.compareTo(startTimeFromSlot) >= 0) ||
					    (eTime.compareTo(endTimeFromSlot) <= 0 && eTime.compareTo(startTimeFromSlot) > 0) ||
					    (sTime.compareTo(startTimeFromSlot) >= 0 && eTime.compareTo(endTimeFromSlot) <= 0) ||
					    (sTime.compareTo(startTimeFromSlot) <= 0 && eTime.compareTo(endTimeFromSlot) >= 0)) {
					    throw new MeetException("Overlapping with existing slots");
					}
			}
		}
		
		slotsSet.add(new Slot(meetingId, date, startTime, endTime));
		
		return (double) startTime.distance(endTime) / (double) 60;
	}

	/**
	 * retrieves the slots available for a given meeting.
	 * The returned map contains a key for each date and the corresponding value
	 * is a list of slots described as strings with the format "hh:mm-hh:mm",
	 * e.g. "14:00-15:30".
	 * 
	 * @param meetingId		id of the meeting
	 * @return a map date -> list of slots
	 */
	public Map<String, List<String>> showSlots(String meetingId) {
		return slotsSet.stream().filter(sl -> sl.getMeetingId() == meetingId)
				.collect(Collectors.groupingBy(Slot::getDate, Collectors.mapping(sl -> sl.getStartTime() + "-" + sl.getEndTime(), Collectors.toList())));
	}

	/**
	 * Declare a meeting open for collecting preferences for the slots.
	 * 
	 * @param meetingId	is of the meeting
	 */
	public void openPoll(String meetingId) {
		meetings.get(meetingId).pollOpen(true);
	}

	/**
	 * Records a preference of a user for a specific slot/option of a meeting.
	 * Preferences can be recorded only for meeting for which poll has been opened.
	 * 
	 * @param email		email of participant
	 * @param name		name of the participant
	 * @param surname	surname of the participant
	 * @param meetingId	id of the meeting
	 * @param date		date of the selected slot
	 * @param slot		time range of the slot
	 * @return the number of preferences for the slot
	 * @throws MeetException	in case of invalid id or slot
	 */
	public int selectPreference(String email, String name, String surname, String meetingId, String date, String slot) throws MeetException {
		if (!meetings.containsKey(meetingId)) throw new MeetException("Invalid meeting id");
		if (!meetings.get(meetingId).isPollOpen()) throw new MeetException("Poll not open for selected meeting");
		String[] times = slot.split("-");
		Time startTime = new Time(times[0]);
		Time endTime = new Time(times[1]);
		Slot sl = new Slot(meetingId, date, startTime, endTime);
		Slot ref = slotsSet.stream().filter(s -> s.equals(sl)).findFirst().orElseThrow(() -> new MeetException("Not existing slot"));
		prefSet.add(new Preference(email, name, surname, ref));
		return (int) prefSet.stream().filter(p -> p.getSlot().equals(sl)).count();
	}

	/**
	 * retrieves the list of the preferences expressed for a meeting.
	 * Preferences are reported as string with the format
	 * "YYYY-MM-DDThh:mm-hh:mm=EMAIL", including date, time interval, and email separated
	 * respectively by "T" and "="
	 * 
	 * @param meetingId meeting id
	 * @return list of preferences for the meeting
	 */
	public Collection<String> listPreferences(String meetingId) {
		return prefSet.stream().filter(p -> p.getSlot().getMeetingId() == meetingId).map(p -> p.toString()).collect(Collectors.toList());
	}

	/**
	 * close the poll associated to a meeting and returns
	 * the most preferred options, i.e. those that have receive the highest number of preferences.
	 * The options are reported as string with the format
	 * "YYYY-MM-DDThh:mm-hh:mm=##", including date, time interval, and preference count separated
	 * respectively by "T" and "="
	 * 
	 * @param meetingId	id of the meeting
	 */
	public Collection<String> closePoll(String meetingId) {
		meetings.get(meetingId).pollOpen(false);
		List<Map.Entry<Slot, Long>> prefPerSlot = prefSet.stream().filter(p -> p.getSlot().getMeetingId() == meetingId)
				.collect(Collectors.groupingBy(p -> p.getSlot(), Collectors.counting())).entrySet().stream().collect(Collectors.toList());
		long maxValue = prefPerSlot.stream().mapToLong(Map.Entry::getValue).max().orElse(0L);
		return prefPerSlot.stream().filter(entry -> entry.getValue() == maxValue).map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList());
	}

	
	/**
	 * returns the preference count for each slot of a meeting
	 * The returned map contains a key for each date and the corresponding value
	 * is a list of slots with preferences described as strings with the format 
	 * "hh:mm-hh:mm=###", including the time interval and the number of preferences 
	 * e.g. "14:00-15:30=2".
	 * 
	 * @param meetingId	the id of the meeting
	 * @return the map data -> slot preferences
	 */
	public Map<String, List<String>> meetingPreferences(String meetingId) {
		return prefSet.stream().filter(p -> p.getSlot().getMeetingId() == meetingId)
				.collect(Collectors.groupingBy(p -> p.getSlot(),Collectors.counting())).entrySet().stream()
				.collect(Collectors.groupingBy(entry -> entry.getKey().getDate(),Collectors.mapping(entry -> {
						return entry.getKey().getStartTime().toString() + "-" + entry.getKey().getEndTime().toString() + "=" + Long.valueOf(entry.getValue());
						},Collectors.toList())));
	}
	/**
	 * computes the number preferences collected for each meeting
	 * The result is a map that associates to each meeting id the relative count of preferences expressed
	 * 
	 * @return the map id : preferences -> count
	 */
	public Map<String, Integer> preferenceCount() {
		return meetings.values().stream().collect(Collectors.toMap(Meeting::getId,m -> (int) prefSet.stream()
								.filter(pref -> pref.getSlot().getMeetingId() == m.getId()).count()));
	}
}
