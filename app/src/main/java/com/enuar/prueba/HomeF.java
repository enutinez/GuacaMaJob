package com.enuar.prueba;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeF extends Fragment {
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView txtHome;

    private Resources res;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Oferta> ofertas;

    public HomeF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        res = getResources();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        txtHome = (TextView) vista.findViewById(R.id.txtHome);
        recyclerView = (RecyclerView) vista.findViewById(R.id.RVHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ofertas = new ArrayList<>();

        obtenerOfertas();


        return vista;

    }

    public void obtenerOfertas(){

        myRef.child("Oferta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if (dataSnapshot.exists()){
                   for (DataSnapshot ds : dataSnapshot.getChildren()){
                       String Titulo = ds.child("titulo").getValue().toString();
                       String Direccion = ds.child("direccion").getValue().toString();
                       String Descripcion = ds.child("descripcion").getValue().toString();
                       String Celular = ds.child("cellphone").getValue().toString();
                       float Precio = Float.parseFloat(ds.child("precio").getValue().toString());
                       String tipoEmer = ds.child("tipoEmergencia").getValue().toString();
                       String IDUsuario = ds.child("idusuario").getValue().toString();
                       Oferta o = new Oferta(Titulo,Descripcion,Direccion,Celular,IDUsuario,Precio,tipoEmer);
                       ofertas.add(o);
                   }

                   listaVisible();
                   recyclerViewAdapter = new RecyclerViewAdapter(ofertas);
                   recyclerView.setAdapter(recyclerViewAdapter);

               }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    private void listaVisible() {
        if ( ofertas.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            txtHome.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }
}
