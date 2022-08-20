public class ClientMain {
    public static void main(String[] args) {
        EchoClient.connectTo(8788).run();
    }
}