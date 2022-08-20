import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.print("Write \"bye\" to exit\n\n");

        try(Socket socket = new Socket(host, port)){
            Scanner farewell = new Scanner(System.in, "UTF-8");

            try(PrintWriter ignored = new PrintWriter(socket.getOutputStream())) {
                while (true) {
                    String message = farewell.nextLine();
                    if ("bye".equalsIgnoreCase(message)) {
                        System.out.print("Bye-bye!\n");
                        return;
                    }
                }
            }
        }
        catch(NoSuchElementException ex) {
            System.out.println("Connection has been dropped!");
        }
        catch(IOException e) {
            System.out.printf("Cannot connect to %s:%s!\n", host, port);
            e.printStackTrace();
        }
    }
}
