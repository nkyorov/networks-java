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
    
    public void saveFile(String filename) throws IOException {
        DataInputStream dataIn = new DataInputStream(client.getInputStream());
        FileOutputStream fileOut = new FileOutputStream("clientFiles/" + filename);
        byte[] buffer = new byte[8*1024];

        File f1 = new File("serverFiles/" + filename);
        long fileSize = f1.length();

        int total = 0 , bytesRead ;
        while (total < fileSize && (bytesRead = dataIn.read(buffer)) != -1 ){
            total += bytesRead;
            fileOut.write(buffer);
        }

        dataIn.close();
        fileOut.close();
    }

    public static void main(String[] args) throws IOException {
        ExecutorServer server = new ExecutorServer();
        server.run();
    }
}
