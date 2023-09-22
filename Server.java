import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import java.util.Date;

public class Server {
   public Server() {
   }

   public static void main(String[] args) throws IOException {

      // Iniciar variavel para armazenar mensagens
      String mensagem = " ";
      
      // Reservar bytes para guardar as mensagens
      byte[] envio = new byte[1024];

      // Scanner para ler a mensagem a partir do teclado
      Scanner sc = new Scanner(System.in);

      // Iniciar o socket Multicast
      MulticastSocket socket = new MulticastSocket();
      
      // Definir grupos distintos
      InetAddress grupo1 = InetAddress.getByName("230.0.0.0");
      InetAddress grupo2 = InetAddress.getByName("231.0.0.0");
      InetAddress grupo3 = InetAddress.getByName("232.0.0.0");

      // Definir formatação do horário
      SimpleDateFormat formatoDataHora = new SimpleDateFormat("HH:mm:ss");

      // Rodar o servidor enquanto não receber "Servidor Encerrado!"
      while(!mensagem.equals("Servidor Encerrado!")) {

         // Iniciar sistema de horário 
         Date horaAtual = new Date();
         String horaFormatada = formatoDataHora.format(horaAtual);

         // Mensagem padrão do servidor e aguardar input
         System.out.print("[Servidor | " + horaFormatada + "] Digite a mensagem: ");
         mensagem = sc.nextLine();

         // Verificar se houve o input para encerrar o servidor
         if (mensagem.equals("encerrar")) {
            mensagem = "Servidor Encerrado!";
         }

         // Separar o input para poder pegar a primeira palavra(que define o grupo)
         String[] palavras = mensagem.split(" ");

         // Definir qual será o canal da mensagem e realizar o envio do pacote
         // Enviar para grupo 1
         if(palavras[0].equals("grupo1")){

            // Tratamento da mensagem para retirar o nome do grupo
            mensagem = mensagem.replaceFirst("\\b\\w+\\b", "");
            mensagem = mensagem.trim();
            // Converter a mensagemm de input em um pacote
            envio = mensagem.getBytes();
            DatagramPacket pacote = new DatagramPacket(envio, envio.length, grupo1, 4321);
            // Enviar o pacote para o servidor
            socket.send(pacote);
         }

         // Enviar para grupo 2
         if(palavras[0].equals("grupo2")){

            // Tratamento da mensagem para retirar o nome do grupo
            mensagem = mensagem.replaceFirst("\\b\\w+\\b", "");
            mensagem = mensagem.trim();
            // Converter a mensagemm de input em um pacote
            envio = mensagem.getBytes();
            DatagramPacket pacote = new DatagramPacket(envio, envio.length, grupo2, 4321);
            // Enviar o pacote para o servidor
            socket.send(pacote);
         }

         // Enviar para grupo 3
         if(palavras[0].equals("grupo3")){

            // Tratamento da mensagem para retirar o nome do grupo
            mensagem = mensagem.replaceFirst("\\b\\w+\\b", "");
            mensagem = mensagem.trim();
            // Converter a mensagemm de input em um pacote
            envio = mensagem.getBytes();
            DatagramPacket pacote = new DatagramPacket(envio, envio.length, grupo3, 4321);
            // Enviar o pacote para o servidor
            socket.send(pacote);
         }
      }
     
      // Fechar servidor
      Date horaAtual = new Date();
      String horaFormatada = formatoDataHora.format(horaAtual);
      System.out.print("[Servidor | " + horaFormatada + "] Multicast Encerrado");
      socket.close();
      sc.close();
   }
}
