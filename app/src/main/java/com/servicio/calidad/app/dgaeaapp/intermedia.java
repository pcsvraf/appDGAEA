package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class intermedia extends AppCompatActivity {
    public static String resultado;//es la variable que tendra la info extraida de la bd
    public static String [] nuevo;
    public static final String EXTRA= "position";

    public void onCreate(Bundle savedInstanceState) {
        /*
        if (CardContentFragment.ViewHolder.name.getText().toString().equals(CardContentFragment.rosa)){
            System.out.println("coincide"+CardContentFragment.ViewHolder.name.getText().toString());
        }else{
            System.out.println("no coincide"+CardContentFragment.ViewHolder.name.getText().toString());
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        int postion = getIntent().getIntExtra(EXTRA, 0);
        new intermedia.DescargarImagen(intermedia.this).execute("","");
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class DescargarImagen extends AsyncTask<String, Void, String> {
        public WeakReference<Context> context;

        public DescargarImagen(Context context){
            this.context = new WeakReference<>(context);
        }

        public String doInBackground (String... params){
            String get_url="https://pcspucv.cl/tp/extraccion.php";
            try{

                URL url2=new URL(get_url);
                HttpsURLConnection httpsURLConnectionn= (HttpsURLConnection) url2.openConnection();
                httpsURLConnectionn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader( httpsURLConnectionn.getInputStream()));
                StringBuffer response = new StringBuffer("");
                String inputLine="";
                StringBuilder sb = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                resultado=response.toString();
                resultado=resultado.replace("[","");
                resultado=resultado.replace("]","");
                nuevo=resultado.split(",");

                list2.ContentAdapter.mPlaces=nuevo;

                //se comenta para que no se cierre la conexion de los datos, y asi evitar retraso en mostrar
                //in.close();
                //httpsURLConnectionn.disconnect();

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
        /*public void onPostExecute(String resultado){
            Toast.makeText(context.get(),resultado, Toast.LENGTH_LONG).show();
        }*/

    }

}
