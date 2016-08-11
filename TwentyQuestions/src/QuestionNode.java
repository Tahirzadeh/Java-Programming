//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 7: Twenty Questions
//May 25th, 2016

//This program 


public class QuestionNode {
	public String data; //data for given node
	public QuestionNode left; //references left subtree
	public QuestionNode right; //references right subtree
	
	//POST:	Constructs a leaf node with the given data
	public QuestionNode(String data) {
		this(data, null, null);
	}
	
	//POST:	Constructs the left and right links of the node
	public QuestionNode(String data, QuestionNode left, QuestionNode right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
	
}
