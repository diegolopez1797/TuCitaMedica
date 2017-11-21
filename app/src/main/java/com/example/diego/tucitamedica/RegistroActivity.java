package com.example.diego.tucitamedica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.diego.tucitamedica.model.Perfil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.diego.tucitamedica.model.Persona;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class RegistroActivity extends AppCompatActivity {

    EditText txtUsuarioR;
    EditText txtClaveR;
    Button btnRegistrarR;
    ProgressBar progressBar2;
    ImageView foto;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuarioR = (EditText) findViewById(R.id.txtUsuarioR);
        txtClaveR = (EditText) findViewById(R.id.txtClaveR);
        btnRegistrarR = (Button) findViewById(R.id.btnRegistrarR);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent intent= new Intent(RegistroActivity.this, FormularioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnRegistrarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar2.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(txtUsuarioR.getText().toString().trim(), txtClaveR.getText().toString().trim())
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegistroActivity.this, "A ocurrido un ERROR o el Usuario YA EXISTE.",
                                                Toast.LENGTH_SHORT).show();

                                        progressBar2.setVisibility(View.INVISIBLE);

                                    } else {
                                        Toast.makeText(RegistroActivity.this, "Registro EXITOSO.",
                                                Toast.LENGTH_SHORT).show();


                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference refUsuario = database.getReference("usuario");

                                        Persona persona = new Persona();

                                        persona.setEmail(mAuth.getCurrentUser().getEmail());
                                        persona.setFechaNacimiento("");
                                        persona.setNombre("");
                                        persona.setCelular("");
                                        persona.setApellidos("");
                                        persona.setCedula("");
                                        persona.setEdad("");
                                        persona.setSexo("");
                                        persona.setEstadoCivil("");
                                        persona.setDireccion("");

                                        refUsuario.child(mAuth.getCurrentUser().getUid()).setValue(persona);

                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegistroActivity.this, "Email de veirificación enviado :)", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(RegistroActivity.this, "Email de veirificación No enviado :(", Toast.LENGTH_SHORT).show();
                                                    mAuth.signOut();
                                                }

                                            }
                                        });

                                    }

                                    // ...
                                }
                            });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            //mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
