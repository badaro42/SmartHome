package com.example.badjoras.smarthome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.badjoras.control.Home;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {

    public static final String AIR_CONDITIONER = "Ar Condicionado";
    public static final String PANTRY_STOCK = "Stock da Despensa";
    public static final String LIGHTS = "Iluminação";
    public static final String BLINDS = "Estores";
    public static final String WATER_CONSUMPTION = "Consumo Electricidade";
    public static final String POWER_CONSUMPTION = "Consumo Água";
    public static final String GAS_CONSUMPTION = "Consumo Gás";
    public static final String GARAGE_DOOR = "Portão da Garagem";
    public static final String SURVEILLANCE_CAMERAS = "Câmaras de Vigilância";
    public static final String SCHEDULED_FUNCTIONS = "Tarefas Agendadas";
    public static final String COFFEE_MACHINE = "Máquina de Café";
    public static final String STOVE_OVEN = "Fogão/Forno";
    public static final String SPRINKLER = "Aspersores da Rega";
    public static final String ENTER_HOUSE = "Entrar em Casa";
    public static final String EXIT_HOUSE = "Sair de Casa";

    public static final String OUTSIDE_GENERAL = "Exterior/Geral";
    public static final String KITCHEN = "Cozinha";
    public static final String BEDROOM = "Quarto";
    public static final String LIVING_ROOM = "Sala de Estar";

    public static final String CAT_LEGUMES = "Legumes";
    public static final String CAT_ENLATADOS = "Enlatados";
    public static final String CAT_MASSAS = "Massas";
    public static final String CAT_CAFE = "Café";
    public static final String CAT_HIGIENE = "Higiene";
    public static final String CAT_FRUTAS = "Frutas";

    public static final String CURRENT_TAB = "current_tab";

    //valor bem grande, para inicializar as distancias dos AP -> 42.000km
    public static final Double UM_CARALHAO_DE_METROS = 42000000.0;

    public static final int DAY = 1;
    public static final int NIGHT = 0;

    public static MediaPlayer cafe;
    public static Toast toast;

    //USAR UM DESTES IPs

//    public static final String IP_ADDRESS = "10.171.241.205";
    //    public static final String IP_ADDRESS = "10.22.107.150";
//    public static final String IP_ADDRESS = "192.168.1.78";
    //    public static final String IP_ADDRESS = "192.168.1.71";
//    public static final String IP_ADDRESS = "192.168.46.1";
  //  public static final String IP_ADDRESS = "10.22.107.141";
    //    public static final String IP_ADDRESS = "10.171.240.101";

//    public static final String IP_ADDRESS = "10.171.241.205"; //ip fac canteiro
//    public static final String IP_ADDRESS = "10.22.107.150"; //ip fac badaro
//    public static final String IP_ADDRESS = "192.168.1.78"; //ip casa badaro
//    public static final String IP_ADDRESS = "192.168.1.71"; //ip casa badaro
//    public static final String IP_ADDRESS = "192.168.46.1"; //ip casa badaro
//    public static final String IP_ADDRESS = "10.171.110.142"; //ip casa badaro
    public static final String IP_ADDRESS = "10.171.240.101"; //ip casa badaro

    public static final int DEFAULT_PORT = 4444;

    //TODO: colocar aqui os ids dos AP mais perto de cada divisao
    //SALA DE CIMA
    public static final String BSSID_LIVING_ROOM_1 = "00:19:07:93:cc:b2";
    public static final String BSSID_LIVING_ROOM_2 = "00:12:da:ae:b2:a1";
    public static final String BSSID_LIVING_ROOM_3 = "00:12:da:ae:b2:a2";

    //SALA DA PRATICA DE PI
    public static final String BSSID_KITCHEN_1 = "00:19:07:93:cc:b0";
    public static final String BSSID_KITCHEN_2 = "00:19:07:93:cc:b1";
    public static final String BSSID_KITCHEN_3 = "00:19:07:93:cc:b2";

    //ANTIGA SALA DE MESTRADO
    public static final String BSSID_BEDROOM_1 = "00:11:21:6c:4a:b1";
    public static final String BSSID_BEDROOM_2 = "00:11:21:6c:4a:b2";
    public static final String BSSID_BEDROOM_3 = "00:11:21:6c:4a:b0";

    public static String last_position;

    //TODO: alterei isto para static para que não sejam reinicializadas com a mudança de orientação
    private static Socket client_socket;
    private static ObjectOutputStream obj_os;
    private static ObjectInputStream obj_is;

    private static Home house;

    public static List<Fragment> fragment_list;

    private String[] kitchen_features = new String[]{
            PANTRY_STOCK, AIR_CONDITIONER, LIGHTS, BLINDS, COFFEE_MACHINE, STOVE_OVEN
    };

    private String[] bedroom_features = new String[]{
            AIR_CONDITIONER, LIGHTS, BLINDS
    };

    private String[] outside_general_features = new String[]{
            ENTER_HOUSE, EXIT_HOUSE, LIGHTS, WATER_CONSUMPTION, POWER_CONSUMPTION, GAS_CONSUMPTION, SPRINKLER, GARAGE_DOOR //, SURVEILLANCE_CAMERAS, SCHEDULED_FUNCTIONS
    };

    private String[] living_room_features = new String[]{
            LIGHTS, BLINDS, AIR_CONDITIONER
    };

    public static PagerSlidingTabStrip tabs;
    public static ViewPager pager;
    public static MyPagerAdapter adapter;
    public static FragmentManager app_fm;
    public static Fragment curr_fragment;
    public static int current_fragment_tab = 0;

    public static Thread input_thread;
    public static Thread connection_thread;

    public static CharSequence m_title;

    private static WifiManager mainWifiObj;
    private WifiScanReceiver wifiReciever;
    private static HashMap<String, ArrayList<Double>> results_map;

    private static boolean receiver_registered = false;

    public double distance_to_ap_kitchen;
    public double distance_to_ap_bedroom;
    public double distance_to_ap_livingroom;

    //TODO: COLOCAR A TRUE PARA EFEITOS DE TESTE -> DPS MUDAR PARA FALSE!!!!!
    public static boolean disable_location_for_good = false;

    public static boolean offline_mode;
    public static boolean first_time_running = true;
    public static boolean connection_thread_finished = false;
    public static boolean trying_to_connect = false;
    public static boolean reset_fragment_list = true;

    public static boolean canRedrawFrags = true;

    private static Handler handler;

    //A ProgressDialog object
    private static ProgressDialog progressDialog;


    // Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
//            TODO: arrebenta nesta merda, ver melhor!!
            if (canRedrawFrags) {
                Toast.makeText(getApplicationContext(), "Posso redesenhar os fragmentos!!!",
                        Toast.LENGTH_SHORT).show();
                refreshTabs();
            } else {
                Toast.makeText(getApplicationContext(), "Já mudei de orientação, não redesenha!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("******ON_CREATE DA MAIN ACTIVITY!!!******");

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        resetScanResultsMap();

        //se for a primeira vez, mostramos o ecra de loading
        //se nao, chamamos todas as inicializações menos a da conexão!
        if (first_time_running) {
            //Initialize a LoadViewTask object and call the execute() method
            new LoadViewTask().execute();
        } else {
            setContentView(R.layout.activity_homepage);
            initPositionThing();
            initFragmentsAndTabs();
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            pager.setCurrentItem(savedInstanceState.getInt(CURRENT_TAB));
        } else {
            // Probably initialize members with default values for a new instance
        }

    }

    //reinicia o mapa que contém os valores das distancias aos AP
    public void resetScanResultsMap() {

        results_map = new HashMap<String, ArrayList<Double>>(15);
        ArrayList<Double> list;

        //para cada divisao, vai criar uma lista com 3 posições
        for (int i = 0; i < 3; i++) {
            list = new ArrayList<Double>(3);

            //para cada um dos AP, vai inicializar o seu valor com um numero mta grande
            for (int ii = 0; ii < 3; ii++)
                list.add(UM_CARALHAO_DE_METROS);

            if (i == 0)
                results_map.put(KITCHEN, list);
            else if (i == 1)
                results_map.put(BEDROOM, list);
            else
                results_map.put(LIVING_ROOM, list);
        }
    }


    public void handleConnection() {
        last_position = "";
        client_socket = null;
        first_time_running = false;

        //tentamos criar o socket para o servidor logo ao iniciar a app
        //para determinar se a app funcionara em modo online ou offline
        //se nao conseguir ligar ao server fica sempre em modo offline, pelo que as alterações
        //nao sao permanentes. Se estiver em modo online e nalguma das comunicaçoes perder
        //a ligação, passa a funcionar em moddo offline até ser reiniciada.
        //MESMO QUE SE VIRE A ORIENTAÇAO, FICA SEMPRE EM ONLINE OU OFFLINE, NAO VOLTA A LIGAR!
        if (trying_to_connect) {
            house = new Home();

            //se conseguimos ligar ao servidor, obtem o estado actual do servidor
            if (!offline_mode) {

                Toast.makeText(getApplicationContext(),
                        "Ligação ao servidor bem sucedida!", Toast.LENGTH_SHORT).show();

                System.out.println("MODO ONLINE!!! VAMOS COMUNICAR COM O SERVER!");

                //fazemos um "fake send" para o server nos enviar o objeto que ele tem
                sendObjectToServer(house);

                System.out.println("***já enviei o objecto inicial para o server e correu tudo bem!***");

                //thread que fica à espera de mensagens do servidor
                //usar isto para a parte da inteligencia em que o server precisa de enviar cenas para a app
                input_thread = new Thread() {
                    public void run() {
                        try {
                            boolean firstTime = true;
                            System.out.println("À ESPERA DE OBJECTOS DO SERVIDOR!!!! ESPERO BEM QUE ENTRE AQUI HEHE");

                            while (true) {
                                //obtemos o estado do server, para o caso de reiniciarmos a aplicação
                                Home temp_house = getObjectFromServer();

                                if (temp_house != null) {
                                    System.out.println("ESTAMOS EM QUE PARTE DO DIA?? " +
                                            temp_house.getCurrentTimeOfDay());

                                    if (firstTime) {
                                        house = temp_house;
                                        firstTime = false;
                                        Thread.sleep(5000);
                                    } else {
                                        //recebemos uma cena do servidor -> actualizamos a info actual
                                        house = temp_house;

                                        System.out.println("Tou na thread. Counter recebido -> " +
                                                temp_house.getCounter());

                                        //TODO: CORRIGIR ISTO, REBENTA QUANDO SE MUDA DE ORIENTAÇÃO!!
                                        mHandler.post(mUpdateResults);

                                        Thread.sleep(5000);
                                    }
                                } else {
                                    System.out.println("VOU INTERROMPER A THREAD, O SERVER TA MORTO!!");
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                input_thread.start();
            } else {

                Toast.makeText(getApplicationContext(),
                        "Falhou a ligação ao server. Modo offline", Toast.LENGTH_SHORT).show();
            }
            trying_to_connect = false;
        }
    }

    public void initPositionThing() {

        //só entra aqui se o utilizador desactivar a cena da localização
        if (!disable_location_for_good) {
            mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiReciever = new WifiScanReceiver();

            if (!receiver_registered) {
                registerReceiver(wifiReciever, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                receiver_registered = true;
            }

            System.out.println("INIT_POSITION_THING -> ANTES DA CRIAÇAO DO HANDLER");

            //cena da posicao, comeca a correr ao fim de 2 segundos
            handler = new Handler();
            handler.postDelayed(runnable, 10000);
        }

        //obtem a posição inicial do utilizador
        getUserPosition();
    }

    public void initFragmentsAndTabs() {
        //calcular o maximo de features que teremos em qualquer momento no ecra
        int max_lenght1 = Math.max(bedroom_features.length, outside_general_features.length);
        int max_length2 = Math.max(kitchen_features.length, living_room_features.length);
        int max_length_final = Math.max(max_lenght1, max_length2);

        if (reset_fragment_list) {
            fragment_list = new ArrayList<Fragment>(max_length_final);
            reset_fragment_list = false;
        }

        refreshTabs();

        //activa o icon da barra e faz com que ele se comporte como um botão de toggle
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setIcon(android.R.color.transparent);
    }

    //cria uma conexao para o servidor.
    //envia uma tosta para alertar o utilizador do sucesso/insucesso da acção
    private void establishConnection(boolean display_toast) {
        trying_to_connect = true;

        try {
            System.out.println("VOU TENTAR LIGAR AO SERVER!!!");

            InetSocketAddress sockaddr = new InetSocketAddress(IP_ADDRESS, DEFAULT_PORT);
            client_socket = new Socket();
            client_socket.connect(sockaddr, 3000);
            client_socket.setSoTimeout(60000); //timeout -> 1 minuto

            System.out.println("CONSEGUI LIGAR AO SERVER, VAMOS CRIAR OS SOCKETS!!");

            obj_os = new ObjectOutputStream(client_socket.getOutputStream());
            obj_is = new ObjectInputStream(client_socket.getInputStream());

            if (display_toast)
                Toast.makeText(getApplicationContext(),
                        "Ligação ao servidor bem sucedida!", Toast.LENGTH_LONG).show();

            offline_mode = false;
        } catch (IOException e) {
            System.out.println("EXCEPTION!! Não é possível ligar ao server!!");

            offline_mode = true;
            if (display_toast)
                Toast.makeText(getBaseContext(),
                        "Falhou a ligação ao server. Modo offline", Toast.LENGTH_LONG).show();
        }
        connection_thread_finished = true;
    }

    //TODO placeholder!!! colocar aqui a obtenção da posição
    public void getUserPosition() {
        if (m_title == null)
            setTitle(KITCHEN);
        else
            setTitle(m_title);
    }

    public Home getHouse() {
        return house;
    }

    public void incrementHouseCounter() {
        this.house.incrementCounter();
    }

    //cria o output stream e envia um objecto com o estado da aplicação para o servidor
    //se estiver em modo offline nao faz nada.
    //se estiver em modo online, abre o socket e recebe um objecto do server
    public Home getObjectFromServer() {
        Home res_house = null;
        try {
            if (!offline_mode) {
                System.out.println("INPUT INPUT Estamos à espera de novas conexoes do server");
                res_house = (Home) obj_is.readObject();
                System.out.println("+++++++++++++++++++++++++++\n" +
                        "Objecto recebido do server!!!! Tamanho: " + res_house.getMap().size() +
                        "\n+++++++++++++++++++++++++++");
            }
            return res_house;
        } catch (InterruptedIOException e) {
            System.out.println("O servidor crashou!");
            offline_mode = true;
        } catch (EOFException e) {
            System.out.println("SocketTimeout do lado do servidor. Vamos abrir novamente!");
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            System.out.println("CLASS NOT FOUND EXCEPTION!!!");
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res_house;
    }

    //cria o output stream e envia um objecto com o estado da aplicação para o servidor
    //se estiver em modo offline nao faz nada.
    //se estiver em modo online, abre o socket e envia um objecto para o server
    public int sendObjectToServer(Home home) {
        if (!offline_mode) {
            try {
                System.out.println("COUNTER COUNTER COUNTER COUNTER COUNTER DA CASA: " +
                        home.getCounter());

                obj_os.writeObject(home);
                obj_os.flush();
                obj_os.reset();

                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    protected void onResume() {
        System.out.println("----------------ON_RESUME---------------");
        System.out.println("************************* WIFIRECIEDER IS NULL?? " +
                (wifiReciever == null));

        if (wifiReciever != null) {
            if (!disable_location_for_good) {
                registerReceiver(wifiReciever, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                receiver_registered = true;
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        System.out.println("----------------ON_PAUSE---------------");

        if (receiver_registered) {
            unregisterReceiver(wifiReciever);
            receiver_registered = false;
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        System.out.println("----------------ON_STOP---------------");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("----------------ON_DESTROY---------------");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshTabs() {
        System.out.println("RefreshTabs. Counter actual -> " +
                house.getCounter());

        if (pager != null) {
            if (current_fragment_tab == -1)
                current_fragment_tab = 0;
            else {
                current_fragment_tab = pager.getCurrentItem();
                System.out.println("------- Tab actual -> " + pager.getCurrentItem());
            }
        }

        System.out.println("********************************************************");
        System.out.println("**********FRAGMENT LIST SIZE (ANTES DO REMOVE): " +
                fragment_list.size() + "**************");

        Fragment frag;
        for (int i = 0; i < fragment_list.size(); i++) {
            frag = fragment_list.get(i);

            System.out.println("++++++++++++++FRAGMENT: " + frag.toString() + "+++++++++++++");
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }

        fragment_list = new ArrayList<Fragment>();

        System.out.println("**********FRAGMENT LIST SIZE (DEPOIS DO REMOVE): " +
                fragment_list.size() + "**************");
        System.out.println("********************************************************");

        System.out.println("**********REFRESH DAS TABS**************");

        System.out.println("PAGER IS NULL?? " + (pager == null));
        System.out.println("ADAPTER IS NULL?? " + (adapter == null));

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        app_fm = getSupportFragmentManager();
        adapter = new MyPagerAdapter(app_fm);

        pager.setAdapter(adapter);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        System.out.println("SÓ DEVIA VIR AQUI DEPOIS DOS FRAGMENTOS TODOS DESENHADINHOS HUEHUEHUE");
        pager.setCurrentItem(current_fragment_tab);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {

        outState.putInt(CURRENT_TAB, pager.getCurrentItem());

        System.out.println("-------------PÁGINA ACTUAL -> " + pager.getCurrentItem());
        System.out.println("ON_SAVE_INSTANCE_STATE");

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        System.out.println("---------------- orientation changed -------------------");
        canRedrawFrags = false;

//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v("menu_id", String.valueOf(id));

        if ((id == R.id.room_menu_bedroom) || (id == R.id.room_menu_outside_general) ||
                (id == R.id.room_menu_kitchen) || (id == R.id.room_menu_living_room)) {
            setTitle(item.getTitle());

            //Coloca o current_fragment_tab a -1 para o refreshtabs não alterar a variavel
            current_fragment_tab = -1;

            System.out.println("NOVO TITULO!!!!! - " + item.getTitle());

            refreshTabs();
        } else if (id == R.id.reconnect_submenu) {
            System.out.println("RECONECTAR AO SERVIDOR!!!");
            if (offline_mode) { //ainda nao estamos ligados, tentamos ligar!
//                Toast.makeText(getApplicationContext(), "Estabelecendo ligação ao servidor...",
//                        Toast.LENGTH_SHORT).show();

                //tenta ligar ao servidor
                establishConnection(true);

                //cria os sockets e a thread para receber mensagens do servidor
                if (!offline_mode)
                    handleConnection();
            } else
                Toast.makeText(getBaseContext(), "Já está ligado ao servidor!",
                        Toast.LENGTH_SHORT).show();
        } else if (id == R.id.disable_location_submenu) {
            if (disable_location_for_good)
                Toast.makeText(getApplicationContext(), "A localização já está desactivada",
                        Toast.LENGTH_SHORT).show();
            else {
                receiver_registered = false;
                disable_location_for_good = true;
                unregisterReceiver(wifiReciever);
                Toast.makeText(getApplicationContext(), "Localização desactivada",
                        Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }


    @Override
    public void setTitle(CharSequence title) {
        m_title = title;
        getActionBar().setTitle(m_title);
    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.v("page_title", String.valueOf(m_title));
            Log.v("frag_man", "TOU NO CONSTRUTOR DO MY_PAGER_ADAPTER");

            //só entra aqui quando mudamos de divisao!!!!
            canRedrawFrags = true;
//            pager.setCurrentItem(current_fragment_tab);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.v("page_item:", "PAGE_ITEM PAGE_ITEM");
            if (m_title != null) {
                if (m_title.toString().equals(OUTSIDE_GENERAL)) {
                    return outside_general_features[position];
                } else if (m_title.toString().equals(BEDROOM)) {
                    return bedroom_features[position];
                } else if (m_title.toString().equals(KITCHEN)) {
                    return kitchen_features[position];
                } else if (m_title.toString().equals(LIVING_ROOM)) {
                    return living_room_features[position];
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            if (m_title != null) {
                if (m_title.toString().equals(OUTSIDE_GENERAL)) {
                    return outside_general_features.length;
                } else if (m_title.toString().equals(BEDROOM)) {
                    return bedroom_features.length;
                } else if (m_title.toString().equals(KITCHEN)) {
                    return kitchen_features.length;
                } else if (m_title.toString().equals(LIVING_ROOM)) {
                    return living_room_features.length;
                }
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            Log.v("GET_ITEM:", String.valueOf(position));

            String feature = "";
            String chosen_arr;
            String[] temp_arr = new String[]{};

            if (m_title != null) {
                if (m_title.toString().equals(OUTSIDE_GENERAL)) {
                    temp_arr = outside_general_features;
                    feature = temp_arr[position];
                } else if (m_title.toString().equals(BEDROOM)) {
                    temp_arr = bedroom_features;
                    feature = temp_arr[position];
                } else if (m_title.toString().equals(KITCHEN)) {
                    temp_arr = kitchen_features;
                    feature = temp_arr[position];
                } else if (m_title.toString().equals(LIVING_ROOM)) {
                    temp_arr = living_room_features;
                    feature = temp_arr[position];
                }
            }

            chosen_arr = m_title.toString();

            Log.v("chosen_array", chosen_arr);
            Log.v("feature_name", feature);

            if (feature.equals(AIR_CONDITIONER)) {
                curr_fragment = AirConditionerFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(PANTRY_STOCK)) {
                curr_fragment = PantryStockFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(LIGHTS)) {
                curr_fragment = LightsFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(BLINDS)) {
                curr_fragment = BlindsFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(COFFEE_MACHINE)) {
                curr_fragment = CoffeeFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(STOVE_OVEN)) {
                curr_fragment = StoveFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(WATER_CONSUMPTION)) {
                curr_fragment = WaterMonitoringFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(POWER_CONSUMPTION)) {
                curr_fragment = PowerMonitoringFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(GAS_CONSUMPTION)) {
                curr_fragment = GasMonitoringFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else {
                curr_fragment = PageFragment.newInstance(position, temp_arr[position], chosen_arr);
            }

//            curr_fragment.setRetainInstance(true);

            fragment_list.add(curr_fragment);
//            app_fm.beginTransaction().add(curr_fragment, feature).commit();

            System.out.println("fragmento adicionado!!! tamanho da lista -> " + fragment_list.size());

            return curr_fragment;
        }
    }

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "A carregar...",
                    "Por favor espere enquanto tentamos ligar ao servidor...", false, false);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            /* This is where the code that is going to be executed on a background
             * thread must be placed.
             */

            //Get the current thread's token
            synchronized (this) {
                System.out.println("LOL TOU AQUI NA ASYNC_TASK");
                establishConnection(false);
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {

            //initialize the View
            setContentView(R.layout.activity_homepage);

            //cria a conexao para o server e tudo mais que esteja relaccionado
            handleConnection();

            //inicia a cena da posição
            initPositionThing();

            //inicia a cena das tabs e dos fragmentos!
            initFragmentsAndTabs();

            //close the progress dialog
            progressDialog.dismiss();
        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mainWifiObj.startScan();

            //volta a chamar este handler, dizendo que vai executar ao fim de 10s
            handler.postDelayed(this, 10000);
        }
    };


    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            double result;
            String bssid_room;

            int last_pos_kitchen = 0;
            int last_pos_bedroom = 0;
            int last_pos_livingroom = 0;

            System.out.println("ACABEI O SCAN, VOU CALCULAR AS DISTANCIAS!!");

            for (ScanResult res : wifiScanList) {

                //obtem a divisão a que corresponde o bssid
                bssid_room = getRoomByBSSID(res.BSSID);

                System.out.println("BSSID INFO -> (" + bssid_room + ", " + res.BSSID + ")");

                if ((bssid_room.equals(KITCHEN)) || (bssid_room.equals(BEDROOM)) ||
                        (bssid_room.equals(LIVING_ROOM))) {
                    result = calculateDistance(res.level, res.frequency);
                    ArrayList<Double> temp_list = results_map.get(bssid_room);

                    if (bssid_room.equals(KITCHEN)) {
                        temp_list.set(last_pos_kitchen, result);
                        last_pos_kitchen++;
                    } else if (bssid_room.equals(BEDROOM)) {
                        temp_list.set(last_pos_bedroom, result);
                        last_pos_bedroom++;
                    } else if (bssid_room.equals(LIVING_ROOM)) {
                        temp_list.set(last_pos_livingroom, result);
                        last_pos_livingroom++;
                    }
                }
            }

            //TODO: EFEITOS DE TESTE, REMOVER!!!
            //TODO: PODER DO MARTELO CARAAAAAAAAAAAAAAALHHHHOOOOOOOOOOOOOO!!!!!!!!!!!!!!!!!!!
            printScanResults();

            //calcula a divisao que se encontra mais perto do utilizador
            getClosestRoom();

            //limpa o mapa. já fica preparadinho para novo scan hehe :-)
            resetScanResultsMap();
        }

        //metodo de teste que imprime os valores do mapa que contem as distancias
        private void printScanResults() {
            System.out.println("++++++++++++++ RESULTADOS DA DISTANCIA ++++++++++++++");

            ArrayList<Double> temp_list;
            for (int index = 0; index < 3; index++) {
                if (index == 0) {
                    temp_list = results_map.get(KITCHEN);
                    System.out.println("------- COZINHA -------");
                } else if (index == 1) {
                    temp_list = results_map.get(BEDROOM);
                    System.out.println("------- QUARTO -------");
                } else {
                    temp_list = results_map.get(LIVING_ROOM);
                    System.out.println("------- SALA DE ESTAR -------");
                }

                String s = "";
                int i = 0;
                if (temp_list != null) {
                    System.out.println("TAMANHO DA LISTA -> " + temp_list.size());
                    for (Double d : temp_list) {
                        i++;
                        s += "AP " + i + ": " + d + "\n";
                    }
                    System.out.println(s);
                } else
                    System.out.println("NÃO TEM RESULTADOS!!!!\n");
            }

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }

        //dado um bssid, devolve a divisao a que ele corresponde
        private String getRoomByBSSID(String bssid) {
            String to_return = "";
            if (bssid.equalsIgnoreCase(BSSID_KITCHEN_1) || bssid.equalsIgnoreCase(BSSID_KITCHEN_2) ||
                    bssid.equalsIgnoreCase(BSSID_KITCHEN_3)) {
                to_return = KITCHEN;
            } else if (bssid.equalsIgnoreCase(BSSID_LIVING_ROOM_1) || bssid.equalsIgnoreCase(BSSID_LIVING_ROOM_2) ||
                    bssid.equalsIgnoreCase(BSSID_LIVING_ROOM_3)) {
                to_return = LIVING_ROOM;
            } else if (bssid.equalsIgnoreCase(BSSID_BEDROOM_1) || bssid.equalsIgnoreCase(BSSID_BEDROOM_2) ||
                    bssid.equalsIgnoreCase(BSSID_BEDROOM_3)) {
                to_return = BEDROOM;
            }
            return to_return;
        }

        //calcula a media das distancias e obtem a divisao onde o user se encontra
        //chama depois os metodos responsaveis por redesenhar os fragmentos
        public void getClosestRoom() {

            String closest;
            distance_to_ap_kitchen = computeMean(results_map.get(KITCHEN));
            distance_to_ap_bedroom = computeMean(results_map.get(BEDROOM));
            distance_to_ap_livingroom = computeMean(results_map.get(LIVING_ROOM));

            System.out.println("<<<<<<<<<<<< DISTANCIAS MÉDIAS A CADA DIVISAO >>>>>>>>>>>>>>");
            System.out.println("Cozinha -> " + distance_to_ap_kitchen + "\nQuarto -> " +
                    distance_to_ap_bedroom + "\nSala da estar -> " + distance_to_ap_livingroom);
            System.out.println("<<<<<<<<<<<<<<<<<< FINAL DAS DISTANCIAS >>>>>>>>>>>>>>>>>>>>");

            if (distance_to_ap_kitchen < distance_to_ap_bedroom &&
                    distance_to_ap_kitchen < distance_to_ap_livingroom) {
                closest = KITCHEN;
            } else if (distance_to_ap_bedroom < distance_to_ap_livingroom &&
                    distance_to_ap_bedroom < distance_to_ap_kitchen) {
                closest = BEDROOM;
            } else
                closest = LIVING_ROOM;

            Toast.makeText(getApplicationContext(), "Divisao mais perto: " + closest,
                    Toast.LENGTH_LONG).show();

            //TODO: esta merda encontra-se desactivada por enquanto
            //altera o titulo e redesenha os fragmentos se o user tiver mudado de posicao
            if (!last_position.equalsIgnoreCase(closest)) {
                last_position = closest;
                setTitle(closest);
                refreshTabs();
            }
        }

        //calcula a media dos elementos numa arraylist
        private double computeMean(ArrayList<Double> list) {
            double sum = 0;
            for (Double el : list) {
                sum += el;
            }
            return (sum / list.size());
        }

        //calcula a distancia ao AP com base nos dB e na frequencia
        public double calculateDistance(double levelInDb, double freqInMHz) {
            double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
            return Math.pow(10.0, exp);
        }
    }
}