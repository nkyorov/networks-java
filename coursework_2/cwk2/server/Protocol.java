import java.net.*;
import java.io.*;
import java.util.*;

public class Protocol {
    private static final int WAITING = 0;
    private static final int EXECUTECMD = 1;
    private int state = WAITING;

    private List<String> files = new ArrayList<String>();

	public Protocol(){
	}

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
        String theOutput = null;
        Server Server = new Server();
        if (state == WAITING) {
            theOutput = "Hello stranger.";
            state = EXECUTECMD;
        } else if (state == EXECUTECMD) {
            if (theInput[0].equals("list")) {
                files = list();
                theOutput = "Files available: " + Arrays.toString(files.toArray()).replace("[","").replace("]","");
                state = EXECUTECMD;
            }
            else if (theInput[0].equals("get")) {
                theOutput = "File sent";
                state = EXECUTECMD;
            }
            else if (theInput[0].equals("bye")) {
                theOutput = "bye";
                state = WAITING;
            }
            
        
        }
        return theOutput;
    }
}