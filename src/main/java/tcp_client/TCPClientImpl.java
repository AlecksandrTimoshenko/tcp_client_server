package tcp_client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.log4j.Logger;

public class TCPClientImpl implements TCPClient {

  public static final Logger LOGGER_CLIENT = Logger.getLogger(TCPClientImpl.class);

  private Socket clientSocket;
  private BufferedReader inFromServer = null;
  private DataOutputStream outToServer = null;
  String responseMessage;

  public TCPClientImpl(String connectionIp, int connectionPort) throws IOException {
    clientSocket = new Socket(connectionIp, connectionPort);
  }

  @Override
  public void start(String message) throws IOException {

    sendRequest(message);
    LOGGER_CLIENT.info("Client send " + message);
    System.out.println("Client send " + message);

    responseMessage = getResponse();
    LOGGER_CLIENT.info("Response message from server " + responseMessage);
    System.out.println("Response message from server " + responseMessage);

    clientSocket.close();
  }

  @Override
  public boolean sendRequest(String message) throws IOException {
    try {
      outToServer = new DataOutputStream(clientSocket.getOutputStream());
      outToServer.writeBytes(message + "\n\r");
      outToServer.flush();
      return true;
    } catch (IOException e) {
      LOGGER_CLIENT.error(e);
      throw new IOException(e);
    }
  }

  @Override
  public String getResponse() throws IOException {
    try {
      inFromServer = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      return inFromServer.readLine();
    } catch (IOException e) {
      LOGGER_CLIENT.error(e);
      throw new IOException(e);
    }
  }

  @Override
  public void closeConnection() throws IOException {
    inFromServer.close();
    outToServer.close();
    clientSocket.close();
  }

  @Override
  public String getResponseMessage() {
    return responseMessage;
  }

  public Socket getClientSocket() {
    return clientSocket;
  }
}
