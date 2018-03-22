import java.io.*;
import java.net.*;

public class Client {
  // Initial declarations
  private Socket Socket = null;
  private PrintWriter socketOutput = null;
  private BufferedReader socketInput = null;
  private DataOutputStream dataOut = null;
  private DataInputStream dataIn = null;
  private BufferedReader stdIn = null;

  /**
    * Default constructor
    */
  public void Client() {
    try {
      // try and create the socket
      Socket = new Socket("localhost", 8888 );

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
    stdIn = new BufferedReader(new InputStreamReader(System.in));
    String fromServer;
    String fromUser;

    try {
      // communication loop
      while ((fromServer = socketInput.readLine()) != null) {
        // Split the instruction from server in case of get/put.
        String[] seg = fromServer.split(" ");
        if (fromServer.contains("bye")){
          break;
        }
        if(fromServer.contains("commands")){
          showCommands(); 
        }
        // Save a file in clientFiles/
        else if(seg[0].contains("get")){
          saveFile(seg[1]);
        }
        // Send a file to serverFiles/
        else if(seg[0].contains("put")){
          sendFile(seg[1]);
        }
        else{
          System.out.println(fromServer);
        }

        fromUser = stdIn.readLine();
        if (fromUser != null) {
          socketOutput.println(fromUser);
        }
      }
      // If we receive termination message, we don't have to close the DataStreams
      if(fromServer.contains("bye")){
        closeOtherStreams();
      }
      else{
        closeDataStreams();
        closeOtherStreams();
      }
    }
    catch (IOException e) {
      System.err.println("I/O exception during execution\n");
    }
  }

  /**
   * Method closing input and output data stream
   */
  public void closeDataStreams(){
    try {
      dataOut.close();
      dataIn.close();
    } catch (IOException e) {
      System.err.println("I/O exception during execution\n");
    }
  }
  
  /**
   * Method closing rest of the streams + socket
   */
  public void closeOtherStreams(){
    try {
      socketOutput.close();
      socketInput.close();
      stdIn.close();
      Socket.close();
    }catch (IOException e) {
      System.err.println("I/O exception during execution\n");
    }
  }
  
  /**
    * Send file method for command "get"
    * @param fname file name
    */
  public void sendFile(String fname){
    try{
      File file = new File("clientFiles/" + fname);
      byte[] buffer = new byte[8 * 1024];
      dataIn = new DataInputStream(new FileInputStream(file));
      dataOut = new DataOutputStream(Socket.getOutputStream());
      
      //Get file size and send it over
      int fileSize = (int)(long)file.length();      
      dataOut.writeInt(fileSize);
      
      int count;
      while((count = dataIn.read(buffer)) > 0){
        dataOut.write(buffer,0,count);
      }
      System.out.println("->File: " + fname + " has been sent successfully.");
    }
    catch (IOException e) {
      System.err.println("File unknown or non-existant.\n");
    }

  }

  /**
    * Send file method for command "put"
    * @param fname file name
    */
  public void saveFile(String fname){
    try{
      dataOut = new DataOutputStream(new FileOutputStream("clientFiles/" + fname));
      dataIn = new DataInputStream(Socket.getInputStream());
      byte[] buffer = new byte[8 * 1024];
      int fileSize = dataIn.readInt(); 

      int total = 0, bytesRead;
      while (total < fileSize && (bytesRead = dataIn.read(buffer)) != -1) {
        total += bytesRead;
        dataOut.write(buffer);
      }
      System.out.println("->File: " + fname + " has been received successfully.");
    }
    catch (IOException e) {
      System.err.println("File unknown or non-existant.\n");
    }
  }
  
  /**
   * Shows available commands upon connecting to the server
   */
  public void showCommands() {
    System.out.println("\n*************************************************************************");
    System.out.println("* Available commands are:                                               *");
    System.out.println("*\t1. list - shows all files in serverFiles/                       *");
    System.out.println("*\t2. get fname - copies file from serverFiles/ to clientFiles/    *");
    System.out.println("*\t3. put fname - copies file from clientFiles/ to serverFiles/    *");
    System.out.println("*\t4. bye - terminates connection                                  *");
    System.out.println("*************************************************************************\n");
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.Client();
  }
}
