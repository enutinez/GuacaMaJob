package com.enuar.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CenterNav extends AppCompatActivity {

    private BottomNavigationView bttnNav;
    private HomeF homeF;
    private MisOfertasF misOfertasF;
    private Perfil perfil;
    private Preferidos preferidos;
    private FloatingActionButton FabAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_nav);
        bttnNav = (BottomNavigationView)findViewById(R.id.bttnNav);
        FabAdd = (FloatingActionButton)findViewById(R.id.FabAdd);

        homeF = new HomeF();
        misOfertasF = new MisOfertasF();
        perfil = new Perfil();
        preferidos = new Preferidos();

        bttnNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.it_home){
                    FabAdd.setVisibility(View.VISIBLE);
                    setFragment(homeF);
                    return true;
                } else
                if (id == R.id.it_favoritos){
                    FabAdd.setVisibility(View.INVISIBLE);
                    setFragment(preferidos);
                    return true;
                } else
                if (id == R.id.it_MOfertas){
                    FabAdd.setVisibility(View.VISIBLE);
                    setFragment(misOfertasF);
                    return true;
                } else
                if (id == R.id.it_perfil){
                    FabAdd.setVisibility(View.INVISIBLE);
                    setFragment(perfil);
                    return true;
                }

                return false;
            }
        });

        bttnNav.setSelectedItemId(R.id.it_home);

        FabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CenterNav.this,AddOferta.class);
                startActivity(i);
            }
        });


    }

    private void setFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}
