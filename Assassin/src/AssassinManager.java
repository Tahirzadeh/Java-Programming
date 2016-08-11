//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 3: Assassin
//April 21st, 2016

//This program is a sort of representation of the game, Assassin, that shows
//who is going after who, and who they were killed by. It stores names of people
//alive in the killRing, and dead people in the graveyard list and chooses
//the next victim in the game. 

import java.util.*; //import linked lists tools

public class AssassinManager {
	private AssassinNode frontAlive; //front of the list of people alive or kill-able
	private AssassinNode frontGraveyard; //front of the list of people dead
	
	//PRE:	Takes in an ArrayList of type String as a parameter.
	//		If the list of names is empty (or size is 0) or null,
	//		Throws IllegalArgumentException.
	//POST:	Initializes AssassinManager to add list of names
	//		into the appropriate list to be used in the game
	public AssassinManager(ArrayList<String> names) {
		if(names == null || names.size() == 0) {
			throw new IllegalArgumentException();
		}
		for(int i = names.size() - 1; i >= 0; i--) {
			frontAlive = new AssassinNode(names.get(i), frontAlive);
		}
	}	
	
	//POST: Prints the names of the people in the kill ring
	//		and who they're stalking.
	public void printKillRing() {
		AssassinNode current = frontAlive; 
		while(current.next != null) {
			System.out.println("  " + current.name + " is stalking " + current.next.name);
			current = current.next;
		}
		System.out.println("  " + current.name + " is stalking " + frontAlive.name);
	}
	
	//POST: Prints the names of the people in the graveyard
	//		and who killed them.
	public void printGraveyard() {
		if(frontGraveyard != null) {
			AssassinNode current = frontGraveyard;
			while(current != null) {
				System.out.println("  " + current.name + " was killed by " + current.killer);
				current = current.next;
			}
		}
	}
	
	//PRE: 	Must take in an AssassinNode & String in as parameters. 
	//POST:	Helper method that checks if the name is the same as is in 
	//		the kill ring or graveyard
	private boolean nameChecker(AssassinNode current, String name) {
		while(current != null) {
			if(current.name.equalsIgnoreCase(name)) {
				return true;
			}
			current = current.next;
		}
		return false;	
	}
	
	//POST:	Checks if the name is in the killRing
	public boolean killRingContains(String name) {
		AssassinNode current = frontAlive;
		return nameChecker(current, name);
	}
	
	//POST:	Checks if the name is in the graveyard
	public boolean graveyardContains(String name) {
		AssassinNode current = frontGraveyard;
		return nameChecker(current, name);
	}
	
	//POST:	Checks if there is only one person alive
	public boolean isGameOver() {
		return frontAlive.next == null;
	}
	
	//POST:	Returns the name of he person that is alive and 'Won',
	//		returns null if the game is not over.
	public String winner() {
		if(isGameOver()) {
			return frontAlive.name;
		}
		return null;
	}
	
	//PRE:	If the game is over, throws IllegalStateException,
	//		if the name is not present in the killRing or if the name is not valid,
	//		throws IllegalArgumentException
	//POST:	Removes name from the 'Alive' list and moves them to the graveyard
	public void kill(String name) {
		if(isGameOver()) {
			throw new IllegalStateException("The game is over.");
		}
		if(!killRingContains(name)) {
			throw new IllegalArgumentException("This person is dead or N/A");
		}
		AssassinNode current = frontAlive;
		AssassinNode temp = frontGraveyard;
		//checks if the name of the next name in the list matches
		while(current.next != null && !current.next.name.equalsIgnoreCase(name)) {
			current = current.next;
		}
		if(frontAlive.name.equalsIgnoreCase(name)) { //For name(s) in the front
			temp = frontAlive;
			frontAlive = frontAlive.next;
		} else { //For name(s) in the middle of the list 
			temp = current.next;
			current.next = current.next.next;
		}
		temp.next = frontGraveyard;
		frontGraveyard = temp;
		frontGraveyard.killer = current.name; //sets killer's name to prev. name
	}
}