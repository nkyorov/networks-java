import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ExecutorServer {
    private ServerSocket serverSocket = null;
    private ExecutorService service = null;
    private Socket client = null;

    public ExecutorServer(){
    }
    public void run()throws IOException{
        // Try to open up the listening port
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8888.");
            System.exit(-1);
        }
        // Initialise the executor.
        service = Executors.newCachedThreadPool();
        
        while (true) {
            client = serverSocket.accept();
            service.submit(new ClientHandler(client));
        }
    }

    public void sendFile(String file) {
        try {
            int count, total = 0;
            byte[] bytes = new byte[8 * 1024];

            DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
            FileInputStream fileIn = new FileInputStream("../server/serverFiles/" + file);

            while ((count = fileIn.read(bytes)) > 0) {
                total += count;
                dataOut.write(bytes, 0, count);
            }
            fileIn.close();
            dataOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorServer server = new ExecutorServer();
        server.run();
    }
}
