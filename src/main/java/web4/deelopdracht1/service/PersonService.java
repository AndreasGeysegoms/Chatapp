package web4.deelopdracht1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web4.deelopdracht1.db.PersonRepository;
import web4.deelopdracht1.db.PersonRepositoryStub;
import web4.deelopdracht1.domain.Person;

@Service
public class PersonService {
	private PersonRepository personRepository = new PersonRepositoryStub();

	@Autowired
	public PersonService(){
	}
	
	public Person getPerson(String personId)  {
		return getPersonRepository().get(personId);
	}

	public List<Person> getPersons() {
		return getPersonRepository().getAll();
	}

	public void addPerson(Person person) {
		getPersonRepository().add(person);
	}

	public void updatePersons(Person person) {
		getPersonRepository().update(person);
	}

	public void deletePerson(String id) {
		getPersonRepository().delete(id);
	}
	
	public Person getAuthenticatedUser(String email, String password) {
		return getPersonRepository().getAuthenticatedUser(email, password);
	}

	private PersonRepository getPersonRepository() {
		return personRepository;
	}

	public Person addFriend(Person user, String email) {
		Person p = personRepository.get(user.getUserId());
		List<Person> l = p.getFriends();
		l.add(personRepository.get(email));
		p.setFriends(l);
		return p;
	}
}
