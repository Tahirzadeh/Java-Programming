//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 6: Anagrams
//May 19th, 2016

//This program finds all the anagrams, words from a dictionary that can be made from
//the letters of a given word of a given word or phrase in a dictionary.

import java.util.*; //For sets, collections, etc.

public class Anagrams {
	private Set<String> dictionarySet; //copy of dictionary
	private Map<String, LetterInventory> LetterInventoryMap; //map of words and their letters
	
	//PRE:	Takes in a Set of strings in as a parameter,
	//		Throws IllegalArgumentException if the passed set is null.
	//POST:	Initializes Anagrams with the dictionary of words passed and creates/adds
	//		an inventory of every letter in each word
	public Anagrams(Set<String> dictionary) {
		if(dictionary == null) {
			throw new IllegalArgumentException("The set is null");
		}
		dictionarySet = new TreeSet<String>(dictionary);
		LetterInventoryMap = new HashMap<String, LetterInventory>();
		for(String wordToken : dictionarySet) {
			//Adds a letter inventory representation of every dictionary word
			LetterInventory wordLetters = new LetterInventory(wordToken);
			LetterInventoryMap.put(wordToken, wordLetters);
		}
	}
	
	//PRE:	Takes in and passes a string,
	//		Throws IllegalArgumentException if the passed string is null
	//POST:	Returns all the words in the passed dictionary in sorted alphabetical order
	//		that contain letters that make up the phrase or word.
	public SortedSet<String> getWords(String phrase) {
		if(phrase == null) {
			throw new IllegalArgumentException("String is null");
		}
		SortedSet<String> anagramsSet = new TreeSet<String>(); //set for possible anagrams
		LetterInventory phraseLI = new LetterInventory(phrase);
		//Checks if all the letters in the phrase is in each dictionary word
		for (String wordToken : dictionarySet) { 
			if(phraseLI.subtract(LetterInventoryMap.get(wordToken)) != null) {
				anagramsSet.add(wordToken); //adds valid word to anagrams set
			}
		}
		return anagramsSet;
	}
	
	//PRE:	Takes in a string of a phrase/word,
	//		Throws IllegalArgumentException if the string is null
	//POST:	Returns/prints all anagrams that can be created from the
	//		letters of the passed phrase while setting max as 0.
	public void print(String phrase) {
		if(phrase == null) {
			throw new IllegalArgumentException("String is null");
		}
		print(phrase, 0);
	}
	
	//PRE:	Takes in a string of a phrase/word and int for max # of anagrams,
	//		Throws IllegalArgumentException if string is null or if max is less than 0
	//POST:	Prints anagrams/words that make up the passed string's letters, and limits
	//		amounts of words based on the max. If no max is entered, 0 is passed and outputs
	//		all anagrams that make up the word.
	public void print(String phrase, int max) {
		if(phrase == null || max < 0) {
			throw new IllegalArgumentException("phrase is null or max is less than 0");
		}
		Set<String> possibleAnagrams = getWords(phrase); //set of all possible anagrams
		Stack<String> currentAnagrams = new Stack<String>(); //track/backtrack anagram path
		LetterInventory phraseLetters = new LetterInventory(phrase); //phrase's letters
		print(phraseLetters, max, possibleAnagrams, currentAnagrams);
	}
	
	//PRE:	Must accept a LetterInventory type of the phrase's letters, an int of the max
	//		of total words to print, a Set of Strings of all the possible anagrams, 
	//		and a Stack of Strings to track/backtrack the current anagram path
	//POST:	Helps in finding all anagrams that can be created by the phrase's letters
	//		and the max amount of anagrams to print per output 
	private void print(LetterInventory phraseLetters,
					int max, Set<String> possibleAnagrams, Stack<String> currentAnagrams) {
		//Prints output if no letters remain in the phrase's inventory of letters
		if(phraseLetters.isEmpty()) {
			System.out.println(currentAnagrams);
		} else if(max == 0 || currentAnagrams.size() < max) {
			for (String dictionaryWord : possibleAnagrams) {
				//Checks to see if there's enough letters to subtract the word's letters
				if(phraseLetters.subtract(LetterInventoryMap.get(dictionaryWord)) != null) {
					//adds initial word 'path'
					currentAnagrams.push(dictionaryWord);
					LetterInventory result = 
							phraseLetters.subtract(LetterInventoryMap.get(dictionaryWord));
					//recursion to continue with remaining letters
					print(result, max, possibleAnagrams, currentAnagrams);
					//backtrack
					phraseLetters.add(LetterInventoryMap.get(dictionaryWord));
					currentAnagrams.pop();
				}
			}
		}
	}
	
}