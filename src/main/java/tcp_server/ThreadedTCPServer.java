package tcp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.log4j.Logger;

public class ThreadedTCPServer {

  static final Logger SERVER_LOGGER = Logger.getLogger(ThreadedTCPServer.class);
  static final int PORT = 3248;

  public static void main(String args[]) {

    int requestsQuantity = 0;
    final int MAX_REQUESTS = 1000;

    while (requestsQuantity < MAX_REQUESTS) {
      try (ServerSocket serverSocket = new ServerSocket(PORT); Socket socket = serverSocket
          .accept()) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        for (int i = 0; i < MAX_REQUESTS; i++) {
          synchronized(executor) {
            Runnable worker = new EchoThread(socket);
            executor.execute(worker);
          }
        }

        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
      } catch (IOException e) {
        SERVER_LOGGER.error(e);
      }
    }
  }
}