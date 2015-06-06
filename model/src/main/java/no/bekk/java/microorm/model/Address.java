package no.bekk.java.microorm.model;

public class Address {

	private long id;
	public final String street;
	public final String zipcode;

	public Address(String street, String zipcode) {
		this.street = street;
		this.zipcode = zipcode;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public String getZipcode() {
		return zipcode;
	}
}
