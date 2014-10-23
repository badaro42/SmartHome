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

public class MainActivity extends FragmentActivity {

    private static final String AIR_CONDITIONER = "Ar Condicionado";
    private static final String PANTRY_STOCK = "Stock da Despensa";
    private static final String LIGHTS = "Iluminação";
    private static final String BLINDS = "Estores";
    private static final String POWER_MONITORING = "Monitorização Energética";
    private static final String GARAGE_DOOR = "Portão da Garagem";
    private static final String SURVEILLANCE_CAMERAS = "Câmaras de Vigilância";
    private static final String SCHEDULED_FUNCTIONS = "Tarefas Agendadas";
    private static final String COFFEE_MACHINE = "Máquina de Café";
    private static final String STOVE_OVEN = "Fogão/Forno";
    private static final String SPRINKLER = "Aspersores da Rega";

    private static final String BATHROOM = "Bathroom";
    private static final String KITCHEN = "Kitchen";
    private static final String BEDROOM = "Bedroom";
    private static final String LIVING_ROOM = "Living Room";

    //TODO rever estas features mais tarde
    //por enquanto estão à toa apenas para efeitos de teste
    private String[] kitchen_features = new String[]{
            PANTRY_STOCK, AIR_CONDITIONER, LIGHTS, BLINDS, COFFEE_MACHINE, STOVE_OVEN};

    private String[] bedroom_features = new String[]{
            POWER_MONITORING, AIR_CONDITIONER, LIGHTS, BLINDS, GARAGE_DOOR, SURVEILLANCE_CAMERAS};

    private String[] bathroom_features = new String[]{
            SPRINKLER, AIR_CONDITIONER, LIGHTS, BLINDS, GARAGE_DOOR, SCHEDULED_FUNCTIONS, STOVE_OVEN};

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private FragmentManager app_fm;

    private CharSequence m_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        refreshTabs();

        m_title = getTitle();

        //activa o icon da barra e faz com que ele se comporte como um botão de toggle
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    /**
     * //     * Diplaying fragment view for selected nav drawer list item
     * //
     */
//    private void displayView(int position) {
//        // update the main content by replacing fragments
//        android.app.Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new Blinds();
//                break;
//            case 1:
//                fragment = new AirConditioner();
//                break;
//            case 2:
//                fragment = new Lights();
//                break;
//            case 3:
//                fragment = new PantryStock();
//                break;
//
//            default:
//                break;
//        }
//
//        if (fragment != null) {
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
//
//            // update selected item and title, then close the drawer
//            m_drawer_list.setItemChecked(position, true);
//            m_drawer_list.setSelection(position);
//            setTitle(nav_menu_titles[position]);
//            m_drawer_layout.closeDrawer(m_drawer_list);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshTabs() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTextSize(28);
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

        if (id == R.id.room_menu_bathroom) {
            setTitle(item.getTitle());
            Log.v("onoptionsselected", "TOU NO ON_OPTIONS_SELECTED");
            Log.v("onoptionsselected", String.valueOf(item.getTitle()));

//            finish();
//            startActivity(getIntent());

            refreshTabs();
            return true;
        }
        else if (id == R.id.room_menu_bedroom) {
            setTitle(item.getTitle());

//            finish();
//            startActivity(getIntent());

            refreshTabs();
            return true;
        }
        else if (id == R.id.room_menu_kitchen) {
            setTitle(item.getTitle());

//            finish();
//            startActivity(getIntent());

            refreshTabs();
            return true;
        }
        else if (id == R.id.room_menu_living_room) {
            setTitle(item.getTitle());

//            finish();
//            startActivity(getIntent());

            refreshTabs();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            if(m_title != null) {
                if (m_title.toString().equals(BATHROOM)) {
                    Log.v("getpagetitle", "loli isto é uma casa de banho");
                    return bathroom_features[position];
                } else if (m_title.toString().equals(BEDROOM))
                    return bedroom_features[position];
                else
                    return kitchen_features[position];
            }
            return kitchen_features[position];
        }

        @Override
        public int getCount() {
            //Log.v("getpagetitle", "TOU NO GET_PAGE_TITLE DO MY_PAGER_ADAPTER");
            if(m_title != null) {
                if (m_title.toString().equals(BATHROOM))
                    return bathroom_features.length;
                else if (m_title.toString().equals(BEDROOM))
                    return bedroom_features.length;
                else
                    return kitchen_features.length;
            }
            return kitchen_features.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.v("GET_ITEM:", String.valueOf(position));

//            Fragment myfrag = null;
            String feature;
            String chosen_arr;
            String[] temp_arr;

            if(m_title != null) {
                if (m_title.toString().equals(BATHROOM)) {
                    temp_arr = bathroom_features;
                    feature = temp_arr[position];
                } else if (m_title.toString().equals(BEDROOM)) {
                    temp_arr = bedroom_features;
                    feature = temp_arr[position];
                } else { //falta o caso da sala de estar. ou seja, se for cozinha ou sala de estar entra aqui
                    temp_arr = kitchen_features;
                    feature = temp_arr[position];
                }
            }
            else {
                temp_arr = kitchen_features;
                feature = temp_arr[position];
            }

            chosen_arr = m_title.toString();

            Log.v("chosen_array", chosen_arr);
            Log.v("feature_name", feature);

            if (feature.equals(AIR_CONDITIONER))
                return AirConditioner.newInstance(position, temp_arr[position]);
//                myfrag = AirConditioner.newInstance(position, temp_arr[position]);
            else if (feature.equals(PANTRY_STOCK))
                return PantryStock.newInstance(position, temp_arr[position]);
//                myfrag = PantryStock.newInstance(position, temp_arr[position]);
            else
                return PageFragment.newInstance(position, temp_arr[position]);
//                myfrag = PageFragment.newInstance(position, temp_arr[position]);

//            app_fm.beginTransaction().replace(R.id.pager, myfrag).commit();
//            return myfrag;
        }
    }
}