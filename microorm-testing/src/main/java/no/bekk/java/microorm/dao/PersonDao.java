package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;

import java.util.List;

public interface PersonDao {

	List<Person> listPersonsWithAddresses();
}
