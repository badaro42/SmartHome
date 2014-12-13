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

import static com.example.badjoras.smarthome.MainActivity.AIR_CONDITIONER;
import static com.example.badjoras.smarthome.MainActivity.BEDROOM;
import static com.example.badjoras.smarthome.MainActivity.BLINDS;
import static com.example.badjoras.smarthome.MainActivity.CAT_CAFE;
import static com.example.badjoras.smarthome.MainActivity.CAT_ENLATADOS;
import static com.example.badjoras.smarthome.MainActivity.CAT_FRUTAS;
import static com.example.badjoras.smarthome.MainActivity.CAT_HIGIENE;
import static com.example.badjoras.smarthome.MainActivity.CAT_LEGUMES;
import static com.example.badjoras.smarthome.MainActivity.CAT_MASSAS;
import static com.example.badjoras.smarthome.MainActivity.DEFAULT_PORT;
import static com.example.badjoras.smarthome.MainActivity.IP_ADDRESS;
import static com.example.badjoras.smarthome.MainActivity.KITCHEN;
import static com.example.badjoras.smarthome.MainActivity.LIVING_ROOM;
import static com.example.badjoras.smarthome.MainActivity.PANTRY_STOCK;
import static com.example.badjoras.smarthome.MainActivity.DAY;
import static com.example.badjoras.smarthome.MainActivity.NIGHT;


public class Server implements Serializable {

    //quando for criada nova categoria, adicionar aqui as respectivas constantes
    public static final int CAT_LEGUMES_MINIMUM = 2;
    public static final int CAT_LEGUMES_TO_ORDER = 3;
    public static final int CAT_ENLATADOS_MINIMUM = 3;
    public static final int CAT_ENLATADOS_TO_ORDER = 8;
    public static final int CAT_MASSAS_MINIMUM = 2;
    public static final int CAT_MASSAS_TO_ORDER = 6;
    public static final int CAT_CAFE_MINIMUM = 5;
    public static final int CAT_CAFE_TO_ORDER = 15;
    public static final int CAT_HIGIENE_MINIMUM = 4;
    public static final int CAT_HIGIENE_TO_ORDER = 4;
    public static final int CAT_FRUTAS_MINIMUM = 2;
    public static final int CAT_FRUTAS_TO_ORDER = 4;

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectInputStream inputstream;
    private static ObjectOutputStream outputstream;
    private static Thread output_thread;

    //TODO: para ser mais facil de testar este valor fica em 30000 (30s). no fim alterar para 60000
    private static final int MILISSECONDS_TO_SLEEP = 30000;

    private static String[] products_names = new String[]{
            "Tomate", "Alface", "Lata de Atum", "Esparguete", "Cebola", "Batata",
            "Alho francês", "Lata de Salsichas", "Arroz Agulha", "Café", "Papel Higiénico"
    };
    private static Home server_house;
    private static boolean createThread;

    private static int current_time_of_day;
    private static int day_night_cicles;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println("Servidor iniciado. Estou à espera de conexões na porta 4444");

