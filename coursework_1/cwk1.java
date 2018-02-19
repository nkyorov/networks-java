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
        byte[] address = inet.getAddress();
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
    }
}
