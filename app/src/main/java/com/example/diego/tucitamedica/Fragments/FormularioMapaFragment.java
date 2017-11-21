package com.example.diego.tucitamedica.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.tucitamedica.FormularioMapaActivity;
import com.example.diego.tucitamedica.MapsActivity;
import com.example.diego.tucitamedica.R;
import com.example.diego.tucitamedica.modelo.Marcador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormularioMapaFragment extends Fragment {


    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText txtNombre;
    private Button btnGuardar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public FormularioMapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_formulario_mapa, container, false);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("[MenuPrincipal]", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                }
                // ...
            }
        };



        txtLatitud = (EditText) v.findViewById(R.id.txtLatitud);
        txtLongitud = (EditText) v.findViewById(R.id.txtLongitud);
        txtNombre = (EditText) v.findViewById(R.id.txtNombre);
        btnGuardar  = (Button) v.findViewById(R.id.btnGuardar);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference RefMarcador = database.getReference("marcadores");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Marcador marcador = new Marcador();

                marcador.setLatitud(Double.parseDouble(txtLatitud.getText().toString().trim()));
                marcador.setLongitud(Double.parseDouble(txtLongitud.getText().toString().trim()));
                marcador.setNombre(txtNombre.getText().toString().trim());

                RefMarcador.child(mAuth.getCurrentUser().getUid()).child(marcador.getNombre()).setValue(marcador);

                Toast.makeText(getContext(), "Marcador guardado", Toast.LENGTH_SHORT).show();

                txtLongitud.setText("");
                txtLatitud.setText("");
                txtNombre.setText("");

            }
        });

        return v;
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
            // mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
