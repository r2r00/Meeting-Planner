package it.polito.meet;

public class Meeting {
	private String title, topic, category,id;
	private boolean pollOpen;
	public Meeting(String id, String title, String topic, String category) {
		this.category= category;
		this.topic = topic;
		this.id = id;
		this.title= title;
		pollOpen = false;
	}
	public String getCategory() {
		return category;
	}

	public String getTitle() {
		return title;
	}

	public String getTopic() {
		return topic;
	}

	public String getId() {
		return id;
	}
	public void pollOpen(boolean open) {
		this.pollOpen =open;
	}
	public boolean isPollOpen() {
		return pollOpen;
	}
}
