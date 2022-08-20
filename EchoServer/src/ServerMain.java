public class ServerMain {
    public static void main(String[] args) {
        EchoServer.bindToPort(8788).run();
    }
}