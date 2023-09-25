import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Scanner;

public class Client implements Runnable {

   int topic;
   String username;
   int port;
   InetAddress grupo;
   MulticastSocket socket;
   String Ip = "230.0.0.0";
   
   public Client(int topic, String username) throws IOException {
      this.topic = topic;
      this.username = username;

      if (topic == 1) {
          this.port = 4321;
          grupo = InetAddress.getByName(Ip);
      } else if(topic == 2){
          this.port = 4322;
          grupo = InetAddress.getByName(Ip);
      } else{
         this.port = 4323;
          grupo = InetAddress.getByName(Ip);
      }

      InetAddress ia = InetAddress.getByName(Ip);
      InetSocketAddress grupo = new InetSocketAddress(ia, port);
      socket = new MulticastSocket(port);
      NetworkInterface ni = NetworkInterface.getByInetAddress(ia);

      socket.joinGroup(grupo, ni);
  }

   public void run() {

   }

   void enviarMensagem(String mensagem, String username){
      String msg = String.format("[Client | $s] $s: ", getTime(), username + mensagem);
      byte[] envio = new byte[1024];
      envio = msg.getBytes();

      try {
         DatagramPacket pacote = new DatagramPacket(envio, envio.length, grupo, port);
         socket.send(pacote);
      } catch (IOException e) {
         e.printStackTrace();
     }
   }

   

   String getTime() {
      return java.time.LocalTime.now().toString().substring(0, 5);
  }

   public static void main(String[] args) throws IOException {

      Scanner sc = new Scanner(System.in);

      System.out.print("Informe seu nome de usuário:");
      String username = sc.nextLine();

      System.out.print("Escolha a sala de bate papo:\n1 - Filmes\n2 - Series\n3 - Novidades");
      int topico = Integer.parseInt(sc.nextLine());

      Client info = new Client(topico, username);
      Thread receiverThread = new Thread(info);
      receiverThread.start();

      String msg = " ";
      String mensagem = " ";
      MulticastSocket socket;
      socket = new MulticastSocket(info.port);

      while(!mensagem.contains("Sair")) {

         System.out.print("Digite a sua mensagem:");
         mensagem = sc.nextLine();
        	info.enviarMensagem(mensagem, username);

         System.out.println("[Cliente] Esperando por mensagem Multicast...");
         byte[] buffer = new byte[1024];
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
         socket.receive(packet);
         msg = new String(packet.getData());
         System.out.println("[Cliente] Mensagem recebida do Servidor: "+ msg);
         if(!msg.contains("Servidor Encerrado!")){
            break;
         }
      }

      socket.leaveGroup(info.grupo);
      System.out.println("[Cliente] Conexao Encerrada!");
      socket.close();
      sc.close();
   }
}
