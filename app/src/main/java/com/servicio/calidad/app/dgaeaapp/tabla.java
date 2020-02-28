package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class tabla extends AppCompatActivity {
    TextView textView;
    public static String resultado;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);
        textView = (TextView) findViewById(R.id.primero);
        new tabla.DescargarImagen(tabla.this);
        textView.setText(resultado);


    }
    public static class DescargarImagen extends AsyncTask<String, Void, String> {
        private WeakReference<Context> context;

        public DescargarImagen(Context context){
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String... params){
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
                System.out.println(response.toString());
                resultado=response.toString();

                in.close();
                httpsURLConnectionn.disconnect();


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


}
