package no.bekk.java.microorm.model;

import java.util.ArrayList;
import java.util.List;

public class Person {

	public enum Gender { MALE, FEMALE }

	private long id;
	public final String name;
	public Gender gender;
	//birthdate (localdate)
	//gender (enum)
	//
	public final List<Address> addresses;

	public Person(String name, Gender gender, List<Address> addresses) {
		this.name = name;
		this.gender = gender;
		this.addresses = addresses != null ? addresses : new ArrayList<>();
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	@Override
	public String toString() {
		return "Person:" + id + ":" + name + ":"+addresses.toString();
	}
}
