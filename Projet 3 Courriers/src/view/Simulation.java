package view;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.City;
import model.ExtendedObservable;
import model.Inhabitant;
import model.content.letter.Letter;
import model.content.letter.PromissoryNote;
import model.content.letter.SimpleLetter;
import model.content.specialletter.RegisteredLetter;
import model.content.specialletter.UrgentLetter;

public class Simulation extends ExtendedObservable {

	protected int nbDays;
	protected ConsoleView consoleView;
	protected City city;
	protected Random r;
	
	protected List<Inhabitant> tempInhabitants;
	
	
	public Simulation(ConsoleView consoleView, int nbDays) {
		this.consoleView = consoleView;
		this.nbDays = nbDays;
		r = new Random();
		
		createCity("BoobsVille", 100);

	
		
	}
	
	protected void createCity(String name, int nbInhabitants) {
		this.city = new City(name, nbInhabitants);
		this.addObserver(consoleView);
		city.addObserver(consoleView);
	}

	public void run() {
		
		Inhabitant sender, receiver;
		int randNbInhabitant = 0;
		
		do {
			randNbInhabitant = r.nextInt();
		} while(randNbInhabitant == 0);
		
		for(int i = 0; i < 6; i++) {
			resetTempList();
			if(i % 2 == 0) {
				for(int j = 0; j < randNbInhabitant; j++) {
					sender = getRandomInhabitant();
					receiver = getRandomInhabitant();
					sender.sendLetter(getRandomLetter(sender, receiver));
				}
			} else {
				city.distibuteLetters();
			}
		}
		
	}
	
	private void resetTempList() {
		this.tempInhabitants.clear();
		Collections.copy(tempInhabitants, city.getInhabitants());
	}

	protected Letter<?> getRandomLetter(Inhabitant sender, Inhabitant receiver) {
		Letter<?> letter = null;
		int randLetterType = r.nextInt(3); //0 = Simple Letter | 1 = PromissoryNote | 2 = Registered Letter
		switch(randLetterType) {
		case 1:
			letter = new PromissoryNote(sender, receiver, r.nextInt((int) sender.getBankAccount()));
			break;
			
		case 2 :
			letter = new SimpleLetter(sender, receiver, "bla bla");
			break;
			
		default :
			int randContainedLetter = r.nextInt(2); //0 = Simple letter | 1 = PromissoryNote
			switch(randContainedLetter) {
			case 1 :
				letter = new RegisteredLetter<Letter<?>>(sender, receiver, new PromissoryNote(sender, receiver, r.nextInt((int) sender.getBankAccount())));
				break;
				
			default :
				letter = new RegisteredLetter<Letter<?>>(sender, receiver, new SimpleLetter(sender, receiver, "blabla"));
				break;
			}
			
			break;
			
		}
		
		//assert(letter != null);
		
		int randSpecialityType;
		
		if(randLetterType == 2) { //if letter is a registered letter we don't wan't another registered letter to contain it
			randSpecialityType = r.nextInt(2); //0 = Not Special | 1 = Urgent Letter
		} else {
			randSpecialityType = r.nextInt(2); //0 = Not Special | 1 = Urgent Letter | 2 = Registered letter
		}
		
		switch (randSpecialityType) {
		case 1 :
			letter = new UrgentLetter<Letter<?>>(sender, receiver, letter);
			break;

		case 2 :
			letter = new RegisteredLetter<Letter<?>>(sender, receiver, letter);
			break;
		default:
			break;
		}
		
		return letter;
	}
	
	protected Inhabitant getRandomInhabitant() {
		return tempInhabitants.remove(r.nextInt(tempInhabitants.size()));
		
	}

}