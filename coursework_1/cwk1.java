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
    public cwk1() {
        kbdReader = new Scanner(System.in);
    }

    /**
     * Reads user input from console
     *
     * @return cmd: User provided text
     */
    public String readInput() {
        while (true) {
            cmd = kbdReader.nextLine();
            return cmd;
        }
    }

    public List getList() {
        return list;
    }

    /**
     * Handles multiple addresses, with space delimeter.
     *
     * @return segmented: Array consisting of separated addresses
     */
    public String[] continousScan() {
        String addr = readInput();
        segmented = addr.split(" ");
        return segmented;
    }

    /**
     * Simple way of checking if address is v4 or v6. Depending on the address,
     * getAddress() returns byte-array of length 4 or 16 byes.
     *
     * @return Version of address
     */
    public String checkVersion() {
        address = inet.getAddress();
        if (address.length == 4) {
            return "IPv4";
        } else {
            return "IPv6";
        }
    }


    /**
     * Provide required output and call relevant InetAddress functions
     *
     * @param String host: hostname or IP address
     */
    public void resolve(String host) {
        try {
            inet = InetAddress.getByName(host);
            byte[] address = inet.getAddress();

            String ipa = inet.getHostAddress();
            String[] res = ipa.split("\\.");
            for (String w : res) {
                list.add(w);
            }

            System.out.println("========================");
            System.out.println("Host name : " + inet.getHostName());
            System.out.println("IP Address: " + inet.getHostAddress());
            System.out.println("IP version: " + checkVersion());
            System.out.println("Reachable: " + inet.isReachable(60));
            System.out.println("========================\n");

        }

        //Handles any exceptions related to InetAddress
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //Handles isReachable()
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hier() {
        boolean starAtOne = false;
        boolean starAtTwo = false;
        boolean starAtThree = false;
        boolean starAtFour = false;
        int i = 1;
        for (; ; ) {
            int i1 = 4;
            int i2 = 5;
            int i3 = 6;
            int i4 = 7;
            System.out.println("Computing common hierarchy.");
            while ((i1 < list.size()) && (i2 < list.size()) && (i3 < list.size()) && (i4 < list.size())) {
                if (list.get(0).equals(list.get(i1))) {
                    starAtOne = true;
                } else {
                    starAtOne = false;
                }
                if (list.get(1).equals(list.get(i2))) {
                    starAtTwo = true;
                } else {
                    starAtTwo = false;
                }
                if (list.get(2).equals(list.get(i3))) {
                    starAtThree = true;
                } else {
                    starAtThree = false;
                }
                if (list.get(3).equals(list.get(i4))) {
                    starAtFour = true;
                } else {
                    starAtFour = false;
                }
                i1 += 4;
                i2 += 4;
                i3 += 4;
                i4 += 4;
            }
            break;
        }

        if ((starAtOne && starAtTwo && starAtThree && starAtFour)) {
            System.out.println("*.*.*.*");
        } else if ((starAtOne && starAtTwo && starAtThree) && (!starAtFour)) {
            System.out.println(list.get(0) + "." + list.get(1) + "." + list.get(2) + ".*");
        } else if ((starAtOne && starAtTwo) && !(starAtFour && starAtThree)) {
            System.out.println(list.get(0) + "." + list.get(1) + ".*" + ".*");
        } else if ((starAtOne) && !(starAtFour && starAtThree && starAtTwo)) {
            System.out.println(list.get(0) + ".*" + ".*" + ".*");
        } else {
            System.out.println("No common hierarchy.");
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
            lookup.hier();
        }

        lookup.hier();
    }
}