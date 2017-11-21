package com.example.diego.tucitamedica.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.diego.tucitamedica.FormularioActivity;
import com.example.diego.tucitamedica.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.diego.tucitamedica.model.Cita;

import java.sql.Time;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitasFragment extends Fragment {

    View mView;
    Button btnAgendar;
    Spinner medico;
    Spinner especialidad;
    View dateFecha;
    View timeHora;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public CitasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_citas, container, false);


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


        btnAgendar = (Button) mView.findViewById(R.id.btnAgendar);
        medico = (Spinner) mView.findViewById(R.id.medico);
        especialidad = (Spinner) mView.findViewById(R.id.especialidad);
        timeHora = mView.findViewById(R.id.timeHora);
        dateFecha = mView.findViewById(R.id.dateFecha);



        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refCita = database.getReference("citas");

                Cita cita = new Cita();

                cita.setEspecialidad(especialidad.toString().trim());
                cita.setFecha(dateFecha.toString().trim());
                cita.setHora(timeHora.toString().trim());
                cita.setMedico(medico.toString().trim());


                refCita.child(mAuth.getCurrentUser().getUid()).setValue(cita);


                Toast.makeText(getContext(), "Cita asignada EXITOSAMENTE :).",
                        Toast.LENGTH_SHORT).show();

            }
        });


        Spinner sValores = (Spinner) mView.findViewById(R.id.especialidad);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.valores , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sValores.setAdapter(adapter);


        Spinner sValoresM = (Spinner) mView.findViewById(R.id.medico);

        ArrayAdapter adapterM = ArrayAdapter.createFromResource(getContext(), R.array.valoresM , android.R.layout.simple_spinner_item);
        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sValoresM.setAdapter(adapterM);


        return mView;

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
