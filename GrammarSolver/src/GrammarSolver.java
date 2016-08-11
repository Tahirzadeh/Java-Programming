//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 4: Grammar Solver
//April 28th, 2016

//This program is takes a text files and, using the BNAF rules, randomly
//generates any number of sentences or phrases using those independent elements. 

import java.util.*; //To use sets, maps, random, etc.

public class GrammarSolver {
	private Map<String, String[]> grammarContents; //Stores grammar rules and values
	
	//PRE:	Only a List of type String can be passed,
	//		Throws IllegalArgumentException if rules is null or empty
	//		and also if there's more than one rule per non-terminal
	//POST:	Initializes GrammarSolver to accept a list of BNAF rules/tags
	public GrammarSolver(List<String> rules) {
		if(rules == null || rules.size() == 0) {
			throw new IllegalArgumentException();
		}
		grammarContents = new TreeMap<String, String[]>();
		for(String ruleToken : rules) {
			String[] splitLines = ruleToken.split("::=");
			String nonTerminal = splitLines[0];
			if(grammarContents.containsKey(nonTerminal)) {
				throw new IllegalArgumentException("Can't have more than one rule!");
			}
			String[] separateRules = splitLines[1].trim().split("[|]");
			grammarContents.put(nonTerminal, separateRules);
		}
	}

	//PRE:	Only a 'String' type can be passed,
	//		Throws IllegalArgumentException if there is no symbol or null is passed
	//POST:	Checks if the passed symbol is a 'non-terminal' type
	public boolean contains(String symbol) {
		if(symbol == null || symbol.length() == 0) {
			throw new IllegalArgumentException();
		}
		return grammarContents.containsKey(symbol);
	}
	
	//POST:	Returns all non-terminal symbols used in grammar in order.
	public Set<String> getSymbols() {
		return grammarContents.keySet();
	}
	
	//PRE:	Only a 'String' type can be passed,
	//		Throws IllegalArgumentException if there is no/empty symbol or null is passed
	//POST:	Generates a word/sentence depending on the passed symbol
	public String generate(String symbol) {
		if(symbol == null || symbol.length() == 0) {
			throw new IllegalArgumentException("Invalid string passed.");
		}
		if(!grammarContents.containsKey(symbol)) {
			return symbol; //base case; returns if terminal value
		}
		Random rand = new Random();
		String sentence = "";
		String[] symbolValues = grammarContents.get(symbol);
		int randomValueNumber = rand.nextInt(symbolValues.length);
		String grammarCurrent = symbolValues[randomValueNumber]; //random symbol
		String[] randomGrammar = grammarCurrent.trim().split("[ \t]+"); //remove all spaces
		for (String currToken: randomGrammar) {
			sentence += generate(currToken).trim() + " "; //recursion to combine strings
		}	
		return sentence;
	}
}