package model.content.letter;

import model.city.Inhabitant;

public class ThanksLetter extends SimpleLetter {

	public ThanksLetter(Inhabitant sender, Inhabitant receiver, String content) {
		super(sender, receiver, content);
	}
	
	@Override
	public String toString() {
		return "a thanks letter which is " + super.toString();
	}
	
	

}