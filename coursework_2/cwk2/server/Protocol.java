import java.net.*;
import java.io.*;
import java.util.*;

public class Protocol {
  // Initialise states
  private static final int WAITING = 0;
  private static final int EXECUTECMD = 1;
  private int state = WAITING;

  // Store results of list() in here
  private List<String> files = new ArrayList<String>();

  // Default constructor
	public Protocol(){
	}

  /**
   * Creates a list of files available on serverFiles/
   * @return res list of said files
   * */
  public List<String> list(){
    List<String> res = new ArrayList<String>();
    File[] files = new File("serverFiles/").listFiles();
    for (File f : files) {
      if (f.isFile()) {
        res.add(f.getName());
      }
    }
    return res;
  }
  
  /**
   * Simple input processing steps
   * @param theInput user specified input
   * @return theOutput instructions for the server
   * */
  public String processInput(String theInput) throws IOException {
    // String to return to client
    String theOutput = null;
    if(state == WAITING) {
      theOutput = "commands";
      state = EXECUTECMD;
    } else if(state == EXECUTECMD) {
      // Return a list of available files if the user types "list"
      if(theInput.equals("list")) {
        files = list();
        theOutput = "Files available: " + Arrays.toString(files.toArray()).replace("[","").replace("]","");
        state = EXECUTECMD;
      }
      else if(theInput.contains("get")) {
        theOutput = theInput;
        state = EXECUTECMD;
      }
      else if(theInput.contains("put")) {
        theOutput = theInput;
        state = EXECUTECMD;
      }
      // Return a "termination" message, if the user wants to stop
      else if(theInput.contains("bye")) {
        theOutput = "bye";
        state = EXECUTECMD;
      }
      else{
        theOutput = "Unknown/unsupported command!";
        state = EXECUTECMD;
      }
    }
    // Return the message
    return theOutput;
  }
}
