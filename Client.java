import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Date;

public class Client {
   public Client() {
   }

   public static void main(String[] args) throws IOException {

      // Iniciar variavel para armazenar mensagens
      String msg = " ";
      String def_grupo = " ";
      Scanner sc = new Scanner(System.in);

      // Criar socket multicast para receber na porta 4321
      MulticastSocket socket = new MulticastSocket(4321);

      System.out.print("Defina os grupos que irá participar: ");
      def_grupo = sc.nextLine();
      String[] def_grupo_split = def_grupo.split(" ");


     if(Arrays.asList(def_grupo_split).contains("grupo1")){
         
         // Definir IP do host
         InetAddress ia1 = InetAddress.getByName("230.0.0.0");

         // Combinar o IP com o port 
         InetSocketAddress grupo1 = new InetSocketAddress(ia1, 4321);

         // Encontra a interface de rede do IP e salvar numa variavel
         NetworkInterface ni1 = NetworkInterface.getByInetAddress(ia1);

         // Se conectar ao grupo definido
         socket.joinGroup(grupo1, ni1); 

      } else if(Arrays.asList(def_grupo_split).contains("grupo2")){
         
         InetAddress ia2 = InetAddress.getByName("231.0.0.0");
         InetSocketAddress grupo2 = new InetSocketAddress(ia2, 4322);
         NetworkInterface ni2 = NetworkInterface.getByInetAddress(ia2);
         socket.joinGroup(grupo2, ni2); 

      } else if(Arrays.asList(def_grupo_split).contains("grupo3")){
         
         InetAddress ia3 = InetAddress.getByName("232.0.0.0");
         InetSocketAddress grupo3 = new InetSocketAddress(ia3, 4323);
         NetworkInterface ni3 = NetworkInterface.getByInetAddress(ia3);
         socket.joinGroup(grupo3,ni3);

      } else{
         System.out.print("Nenhum grupo selecionado");
      }

      // Definir formatação do horário
      SimpleDateFormat formatoDataHora = new SimpleDateFormat("HH:mm:ss");

      // Receber mensagens do servidor enquanto não receber "Servidor Encerrado!"
      while(!msg.contains("Servidor Encerrado!")) {
        
         // Iniciar sistema de horário 
         Date horaAtualClient = new Date();
         String horaFormatadaClient = formatoDataHora.format(horaAtualClient);

         // Mensagem padrão do servidor e aguardar input
         System.out.println("[Cliente | "+ horaFormatadaClient +"] Esperando por mensagem Multicast...");

         // Criar buffer de dados para armazenar a mensagem
         byte[] buffer = new byte[1024];

         // Criar pacote
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

         // Receber pacote e salvar numa variavel
         socket.receive(packet);
         msg = new String(packet.getData());

         Date horaAtualServer = new Date();
         String horaFormatadaServer = formatoDataHora.format(horaAtualServer);

         // Printar mensagem recebida
         System.out.println("[Cliente | "+ horaFormatadaServer + "] Mensagem recebida do Servidor: " + msg);
      }

      // Desconectar do servidor
      System.out.println("[Cliente] Conexao Encerrada!");
      socket.leaveGroup(grupo1, ni1);
      socket.close();
   }
}
