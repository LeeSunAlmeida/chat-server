package org.academiadecodigo.rapunshells;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ChatServer {

    public static final int DEFAULT_PORT = 8080;
    private ServerSocket serverSocket;


    private LinkedList<ServerWorker> clientsConnectionsContainer;



    public static void main(String[] args) {

        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

    private void listen() {



        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            clientsConnectionsContainer = new LinkedList<>();

            while (true) {


                Socket clientsSocket = serverSocket.accept();
                ChatServer.ServerWorker serverWorker = new ChatServer.ServerWorker(clientsSocket);
                Thread thread = new Thread(serverWorker);
                thread.start();
                clientsConnectionsContainer.add(serverWorker);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sendAll(String receivedInput) {

       for(ServerWorker serverWorker : clientsConnectionsContainer){

           serverWorker.send(receivedInput);
       }
    }


    class ServerWorker implements Runnable {


        private BufferedReader in;
        private Socket clientsSocket;
        private PrintWriter out;


        private boolean clientsConnection = true;

        public ServerWorker(Socket clientSocket){

            this.clientsSocket = clientSocket;
            try {
                out = new PrintWriter(clientsSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String str){

                out.println(str);
                out.flush();
        }

        @Override
        public void run() {

            try {
                in = new BufferedReader(new InputStreamReader(clientsSocket.getInputStream()));


                while (clientsConnection) {
                    String receivedInput = in.readLine();

                    System.out.println(receivedInput);

                    sendAll(receivedInput);


                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            finally{
                try {
                    clientsSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
    }
}
