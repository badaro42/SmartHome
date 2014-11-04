package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.badjoras.control.Home;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    public static final String OUTSIDE_GENERAL = "Exterior/Geral";
    public static final String KITCHEN = "Cozinha";
    public static final String BEDROOM = "Quarto";
    public static final String LIVING_ROOM = "Sala de Estar";

    private Socket client;
    private ObjectOutputStream obj_os;
    private ObjectInputStream obj_is;

    private Home house;

    private List<Fragment> fragment_list;

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

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private FragmentManager app_fm;

    private CharSequence m_title;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        house = new Home();

        try {
            client = new Socket("192.168.1.78", 4444);  //ip de casa

            //TODO descomentar aqui para obter do servidor o estado inicial da casa
//            obj_is = getInputStream();
//            obj_os = getOutputStream();
//            obj_os = getOutputStream();
//            house = (Home)obj_is.readObject();
//            obj_os.writeObject(house);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        //cria um intervalo para actualizar a posição do utilizador. alterar o intervalo!!!
//        timer = new Timer();
//        timer.schedule(new AlertTask(), 0, //initial delay
//                1 * 5000); //subsequent rate (in ms)

        //obtem a posição inicial do utilizador
        getUserPosition();

        //calcular o maximo de features que teremos em qualquer momento no ecra
        int max_lenght1 = Math.max(bedroom_features.length, outside_general_features.length);
        int max_length2 = Math.max(kitchen_features.length, living_room_features.length);
        int max_length_final = Math.max(max_lenght1, max_length2);

        fragment_list = new ArrayList<Fragment>(max_length_final);

        refreshTabs();

        //m_title = getTitle();

        //activa o icon da barra e faz com que ele se comporte como um botão de toggle
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setIcon(android.R.color.transparent);
//        getActionBar().setHomeButtonEnabled(true);
    }

    //TODO placeholder!!! colocar aqui a obtenção da posição
    public void getUserPosition() {
        setTitle(KITCHEN);
    }

    public Home getHouse() {
        return house;
    }

    public void modifyHouse(Home home) {
        this.house = home;
    }

    //cria o output stream e envia um objecto com o estado da aplicação para o servidor
    public int sendObjectToServer(Home home) {
        int result = 0;
        try {
            createOutputStream();
            obj_os.writeObject(home);
            result = 1;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }

    //TODO: talvez nao seja preciso ser public!!
    //cria (ou devolve, caso ja exista) um outputstream para comunicar com o server
    public void createOutputStream() {
        try {
            if(obj_os == null)
                obj_os = new ObjectOutputStream(client.getOutputStream());

//            return obj_os;

        } catch (IOException e) {
            e.printStackTrace();
//            return null;
        }
    }

    //cria (ou devolve, caso ja exista) um inputstream para comunicar com o server
    public ObjectInputStream getInputStream() {
        try {
            if(obj_is == null)
                obj_is = new ObjectInputStream(client.getInputStream());

            return obj_is;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshTabs() {

        for (Fragment frag : fragment_list) {
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }

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
            refreshTabs();
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(CharSequence title) {
        m_title = title;
        getActionBar().setTitle(m_title);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        //private final String[] TITLES = {"Ar Condicionado", "Stock da Dispensa", "Fogão", "Luzes", "Estores"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.v("page_title", String.valueOf(m_title));
            Log.v("frag_man", "TOU NO CONSTRUTOR DO MY_PAGER_ADAPTER");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.v("getpagetitle", "TOU NO GET_PAGE_TITLE DO MY_PAGER_ADAPTER");
            //Log.v("str_eq_bathroom", String.valueOf(m_title == BATHROOM));
            if (m_title != null) {
                if (m_title.toString().equals(OUTSIDE_GENERAL)) {
                    //Log.v("getpagetitle", "loli isto é uma casa de banho");
                    return outside_general_features[position];
                } else if (m_title.toString().equals(BEDROOM)) {
                    return bedroom_features[position];
                } else if (m_title.toString().equals(KITCHEN)) {
                    return kitchen_features[position];
                } else if (m_title.toString().equals(LIVING_ROOM)) {
                    return living_room_features[position];
                }
            }
            //return kitchen_features[position];
            return null;
        }

        @Override
        public int getCount() {
            //Log.v("getpagetitle", "TOU NO GET_PAGE_TITLE DO MY_PAGER_ADAPTER");
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
            //return kitchen_features.length;
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
//            else {
//                temp_arr = kitchen_features;
//                feature = temp_arr[position];
//            }

            chosen_arr = m_title.toString();

            Log.v("chosen_array", chosen_arr);
            Log.v("feature_name", feature);

            if (feature.equals(AIR_CONDITIONER)) {
                myfrag = AirConditionerFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(PANTRY_STOCK)) {
                myfrag = PantryStockFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else if (feature.equals(LIGHTS)) {
                myfrag = LightsFragment.newInstance(position, temp_arr[position], chosen_arr);
            } else {
                myfrag = AirConditionerFragment.newInstance(position, temp_arr[position], chosen_arr);
            }

            fragment_list.add(myfrag);
            return myfrag;
        }
    }


    class AlertTask extends TimerTask {
        int numWarningBeeps = 5;

        public void run() {
            if (numWarningBeeps > 0) {
                Log.v("Beep-test", "Beep Beep!!");
                numWarningBeeps--;
            } else {
                Log.v("Beep-test-last", "Last Beep Beep!!");
                timer.cancel(); //Not necessary because we call System.exit
//                System.exit(0); //Stops the AWT thread (and everything else)
            }
        }
    }

}