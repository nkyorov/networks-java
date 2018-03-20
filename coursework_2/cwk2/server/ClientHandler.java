import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class ClientHandler extends Thread {
    private Socket socket = null;

    /**
     * Constructor
     */
    public ClientHandler(Socket socket) {
		super("ClientHandler");
		this.socket = socket;
    }

    /**
     * Log every action to external file
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

    public void run() {
        try {
            //Chain streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
				                    new InputStreamReader(
                                        socket.getInputStream()));

            InetAddress inet = socket.getInetAddress();
            Date date = new Date();

            //Log each time a new connection is established.
            logging(inet,"Established connection");
            System.out.println("\nDate " + date.toString() );
            System.out.println("Connection made from " + inet.getHostName() );

	        String inputLine, outputLine;
	        Protocol protocol = new Protocol();
	        outputLine = protocol.processInput(null);
	        out.println(outputLine);

	        while ((inputLine = in.readLine()) != null) {
                //Separate the input by space in case of multiple arguments
                String[] segmented = inputLine.split(" ");
                outputLine = protocol.processInput(segmented);
                out.println(outputLine);
                //Log each user request
                logging(inet,inputLine);
            }

            //Close streams
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
