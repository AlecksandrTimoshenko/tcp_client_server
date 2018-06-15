package tcp_server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

public class TCPServer {

  public static final Logger SERVER_LOGGER = Logger.getLogger(TCPServer.class);
  static final int PORT = 3248;

  public static void main(String args[]) throws IOException {

    final int MAX_REQUESTS = 1000;

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      ExecutorService service = Executors.newFixedThreadPool(10);

      for (int i = 0; i < MAX_REQUESTS; i++) {
        Socket socket = serverSocket.accept();
        service.submit(new Runnable() {

          @Override
          public void run() {
            try {
              BufferedReader inFromClient = new BufferedReader(
                  new InputStreamReader(socket.getInputStream()));
              DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
              String clientSentence;

              clientSentence = inFromClient.readLine();

              System.out.println("Message from client: " + clientSentence);
              SERVER_LOGGER.info("Message from client: " + clientSentence);

              StringBuffer reversMessage = new StringBuffer(clientSentence);
              reversMessage.reverse();

              String thread = Thread.currentThread().getName();
              System.out.println(reversMessage.toString() + " " + thread);
              SERVER_LOGGER.info(reversMessage.toString() + " " + thread);

              outToClient.writeBytes(reversMessage + " " + thread + "\r");
              outToClient.flush();

            } catch (IOException e) {
              SERVER_LOGGER.error(e);
            }
          }
        });
      }
    } catch (IOException e) {
      SERVER_LOGGER.error(e);
      throw new IOException("Connection refused");
    }
  }
}