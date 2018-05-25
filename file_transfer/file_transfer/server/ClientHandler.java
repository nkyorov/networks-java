import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ClientHandler extends Thread {
  // Initial declarations
  private Socket socket = null;
  private DataInputStream inputStream = null;
  private DataOutputStream outputStream = null;

  /**
   * Constructor
   */
  public ClientHandler(Socket socket) {
      super("ClientHandler");
      this.socket = socket;
  }

  /**
   * Logging to external file. Overwrites, but keeps past information as well
   * @param inet assigned internet address
   * @param input user request
   * @throws IOException file not found
   */
  public void logging(InetAddress inet, String input) throws IOException {
      File log = new File("log.txt");
      try{
          //Set date format
          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd : HH:mm:ss");
          Date date = new Date();

          //Write to file without erasing previous contents
          BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log,true));

          //Write following the format "date:time:client IP address:request"
          bufferedWriter.write("" + dateFormat.format(date) + " : " + inet.getHostName() + "  " + inet.getHostAddress() + " : " + input + "\n");
          bufferedWriter.close();
      } catch(IOException e) {
          System.out.println("Log operation failed!");
      }
  }

  /**
   * Main code logic.
   */
  public void run() {
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

      InetAddress inet = socket.getInetAddress();
      Date date = new Date();

      System.out.println("\nNew connection:");
      System.out.println("Date " + date.toString());
      System.out.println("Connection made from " + inet.getHostName());

      String inputLine, outputLine;
      Protocol fileTranfer = new Protocol();
      outputLine = fileTranfer.processInput(null);
      out.println(outputLine);

      while ((inputLine = in.readLine()) != null) {
        outputLine = fileTranfer.processInput(inputLine);
        out.println(outputLine);

        // Log each user request
        logging(inet,inputLine);

        // Seprate the input
        String[] seg = outputLine.split(" ");

        if (outputLine.equals("bye")){
          break;
        }
        // Send file to clientFiles
        else if(seg[0].equals("get")){
          sendFile(seg[1]);
        }
        // Save file to serverFiles
        else if(seg[0].equals("put")){
          saveFile(seg[1]);
        }
      }

      // Close streams and sockets
      out.close();
      in.close();
      socket.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch(ArrayIndexOutOfBoundsException exception) {
        System.out.println("Too many arguments specified");
    }
  }

  /**
  * Save file method for command "put"
  * @param fname name of file
  */
  public void saveFile(String fname){
    try{
      outputStream = new DataOutputStream(new FileOutputStream("serverFiles/" + fname));
      inputStream = new DataInputStream(socket.getInputStream());

      byte[] buffer = new byte[8 * 1024];
      int fileSize = inputStream.readInt();

      int total = 0, bytesRead;
      while (total < fileSize && (bytesRead = inputStream.read(buffer)) != -1) {
        total += bytesRead;
        outputStream.write(buffer);
      }
      System.out.println("->File: " + fname + " has been received successfully.");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
    * Send file method for command 'get'
    * @param fname name of file
    */
  public void sendFile(String fname){
    try{
      File file = new File("serverFiles/" + fname);
      byte[] buffer = new byte[8 * 1024];
      inputStream = new DataInputStream(new FileInputStream(file));
      outputStream = new DataOutputStream(socket.getOutputStream());
      int count;
      int fileSize = (int)(long)file.length();

      outputStream.writeInt(fileSize);

      while((count = inputStream.read(buffer)) > 0){
        outputStream.write(buffer,0,count);
      }
      System.out.println("->File: " + fname + " has been sent successfully.");
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}
