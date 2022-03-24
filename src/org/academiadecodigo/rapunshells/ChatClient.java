package org.academiadecodigo.rapunshells;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    public static final int DEFAULT_PORT = 8080;
    private String hostName;

    private Socket socket;



    private boolean isConnected = true;

    public ChatClient() {
        this.hostName = "127.0.0.1";

        try {
            socket = new Socket(hostName, DEFAULT_PORT);
            start();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args){




        ChatClient chatClient = new ChatClient();
        chatClient.start();


    }


    public void start() {




            Thread thread = new Thread(new Chat());
            thread.start();





        BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
        try {
            BufferedWriter keyboardOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            while (true) {

                try {
                    String keyBoardText = keyboardInput.readLine();
                    System.out.println(keyBoardText);

                    keyboardOutput.write(keyBoardText);
                    keyboardOutput.newLine();
                    keyboardOutput.flush();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                keyboardInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    class Chat implements Runnable {

        private BufferedReader in;



        @Override
        public void run() {

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (isConnected) {

                    String receivedInput = in.readLine();
                    System.out.println(receivedInput);



                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
