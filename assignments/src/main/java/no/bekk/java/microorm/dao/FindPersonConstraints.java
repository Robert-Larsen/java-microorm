package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.of;

public class FindPersonConstraints {

	public Optional<Person.Gender> gender = Optional.empty();
	public Optional<String> name = Optional.empty();
	public Optional<LocalDate> birthdateBefore = Optional.empty();

	private FindPersonConstraints() {
	}

	public static FindPersonConstraints newConstraints() {
		return new FindPersonConstraints();
	}

	public FindPersonConstraints gender(Person.Gender gender) {
		this.gender = of(gender);
		return this;
	}

	public FindPersonConstraints nameStartingWith(String name) {
		this.name = of(name);
		return this;
	}

	public FindPersonConstraints birthdateBefore(LocalDate birthdateBefore) {
		this.birthdateBefore = of(birthdateBefore);
		return this;
	}

}
