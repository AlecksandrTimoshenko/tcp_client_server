import org.junit.Assert;
import org.junit.Test;
import tcp_client.TCPClientImpl;
import tcp_server.TCPServerImpl;

public class TestTCPClientServer {

  @Test
  public void tcpClientServerTest() throws Exception {
    Thread serverThready = new Thread(new Runnable() {
      public void run() //Этот метод будет выполняться в побочном потоке
      {
        try {
          TCPServerImpl server = new TCPServerImpl(3248);
          server.start(10, 1000);
          Assert.assertTrue(!server.getServerSocket().isClosed());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    serverThready.start();
    for (int i = 0; i < 1000; i++) {
      TCPClientImpl client = new TCPClientImpl("localhost", 3248);
      client.start("My message");
      Assert.assertTrue((client.getClientSocket()).isClosed());
      Assert.assertEquals("egassem yM", client.getResponseMessage());
      client.closeConnection();
    }
  }

}
