package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class peticion extends AppCompatActivity {
    public static String resultado;
    public static String [] nuevo;
    public static String dato,dato1,dato2,dato3,dato4;
    public static TextView texto, texto1,texto2,texto3,texto4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticion);
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        new peticion.mostrarDatos(peticion.this).execute(String.valueOf(list2.idPeticion));

        final Button boton_aprobar= findViewById(R.id.buttonAprobar);
        final Button boton_rechazar= findViewById(R.id.buttonRechazar);
        texto=findViewById(R.id.text);
        texto1=findViewById(R.id.text1);
        texto2=findViewById(R.id.text2);
        texto3=findViewById(R.id.text3);
        texto4=findViewById(R.id.text4);

        boton_aprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estado=boton_aprobar.getText().toString();
                new actualizarPeticion(peticion.this).execute(estado,String.valueOf(list2.idPeticion));
            }
        });
        boton_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estado=boton_rechazar.getText().toString();
                new actualizarPeticion(peticion.this).execute(estado,String.valueOf(list2.idPeticion));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class actualizarPeticion extends AsyncTask<String, Void, String>{
        private WeakReference<Context> context;

        public actualizarPeticion(Context context){
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String... params){
            String registrar_url="https://pcspucv.cl/tp/actualizar.php";
            String resultado;

            try{

                URL url=new URL(registrar_url);
                HttpsURLConnection httpsURLConnection= (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream= httpsURLConnection.getOutputStream();
                BufferedWriter  bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

                //String estado= params[0];
                String estado;
                String idObra=list2.ContentAdapter.mPlaces[Integer.valueOf(params[1])];
                System.out.println(idObra+"id");
                if(params[0].equals("Aprobar")){
                    System.out.println("hola");
                    estado="2";

                }else{
                    estado="3";
                }

                String data= URLEncoder.encode("estado", "UTF-8")+"="+ URLEncoder.encode(estado, "UTF-8")
                        + "&"+ URLEncoder.encode("idObra", "UTF-8")+"="+ URLEncoder.encode(idObra, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =httpsURLConnection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder stringBuilder= new StringBuilder();

                String line;
                while ((line= bufferedReader.readLine())!= null){
                    stringBuilder.append(line);
                }
                resultado= stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();

            }catch (MalformedURLException e){
                Log.d("MI APP", "se ha utilizado una url de formato incorrecto");
                resultado ="Se ha producido un error";
            }catch (IOException e){
                Log.d("MI APP", "error inesperado");
                resultado= "Se ha producido un error";
            }
            return resultado;
        }
        protected void onPostExecute(String resultado){
            Toast.makeText(context.get(),resultado, Toast.LENGTH_LONG).show();
        }
    }

    public static class mostrarDatos extends AsyncTask<String, Void, String> {
        public WeakReference<Context> context;

        public mostrarDatos(Context context){
            this.context = new WeakReference<>(context);
        }

        public String doInBackground (String... params){
            String getUrl="https://pcspucv.cl/tp/datos.php";
            try{

                URL url=new URL(getUrl);
                HttpsURLConnection httpsURLConnection= (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream= httpsURLConnection.getOutputStream();
                BufferedWriter  bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

                String id= list2.ContentAdapter.mPlaces[Integer.valueOf(params[0])];
                System.out.println(id+"va el id");

                String data= URLEncoder.encode("idObra", "UTF-8")+"="+ URLEncoder.encode(id, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                //bufferedWriter.close();
                //outputStream.close();

                InputStream inputStream =httpsURLConnection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder stringBuilder= new StringBuilder();

                String line;
                while ((line= bufferedReader.readLine())!= null){
                    stringBuilder.append(line);
                }
                resultado= stringBuilder.toString();
                resultado=resultado.replace("[","");
                resultado=resultado.replace("]","");
                resultado=resultado.replace("\"","");
                nuevo=resultado.split(",");
                dato=nuevo[0];
                dato1=nuevo[1];
                dato2=nuevo[2];
                dato3=nuevo[3];
                dato4=nuevo[4];

            }catch (MalformedURLException e){
                Log.d("MI APP", "se ha utilizado una url de formato incorrecto");
                resultado ="Se ha producido un error";
            }catch (IOException e){
                Log.d("MI APP", "error inesperado");
                resultado= "Se ha producido un error";
            }
            return resultado;
        }

        //para mostrar resultado por pantalla
        public void onPostExecute(String resultado){
            //Toast.makeText(context.get(),resultado, Toast.LENGTH_LONG).show();
            texto.setText(dato);
            texto1.setText(dato1);
            texto2.setText(dato2);
            texto3.setText(dato3);
            texto4.setText(dato4);
        }

    }

}
