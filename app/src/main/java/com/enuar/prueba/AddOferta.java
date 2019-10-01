package com.enuar.prueba;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddOferta extends AppCompatActivity {

    private EditText txtTitulo, txtPrecio, txtDirec, txtDescrip, txtPhone;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Spinner Temergencia;
    private String[] OpcEmerg;
    private Resources Res;
    private static final int GALLERY_INTENT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_oferta);
        Res = this.getResources();
        Temergencia = (Spinner)findViewById(R.id.Temergencia);
        txtTitulo = (EditText)findViewById(R.id.txtTituloOf);
        txtPrecio = (EditText)findViewById(R.id.txtPrecio);
        txtDirec = (EditText)findViewById(R.id.txtDireccion);
        txtDescrip = (EditText)findViewById(R.id.txtDescripcion);
        txtPhone = (EditText)findViewById(R.id.txtCell);
        OpcEmerg = Res.getStringArray(R.array.emergencias);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_dropdown_layout,OpcEmerg);
        Temergencia.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    
    public void GuardarDB (View view){
        String Titulo, Direccion, Descripcion, Celular, P2;
        float Precio;
        String TipoEmergencia;
        Titulo = txtTitulo.getText().toString();
        Direccion = txtDirec.getText().toString();
        Descripcion = txtDescrip.getText().toString();
        Celular = txtPhone.getText().toString();
        Precio = Float.parseFloat(txtPrecio.getText().toString());
        P2 = txtPrecio.getText().toString();
        TipoEmergencia = Temergencia.getSelectedItem().toString();
        if (Titulo.equals("") || Direccion.equals("") || Descripcion.equals("") || Celular.equals("") || P2.equals("") || TipoEmergencia.equals("")){
            verificar(Titulo,Direccion,Descripcion,Celular,P2);
        }else{

            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            if (u != null){
                String ID = u.getUid();
                Oferta o = new Oferta(Titulo,Descripcion,Direccion,Celular, ID,Precio,TipoEmergencia);
                myRef.child("Oferta").push().setValue(o);
                limpiar();
                goScreenMain();
            } else {
                Toast.makeText(this, "Ha ocurrido un problema con la sesion.", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void goScreenMain() {
    Intent i = new Intent(AddOferta.this, CenterNav.class);
    startActivity(i);
    finish();
    }

    private void verificar(String titulo, String direccion, String descripcion, String celular, String precio) {
        if (titulo.equals("")){ txtTitulo.setError("*");  }
        if (direccion.equals("")){ txtDirec.setError("*");  }
        if (descripcion.equals("")){ txtDescrip.setError("*");  }
        if (celular.equals("")){ txtPhone.setError("*");  }
        if (precio.equals("") ){ txtPrecio.setError("*");  }
    }

    private void limpiar() {
        txtTitulo.setText("");
        txtPrecio.setText("");
        txtDirec.setText("");
        txtDescrip.setText("");
        txtPhone.setText("");

    }


}

