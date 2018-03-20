import java.net.*;
import java.io.*;
import java.util.*;

public class Protocol {
    //Initialise states
    private static final int WAITING = 0;
    private static final int EXECUTECMD = 1;
    private int state = WAITING;

    //Store results of list() in here
    private List<String> files = new ArrayList<String>();

    //Default constructor
	public Protocol(){
	}

    /**
     * Function returning a list of files located in "serverFiles" directory
     */
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

    public String processInput(String[] theInput) throws IOException {
        //String to return to client
        String theOutput = null;        
        if (state == WAITING) {
            theOutput = "Hello stranger.";
            state = EXECUTECMD;
        } else if (state == EXECUTECMD) {
            //Return a list of available files if the user types "list"
            if (theInput[0].equals("list")) {
                files = list();
                theOutput = "Files available: " + Arrays.toString(files.toArray()).replace("[","").replace("]","");
                state = EXECUTECMD;
            }
            else if (theInput[0].equals("get")) {
                theOutput = "File sent";
                state = EXECUTECMD;
            }
            //Return a "termination" message, if the user wants to stop
            else if (theInput[0].equals("bye")) {
                theOutput = "bye";
                state = WAITING;
            }
            else{
                theOutput = "Unknown/unsupported command!";
                state = WAITING;
            }
        }
        //Return the message
        return theOutput;
    }
}
