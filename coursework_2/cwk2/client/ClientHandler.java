package client;
import java.net.*;
import java.io.*;
import java.util.*;
import Protocol.*;

public class ClientHandler extends Thread {
    private Socket socket = null;

    public ClientHandler(Socket socket) {
		super("ClientHandler");
		this.socket = socket;
    }


    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
				                    new InputStreamReader(
                                        socket.getInputStream()));

            InetAddress inet = socket.getInetAddress();
            Date date = new Date();

            System.out.println("\nDate " + date.toString() );
            System.out.println("Connection made from " + inet.getHostName() );

	        String inputLine, outputLine;
	        Protocol protocol = new Protocol();
	        outputLine = protocol.processInput(null);
	        out.println(outputLine);

	        while ((inputLine = in.readLine()) != null) {
                outputLine = protocol.processInput(inputLine);
                out.println(outputLine);
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // public void sendFile(String file){
    //     try{
    //         int count, total = 0;
    //         byte[] bytes = new byte[8*1024];
    //
    //         DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
    //         FileInputStream fileIn = new FileInputStream("/clientFiles/" + file);
    //         byte[] buffer = new byte[4096];
    //
    //         while((count=fileIn.read(bytes))>0 ){
    //             total +=  count;
    //             dataOut.write( bytes , 0, count );
    //         }
    //     }
    //     catch( IOException e ) {
    //         System.err.println( "Invalid filename");
    //     }
    // }
}
