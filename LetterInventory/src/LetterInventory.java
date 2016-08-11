//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 1: LetterInventory
//April 7th, 2016

//The purpose of the LetterInventory assignment is to keep track of letters of
//the alphabet in an inventory and computing how many of each letter there are
//in the string regardless of cases.

public class LetterInventory {
	private int[] elementData; //keeps track of all the counts for each letter in the object
	private int size; //keeps track of the size of the resulting string

	public static final int TOTAL_CHAR = 26; //constant for total characters in the alphabet

	//PRE:	Takes the string 'data' as a parameter
	//POST: Ignores non-alphabetic characters and counts the number that a given
	//		letter occurs in 'data' in a new array as well as tracking size of
	//		the string.
	public LetterInventory(String data) {
		elementData = new int[TOTAL_CHAR]; 
		data = data.toLowerCase();
		for(int i = 0; i < data.length(); i++) { //Goes through the entire string
			char letter = data.charAt(i);
			for(int j = 0; j < TOTAL_CHAR; j++) { //Goes through entire alphabet
				if(j == (letter - 'a')) { //Checks whether the letter is equal to an letter
					elementData[j]++;
					size++;
				}				
			}
		}
	}

	//PRE:	Takes in a 'char' type alphabetic character as a parameter,
	//		Throws an 'IllegalArgumentException' if a non-alphabetic character is passed.
	//POST: Returns the count of how many of a given letter is in the inventory
	public int get(char letter) {
		char ch = Character.toLowerCase(letter); //Converts letters to lower-case
		if(ch < 'a' || ch > 'z') { //Unicode for characters 'a = 97' and 'z = 122'
			throw new IllegalArgumentException("Non-alphabetic character was passed");
		}
		int charValue = (ch - 'a'); //elementData[] uses 0-25 indices for each alphabet letter
		return elementData[charValue];
	}
	
	//PRE: 	Accepts character 'letter' and integer 'value' parameters,
	//		Throws IllegalArgumentException if the a non-alphabetic character is 
	//		passed or if the value is negative since there is no negative array index
	//POST:	Sets the count for the given letter to the given value
	public void set(char letter, int value) {
		char ch = Character.toLowerCase(letter);
		int charValue = ch - 'a';
		if(ch < 'a' || ch > 'z' || value <= -1) {
			throw new IllegalArgumentException("Invalid Passed Parameters");
		}
		size += value - elementData[charValue]; 
		elementData[charValue] = value; 
	}
	
	//POST: Returns the sum of all of the letter counts in the inventory. 
	public int size() {
		return size; 
	}
	
	//POST: Returns true if the size (counts) is 0 or 'empty', 
	//		otherwise it will return false. 
	public boolean isEmpty() {
		return size == 0;
	}
	
	//POST: Returns the string of the inventory that has all of the stored 
	//		letters in sorted order starting and ending with brackets
	public String toString() {
		String result ="[";
			for(int i = 0; i < TOTAL_CHAR; i++) {
				int occurrences = elementData[i]; //number of occurrences of a given letter
				for(int j = 0; j < occurrences; j++) { //adds the letter and quantity of it
					result += (char) (i + 'a'); 
				}
			}
		result += "]";
		return result;
	}
	
	//PRE:	Must pass a LetterInventory object
	//POST: Returns a new LetterInventory object that adds the count for each
	//		letter for one object to the count of each alphabetical letter to the 
	//		other LetterInventory object containing the total count for both, not 
	//		affecting the previous two objects. 
	public LetterInventory add(LetterInventory other) {
		LetterInventory sum = new LetterInventory(""); 
		for(int i = 0; i < TOTAL_CHAR; i++) {
			sum.elementData[i] = this.elementData[i] + other.elementData[i]; //combines counts per letter
			sum.size += sum.elementData[i]; //increases-updates size based on total count of letters
		}
		return sum; 
	}
	
	
	//PRE: 	Parameter must pass a LetterInventory object, and if any of
	//		the resulting count is negative, the method will return 'null'
	//POST: Returns a new LetterInventory object that subtracts the count of one object
	//		from the other resulting in the difference of count and size accordingly,
	//		not affecting the previous two objects. 
	public LetterInventory subtract(LetterInventory other) {
		LetterInventory result = new LetterInventory("");
		for(int i = 0; i < TOTAL_CHAR; i++) {
			result.elementData[i] = this.elementData[i] - other.elementData[i];
			if(result.elementData[i] < 0) {
				return null; 
			}
			result.size += result.elementData[i];
		}
		return result;
	}
}
