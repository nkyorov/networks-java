package server;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import client.*;

public class ExecutorServer {

    public static void main(String[] args) throws IOException {

        ServerSocket server = null;
        ExecutorService service = null;

		// Try to open up the listening port
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8888.");
            System.exit(-1);
        }

		// Initialise the executor.
		service = Executors.newCachedThreadPool();

		// For each new client, submit a new handler to the thread pool.
		while( true )
		{
			Socket client = server.accept();
			service.submit( new ClientHandler(client) );
		}
    }
}
