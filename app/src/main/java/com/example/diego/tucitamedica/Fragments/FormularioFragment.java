package com.example.diego.tucitamedica.Fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.tucitamedica.DrawerActivity;
import com.example.diego.tucitamedica.FormularioActivity;
import com.example.diego.tucitamedica.FormularioMapaActivity;
import com.example.diego.tucitamedica.R;
import com.example.diego.tucitamedica.SplashActivity;
import com.example.diego.tucitamedica.model.Persona;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormularioFragment extends Fragment {

    FirebaseDatabase database;
    Button btnTerminar;
    DatabaseReference refPersona;
    DatabaseReference refMensaje;
    TextView txtSaludo;

    Persona persona ;
    ImageView imgFoto;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtCedula;
    EditText txtCelular;
    EditText txtFechaNacimiento;
    EditText txtEmail;
    EditText txtSexo;
    EditText txtEdad;
    EditText txtDireccion;
    EditText txtEstadoCivil;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    public FormularioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_formulario, container, false);


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


        persona = new Persona();
        btnTerminar = (Button) v.findViewById(R.id.btnTerminar);
        imgFoto = (ImageView) v.findViewById(R.id.imgFoto);

        txtSaludo= (TextView) v.findViewById(R.id.txtSaludo);

        txtEmail= (EditText) v.findViewById(R.id.txtEmail);

        txtApellido= (EditText) v.findViewById(R.id.txtApellidos);
        txtNombre= (EditText) v.findViewById(R.id.txtNombre);
        txtCedula= (EditText) v.findViewById(R.id.txtIdentificacion);
        txtCelular= (EditText) v.findViewById(R.id.txtCelular);
        txtFechaNacimiento= (EditText) v.findViewById(R.id.txtFechaNacimiento);
        txtEdad= (EditText) v.findViewById(R.id.txtEdad);
        txtDireccion= (EditText) v.findViewById(R.id.txtDireccion);
        txtEstadoCivil= (EditText) v.findViewById(R.id.txtEstadoCivil);
        txtSexo= (EditText) v.findViewById(R.id.txtSexo);

        database= FirebaseDatabase.getInstance();
        refPersona = database.getReference("usuario");
        refMensaje = database.getReference("txtSaludo");



        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {


                persona.setNombre(txtNombre.getText().toString().trim());
                persona.setApellidos(txtApellido.getText().toString().trim());
                persona.setCedula(txtCedula.getText().toString().trim());
                persona.setCelular(txtCelular.getText().toString().trim());
                persona.setFechaNacimiento(txtFechaNacimiento.getText().toString().trim());
                persona.setDireccion(txtDireccion.getText().toString().trim());
                persona.setSexo(txtSexo.getText().toString().trim());
                persona.setEstadoCivil(txtEstadoCivil.getText().toString().trim());
                persona.setEdad(txtEdad.getText().toString().trim());
                persona.setEmail(txtEmail.getText().toString().trim());

                refPersona.child(mAuth.getCurrentUser().getUid()).setValue(persona);


                Toast.makeText(getContext(), "Datos actualizados EXITOSAMENTE :).",
                        Toast.LENGTH_SHORT).show();



            }
        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference refPerfil = storage.getReferenceFromUrl("gs://tucitamedica-f835a.appspot.com").child("fotoPerfil");

        final long ONE_MEGABYTE = 400  * 400;
        refPerfil.child(mAuth.getCurrentUser().getUid()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgFoto.setImageBitmap(bitmap);
            }
        });


                refPersona.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot hijos: dataSnapshot.getChildren()){

                            System.out.println(hijos.getKey());
                            System.out.println(hijos.getValue());


                        }
                        persona= dataSnapshot.getValue(Persona.class);

                        txtEmail.setText(persona.getEmail());
                        txtApellido.setText(persona.getApellidos());
                        txtNombre.setText(persona.getNombre());
                        txtCelular.setText(persona.getCelular());
                        txtFechaNacimiento.setText(persona.getFechaNacimiento());
                        txtCedula.setText(persona.getCedula());
                        txtEdad.setText(persona.getEdad());
                        txtSexo.setText(persona.getSexo());
                        txtDireccion.setText(persona.getDireccion());
                        txtEstadoCivil.setText(persona.getEstadoCivil());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("[ERROR BASE DE DATOS]: "+databaseError.toString());

                    }
                });




        refMensaje.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                String value = dataSnapshot.getValue(String.class);
                txtSaludo.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
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
