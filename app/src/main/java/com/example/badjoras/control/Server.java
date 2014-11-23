package com.example.badjoras.control;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import static com.example.badjoras.smarthome.MainActivity.DEFAULT_PORT;
import static com.example.badjoras.smarthome.MainActivity.IP_ADDRESS;
import static com.example.badjoras.smarthome.MainActivity.KITCHEN;
import static com.example.badjoras.smarthome.MainActivity.PANTRY_STOCK;


public class Server implements Serializable {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectInputStream inputstream;
    private static ObjectOutputStream outputstream;
    private static int MANY_QUANTITY = 7;
    private static int QUANTITY_TO_MANY = 20;
    private static int FEW_QUANTITY = 3;
    private static int QUANTITY_TO_FEW = 5;
    private static String[] products_names = new String[]{
            "Tomate", "Alface", "Lata de Atum", "Esparguete", "Cebola", "Batata",
            "Alho francês", "Lata de Salsichas", "Arroz Agulha", "Café", "Papel Higiénico"
    };
    private static Home server_house;


    public static void main(String[] args) throws IOException {

        System.out.println("Servidor iniciado. Estou à espera de conexões na porta 4444");

        //cria um serversocket e fica à espera de novos clientes
        try {
            server_house = null;

            String aux = IP_ADDRESS;
            System.out.println("Server IP: " + aux);
            InetAddress addr = InetAddress.getByName(aux); //ip de casa
            serverSocket = new ServerSocket(DEFAULT_PORT, 50, addr);  //Server socket
            System.out.println("ServerSocket criado.");

            //fica infinitamente à espera de conexões
            while (true) {
                System.out.println("À espera de novo cliente...");
                clientSocket = serverSocket.accept();   //accept the client connection

                //se ao fim de 20s o cliente não enviar nada, dá timeout e volta a ligar
                clientSocket.setSoTimeout(20000);
                System.out.println("Cliente ligado com sucesso!");

                outputstream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputstream = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("Consegui criar sockets para o endereço" +
                        clientSocket.getInetAddress().toString());

                //apos ter uma conexao para o cliente, fica infinitamente à espera de mensagens,
                //até o socket dar timeout
                while (true) {
                    try {
                        //fica bloqueado no read à espera de novos objectos para ler
                        System.out.println("Estou à espera de mensagens do cliente...");
                        Home house = (Home) inputstream.readObject();

                        System.out.println("****************\n" + "Counter: " + house.getCounter() +
                                "\n****************");

                        //verifica se se trata do "fake send", para assim enviar o estado actual do server
                        if (house.getCounter() == 0) {

                            System.out.println("******** É A PRIMEIRA VEZ, VOU ENVIAR O MEU ESTADO");
                            System.out.println("server_house is null? " + server_house == null);
                            System.out.println("**************************************************");

                            if (server_house == null) {
                                server_house = new Home();
                            }

                            //esta instrução repoe o counter a 1 e devolve-a ao novo cliente
                            server_house.setCounter(1);
                            outputstream.writeObject(server_house);
                            outputstream.flush();

                        } else {
                            server_house = house;

                            System.out.println("JÁ NÃO É A PRIMEIRA VEZ!!!\nCounter = " +
                                    server_house.getCounter());

                            Room room = (Room) server_house.getMap().get(KITCHEN);
                            PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
                            LinkedList<Product> prods = pantry.getProductList();

                            String res = "LISTA DE PRODUTOS!!!\n";
                            for (Product prod : prods) {
                                res += prod.getName() + ": " + prod.getQuantity() + "\n";
                            }
                            System.out.print(res);
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("SocketTimeout. vamos fechar o socket e abrir de novo");

                        outputstream.close();
                        inputstream.close();
                        clientSocket.close();

                        break;
                    } catch (EOFException e) {
                        System.out.println("EOFException! O cliente fechou, vamos criar novo socket");

                        outputstream.close();
                        inputstream.close();
                        clientSocket.close();

                        break;
                    }
                }

//                outputstream.close();
//                inputstream.close();
//                clientSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            e.printStackTrace();
            serverSocket.close();
        } catch (ClassNotFoundException e) {
            serverSocket.close();
            e.printStackTrace();
        }
    }
}