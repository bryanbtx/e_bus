package com.e_bus.e_bus;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changeScreen(R.id.menu);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return changeScreen(item.getItemId());
    }
    public boolean changeScreen(int id){
        if (id == R.id.menu) {
            fragment = new Menu();
        } else if (id == R.id.comprar) {
            fragment = new Comprar();
        } else if (id == R.id.histC) {
            fragment = new HistCompras();
        } else if (id == R.id.histU) {
            fragment = new HistUtilizacao();
        } else if (id == R.id.configuracoes) {
            fragment = new Configuracao();
        }

        if (fragment != null) {
            FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
            frag.replace(R.id.content_frame, fragment);
            frag.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
