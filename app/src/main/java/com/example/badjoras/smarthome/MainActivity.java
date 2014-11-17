package com.example.badjoras.smarthome;

import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.badjoras.control.Home;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class MainActivity extends FragmentActivity {

    public static final String AIR_CONDITIONER = "Ar Condicionado";
    public static final String PANTRY_STOCK = "Stock da Despensa";
    public static final String LIGHTS = "Iluminação";
    public static final String BLINDS = "Estores";
    public static final String POWER_MONITORING = "Monitorização Energética";
    public static final String GARAGE_DOOR = "Portão da Garagem";
    public static final String SURVEILLANCE_CAMERAS = "Câmaras de Vigilância";
    public static final String SCHEDULED_FUNCTIONS = "Tarefas Agendadas";
    public static final String COFFEE_MACHINE = "Máquina de Café";
    public static final String STOVE_OVEN = "Fogão/Forno";
    public static final String SPRINKLER = "Aspersores da Rega";
    public static MediaPlayer cafe;

    public static final String OUTSIDE_GENERAL = "Exterior/Geral";
    public static final String KITCHEN = "Cozinha";
    public static final String BEDROOM = "Quarto";
    public static final String LIVING_ROOM = "Sala de Estar";

    //USAR UM DESTES IPs
    public static final String IP_ADDRESS = "10.171.240.101"; //ip fac canteiro
//    public static final String IP_ADDRESS = "10.22.107.150"; //ip fac badaro
//    public static final String IP_ADDRESS = "192.168.2.2"; //ip casa badaro
 //   public static final String IP_ADDRESS = "192.168.46.1"; //ip casa badaro
//    public static final String IP_ADDRESS = "10.171.110.142"; //ip casa badaro
//    public static final String IP_ADDRESS = "10.171.239.99"; //ip casa badaro
    public static final int DEFAULT_PORT = 4444;

    //TODO: colocar aqui os ids dos AP mais perto de cada sala
    public static final String BSSID_1 = "AP1";
    public static final String BSSID_2 = "AP2";
    public static final String BSSID_3 = "AP3";

    public static String last_position;

    private Socket client;
    private ObjectOutputStream obj_os;
    private ObjectInputStream obj_is;

    private Home house = new Home();

    public static List<Fragment> fragment_list;

    //TODO rever estas features mais tarde
    private String[] kitchen_features = new String[]{
            PANTRY_STOCK, AIR_CONDITIONER, LIGHTS, BLINDS, COFFEE_MACHINE, STOVE_OVEN
    };

    private String[] bedroom_features = new String[]{ //TODO acrescentar mais??
            AIR_CONDITIONER, LIGHTS, BLINDS
    };

    private String[] outside_general_features = new String[]{
            SPRINKLER, GARAGE_DOOR, SURVEILLANCE_CAMERAS, POWER_MONITORING, SCHEDULED_FUNCTIONS
    };

    private String[] living_room_features = new String[]{
            LIGHTS, BLINDS, AIR_CONDITIONER
    };

//    private String[] outside_general_features = new String[]{
//            SPRINKLER, GARAGE_DOOR, SURVEILLANCE_CAMERAS, POWER_MONITORING, SCHEDULED_FUNCTIONS
//    };

//    public static boolean firstTime = true;

    public static PagerSlidingTabStrip tabs;
    public static ViewPager pager;
    public static MyPagerAdapter adapter;
    public static FragmentManager app_fm;

    public static CharSequence m_title;

    private Timer timer;

    private WifiManager mainWifiObj;
    //    private WifiScanReceiver wifiReciever;
    private ListView list;
    private String wifis[];
    private HashMap<String, ArrayList<Double>> results_map;

    public double distance_to_ap1;
    public double distance_to_ap2;
    public double distance_to_ap3;

    public boolean connected_to_server;

    public int wifiScanCount;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("******ON_CREATE DA MAIN ACTIVITY!!!******");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


//        house = new Home();

        try {
            last_position = "";
            client = null;

//            results_map = new HashMap<String, ArrayList<Double>>(30);
//            mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//            wifiReciever = new WifiScanReceiver();

            //            client = new Socket(IP_ADDRESS, DEFAULT_PORT); //ip casa

            establishConnection();

//            house = new Home();

            System.out.println("********* ESTOU LIGADO AO SERVER???? " + connected_to_server);
            System.out.println(client.toString());

            //se conseguimos ligar ao servidor, obtem o estado actual do servidor
            if (connected_to_server) {
                obj_os = new ObjectOutputStream(client.getOutputStream());
                obj_is = new ObjectInputStream(client.getInputStream());


                System.out.println("11111 - SOCKET IS BOUND??" + client.isBound());
                System.out.println("11111 - SOCKET IS CLOSED??" + client.isClosed());
                System.out.println("11111 - SOCKET IS CONNECTED??" + client.isConnected());


                System.out.println("CRIEI UMA CASA NOVA!!!! HEHEHEHEHE\n Counter:" + house.getCounter());

                //fazemos um "fake send" para o server nos enviar o objeto que ele tem
                sendObjectToServer(house, false);

                System.out.println("22222 - SOCKET IS BOUND??" + client.isBound());
                System.out.println("22222 - SOCKET IS CLOSED??" + client.isClosed());
                System.out.println("22222 - SOCKET IS CONNECTED??" + client.isConnected());

                System.out.println("***enviei obj para o server, espero pela resposta***");

                //obtemos o estado do server, para o caso de reiniciarmos a aplicação
                Home temp_house = getObjectFromServer();
                if (temp_house != null)
                    house = temp_house;

                obj_os.close();
                obj_is.close();
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //cria um intervalo para actualizar a posição do utilizador. alterar o intervalo!!!
//        timer = new Timer();
//        timer.schedule(new AlertTask(), 0, //initial delay
//                1 * 3000); //subsequent rate (in ms)

        //cena da posicao, comeca a correr ao fim de 2 segundos
//        handler = new Handler();
//        handler.postDelayed(runnable, 2000);


        //obtem a posição inicial do utilizador
        getUserPosition();

        //calcular o maximo de features que teremos em qualquer momento no ecra
        int max_lenght1 = Math.max(bedroom_features.length, outside_general_features.length);
        int max_length2 = Math.max(kitchen_features.length, living_room_features.length);
        int max_length_final = Math.max(max_lenght1, max_length2);

        fragment_list = new ArrayList<Fragment>(max_length_final);

        refreshTabs();

        //activa o icon da barra e faz com que ele se comporte como um botão de toggle
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setIcon(android.R.color.transparent);
    }


    private void establishConnection() {
        Toast t;
        try {
            SocketAddress sockaddr = new InetSocketAddress(IP_ADDRESS, DEFAULT_PORT);
            // Create your socket
            client = new Socket();
            // Connect with 2 s timeout
            client.connect(sockaddr, 2000);

            t = Toast.makeText(getBaseContext(), "Ligação ao servidor bem sucedida!", Toast.LENGTH_LONG);
            t.show();
            connected_to_server = true;
        } catch (IOException e) {
            e.printStackTrace();
            t = Toast.makeText(getBaseContext(), "Falhou a ligação ao server. Modo offline", Toast.LENGTH_LONG);
            t.show();
        }
    }


//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            wifiScanCount = 0;
//            //ja fez 2 scans. assim que fizer o terceiro, calcula a media
//            if (wifiScanCount % 3 == 2) {
//                System.out.println("Vou fazer o 3º scan! Dps disto calculo a media");
//                mainWifiObj.startScan();
//            } else {
//                System.out.println("Vou fazer novo scan da rede!!!\n" +
//                        "Estou a fazer o scan " + (wifiScanCount + 1) % 3 + " de 3");
//                mainWifiObj.startScan();
//            }
//            wifiScanCount++;
//
//            //volta a chamar este handler, dizendo que vai executar ao fim de 3000ms
//            handler.postDelayed(this, 3000);
//        }
//    };


    //TODO: apenas para teste, remover!!
    public boolean isOutputStreamOpen() {
        return obj_os == null;
    }

    //TODO: apenas para teste, remover!!
    public boolean isClientSocketOpen() {
        return client == null;
    }


    //TODO placeholder!!! colocar aqui a obtenção da posição
    public void getUserPosition() {
        if(m_title == null)
            setTitle(KITCHEN);
        else
            setTitle(m_title);
    }

    public Home getHouse() {
        return house;
    }

    public void modifyHouse(Home home) {
        this.house = home;
    }

    //cria o output stream e envia um objecto com o estado da aplicação para o servidor
    public Home getObjectFromServer() {
        Home res_house = null;
        try {
            System.out.println("33333 - SOCKET IS BOUND??" + client.isBound());
            System.out.println("33333 - SOCKET IS CLOSED??" + client.isClosed());
            System.out.println("33333 - SOCKET IS CONNECTED??" + client.isConnected());

            System.out.println("****criei o inputstream, fico à espera do server****");

            res_house = (Home) obj_is.readObject();

            System.out.println("+++++++++++++++++++++++++++\n" +
                    "Objecto recebido do server!!!! Tamanho: " + res_house.getMap().size() +
                    "\n+++++++++++++++++++++++++++");

            return res_house;
        } catch (IOException e) {
            e.printStackTrace();
            return res_house;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return res_house;
        }
    }

    //cria o output stream e envia um objecto com o estado da aplicação para o servidor
    public int sendObjectToServer(Home home, boolean closeConnection) {
        int result = 0;
        try {
            if (connected_to_server) {
                if (closeConnection) {
                    System.out.println("***************ANTES DE CRIAR O SOCKET*****************");
                    client = new Socket(IP_ADDRESS, DEFAULT_PORT); //ip casa
                    System.out.println("***************ANTES DE ABRIR O OUTPUTSTREAM*****************");
                    obj_os = new ObjectOutputStream(client.getOutputStream());
                    obj_is = new ObjectInputStream(client.getInputStream());
                    System.out.println("***************TUDO ABERTO, PROSSIGA!*****************");

                    System.out.println("44444 - SOCKET IS BOUND??" + client.isBound());
                    System.out.println("44444 - SOCKET IS CLOSED??" + client.isClosed());
                    System.out.println("44444 - SOCKET IS CONNECTED??" + client.isConnected());
                }

                obj_os.writeObject(home);
                obj_os.flush();
                home.incrementCounter();

                if (closeConnection) {
                    System.out.println("jejejeje vou fechar o socket :D :D");
                    obj_is.close();
                    obj_os.close();
                    client.close();
                }
            }
            result = 1;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }

    protected void onResume() {
        System.out.println("----------------ON_RESUME---------------");
//        Toast.makeText(getBaseContext(), "resumeindo :D", Toast.LENGTH_LONG);
//        Log.v("ScanResults ONRESUME", "ON RESUME CARALHO");
//
//        registerReceiver(wifiReciever, new IntentFilter(
//                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
//        refreshTabs();
    }

    @Override
    protected void onPause() {
        System.out.println("----------------ON_PAUSE---------------");
//        try {
//            Toast.makeText(getBaseContext(), "ESTOU A PARAAR!!! :D", Toast.LENGTH_LONG);
//            Log.v("ScanResults ONPAUSE", "ON PAUSE CARALHO");

//            unregisterReceiver(wifiReciever);
            super.onPause();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

        System.out.println("**********FRAGMENT LIST SIZE: " + fragment_list.size() + "**************");

        for (Fragment frag : fragment_list) {
            System.out.println("++++++++++++++FRAGMENT: " + frag.toString() + "+++++++++++++");
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }

        fragment_list = new ArrayList<Fragment>();

        System.out.println("**********REFRESH DAS TABS**************");

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        app_fm = getSupportFragmentManager();
        adapter = new MyPagerAdapter(app_fm);

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
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

            System.out.println("NOVO TITULO!!!!! - " + item.getTitle());

            refreshTabs();
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
//                    System.out.println("KITCHEN KITCHEN KITCHEN");
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
        public Fragment getItem(int position) {
            Log.v("GET_ITEM:", String.valueOf(position));

            Fragment myfrag = null;
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
                myfrag = AirConditionerFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(PANTRY_STOCK)) {
                myfrag = PantryStockFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(LIGHTS)) {
                myfrag = LightsFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(BLINDS)) {
                myfrag = BlindsFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(COFFEE_MACHINE)) {
                myfrag = CoffeeFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(STOVE_OVEN)) {
                myfrag = StoveFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else {
                myfrag = PageFragment.newInstance(position, temp_arr[position], chosen_arr);
            }

            fragment_list.add(myfrag);
            return myfrag;
        }
    }


//    class AlertTask extends TimerTask {
//
//        public void run() {
//            wifiScanCount = 0;
//            //ja fez 2 scans. assim que fizer o terceiro, calcula a media
//            if (wifiScanCount % 3 == 2) {
//                System.out.println("Vou fazer o 3º scan! Dps disto calculo a media");
//                mainWifiObj.startScan();
//            }
//            else {
//                System.out.println("Vou fazer novo scan da rede!!!\n" +
//                        "Estou a fazer o scan " + (wifiScanCount+1)%3 + " de 3");
//                mainWifiObj.startScan();
//            }
//            wifiScanCount++;
////            else {
////                System.out.println("Já tenho tres scans");
////                wifiScanCount
////                timer.cancel(); //Not necessary because we call System.exit
////                System.exit(0); //Stops the AWT thread (and everything else)
////            }
//        }
//    }

//    class WifiScanReceiver extends BroadcastReceiver {
//        @SuppressLint("UseValueOf")
//        public void onReceive(Context c, Intent intent) {
//            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
//            wifis = new String[wifiScanList.size()];
//            double result;
//
//            for (ScanResult res : wifiScanList) {
//                if (res.BSSID.equalsIgnoreCase(BSSID_1) ||
//                        res.BSSID.equalsIgnoreCase(BSSID_2) ||
//                        res.BSSID.equalsIgnoreCase(BSSID_3)) {
//                    result = calculateDistance(res.level, res.frequency);
//                    ArrayList<Double> temp_list = results_map.get(res.BSSID);
//                    if (temp_list == null) {
//                        temp_list = new ArrayList<Double>();
//                        temp_list.add(result);
//                        results_map.put(res.BSSID, temp_list);
//                    } else
//                        temp_list.add(result);
//                }
//            }
//
//            if (wifiScanCount % 3 == 2) {
//                getClosestRoom();
//            }
//
//            Toast t = Toast.makeText(c, "Teste concluído hehe :D", Toast.LENGTH_LONG);
//            t.show();
//        }
//
//        //calcula a media das distancias e obtem a divisao onde o user se encontra
//        //chama depois os metodos responsaveis por redesenhar os fragmentos
//        public void getClosestRoom() {
//
//            String closest;
//
//            distance_to_ap1 = computeMean(results_map.get(BSSID_1));
//            distance_to_ap2 = computeMean(results_map.get(BSSID_2));
//            distance_to_ap3 = computeMean(results_map.get(BSSID_3));
//
//            if (distance_to_ap1 < distance_to_ap2 &&
//                    distance_to_ap1 < distance_to_ap3) {
//                closest = KITCHEN;
//            } else if (distance_to_ap2 < distance_to_ap3 &&
//                    distance_to_ap2 < distance_to_ap1) {
//                closest = BEDROOM;
//            } else
//                closest = LIVING_ROOM;
//
//            //altera o titulo e redesenha os fragmentos se o user tiver mudado de posicao
//            if (!last_position.equalsIgnoreCase(closest)) {
//                last_position = closest;
//                setTitle(closest);
//                refreshTabs();
//            }
//        }
//
//
//        public double computeMean(ArrayList<Double> list) {
//            int sum = 0;
//            for (Double el : list) {
//                sum += el;
//            }
//            return sum / list.size();
//        }
//
//        public double calculateDistance(double levelInDb, double freqInMHz) {
//            double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
//            return Math.pow(10.0, exp);
//        }
//    }
}