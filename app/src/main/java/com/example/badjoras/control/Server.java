package com.example.badjoras.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import static com.example.badjoras.smarthome.MainActivity.*;


public class Server implements Serializable {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectInputStream inputstream;

    private static HashMap<String, Room> server_rooms;

    public static void main(String[] args) throws IOException {

        System.out.println("Servidor iniciado. Estou à espera de conexões na porta 4444");

        //TODO: arranjar maneira de saber que o cliente morreu,
        //TODO: e que da proxima vez vai obter o estado do servidor!!
        //cria um serversocket e fica à espera de novos clientes
        try {
            String aux =InetAddress.getLocalHost().getHostAddress();
            System.out.println(aux);
            InetAddress addr = InetAddress.getByName(aux); //ip de casa
            serverSocket = new ServerSocket(4444, 50, addr);  //Server socket
            System.out.println("ServerSocket criado.");

            while (true) {
                System.out.println("À espera de novo cliente...");
                clientSocket = serverSocket.accept();   //accept the client connection
                System.out.println("Cliente ligado com sucesso!");

                inputstream = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("Consegui abrir um socket para o endereço" +
                        clientSocket.getInetAddress().toString());

                //fica bloqueado no read à espera de novos objectos para ler
                System.out.println("Estou à espera de mensagens do cliente...");
                Home house = (Home) inputstream.readObject();
                server_rooms = house.getMap();

                //TODO: para efeitos de teste, remover!!!
                Room room = server_rooms.get(KITCHEN);
                PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
                LinkedList<Product> prods = pantry.getProductList();

                String res = "";
                for(Product prod : prods) {
                    res += prod.getName() + ": " + prod.getQuantity() + "\n";
                }
                System.out.print(res);

                //TODO para efeitos de teste, remover mais tarde
                System.out.println(server_rooms.size());

                inputstream.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            serverSocket.close();
        } catch (ClassNotFoundException e) {
            serverSocket.close();
            e.printStackTrace();
        }

        //enquanto o cliente estiver ligado, espera por novas mensagens
//            while (true) {
//                try {
//                    //fica bloqueado no read à espera de novos objectos para ler
//                    Home house = (Home) inputstream.readObject();
//                    server_rooms = house.getMap();
//
//                    //TODO para efeitos de teste, remover mais tarde
//                    System.out.println(server_rooms.size());
//
//                } catch (IOException ex) {
//                    System.out.println("Problemas a ler a mensagem!");
//                    serverSocket.close();
//                    clientSocket.close();
//                    inputstream.close();
//                    break;
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
    }


//        while (true) {
//            try {
//                System.out.println("À espera de novo cliente...");
//                InetAddress addr = InetAddress.getByName("192.168.1.78");
//                serverSocket = new ServerSocket(4444, 50, addr);  //Server socket
//                System.out.println("ServerSocket criado.");
//
//                clientSocket = serverSocket.accept();   //accept the client connection
//                System.out.println("Cliente ligado com sucesso!");
//
//                inputstream = new ObjectInputStream(clientSocket.getInputStream());
//                System.out.println("Consegui abrir um socket para o endereço" +
//                                clientSocket.getInetAddress().toString());
//
//            } catch (IOException e) {
//                System.out.println("Could not listen on port: 4444");
//            }
//
//            //enquanto o cliente estiver ligado, espera por novas mensagens
//            while (true) {
//                try {
//                    //fica bloqueado no read à espera de novos objectos para ler
//                    Home house = (Home) inputstream.readObject();
//                    server_rooms = house.getMap();
//
//                    //TODO para efeitos de teste, remover mais tarde
//                    System.out.println(server_rooms.size());
//
//                } catch (IOException ex) {
//                    System.out.println("Problemas a ler a mensagem!");
//                    serverSocket.close();
//                    clientSocket.close();
//                    inputstream.close();
//                    break;
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
}
