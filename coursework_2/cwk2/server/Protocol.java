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

    public String processInput(String theInput) throws IOException {
        String theOutput = null;
        ExecutorServer exSever = new ExecutorServer();
        if (state == WAITING) {
            theOutput = "Hello stranger.";
            state = EXECUTECMD;
        } else if (state == EXECUTECMD) {
            if (theInput.equalsIgnoreCase("list")) {
                files = list();
                theOutput = "Files available: " + Arrays.toString(files.toArray()).replace("[","").replace("]","");
                state = EXECUTECMD;
            }
            else if (theInput.equalsIgnoreCase("get")) {
                exSever.sendFile("lipsum1.txt");
                theOutput = "File sent";
                state = EXECUTECMD;
            }
            else{
                theOutput = "Command is unknown/not supported!";
            }
        
        }
        return theOutput;
    }
}
