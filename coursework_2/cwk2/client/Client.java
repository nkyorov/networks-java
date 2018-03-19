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
                    sendFile("lipsum1.txt");
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
    
    public void sendFile(String file){
        try{
            int count, total = 0;
            byte[] bytes = new byte[8*1024];
    
            DataOutputStream dataOut = new DataOutputStream(Socket.getOutputStream());
            FileInputStream fileIn = new FileInputStream("../server/serverFiles/" + file);
    
            while((count=fileIn.read(bytes))>0 ){
                total +=  count;
                dataOut.write(bytes, 0, count);
            }
            fileIn.close();
            dataOut.close();
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
      Client client = new Client();
      client.run();
    }

}
