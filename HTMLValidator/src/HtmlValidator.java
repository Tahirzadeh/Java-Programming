//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 2: HTML Validator
//April 14th, 2016

//The purpose of HtmlValidator is to examine the HTML of a page and figures out
//whether it has "valid" sequences of tags in accordance to HTML conditions. This also
//includes options to add, remove, and get the tags stored on a page. 

import java.util.*;

public class HtmlValidator {
	private Queue<HtmlTag> tagsQ; //Creates queue to store a copy of an HTML page's tags

	//POST:	Initializes constructor to store an empty queue of HTML tags,
	//		and calls second constructor
	public HtmlValidator() {
		this(new LinkedList<HtmlTag>()); //passes empty queue to second constructor
	}
	
	//PRE: 	Takes a 'tag' type as a parameter,
	//		throw 'IllegalArgumentException' if a null value is passed.
	//POST:	Initializes a constructor with a passed queue, 
	public HtmlValidator(Queue<HtmlTag> tags) {
		if(tags == null) {
			throw new IllegalArgumentException("Null passed");
		}
		tagsQ = new LinkedList<HtmlTag>();
		tagsQ = tags;//add parameter queue to tagsQ
	}
	
	//PRE: 	Takes a 'tag' type as a parameter,
	//		Throws 'IllegalArgumentException' if a null value is passed. 
	//POST: Adds tag value to the end of the queue. 
	public void addTag(HtmlTag tag) {
		if(tag == null) {
			throw new IllegalArgumentException("Null passed");
		}
		tagsQ.add(tag);
	}
	
	//POST:	Returns a copy of the HTML tags in the queue in order.
	public Queue<HtmlTag> getTags() {
		return tagsQ;
	}
	
	//PRE:	Takes in a 'string' type as a parameter,
	//		Throws 'IllegalArgumentException' if a null is passed.
	//POST:	Removes all tags in the queue that matches the passed 'element'
	//		If there are not matches, queue is unchanged. 
	public void removeAll(String element) {
		if(element == null) {
			throw new IllegalArgumentException("Null passed");
		}
		int sizeQ = tagsQ.size(); 
		for(int i = 0; i < sizeQ; i++) {
			HtmlTag tagChecker = tagsQ.remove();
			if(!element.equals(tagChecker.getElement())) { 
				tagsQ.add(tagChecker);
			}
		}
	}
	
	//POST:	Prints indented text representation of the HTML tags in the queue,
	//		Also, prints error messages if the order of tags is not correct, or
	//		if a closing '/' is not found for self-closing tags
	public void validate() {
		Stack<HtmlTag> tagsS = new Stack<HtmlTag>(); //constructs stack for error detection
		int numIndents = 0; //tracks number of indents
		int sizeQ = tagsQ.size(); //keeps size of the queue constant
		for(int i = 0; i < sizeQ; i++) {
			HtmlTag tagChecker = tagsQ.remove(); //removes from queue for be checked
			//checks if opening tag and doesn't need a closing tag
			if(tagChecker.isOpenTag() && tagChecker.isSelfClosing()) {
				tagPrinter(numIndents, tagChecker);
			} else if(tagChecker.isOpenTag() && !tagChecker.isSelfClosing()) {
				//checks if element doesn't need a closing tag
				tagPrinter(numIndents, tagChecker);
				tagsS.push(tagChecker);
				numIndents++; //increases indent of the next tag
			} else if(!tagsS.isEmpty() && tagChecker.matches(tagsS.peek())) {
				//checks stack with elements that require closing tags matches current tag
				numIndents--; //decreases indent for current line
				tagPrinter(numIndents, tagChecker);
				tagsS.pop(); //removes the tag that requires a closing tag pair from stack
			} else { //Checks for unexpected tags and prints respective error line
				System.out.println("ERROR unexpected tag: " + tagChecker);
			}
			tagsQ.add(tagChecker); //returns tags back to the queue
		}
		if(!tagsS.isEmpty()) { //Prints unclosed tag error for remaining elements in the stack
			while(!tagsS.isEmpty()) {
				System.out.println("ERROR unclosed tag: " + tagsS.pop());
			}
		}
	}
	
	//PRE:	Takes an 'int' for number of indents needed, and an 'HtmlTag' type tagChecker
	//POST:	Prints out the number of indents and the given tag on a line
	private void tagPrinter(int numIndents, HtmlTag tagChecker) {
		String indent = "    "; //4 spaces
		for(int i = 0; i < numIndents; i++) {
			System.out.print(indent);
		}
		System.out.println(tagChecker);
	}
	
}
