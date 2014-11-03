package com.example.badjoras.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Serializable {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectInputStream inputstream;

    private static HashMap<String, Room> server_rooms;

    public static void main(String[] args) {

        System.out.println("Server started. Listening to the port 4444");

        try {
            InetAddress addr = InetAddress.getByName("192.168.1.78");
            serverSocket = new ServerSocket(4444, 50, addr);  //Server socket
            clientSocket = serverSocket.accept();   //accept the client connection
            System.out.println("tenho um client ligado!");

            inputstream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("consegui abrir um socket para o endereço" + clientSocket.getInetAddress().toString()
            );

        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
        }

        while (true) {
            try {

                //System.out.println(inputstream.;

                //fica bloqueado no read à espera de novos objectos para ler
                Home house = (Home)inputstream.readObject();
                server_rooms = house.getMap();

                //TODO para efeitos de teste, remover mais tarde
                System.out.println(server_rooms.size());

            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

//        TODO: alguma vez iremos querer fechar o socket???
//        try {
//            inputstream.close();
//            clientSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}