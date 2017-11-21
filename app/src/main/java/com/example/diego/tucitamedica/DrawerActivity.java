package com.example.diego.tucitamedica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diego.tucitamedica.Fragments.BlankFragment;
import com.example.diego.tucitamedica.Fragments.CalendarioFragment;
import com.example.diego.tucitamedica.Fragments.CentrosFragment;
import com.example.diego.tucitamedica.Fragments.CitasFragment;
import com.example.diego.tucitamedica.Fragments.FormularioFragment;
import com.example.diego.tucitamedica.Fragments.FormularioMapaFragment;
import com.example.diego.tucitamedica.Fragments.InicioFragment;
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

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference refPersona;
    TextView txtNombreUsuario;
    TextView txtCorreoUsuario;
    Persona persona;
    ImageView imgPerfil;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");

                    Intent intent= new Intent(DrawerActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                // ...
            }
        };




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

 /*       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });  */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);


        txtCorreoUsuario = (TextView) hView.findViewById(R.id.txtCorreoUsuario);
        txtNombreUsuario = (TextView) hView.findViewById(R.id.txtNombreUsuario);
        imgPerfil = (ImageView) hView.findViewById(R.id.imgPerfil);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference refPerfil = storage.getReferenceFromUrl("gs://tucitamedica-f835a.appspot.com").child("fotoPerfil");

        final long ONE_MEGABYTE = 1024 * 1024;
        refPerfil.child(mAuth.getCurrentUser().getUid()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgPerfil.setImageBitmap(bitmap);
            }
        });


        persona = new Persona();
        database = FirebaseDatabase.getInstance();
        refPersona = database.getReference("usuario");


        refPersona.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot hijos: dataSnapshot.getChildren()){

                    System.out.println(hijos.getKey());
                    System.out.println(hijos.getValue());


                }
                persona = dataSnapshot.getValue(Persona.class);

                txtNombreUsuario.setText(persona.getNombre());
                txtCorreoUsuario.setText(persona.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("[ERROR BASE DE DATOS]: "+databaseError.toString());

            }
        });



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            mAuth.signOut();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        FragmentManager fm= getSupportFragmentManager();

        if (id == R.id.nav_inicio) {

        fm.beginTransaction().replace(R.id.content_main, new InicioFragment()).commit();

    } else if (id == R.id.nav_citas) {

        fm.beginTransaction().replace(R.id.content_main, new CitasFragment()).commit();

    } else if (id == R.id.nav_centros) {

            fm.beginTransaction().replace(R.id.content_main, new CentrosFragment()).commit();

    } else if (id == R.id.nav_tus_mapas) {

        fm.beginTransaction().replace(R.id.content_main, new BlankFragment()).commit();

    } else if (id == R.id.nav_actualizar_tus_datos) {

        fm.beginTransaction().replace(R.id.content_main, new FormularioFragment()).commit();

    } else if (id == R.id.nav_calendario) {

            fm.beginTransaction().replace(R.id.content_main, new CalendarioFragment()).commit();

    }

    item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

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


