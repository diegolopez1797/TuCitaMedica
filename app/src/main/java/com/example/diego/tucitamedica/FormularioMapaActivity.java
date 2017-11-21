package com.example.diego.tucitamedica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.tucitamedica.modelo.Marcador;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioMapaActivity extends AppCompatActivity {

    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText txtNombre;
    private Button btnGuardar;
    private Button btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_mapa);

        txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        txtLongitud = (EditText) findViewById(R.id.txtLongitud);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        btnGuardar  = (Button) findViewById(R.id.btnGuardar);
        btnMapa  = (Button) findViewById(R.id.btnMapa);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference RefMarcador = database.getReference("marcadores");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Marcador marcador = new Marcador();

                marcador.setLatitud(Double.parseDouble(txtLatitud.getText().toString().trim()));
                marcador.setLongitud(Double.parseDouble(txtLongitud.getText().toString().trim()));
                marcador.setNombre(txtNombre.getText().toString().trim());

                RefMarcador.child(marcador.getNombre()).setValue(marcador);

                Toast.makeText(FormularioMapaActivity.this, "Marcador guardado", Toast.LENGTH_SHORT).show();

                txtLongitud.setText("");
                txtLatitud.setText("");
                txtNombre.setText("");

            }
        });


        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FormularioMapaActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });


    }
}
