package tcp_server;

import static tcp_server.ThreadedTCPServer.SERVER_LOGGER;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoThread implements Runnable {

  protected Socket socket;
  private int requestsQuantity = 0;
  private final int MAX_REQUESTS = 1000;
  private String clientSentence;

  public EchoThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    while (requestsQuantity < MAX_REQUESTS) {
      try {
        BufferedReader inFromClient = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

        clientSentence = inFromClient.readLine();

        Thread.sleep(10);

        System.out.println("From client " + clientSentence);
        SERVER_LOGGER.info("" + clientSentence);

        String thread = Thread.currentThread().getName();
        System.out.println(clientSentence + " " + thread);
        outToClient.writeBytes(clientSentence + " " + thread + "\r");
        outToClient.flush();
      } catch (IOException e) {
        SERVER_LOGGER.error(e);
      } catch (InterruptedException e) {
        SERVER_LOGGER.error(e);
      }
    }
  }
}