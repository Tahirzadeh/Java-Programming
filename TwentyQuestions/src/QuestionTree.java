//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 7: Twenty Questions
//May 25th, 2016

//This program questions the user in an attempt to guess what word/name the
//user has in their head through various yes/no questions. It stores the question
//and respective answer if it's unable to guess the initial guessing round.

import java.io.*; //for PrintStream
import java.util.*; //for Scanner

public class QuestionTree {
	private QuestionNode mainRoot; //main tree root
	private UserInterface ui; //user interface details
	private int numGames; //tracks number of games
	private int numGamesWon; //tracks number of games won
	
	//PRE:	Takes in the UserInterface to be used,
	//		Throws IllegalArgumentException if the UI is null
	//POST:	Initializes QuestionTree with the item "computer with the UI,
	//		And starting counter for total number of games and those won to 0.
	public QuestionTree(UserInterface ui) {
		exception(ui);
		mainRoot = new QuestionNode("computer");
		this.ui = ui;
		numGames = 0;
		numGamesWon = 0;
	}
	
	//POST:	Plays one complete guessing game with the user until the computer
	//		wins or loses. 
	public void play() {
		mainRoot = play(mainRoot);
		numGames++;
	}
	
	//PRE:	The QuestionNode passed must not be null,
	//		Throws IllegalArgumentException if it is.
	//POST	Asks yes/no questions to determine what item the user was thinking,
	//		a question to distinguish the item from others if it wasn't guessed,
	//		and what the answer it to that distinguishing question.
	//		Prints out statements to do so and if the computer won.
	private QuestionNode play(QuestionNode currQuestionRoot) {
		exception(currQuestionRoot);
		if(currQuestionRoot.left == null && currQuestionRoot.right == null) {
			ui.print("Would your object happen to be " + currQuestionRoot.data + "?");
			if(!ui.nextBoolean()) { //if incorrect item is guessed, user says no
				ui.print("I lose. What is your object?");
				String newUserItem = ui.nextLine();
				ui.print("Type a yes/no question to distinguish your item from");
				ui.print("" + currQuestionRoot.data + ":");
				String newQuestion = ui.nextLine();
				QuestionNode newNode = new QuestionNode(newQuestion);
				ui.print("And what is the answer for your object?");
				if(ui.nextBoolean()) { //Adds a new question (node) and answers
					newNode.left = new QuestionNode(newUserItem);
					newNode.right = currQuestionRoot; //moves question/object to other node
				} else { //if the new user's item is not the answer for the new question
					newNode.right = new QuestionNode(newUserItem);
					newNode.left = currQuestionRoot;
				}
				return newNode;
			} else { //if item was guessed by computer
				ui.println("I win!");
				numGamesWon++;
			}
		} else {
			ui.print(currQuestionRoot.data); //asks question
			if(ui.nextBoolean()) { //if user says yes, go to left node
				currQuestionRoot.left = play(currQuestionRoot.left);
			} else { //otherwise, go to right node
				currQuestionRoot.right = play(currQuestionRoot.right);
			}
		}
		return currQuestionRoot;
	}
	
	//PRE:	The output file passed must not be null,
	//		Throws IllegalArgumentException if it is.
	//POST:	Stores the current tree of questions to an output file
	public void save(PrintStream output) {
		exception(output);
		save(output, mainRoot);
	}
	
	//PRE:	The question-node tree must not be null,
	//		Throws IllegalArgumentException if it is.
	//POST:	Saves the tree's data of questions and answers into an output text file
	private void save(PrintStream output, QuestionNode qRoot) {
		exception(qRoot);
		if(qRoot.left != null && qRoot.right != null) { //checks if question or answer node
			output.println("Q:" + qRoot.data);
			save(output, qRoot.left); //recurse through the left node
			save(output, qRoot.right); //recurse through right node
		} else { //if not a question node
			output.println("A:" + qRoot.data); 
		}
	}
	
	//PRE:	The input file must not be null,
	//		Throws IllegalArgumentException if it is.
	//POST:	Loads the question tree from the input file and 
	//		replaces the current tree
	public void load(Scanner input) {
		exception(input);
		this.mainRoot = load(input, mainRoot);
	}
	
	//PRE:	The QuestionNode param. is not null,
	//		Throws IllegalArgumentException if it is.
	//POST:	Creates and returns a question tree from the given file
	private QuestionNode load(Scanner input, QuestionNode mRoot) {
		exception(mRoot);
		if(input.hasNextLine()) { //checks if there's another line in file
			String inputLine = input.nextLine();
			//Splits string by Q/A identifier and content, and retrieves the latter
			String content = inputLine.substring(2); //contains Q/A content
			if(inputLine.startsWith("Q")) { //Checks if it's a question node
				//Adds to tree and continually recurses for both left and right nodes
				return new QuestionNode(content, load(input, mRoot), load(input, mRoot));
			} else { //If it's an answer (aka leaf node), returns that node
				return new QuestionNode(content);
			}
		}
		return mRoot; //returns newly created tree from file when file ends (no next line)
	}
	
	//POST:	Returns the total number of games the user has played
	public int totalGames() {
		return numGames;
	}
	
	//POST:	Returns the number of games the computer has guessed correctly
	public int gamesWon() {
		return numGamesWon;
	}
	
	//PRE:	Passes in an object of various types (Scanner, UserInterface, QuestionNode),
	//		Throws IllegalArgumentException if any object passed is null.
	//POST:	Tests whether the objected passed is null or not. 
	private void exception(Object test) {
		if(test == null) {
			throw new IllegalArgumentException("Null Passed");
		}
	}
	
}


