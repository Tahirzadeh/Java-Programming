//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 8: Huffman Coding
//June 2nd, 2016

//This program creates a binary Huffman Tree counting the number of characters
//from a given text based file.

import java.io.*; //For Scanner, Printstream, etc.
import java.util.*; //For Queues, PriorityQueue, etc.

public class HuffmanTree {
	private HuffmanNode overallRoot;
	
	//PRE:	Passes an array of type int of the counts of each letter in a given text
	//POST:	Initializes HuffmanTree using the count of frequencies of each character
	//		that occurs in a text file. Sorts each character in order of occurrences. 
	public HuffmanTree(int[] counts) {
		Queue<HuffmanNode> freqQueue = new PriorityQueue<HuffmanNode>(); //creates sorted Queue
		HuffmanNode eof = new HuffmanNode(counts.length, 1); //creates EOF value node
		//Goes through array creating nodes of each character and their frequency
		for(int i = 0; i < counts.length; i++) {
			if(counts[i] > 0) {
				HuffmanNode freqNode = new HuffmanNode(i, counts[i]);
				freqQueue.add(freqNode); //adds char node to the priority 'frequency' queue
			}
		}
		freqQueue.add(eof); //adds End of File (EOF) to queue
		while(freqQueue.size() > 1) { //keeps iterating unless there is only one node on top
			HuffmanNode leftChild = freqQueue.remove();
			HuffmanNode rightChild = freqQueue.remove();
			//combines freq. values of the first two character nodes with lowest freq.
			int combineValue = leftChild.freq + rightChild.freq;
			HuffmanNode parentNode = new HuffmanNode (-1, combineValue, leftChild, rightChild);
			freqQueue.add(parentNode); //adds combined 'parent' node back to the queue
		}
		overallRoot = freqQueue.remove(); //sets largest value node as the main, initial root
	}
	
	//POST:	Writes the ascii code and binary code to a user's specified output
	public void write(PrintStream output) {
		if(overallRoot != null) {
			String binaryCode = ""; //initializes this var. to contain string of 1's and 0's
			write(output, overallRoot, binaryCode);
		}
	}
	
	//PRE: 	Takes in a HuffmanNode of the tree and a String of the binary code of the text
	//POST: Prints the tree to whatever the user designates as the output (console or file)
	//		by outputting each character's ASCII representation and binary code.
	private void write(PrintStream output, HuffmanNode currentRoot, String binaryCode) {
		if(currentRoot.left == null && currentRoot.right == null) { //if a leaf node
			output.println(currentRoot.character); //prints character's ascii code
			output.println(binaryCode); //prints character's binary code on next line
		} else { //recurses through the left/right child nodes and adds a 0 or 1 to binary code
			write(output, currentRoot.left, binaryCode + "0");
			write(output, currentRoot.right, binaryCode + "1");
		}
	}
	
	//PRE:	Input file contains a Huffman Tree based text file in the correct format
	//POST:	Creates the overall tree from the input file
	public HuffmanTree(Scanner input) {
		overallRoot = new HuffmanNode(-1, -1); //initial parent node
		while(input.hasNextLine()) { //Continues if not the end of the file
			int asciiCode = Integer.parseInt(input.nextLine()); //sets first num as ascii code
			String binaryCode = input.nextLine(); //sets the next num of 1s & 0s as binary code
			overallRoot = treeConstructor(overallRoot, asciiCode, binaryCode);
		}
	}
	
	//PRE:	Takes in a HuffmanNode, the int of a character's ascii code, and the string
	//		of binary code to get to that character.
	//POST:	Constructs the tree pathway to get to the character(s) from the input text
	//		file that is in the correct specified format. 
	private HuffmanNode treeConstructor(HuffmanNode root, int asciiCode, String binaryCode) {
		if(root == null) { //empty 'parent' node for traversing to leaf nodes
			root = new HuffmanNode(-1, -1);
		}
      if (binaryCode.length() == 1) { //check if one num left at end of binary code
         if(binaryCode.charAt(0) == '0') { //if last num is 0, creates node on left child
            if(root.left == null) { //checks if the left node is null
               root.left = new HuffmanNode(asciiCode, -1); //creates leaf node with char value
            } else { //continues traversing through left node if not null
               treeConstructor(root.left, asciiCode, binaryCode);
            }
         } else { //if last num is 1, creates node on right child
            if(root.right == null) { //if right is empty or null
               root.right = new HuffmanNode(asciiCode, -1); //create leaf node with char value
            } else { //continue traversing through right node if not null
               treeConstructor(root.right, asciiCode, binaryCode);
            }
         }
		} else { //Code for navigating through the tree according to binary code
			String pathNum = binaryCode.substring(0, 1); //get first num of code
			binaryCode = binaryCode.substring(1); //stores rest of code
			if(pathNum.startsWith("0")) { //if the first binary code num is 0 - go left
				root.left = treeConstructor(root.left, asciiCode, binaryCode);
			} else { //if the first binary code num is 1 - go right
				root.right = treeConstructor(root.right, asciiCode, binaryCode);
			}
		}
		return root; //returns tree node structure
	}
	
	//PRE:	Accepts a bit format input file, the 'end of file' character which stops reading
	//		of the file, and the output parameter to print in console or a file.
	//POST:	Reads the bits from the input file and decodes it by writing the characters
	//		to it in the specified location. Stops reading with the EOF value is encountered.
	public void decode(BitInputStream input, PrintStream output, int eof) {
 		HuffmanNode current = overallRoot;
 		while(current.character != eof) { //checks if end of file character, continues if not
 			if(current.left == null && current.right == null) { //checks if leaf node
 				output.write(current.character); //prints characters
 				current = overallRoot;
 			} else if (input.readBit() == 0) { //goes left if the bit value is 0
 				current = current.left;
 			} else { //otherwise goes right
 				current = current.right;
 			}
 		}
	}

	//PRE:	Implements the Comparable interface to compare object's natural ordering
	//POST:	Creates a HuffmanNode that contains the character and
	//		the frequency it occurs in a given file.
	private class HuffmanNode implements Comparable<HuffmanNode> {
		public int freq; //num of times this character occurs
		public int character; //ascii int value for the given character
		public HuffmanNode left; //left child reference
		public HuffmanNode right; //right child reference
		
		//PRE:	Accepts a int value representation of the character and the frequency
		//		in a text file.
		//POST:	Initializes HuffmanNode to create a new node
		public HuffmanNode(int character, int freq) {
			this(character, freq, null, null);
		}
		
		//PRE:	Accepts a int value representation of the character and the frequency
		//		in a text file as well as the left and right child nodes
		//POST:	Creates a HuffmanNode with the character's ascii code and number of times
		//		it's seen in a file as well as references to the two child nodes.
		private HuffmanNode(int character, int freq, HuffmanNode left, HuffmanNode right) {
			this.character = character;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		//PRE:	Accepts a second HuffmanNode to compare with
		//POST:	Compares two HuffmanNodes by their frequency, and returns
		//		their differences indicating if one is bigger/smaller/equal to
		//		the other node
		public int compareTo(HuffmanNode other) {
			return this.freq - other.freq;
		}
	}
	
}
