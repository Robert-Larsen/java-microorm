package model;

import java.util.List;

public class Person {

	private long id;
	public final String name;
	public final List<Address> addresses;

	public Person(String name, List<Address> addresses) {
		this.name = name;
		this.addresses = addresses;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getId() {
		return id;
	}

	public void print() {
		System.out.println(id + ": " + name);
		for (Address a : addresses) {
			System.out.println("\t" + a.getId() + ": " + a.street + ", " + a.zipcode);
		}
	}
}
