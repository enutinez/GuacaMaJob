package com.enuar.prueba;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private LoginManager lMan;

    private Button btn;
    private TextView Nombre, Email, UID;
    private ImageView imgPerfil;
    private GoogleApiClient googleApiClient;


    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vista2 = inflater.inflate(R.layout.fragment_perfil, container, false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        lMan = LoginManager.getInstance();

        btn = (Button)vista2.findViewById(R.id.btn);
        Nombre = (TextView)vista2.findViewById(R.id.txtNombre);
        Email   = (TextView)vista2.findViewById(R.id.txtEmail);
        UID = (TextView)vista2.findViewById(R.id.txtUID);
        imgPerfil = (ImageView)vista2.findViewById(R.id.imgPerfil);

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

        if (User != null){
            LlenarCampos();
        } else{
            goLoginScreen();
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                lMan.logOut();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                            Toast.makeText(getActivity(), "Vuelve pronto.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "No se pudo cerrar sesion.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        //*********************************PERFIL GOOGLE*****************************************************
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestId().requestProfile().build();

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        return vista2;
    }

    private void goLoginScreen() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
           LlenarCampos();
        }else{
            goLoginScreen();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void LlenarCampos(){

        FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();
        Nombre.setText(U.getDisplayName());
        Email.setText(U.getEmail());
        UID.setText(U.getUid());
        Glide.with(getContext())
                .load(U.getPhotoUrl())
                .crossFade()
                .centerCrop()
                .placeholder(R.drawable.ic_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(imgPerfil);
    }
}
