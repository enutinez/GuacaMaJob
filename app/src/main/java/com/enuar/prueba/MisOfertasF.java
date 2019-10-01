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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MisOfertasF extends Fragment {

    private TextView txtMisOfertas;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Resources res;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Oferta> ofertas;

    public MisOfertasF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mis_ofertas, container, false);
        res = getResources();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        txtMisOfertas = (TextView) vista.findViewById(R.id.txtMisOfertas);
        recyclerView = (RecyclerView) vista.findViewById(R.id.RVMOff);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ofertas = new ArrayList<>();
        obtenerOfertas();
        
        return vista;
    }

    private void obtenerOfertas() {

        myRef.child("Oferta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
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
                        if (u.getUid().equals(IDUsuario)){
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
            txtMisOfertas.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

}
