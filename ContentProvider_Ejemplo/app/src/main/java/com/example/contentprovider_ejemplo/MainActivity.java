package com.example.contentprovider_ejemplo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ArrayList contactos;
    TextView uri_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactos = new ArrayList<String>();

        listview = (ListView)findViewById(R.id.listview);
        uri_textView = findViewById(R.id.uri_textView);
    }

    public void getContacts(View v){
        //revisar si el usuario aún no ha aceptado que se lean sus contactos.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //Si el usuario no ha dado permiso. Perdirselo.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
            return;
        }

        //Instanciar content resolver.
        ContentResolver contentResolver = getContentResolver();

        //URI de los números de telefono.
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //imprimir URI en string.
        uri_textView.setText("URI:\n" + uri.toString());

        //Asignar variable a la respuesta del content resolver.
        //Hacer la consulta a la base de datos.
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if(cursor.getCount() > 0){ //si se encuentra al menos 1 registro.
            contactos.clear();
            int i = 0;

            while(cursor.moveToNext() && i<10){
                //guardar el nombre y el número de cada contacto.
                @SuppressLint("Range")
                String nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range")
                String numero = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //añadir contactos a una array list.
                contactos.add("\n" + nombre + ":\n" + numero + "\n");

                //insertar array list en list view.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, contactos);
                listview.setAdapter(adapter);

                i++;
            }
        }
    }
}