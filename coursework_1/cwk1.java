import java.util.*;
import java.net.*;
import java.io.*;

public class cwk1 {
    //Initial declaratioms
    private Scanner input = null;
    private String cmd = null;
    private String[] segmented = null;
    private Scanner kbdReader = null;
    private InetAddress inet = null;
    private byte[] address = null;
    private List<String> list = new ArrayList<String>();


    /**
     * Default constructor for Scanner
     */
    public cwk1(){
        kbdReader = new Scanner(System.in);
    }

    /**
     * Reads user input from console
     * @return cmd: User provided text
     */
    public String readInput(){
        while (true) {
            cmd = kbdReader.nextLine();
            return cmd;
        }
    }
    public List getList(){
        return list;
    }

    /**
     * Handles multiple addresses, with space delimeter.
     * @return segmented: Array consisting of separated addresses
     */
    public String[] continousScan(){
        String addr = readInput();
        segmented = addr.split(" ");
        return segmented;
    }

    /**
     * Simple way of checking if address is v4 or v6. Depending on the address,
     * getAddress() returns byte-array of length 4 or 16 byes.
     * @return Version of address
     */
    public String checkVersion(){
        address = inet.getAddress();
        if (address.length == 4) {
            return "IPv4";
        }
        else {
            return "IPv6";
        }
    }


    /**
     * Provide required output and call relevant InetAddress functions
     * @param String host: hostname or IP address
     */
    public void resolve(String host) {
        try {
            inet = InetAddress.getByName( host );
            byte[] address = inet.getAddress();

            String ipa = inet.getHostAddress();
            String[] res = ipa.split("\\.");
            for(String w : res) {
                list.add(w);
            }

            System.out.println("========================");
            System.out.println( "Host name : " + inet.getHostName   () );
            System.out.println( "IP Address: " + inet.getHostAddress() );
            System.out.println( "IP version: " + checkVersion());
            System.out.println( "Reachable: " + inet.isReachable(60) );
            System.out.println("========================\n");

        }

        //Handles any exceptions related to InetAddress
        catch( UnknownHostException e ){
            e.printStackTrace();
        }

        //Handles isReachable()
        catch( IOException e){
            e.printStackTrace();
        }
    }

    public String hier(){

        boolean starAtOne = false;
        boolean starAtTwo = false;
        boolean starAtThree = false;
        boolean starAtFour = false;

        for (int i = 1; ; i++ ) {
            while (i < list.size()) {
                int index = (3 * i) + 1;
                int index2 = (3 * i) - 1;

                if (list.get(0) == list.get(index)) {
                    starAtOne = true;
                } else {
                    starAtOne = false;
                }
                if (list.get(1) == list.get(index2)) {
                    starAtTwo = true;
                } else {
                    starAtTwo = false;
                }
                if (list.get(2) == list.get(index2)) {
                    starAtThree = true;
                } else {
                    starAtThree = false;
                }
                if (list.get(3) == list.get(index2)) {
                    starAtFour = true;
                } else {
                    starAtFour = false;
                }

                if((starAtOne && starAtTwo && starAtThree && starAtFour) == true){
                    return "*.*.*.*";
                }
                if(((starAtOne && starAtTwo && starAtThree)== true) && (starAtFour == false)){
                    return "*.*.*.";
                }
                if(((starAtOne && starAtTwo)== true) && ((starAtFour && starAtThree) == false)){
                    return "*.*..";
                }
                if((starAtOne == true) && ((starAtFour && starAtThree && starAtTwo) == false)){
                    return "*...";
                }
                else {
                    return "No common hierarchy.";
                }
            }
        }
    }

    public static void main(String[] args) {
        cwk1 kbd = new cwk1();
        cwk1 lookup = new cwk1();
        // Reads one or more hostnames provided as command line argument
        if (args.length != 0) {
            for (int i = 0; i<args.length ;i++) {
                lookup.resolve(args[i]);
            }
        }
        // Read continously from the keyboard
        else {
            String[] seg = kbd.continousScan();
            for (String segment: seg) {
                lookup.resolve(segment);
            }
        }
        if (args.length > 1) {

        }
    }
}
