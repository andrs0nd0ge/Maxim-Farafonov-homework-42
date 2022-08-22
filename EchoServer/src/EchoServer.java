import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Service service = new Service();
                Socket theSocket = server.accept();
                pool.submit(() -> service.handle(theSocket));
            }
        } catch (IOException e) {
            System.out.printf("Port %s is probably busy.\n", port);
            e.printStackTrace();
        }
    }
}