package com.enuar.prueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private Boolean isLoggedIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fAuthStateListener;
    private ProgressBar progressBar;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private int SIGN_IN_CODE = 777;
    private GoogleSignInResult google;
    private AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        callbackManager = CallbackManager.Factory.create();
        progressBar = (ProgressBar)findViewById(R.id.pgsBar);

        //GOOGLE
        signInButton = (SignInButton)findViewById(R.id.signInBttn);

        signInButton.setSize(signInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(in,SIGN_IN_CODE);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInBttn:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null){
                goMainScreen();
            }

                //FACEBOOK
        loginButton = (LoginButton)findViewById(R.id.btnLogIn);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                token = loginResult.getAccessToken();


            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancelado ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"Error en el Login", Toast.LENGTH_SHORT).show();
            }
        });
//**********************************************************************************************************
        mAuth = FirebaseAuth.getInstance();
        fAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null){
                    cargarUsuarioGoogle(google, token);
                    goMainScreen();

                }
            }
        };
    }

    private void goMainScreen() {
        Intent in = new Intent(MainActivity.this,CenterNav.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            google = result;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Autenticacion Fallida.", Toast.LENGTH_SHORT).show(); }
                        else{
                            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

                            if (User != null){
                                String name = User.getDisplayName();
                                String email = User.getEmail();
                                String UrlPhoto = User.getPhotoUrl().toString()+"?type=large";
                                String uid = User.getUid();
                                Data Usuario = new Data(name,UrlPhoto,uid,email);
                                Usuario.SaveUser();

                                // myRef.child("Usuarios").child(Usuario.getID()).setValue(Usuario);
                            }
                            Toast.makeText(MainActivity.this, "Autenticacion exitosa.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                            signInButton.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                        signInButton.setVisibility(View.VISIBLE);

                        // ...
                    }
                });
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fAuthStateListener);




    }

    private void cargarUsuarioGoogle(GoogleSignInResult result, AccessToken token) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            String urlphoto = account.getPhotoUrl().toString() + "?type=large";
            Data Usuario = new Data(name, urlphoto, id, email);
            Usuario.SaveUser();
        } else {
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Autenticacion Fallida.", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

                        if (User != null) {
                            String name = User.getDisplayName();
                            String email = User.getEmail();
                            String UrlPhoto = User.getPhotoUrl().toString() + "?type=large";
                            String uid = User.getUid();
                            Data Usuario = new Data(name, UrlPhoto, uid, email);
                            Usuario.SaveUser();

                            // myRef.child("Usuarios").child(Usuario.getID()).setValue(Usuario);
                        }
                    }

                }
            });
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
      if (fAuthStateListener != null){
          mAuth.removeAuthStateListener(fAuthStateListener);
      }
    }

//*********************************************GOOGLE*********************************************************
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            String urlphoto =  account.getPhotoUrl().toString()+"?type=large";
            Data Usuario = new Data(name,urlphoto,id,email);
            Usuario.SaveUser();
            //myRef.child("Usuarios").child(Usuario.getID()).setValue(Usuario);

            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            FirebaseAuthWithGoogle(result.getSignInAccount());
        }else{
            Toast.makeText(this, "No se pudo iniciar sesion.", Toast.LENGTH_SHORT).show();

        }


    }

    private void FirebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
    AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!task.isSuccessful()){
                Toast.makeText(MainActivity.this, "No se pudo autenticar.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "Autenticacion exitosa.", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }
}

