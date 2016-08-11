//Abbas Tahirzadeh
//CSE 143, Spring 2016
//Section BI
//Assignment 2: HTML Validator Test
//April 14th, 2016

// This testing program stub creates a queue of HTML tags 
// in a valid sequence.
// You may use this as a starting point for being a client of your
// HtmlValidator object
import java.util.*;

public class HtmlValidatorTest {
	public static void main(String[] args) {
		// <b>Hi</b><br/>
		// A Queue of tags you may modify and pass to your HtmlValidator object
		Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
		tags.add(new HtmlTag("b", true));      // <b>
		tags.add(new HtmlTag("b", false));     // </b>
		tags.add(new HtmlTag("br"));           // <br/>
		
		HtmlValidator validator = new HtmlValidator(tags); //Creates a new HtmlValidator object
		HtmlTag sample = new HtmlTag("div", true); //Creates new HtmlTag for testing
		validator.getTags();
		validator.addTag(sample);
		validator.getTags();
		validator.validate();
		validator.removeAll("div");
		validator.validate();
	}
}