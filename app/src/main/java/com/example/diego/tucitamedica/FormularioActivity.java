package com.example.diego.tucitamedica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.example.diego.tucitamedica.model.Perfil;
import com.example.diego.tucitamedica.model.Persona;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class FormularioActivity extends AppCompatActivity {

    FirebaseDatabase database;
    Button btnTerminar;
    DatabaseReference refPersona;
    DatabaseReference refMensaje;
    TextView txtSaludo;

    ImageView imgFoto;
    Button btnTomar;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    Persona persona ;
    Button btnBuscar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

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
                    Intent intent= new Intent(FormularioActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };

        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        btnTomar = (Button) findViewById(R.id.btnTomar);

        persona = new Persona();
        btnTerminar = (Button) findViewById(R.id.btnTerminar);
        btnBuscar= (Button) findViewById(R.id.btnBuscar);

        txtSaludo= (TextView) findViewById(R.id.txtSaludo);

        txtEmail= (EditText) findViewById(R.id.txtEmail);

        txtApellido= (EditText) findViewById(R.id.txtApellidos);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtCedula= (EditText) findViewById(R.id.txtIdentificacion);
        txtCelular= (EditText) findViewById(R.id.txtCelular);
        txtFechaNacimiento= (EditText) findViewById(R.id.txtFechaNacimiento);
        txtEdad= (EditText) findViewById(R.id.txtEdad);
        txtDireccion= (EditText) findViewById(R.id.txtDireccion);
        txtEstadoCivil= (EditText) findViewById(R.id.txtEstadoCivil);
        txtSexo= (EditText) findViewById(R.id.txtSexo);

        database = FirebaseDatabase.getInstance();
        refPersona = database.getReference("usuario");
        refMensaje = database.getReference("txtSaludo");


        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntent();
            }
        });


        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {



                //ENVIAR LA FOTO AL ALMACENAMIENTO DE FAREBASE

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference refPerfil = storage.getReferenceFromUrl("gs://tucitamedica-f835a.appspot.com").child("fotoPerfil");


                imgFoto.setDrawingCacheEnabled(true);
                imgFoto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                imgFoto.layout(0, 0, imgFoto.getMeasuredWidth(), imgFoto.getMeasuredHeight());
                imgFoto.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgFoto.getDrawingCache());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] data = outputStream.toByteArray();


                UploadTask uploadTask = refPerfil.child(mAuth.getCurrentUser().getUid()).putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });



// ENVIAR DATOS A LA BASE DE DATOS

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



                Intent intent= new Intent(FormularioActivity.this, DrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                Toast.makeText(FormularioActivity.this, "Registro completado EXITOSAMENTE :).",
                        Toast.LENGTH_SHORT).show();



            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(imageBitmap);
        }
    }


    private void llamarIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
