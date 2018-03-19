import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    private Socket Socket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public void run() {
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
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")){
                    break;
                }

                // client types in response
                fromUser = stdIn.readLine();

                if (fromUser.equals("Bye.")){
                      break;
                }
                if (fromUser.equals("get")) {
                    saveFile("lipsum1.txt");
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
            e.printStackTrace();
        }
    }

    public void saveFile(String filename) throws IOException {
        DataInputStream dataIn = new DataInputStream(Socket.getInputStream());
        FileOutputStream fileOut = new FileOutputStream("clientFiles/" + filename);
        byte[] buffer = new byte[8 * 1024];

        File f1 = new File("../server/serverFiles/" + filename);
        long fileSize = f1.length();

        int total = 0, bytesRead;
        while (total < fileSize && (bytesRead = dataIn.read(buffer)) != -1) {
            total += bytesRead;
            fileOut.write(buffer);
        }

        dataIn.close();
        fileOut.close();
    }

    public static void main(String[] args) {
      Client client = new Client();
      client.run();
    }

}
