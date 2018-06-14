package tcp_client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

  static final org.apache.log4j.Logger CLIENT_LOGGER = org.apache.log4j.Logger
      .getLogger(TCPClient.class);

  public static void main(String[] args) {

    String modifiedSentence;
    int requestsQuantity = 0;
    final int MAX_REQUESTS = 1000;

    try (Socket clientSocket = new Socket("127.0.0.1", 3248);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()))) {
      while (requestsQuantity <= MAX_REQUESTS) {
        String message = String.format(" request number %d ", requestsQuantity);
        CLIENT_LOGGER.info("Client send " + message);
        outToServer.writeBytes(message + "\n\r");
        outToServer.flush();

        modifiedSentence = inFromServer.readLine();
        CLIENT_LOGGER.info("Response message from server " + modifiedSentence);

        System.out.println("FROM SERVER: " + modifiedSentence);
        requestsQuantity++;
      }
    } catch (Exception e) {
      CLIENT_LOGGER.error(e);
    }
  }
}
