import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Service {
    public void handle(Socket socket) {

        System.out.printf("Client %s has connected to the server\n", socket);

        try (Scanner reader = getReader(socket); PrintWriter writer = getWriter(socket)) {
            sendResponse("Hello, " + socket, writer);
            while (true) {
                String message = reader.nextLine();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                try {
                    for (Socket client : EchoServer.clients) {
                        if(client != socket) {
                            sendResponse(message, getWriter(client));
                        }
                    }
                } catch (SocketException se) {
                    System.out.println("Unexpected error!");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client has dropped the connection!");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.printf("Client %s has disconnected from the server\n", socket);
        }
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    private Scanner getReader(Socket socket) throws IOException {
        return new Scanner(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    }

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    private void sendResponse(String response, PrintWriter writer) throws IOException {
        writer.println(response);
        writer.flush();
    }
}