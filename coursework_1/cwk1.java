import java.util.*;
import java.net.*;
import java.io.*;

public class cwk1 {
  private Scanner input = null;
  private String cmd = null;
  private String[] segmented = null;
  private Scanner kbdReader = null;
  private InetAddress inet = null;

  public cwk1(){
    kbdReader = new Scanner(System.in);
  }

  public String readInput(){
    while (true) {
      cmd = kbdReader.nextLine();
      return cmd;
    }
  }
  public String[] continousScan(){
    String addr = readInput();
    segmented = addr.split(" ");
    return segmented;
  }
  public void resolve(String host) {
    try {
      // Try to create an instance of InetAddress using the factory method (public static).
      // If fails, may throw an instance of UnknownHostException.
      inet = InetAddress.getByName( host );

      System.out.println("========================");
      // Use two getter methods to print the results. Can also just print the object itself (which combines both).
      System.out.println( "Host name : " + inet.getHostName   () );
      System.out.println( "IP Address: " + inet.getHostAddress() );
      System.out.println( "Reachable: " + inet.isReachable(60) );
      System.out.println("========================\n");
    }
    catch( UnknownHostException e ){ 		// If an exception was thrown, echo to stdout.
      e.printStackTrace();
    }
    catch( IOException e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    cwk1 kbd = new cwk1();
    cwk1 lookup = new cwk1();
    if (args.length != 0) {
      for (int i = 0; i<args.length ;i++) {
        lookup.resolve(args[i]);
      }
    } else {
      String[] seg = kbd.continousScan();
      for (String segment: seg) {
        lookup.resolve(segment);
      }
    }
  }
}
