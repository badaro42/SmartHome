package com.example.badjoras.smarthome;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private CharSequence m_title;

    private DrawerLayout m_drawer_layout;
    private ListView m_drawer_list;
    private ActionBarDrawerToggle m_drawer_toggle;

    // nav drawer title
    private CharSequence m_drawer_title;

    // slide menu items
    private String[] nav_menu_titles;
    private TypedArray nav_menu_icons;

    private ArrayList<NavDrawerItem> nav_drawer_items;
    private NavDrawerListAdapter nav_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        m_drawer_title = getTitle();
        m_title = getTitle();

        //popula o array com os itens do slider
        nav_menu_titles = getResources().getStringArray(R.array.nav_drawer_items);

        //popula o array com os icons dos itens do drawer
        //TODO alterar os icones para outros mais elucidativos
        nav_menu_icons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        m_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_drawer_list = (ListView) findViewById(R.id.list_slidermenu);


        nav_drawer_items = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Cozinha
        nav_drawer_items.add(new NavDrawerItem(nav_menu_titles[0], nav_menu_icons.getResourceId(0, -1)));
        // Casa de banho
        nav_drawer_items.add(new NavDrawerItem(nav_menu_titles[1], nav_menu_icons.getResourceId(1, -1)));
        // Sala
        nav_drawer_items.add(new NavDrawerItem(nav_menu_titles[2], nav_menu_icons.getResourceId(2, -1)));
        // Quarto
        nav_drawer_items.add(new NavDrawerItem(nav_menu_titles[3], nav_menu_icons.getResourceId(3, -1)));

        nav_menu_icons.recycle();
        m_drawer_list.setOnItemClickListener(new SlideMenuClickListener());

        //criamos um listener para o adapter da lista de navegação
        nav_adapter = new NavDrawerListAdapter(getApplicationContext(),
                nav_drawer_items);
        m_drawer_list.setAdapter(nav_adapter);

        //activa o icon da barra e faz com que ele se comporte como um botão de toggle
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        m_drawer_toggle = new ActionBarDrawerToggle(this, m_drawer_layout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(m_title);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(m_drawer_title);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        m_drawer_layout.setDrawerListener(m_drawer_toggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Kitchen();
                break;
            case 1:
                fragment = new Bathroom();
                break;
            case 2:
                fragment = new LivingRoom();
                break;
            case 3:
                fragment = new Bedroom();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            m_drawer_list.setItemChecked(position, true);
            m_drawer_list.setSelection(position);
            setTitle(nav_menu_titles[position]);
            m_drawer_layout.closeDrawer(m_drawer_list);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
        if (m_drawer_toggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = m_drawer_layout.isDrawerOpen(m_drawer_list);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void setTitle(CharSequence title) {
        m_title = title;
        getActionBar().setTitle(m_title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        m_drawer_toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        m_drawer_toggle.onConfigurationChanged(newConfig);
    }
}