        //cria um serversocket e fica à espera de novos clientes
        try {
            server_house = null;
            current_time_of_day = DAY;
            day_night_cicles = 0;

            String aux = IP_ADDRESS;
            System.out.println("Server IP: " + aux);
            InetAddress addr = InetAddress.getByName(aux); //ip de casa
            serverSocket = new ServerSocket(DEFAULT_PORT, 50, addr);  //Server socket
            System.out.println("ServerSocket criado.");

            //fica infinitamente à espera de conexões
            while (true) {
                System.out.println("À espera de novo cliente...");
                clientSocket = serverSocket.accept();   //accept the client connection

                //aceitámos nova conexao do cliente, criamos nova output_thread
                createThread = true;

                //se ao fim de 30s o cliente não enviar nada, dá timeout e volta a ligar
//                clientSocket.setSoTimeout(30000);
                System.out.println("Cliente ligado com sucesso!");

                outputstream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputstream = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("Consegui criar sockets para o endereço" +
                        clientSocket.getInetAddress().toString());

                //apos ter uma conexao para o cliente, fica infinitamente à espera de mensagens,
                //até o server detectar que o cliente que estava a servir crashou
                while (true) {
                    try {
                        //fica bloqueado no read à espera de novos objectos para ler
                        System.out.println("Estou à espera de mensagens do cliente...");
                        Home house = (Home) inputstream.readObject();

                        System.out.println("****************\n" + "Counter: " + house.getCounter() +
                                "\n****************");

                        //verifica se se trata do "fake send", para assim enviar o estado actual do server
                        //isto só corre UMA vez, quando a aplicação inicia (assim que reiniciar corre outra vez)
                        if (house.getCounter() == 0) {

                            System.out.println("******** É A PRIMEIRA VEZ, VOU ENVIAR O MEU ESTADO");
                            System.out.println("server_house is null? " + (server_house == null));
                            System.out.println("**************************************************");

                            if (server_house == null) {
                                server_house = new Home();
                            }

                            //esta instrução repoe o counter a 1 e devolve-a ao novo cliente
//                            server_house.setCounter(1);
//                            outputstream.writeObject(server_house);
//                            outputstream.flush();

                            //cria nova thread para enviar cenas para a aplicação
                            makeNewThread();
                            createThread = false;

                        }
                        //nas restantes vezes vem parar aqui!
                        else {
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

                            //se o socket tiver dado timeout e a aplicação continuar a correr,
                            //abrimos novo socket e continuamos à espera de cenas do cliente e
                            //temos que criar nova thread para lhe enviar coisas (ciclo dia e noite)
                            if (createThread) {
                                //cria nova thread para enviar cenas para a aplicação
                                makeNewThread();
                                createThread = false;
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("SocketTimeout!!\nO cliente já não envia mensagens " +
                                "à algum tempo, pode ter crashado. Criamos novo socket.");

                        output_thread.interrupt();

                        outputstream.close();
                        inputstream.close();
                        clientSocket.close();

                        break;
                    } catch (EOFException e) {
                        System.out.println("EOFException!!\nO cliente terminou, " +
                                "criamos novo socket.");

                        output_thread.interrupt();

                        outputstream.close();
                        inputstream.close();
                        clientSocket.close();

                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            e.printStackTrace();
            serverSocket.close();
            System.exit(0);
        } catch (ClassNotFoundException e) {
            serverSocket.close();
            e.printStackTrace();
            System.exit(0);
        }
    }

    //thread responsavel por enviar cenas para a aplicação.
    //envia para a aplicação, logo ao inicio, o objecto que contem o estado do servidor
    //nas restantes vezes, envia periodicamente um objecto que servirá para
    //simular o ciclo de dia e noite
    public static void makeNewThread() {
        output_thread = new Thread() {
            public void run() {
                try {
                    boolean firstTime = true;

                    while (true) {
                        if (firstTime) {
                            System.out.println("Primeiro envio: para a app ficar consistente com o server");

                            //actualizamos a altura do dia em que estamos apenas do objecto do server!!
                            server_house.changeTimeOfDay(current_time_of_day);

                            //esta instrução repoe o counter a 1 e devolve-a ao novo cliente
                            server_house.setCounter(1);

                            outputstream.writeObject(server_house);
                            outputstream.flush();
                            outputstream.reset();

                            System.out.println("Enviei obj para o cliente. Vou dormir " +
                                    (MILISSECONDS_TO_SLEEP / 1000) + "s.");

                            //dorme por 1 minuto -> simula o ciclo dia e noite (1m = 1 dia)
                            Thread.sleep(MILISSECONDS_TO_SLEEP);
                            firstTime = false;
                        } else {
                            System.out.println("Novo envio, desta vez é daqueles periódicos\n" +
                                    "DAY_NIGHT_CYCLES -> " + day_night_cicles);

                            //actualizamos a altura do dia do server e enviamos para a aplicação
                            changeTimeOfDay();
                            server_house.changeTimeOfDay(current_time_of_day);

                            //consoante a altura do dia, altera vários elementos da casa
                            //p.e. fecha/abre estores
                            //verifica tambem se é preciso actualizar o stock da despensa
                            performChangesInHouse();

                            server_house.incrementCounter();
                            outputstream.writeObject(server_house);
                            outputstream.flush();
                            outputstream.reset();

                            System.out.println("Já enviei, agr volto a dormir " +
                                    (MILISSECONDS_TO_SLEEP / 1000) + "s.\n" +
                                    "Counter da casa enviada -> " + server_house.getCounter());

                            //dorme por 1 minuto -> simula o ciclo dia e noite (1m = 1 dia)
                            Thread.sleep(MILISSECONDS_TO_SLEEP);
                        }
                        //por cada alteração, dia -> noite e vice-versa, este contador é incrementado
                        day_night_cicles++;
                    }
                } catch (InterruptedException e) {
                    System.out.println("THREAD THREAD THREAD estou a finalizar, acho que é assim hehe");
                    Thread.currentThread().interrupt(); //propagate interrupt
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        output_thread.start();
    }

    //quando é de DIA -> proximo: NOITE
    //current_time_of_day = 1; next = 0
    //next = (1 + 1) % 2 <=> 2 % 2 = 0 jeje
    //quando é de NOITE -> proximo: DIA
    //current_time_of_day = 0; next = 1
    //next = (0 + 1) % 2 <=> 1 % 2 = 1 jaja
    private static void changeTimeOfDay() {
        current_time_of_day = (current_time_of_day + 1) % 2;
        System.out.println("MUDOU A ALTURA DO DIA!!!\nActual: " +
                ((current_time_of_day == 1) ? "Dia" : "Noite"));
    }

    //por enquanto, apenas abre/fecha os estores consoante a altura do dia
    private static void performChangesInHouse() {
        //adicionamos aqui as divisões que vamos alterar
        String[] rooms = new String[]{KITCHEN, LIVING_ROOM, BEDROOM};

        //altera o estado dos estores consoante a altura do dia
        changeBlinds(current_time_of_day, rooms);

        for(String r : rooms) {
            Room room1 = (Room) server_house.getMap().get(r);
            Blinds bl = (Blinds) room1.getMap().get(BLINDS);
            System.out.println(":::: Estores -> (" + r + ", " + bl.getOpening() + ")");
        }

        //TODO: TESTE TESTE -> REMOVER!
        Room room = (Room) server_house.getMap().get(KITCHEN);
        Blinds blind = (Blinds) room.getMap().get(BLINDS);
        System.out.println("Abertura dos estores da cozinha -> " + blind.getOpening());


        //TODO: nao precisamos de calcular o stock por cada alteração dia/noite
        //TODO usando o contador day_night_cycles podemos calcular o stock apenas
        //TODO quando day_night_cycles % 4 == 0, por exemplo (de 2 em 2 dias)
        //só verifica uma vez "por dia"
        if (day_night_cicles % 2 == 0) {
            boolean updated = updatePantryStock();
            System.out.println("STOCK DA DESPENSA ACTUALIZADO? -> " + updated);
        }
    }


    private static void changeBlinds(int time_of_day, String[] rooms) {
        for (String r_name : rooms) {
            Room room = (Room) server_house.getMap().get(r_name);

            //à noite, fecha todos os estores da casa
            Blinds blind = (Blinds) room.getMap().get(BLINDS);
            blind.changeUnlockingStatus(false);

            if (time_of_day == NIGHT) //fecha totalmente os estores
                blind.changeOpening(100);
            else if (time_of_day == DAY) {//abre quase totalmente os estores, mas só se tiver alguem em casa
                if(server_house.getUserInHouse())
                    blind.changeOpening(20);
            }
        }
    }

    private static boolean updatePantryStock() {
        Room room = (Room) server_house.getMap().get(KITCHEN);
        PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
        LinkedList<Product> prods = pantry.getProductList();

        int current_quantity;
        boolean any_product_updated = false;
        for (Product p : prods) {
            current_quantity = p.getQuantity();
            if (p.getCategoty().equals(CAT_LEGUMES)) {
                if (current_quantity < CAT_LEGUMES_MINIMUM) { //abaixo do minimo, actualiza!
                    current_quantity += CAT_LEGUMES_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            } else if (p.getCategoty().equals(CAT_ENLATADOS)) {
                if (current_quantity < CAT_ENLATADOS_MINIMUM) {
                    current_quantity += CAT_ENLATADOS_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            } else if (p.getCategoty().equals(CAT_MASSAS)) {
                if (current_quantity < CAT_MASSAS_MINIMUM) {
                    current_quantity += CAT_MASSAS_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            } else if (p.getCategoty().equals(CAT_CAFE)) {
                if (current_quantity < CAT_CAFE_MINIMUM) {
                    current_quantity += CAT_CAFE_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            } else if (p.getCategoty().equals(CAT_FRUTAS)) {
                if (current_quantity < CAT_FRUTAS_MINIMUM) {
                    current_quantity += CAT_FRUTAS_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            } else if (p.getCategoty().equals(CAT_HIGIENE)) {
                if (current_quantity < CAT_HIGIENE_MINIMUM) {
                    current_quantity += CAT_HIGIENE_TO_ORDER;
                    p.changeQuantity(current_quantity);
                    any_product_updated = true;
                }
            }
        }
        return any_product_updated;
    }
}
