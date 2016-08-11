//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 5: Evil Hangman
//May 5th, 2016

//This program uses a dictionary list to replicate the commonly known 'Hangman' game
//but instead it has a twist where it makes it as difficult as possible for the user
//to guess the correct word. It does this by eliminating sets of words through 
//guessed letters and choosing the set with the most words until one word  
//is left, thus giving an "Evil Hangman" program. 

import java.util.*; //To use maps, sets, lists, etc.

public class HangmanManager {
	private Set<String> remainingWords; //Stores list of remaining words from file
	private int remainingGuesses; //Stores how many guesses the user has left
	private SortedSet<Character> guessedLetters; //Stores the user's guessed letters
	private String wordPattern; //Stores a string representation of a word's pattern
	
	//PRE:	Can only take in a valid list of type string and int parameters,
	//		Throws IllegalArgumentException if the length is less than 1 
	//		or the max is less than 0.
	//POST:	Initializes HangmanManager with a set of dictionary words, the max number
	//		of guesses, and a set of words based on the length passed.
	public HangmanManager(List<String> dictionary, int length, int max) {
		if(length < 1 || max < 0) {
			throw new IllegalArgumentException("Invalid Parameters");
		}
		guessedLetters = new TreeSet<Character>();
		remainingGuesses = max;
		remainingWords = new TreeSet<String>();
		wordPattern = "";
		for(String wordToken : dictionary) {
			if(wordToken.length() == length) {
				remainingWords.add(wordToken);
			}
		}
		for(int i = 0; i < length; i++) {
			wordPattern += " -";
		}
	}
	
	//POST:	Returns the remaining words that the computer can choose from
	public Set<String> words() {
		return remainingWords;
	}
	
	//POST:	Returns the remaining number of guesses the user has
	public int guessesLeft() {
		return remainingGuesses;
	}
	
	//POST:	Returns the letters the user has guessed in sorted order
	public SortedSet<Character> guesses() {
		return guessedLetters;
	}
	
	//PRE:	There is at least one word remaining in the set,
	//		Throws IllegalStateException if the set is empty
	//POST:	Returns the current pattern after every guess,
	//		and returns dashes if there is no correct letters guessed
	public String pattern() {
		if(remainingWords.isEmpty()) {
			throw new IllegalStateException("Set of Words is Empty");
		}
		return wordPattern.trim();
	}
	
	//PRE:	Takes a char type guess with passed value being lower-case,
	//		Throws IllegalStateException if the num of guesses is less than 1 or
	//		if there are no more words in the remaining set (size is 0).
	//		Throws IllegalArgumentException if the set of guessed letters already
	//		contains the current guessed letter
	//POST:	Records the guesses made by the user, chooses and creates the pattern based
	//		on the user's guess. Returns the number of occurrences of the letter in the word
	public int record(char guess) {
		if(remainingGuesses < 1 || remainingWords.isEmpty()) {
			throw new IllegalStateException("No guesses left or word list is empty");
		}
		if(guessedLetters.contains(guess)) {
			throw new IllegalArgumentException("Letter already guessed!");
		}
		Map<String, Set<String>> mapPatterns = new TreeMap<String, Set<String>>();
		guessedLetters.add(guess);
		mapPatterns = mapFormatter(guess, mapPatterns);
		wordPattern = patternCreater(guess, mapPatterns);
		return numOccurrence(guess, wordPattern);
	}
	
	//PRE:	Can only pass a char letter and a map of patterns
	//POST:	Formats a map of patterns where each unique pattern contains a
	//		set of words related to that particular pattern.
	private Map<String, Set<String>> mapFormatter(char guess,
									Map<String, Set<String>> mapPatterns) {
		for(String wordToken : remainingWords) { //goes through every word in the set
			String patternUpdate = "";
			for(int i = 0; i < wordToken.length(); i++) { //creates pattern based on guess
				if(wordToken.charAt(i) == guess) {
					patternUpdate += " " + wordToken.charAt(i);
				} else {
					patternUpdate += " -";
				}
			}
			//checks if map of patterns has given pattern 
			if(mapPatterns.containsKey(patternUpdate)) {
				mapPatterns.get(patternUpdate).add(wordToken); //add words to a pattern's set
			} else { //adds new pattern (set) to the map of patterns, adds word to the set
				Set<String> patternAdder = new TreeSet<String>();
				patternAdder.add(wordToken);
				mapPatterns.put(patternUpdate, patternAdder);
			}
		}
		return mapPatterns;
	}
	
	//PRE:	Can only pass a char type letter and a map of patterns and their word sets
	//POST:	Finds the pattern with the largest set of words based on the user's guesses
	//		and creates a string of that pattern with either dashes or letters
	private String patternCreater(char guess, Map<String, Set<String>>  mapPatterns) {
		int numValues = 0;
		String maxPatternChecker = "";
		for(String patternToken : mapPatterns.keySet()) { //goes through every pattern in the map
			int maxPatternSize = mapPatterns.get(patternToken).size();
			if(numValues < maxPatternSize) { //compares patterns based on num of words each has
				numValues = maxPatternSize;
				maxPatternChecker = patternToken;
				remainingWords = mapPatterns.get(patternToken); //sets pattern with largest set
			}
		}
		String newPattern = "";
		for(int i = 0; i < maxPatternChecker.length(); i++) { //creates pattern string
			if(wordPattern.charAt(i) == '-') {
				newPattern += maxPatternChecker.charAt(i);
			} else {
				newPattern += wordPattern.charAt(i);
			}
		}
		return newPattern;
	}
	
	//PRE:	Takes in a char and a string type pattern 
	//POST:	Calculates the number of times a letter occurs in the string,
	//		Reduces number of guesses if no letters are in the string
	private int numOccurrence(char guess, String wordPattern) {
		int occurrences = 0;
		for(int i = 0; i < wordPattern.length(); i++) {
			if(wordPattern.charAt(i) == guess) {
				occurrences++;
			}
		}
		if (occurrences == 0) {
			remainingGuesses--; //lose 1 guess for every incorrect guess
		}
		return occurrences;
	}

}
