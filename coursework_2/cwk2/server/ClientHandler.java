import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class ClientHandler extends Thread {
    private Socket socket = null;

    public ClientHandler(Socket socket) {
		super("ClientHandler");
		this.socket = socket;
    }

    public void logging(InetAddress inet, String input) throws IOException {
        File log = new File("log.txt");
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd : HH:mm:ss");
            Date date = new Date();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log,true));
            bufferedWriter.write("" + dateFormat.format(date) + " : " + inet.getHostName() + "  " + inet.getHostAddress() + " : " + input + "\n");
            bufferedWriter.close();
        } catch(IOException e) {
            System.out.println("Log operation failed!");
        }
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
				                    new InputStreamReader(
                                        socket.getInputStream()));

            InetAddress inet = socket.getInetAddress();
            Date date = new Date();

            logging(inet,"Established connection");
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
                logging(inet,inputLine);
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
