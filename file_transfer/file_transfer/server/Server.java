import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
  // Initial declarations
  private ServerSocket serverSocket = null;
  private ExecutorService service = null;
  private Socket client = null;

  // Default construcctor
  public Server(){
  }

  /**
    * Method dealing with new connections
    * @throws IOException availability of port 8888
    */
  public void run()throws IOException{
    // Try to open up the listening port
    try {
      serverSocket = new ServerSocket(8888);
    } 
    catch (IOException e) {
      System.err.println("Could not listen on port: 8888.");
      System.exit(-1);
    }
    // Initialise the executor.
    service = Executors.newFixedThreadPool(10);

    // Submit a new handler for each new client
    while (true) {
      client = serverSocket.accept();
      service.submit(new ClientHandler(client));
    }
  }

  public static void main(String[] args) throws IOException {
    Server server = new Server();
    server.run();
  }
}
