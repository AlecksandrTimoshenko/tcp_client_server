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

public class TCPServerImpl implements TCPServer {

  public static final Logger SERVER_LOGGER = Logger.getLogger(TCPServerImpl.class);

  private ServerSocket serverSocket;
  Socket socket;
  BufferedReader inFromClient = null;
  DataOutputStream outToClient = null;
  int i = 0;


  public TCPServerImpl(int connectionPort) throws Exception {
    this.serverSocket = new ServerSocket(connectionPort);
  }

  public void start(int threadsNumber, int maxRequests) throws IOException {
    ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
    while (true) {
      socket = serverSocket.accept();
      service.submit(new Runnable() {
        @Override
        public void run() {
          try {
            String messageFromClient = getRequest(socket);
            logThreadNumber();
            sendResponse(socket, reverseClientMessage(messageFromClient));
            i++;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      if (i == maxRequests) {
        closeConnection();
        break;
      }
    }
  }

  @Override
  public String getRequest(Socket socket) throws IOException {
    try {
      inFromClient = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      return inFromClient.readLine();
    } catch (IOException e) {
      SERVER_LOGGER.error(e);
      throw new IOException(e);
    }
  }

  public void logThreadNumber() {
    SERVER_LOGGER.info(Thread.currentThread().getName());
    System.out.println(Thread.currentThread().getName());
  }

  @Override
  public String reverseClientMessage(String message) {
    return new StringBuffer(message).reverse().toString();
  }

  @Override
  public void sendResponse(Socket socket, String reversMessage) throws IOException {
    try {
      outToClient = new DataOutputStream(socket.getOutputStream());
      outToClient.writeBytes(reversMessage + "\r");
      outToClient.flush();
    } catch (IOException e) {
      SERVER_LOGGER.error(e);
      throw new IOException(e);
    }
  }

  public void closeConnection() throws IOException {
    outToClient.close();
    inFromClient.close();
    socket.close();
    serverSocket.close();
    System.out.println("close");
  }

  public ServerSocket getServerSocket() {
    return serverSocket;
  }
}
