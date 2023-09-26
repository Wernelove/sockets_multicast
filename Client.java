import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable {
   private Socket socket;
   private String nome;

   public Client(String nome, String servidorIP, int porta) {
        try {
            this.nome = nome;
            this.socket = new Socket(servidorIP, porta);
            System.out.println("Conectado ao servidor.");
         } catch (IOException e) {
            e.printStackTrace();
         }
   }

   @Override
   public void run() {
      try {
         // Configurar fluxos de entrada e saída
         BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

         // Enviar o nome do cliente para o servidor
         saida.println(nome);

         Scanner scanner = new Scanner(System.in);

         while (true) {
            // Ler mensagem do usuário
            String mensagem = scanner.nextLine();

            // Enviar mensagem para o servidor
            saida.println(mensagem);

            // Se o cliente desejar sair
            if (mensagem.equalsIgnoreCase("SAIR")) {
               break;
            }
         }

         // Fechar recursos
         entrada.close();
         saida.close();
         socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
   }

   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);
      System.out.print("Digite seu nome: ");
      String nome = sc.nextLine();

      String servidorIP = "0.0.0.0";

      System.out.print("Escolha um tópico de interesse:\n1 - Cinema\n2 - Series\n3 - Anuncios\n");
      int grupo = sc.nextInt();

      int porta;

      if(grupo == 1){
         porta = 12345;
      } else if(grupo == 2){
         porta = 56789; 
      } else{
         porta = 13579;
      }

      Client cliente = new Client(nome, servidorIP, porta);

      Thread thread = new Thread(cliente);
      thread.start();
   }
}
