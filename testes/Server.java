import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] portas = { 12345, 56789, 13579 };
         String[] grupos = {"Cinema", "Series", "Anuncios"};

        System.out.println("Servidor escutando nas portas: " + Arrays.toString(portas));

        for (int porta : portas) {
            Thread thread = new Thread(new ServerRunnable(porta));
            thread.start();
        }
    }

    public static void broadcast(String mensagem) {
        for (PrintWriter cliente : clientes) {
            cliente.println(mensagem);
        }
        // Exibir a mensagem no servidor
        System.out.println(mensagem);
    }

    private static class ServerRunnable implements Runnable {
        private int porta;

        public ServerRunnable(int porta) {
            this.porta = porta;
        }

        @Override
        public void run() {
            try (ServerSocket servidorSocket = new ServerSocket(porta)) {

                while (true) {
                    Socket clienteSocket = servidorSocket.accept();
                    // Criar thread para o cliente
                    Thread thread = new Thread(new ClienteHandler(clienteSocket));
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket clienteSocket;
        private PrintWriter out;

        public ClienteHandler(Socket socket) {
            this.clienteSocket = socket;
        }
      @Override
      public void run() {
         try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            out = new PrintWriter(clienteSocket.getOutputStream(), true);

            String nomeCliente = in.readLine();
            clientes.add(out);

            broadcast(nomeCliente + " entrou no chat ");

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
               if (mensagem.equalsIgnoreCase("SAIR")) {
                  break;
               }
               broadcast(nomeCliente + ": " + mensagem);
            }

            // Remover cliente da lista
            clientes.remove(out);
            broadcast(nomeCliente + " saiu do chat.");
            clienteSocket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
