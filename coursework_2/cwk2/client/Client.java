package client;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    private Socket Socket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public void playKnockKnock() {

        try {

            // try and create the socket
            Socket = new Socket( "localhost", 8888 );

            // chain a writing stream
            socketOutput = new PrintWriter(Socket.getOutputStream(), true);

            // chain a reading stream
            socketInput = new BufferedReader(new InputStreamReader(Socket.getInputStream()));

        }
        catch (UnknownHostException e) {
            System.err.println("Cannot resolve host!\n");
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Have you started the server?\n");
            System.exit(1);
        }

        // chain a reader from the keyboard
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        Date date = new Date();
        // communication loop

        // read from server
        try {
          while ((fromServer = socketInput.readLine()) != null) {

              // echo server string
              System.out.println("Server: " + fromServer);
              if (fromServer.equals("Bye.")){
                  break;
              }

              // client types in response
              fromUser = stdIn.readLine();

              if (fromUser.equals("Bye.")){
                  break;
              }

    	      if (fromUser != null) {
                  // echo client string
                  System.out.println("Client: " + fromUser);
                  // write to server
                  socketOutput.println(fromUser);
              }
          }
          socketOutput.close();
          socketInput.close();
          stdIn.close();
          Socket.close();
        }
        catch (IOException e) {
            System.err.println("I/O exception during execution\n");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
      Client client = new Client();
      client.playKnockKnock();
    }

}
