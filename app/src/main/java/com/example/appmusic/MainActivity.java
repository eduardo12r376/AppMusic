package com.example.appmusic;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmusic.entidades.Artista;
import com.example.appmusic.interfaces.iPasarDetallesArtista;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iPasarDetallesArtista{


    PerfilContratacion perfilContratacion;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void enviarPersona(Artista artista) {
        perfilContratacion = new PerfilContratacion();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto",artista);
        perfilContratacion.setArguments(bundleEnvio);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.idcontainer,perfilContratacion);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}