package tcp_client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.log4j.Logger;
import tcp_server.TCPServer;

public class TCPClient {

  public static final Logger LOGGER_CLIENT = Logger.getLogger(TCPServer.class);
  static String IP = "127.0.0.1";
  static final int PORT = 3248;


  public static void main(String[] args) throws IOException {

    String modifiedSentence;
    int requestsQuantity = 0;
    final int MAX_REQUESTS = 1000;

    for (int i = 0; i < MAX_REQUESTS; i++) {
      try (Socket clientSocket = new Socket(IP, PORT)) {

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));

        String message = String.format(" request number %d ", requestsQuantity);
        LOGGER_CLIENT.info("Client send " + message);

        outToServer.writeBytes(message + "\n\r");
        outToServer.flush();

        modifiedSentence = inFromServer.readLine();
        LOGGER_CLIENT.info("Response message from server " + modifiedSentence);
        System.out.println("FROM SERVER: " + modifiedSentence);
        requestsQuantity++;
      } catch (IOException e) {
        LOGGER_CLIENT.error(e);
        throw new IOException("Connection refused");
      }
    }
  }
}

