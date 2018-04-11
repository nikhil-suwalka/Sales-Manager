package com.nikhil.salesmanager;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements
        Home.OnFragmentInteractionListener,
        AddRemoveItems.OnFragmentInteractionListener,
        MonthlySales.OnFragmentInteractionListener,
        TopFive.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    View v;
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);




        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                // Change fragment on floating button click
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new AddRemoveItems());
                ft.commit();
                getSupportActionBar().setTitle("Add/Remove Items");
                navigationView.setCheckedItem(R.id.nav_addRemove);
                fab.hide();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        // Checks first item in navigation drawer initially
        navigationView.setCheckedItem(R.id.nav_home);


        // Open home fragment initially
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Home());
        ft.commit();
        getSupportActionBar().setTitle("Home");


        // Load items in Home Fragment


    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView todaysDate = (TextView) findViewById(R.id.todaysDate);
        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);
        String mm = new SimpleDateFormat("MMM").format(date.getTime());
        int dd = date.get(Calendar.DAY_OF_MONTH);

        String str = Integer.toString(dd) + " " + mm + " " + Integer.toString(yy);
        todaysDate.setText(str);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // Creating a Fragment object
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new Home();
            getSupportActionBar().setTitle("Home");
            fab.show();
        } else if (id == R.id.nav_addRemove) {
            fragment = new AddRemoveItems();
            getSupportActionBar().setTitle("Add/Remove Items");
            fab.hide();
        } else if (id == R.id.nav_monthlySales) {
            fragment = new MonthlySales();
            getSupportActionBar().setTitle("Monthly Sales");
            fab.hide();
        } else if (id == R.id.nav_topFive) {
            fragment = new TopFive();
            getSupportActionBar().setTitle("Top Five");
            fab.hide();
        }

        // Code for changing fragment
        if (fragment != null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
