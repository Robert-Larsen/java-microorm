package no.bekk.java.microorm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person {

	public enum Gender { MALE, FEMALE }

	private long id;
	public final String name;
	public final Gender gender;
	public final LocalDate birthdate;
	public final List<Address> addresses;

	public Person(String name, Gender gender, LocalDate birthdate, List<Address> addresses) {
		this.name = name;
		this.gender = gender;
		this.birthdate = birthdate;
		this.addresses = addresses != null ? addresses : new ArrayList<>();
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	@Override
	public String toString() {
		return "Person:" + id + ":" + name + ":"+addresses.toString();
	}
}
