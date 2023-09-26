import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class MulticastChat implements Runnable {

    private static final String MULTICAST_IP = "230.0.0.0";
    private int port;

    private static final int BUFFER_SIZE = 1024;

    private MulticastSocket socket;
    private InetAddress group;
    private int topic;
    private String username;
    private String grupo;

    public MulticastChat(int topic, String username) throws IOException {
        this.topic = topic;
        this.username = username;

        if (topic == 1) {
            this.port = 12345;
            this.grupo = "Filmes";
            group = InetAddress.getByName(MULTICAST_IP);
            
        } else if(topic == 2){
            this.port = 54321;
            this.grupo = "Series";
            group = InetAddress.getByName(MULTICAST_IP);
        }else{
            this.port = 13579;
            this.grupo = "Animes";
            group = InetAddress.getByName(MULTICAST_IP);
        }

        socket = new MulticastSocket(port);
        socket.joinGroup(group);
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                if (message.startsWith("[")) {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        String formattedMessage = String.format("[%s | %s] %s : %s", grupo, getCurrentTime(), username, message);
        byte[] buffer = formattedMessage.getBytes();

        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leaveGroup() {
        try {
            String message = String.format("[%s | %s] %s saiu do grupo.", grupo, getCurrentTime(), username);
            byte[] buffer = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);

            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        return java.time.LocalTime.now().toString().substring(0, 5);
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        clearScreen();

        System.out.print("Escolha um tópico de interesse:\n1 - Filmes\n2 - Series\n3 - Animes\n");
        int topic = scanner.nextInt();
        clearScreen();

        scanner.nextLine(); 

        System.out.print("Informe seu nome de usuário: ");
        String username = scanner.nextLine();
        clearScreen();

        MulticastChat chat = new MulticastChat(topic, username);
        Thread receiverThread = new Thread(chat);
        receiverThread.start();

        String message = "";

        try {
            while (!message.equals("sair")) {
                message = scanner.nextLine();
                if (!message.trim().isEmpty()) {
                    chat.sendMessage(message);
                }
                System.out.print("\r");
            }
        } catch (NullPointerException e) {
        }

        chat.leaveGroup();
        scanner.close();
    }
}
