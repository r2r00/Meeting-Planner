package it.polito.meet;

public class Preference {
	
	private String email;
	private String name;
	private String surname;
	private Slot slot;

	public Preference(String email, String name, String surname, Slot slot) {
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.slot = slot;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Slot getSlot() {
		return slot;
	}
	
	@Override
	public String toString() {
		return slot.toString() + "=" + email;
	}
}