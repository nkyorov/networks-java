import java.net.*;
import java.io.*;
import java.util.*;

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
                // Split into segments separated by spaces.
                String[] segmented = inputLine.split(" ");
                outputLine = protocol.processInput(segmented);
                out.println(outputLine);
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
