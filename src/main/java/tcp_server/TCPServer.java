package tcp_server;

import java.io.IOException;
import java.net.Socket;

public interface TCPServer {

  void start(int threadsNumber, int maxRequests) throws IOException;

  String getRequest(Socket socket) throws IOException;

  String reverseClientMessage(String message);

  void sendResponse(Socket socket, String reverseMessage) throws IOException;

  void closeConnection() throws IOException;

}
