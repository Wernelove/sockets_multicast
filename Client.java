import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;

import java.util.Date;

public class Client {
   public Client() {
   }

   public static void main(String[] args) throws IOException {
      String msg = " ";
      MulticastSocket socket = new MulticastSocket(4321);
      InetAddress ia = InetAddress.getByName("230.0.0.0");
      InetSocketAddress grupo = new InetSocketAddress(ia, 4321);
      NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
      socket.joinGroup(grupo, ni);

      SimpleDateFormat formatoDataHora = new SimpleDateFormat("HH:mm:ss");

      while(!msg.contains("Servidor Encerrado!")) {
        
        Date horaAtualClient = new Date();
        String horaFormatadaClient = formatoDataHora.format(horaAtualClient);

        System.out.println("[Cliente | "+ horaFormatadaClient +"] Esperando por mensagem Multicast...");
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        msg = new String(packet.getData());

        Date horaAtualServer = new Date();
        String horaFormatadaServer = formatoDataHora.format(horaAtualServer);
        System.out.println("[Cliente | "+ horaFormatadaServer + "] Mensagem recebida do Servidor: " + msg);
      }

      System.out.println("[Cliente] Conexao Encerrada!");
      socket.leaveGroup(grupo, ni);
      socket.close();
   }
}
