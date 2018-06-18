package tcp_client;

import java.io.IOException;

public interface TCPClient {

  void start(String message) throws IOException;

  boolean sendRequest(String message) throws IOException;

  String getResponse() throws IOException;

  String getResponseMessage();

  void closeConnection() throws IOException;

}
