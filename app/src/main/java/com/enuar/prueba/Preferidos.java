package com.enuar.prueba;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Preferidos extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView txtBuscar;
    private EditText txtPalabra;
    private ImageButton btnBuscar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Oferta> ofertas;

    public Preferidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_preferidos, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        txtPalabra = (EditText)vista.findViewById(R.id.txtPalabra);
        txtBuscar = (TextView) vista.findViewById(R.id.txtBuscar);
        btnBuscar = (ImageButton)vista.findViewById(R.id.btnBuscar);
        recyclerView = (RecyclerView) vista.findViewById(R.id.RVBuscar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ofertas = new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Palabra = txtPalabra.getText().toString();
                if (Palabra.equals("")){
                    txtPalabra.setError("*");
                    Toast.makeText(getContext(), "Ingrese palabra clave.", Toast.LENGTH_SHORT).show();
                } else {
                    obtenerOfertas(Palabra);
                }
            }
        });

        return vista;
    }

    public void obtenerOfertas(final String key){

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
                        if (Titulo.indexOf(key) != -1){
                            ofertas.add(o);
                        }
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
            txtBuscar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

}
