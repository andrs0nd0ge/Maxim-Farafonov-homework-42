import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
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
//            try (Socket clientSocket = server.accept()) {
//                handle(clientSocket);
//            }
            while(!server.isClosed()){
                Socket theSocket = server.accept();
                pool.submit(() -> handle(theSocket));
            }
        } catch (IOException e) {
            System.out.printf("Port %s is probably busy.\n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) {
        System.out.printf("Client %s has connected to the server\n", socket);

        try (Scanner reader = getReader(socket); PrintWriter writer = getWriter(socket); socket) {
            sendResponse("Hello, " + socket, writer);
            while (true) {
                String message = reader.nextLine();

                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }

                sendResponse(message.toUpperCase(), writer);
            }

        } catch (NoSuchElementException e) {
            System.out.println("Client has dropped the connection!");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.printf("Client is disconnected: %s\n", socket);
        }
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        return new Scanner(input);
    }

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    private void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
