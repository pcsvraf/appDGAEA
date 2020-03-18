package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class tabla extends AppCompatActivity {
    public static String resultado;//es la variable que tendra la info extraida de la bd
    public static String [] nuevo;
    public static String [] nuevo2;
    public static final String EXTRA= "position";
    public static String get_url;

    public void onCreate(Bundle savedInstanceState) {
        /*
        if (CardContentFragment.ViewHolder.name.getText().toString().equals(CardContentFragment.rosa)){
            System.out.println("coincide"+CardContentFragment.ViewHolder.name.getText().toString());
        }else{
            System.out.println("no coincide"+CardContentFragment.ViewHolder.name.getText().toString());
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        int postion = getIntent().getIntExtra(EXTRA, 0);
        new DescargarImagen(tabla.this).execute("","");
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new list2(), "List");
        viewPager.setAdapter(adapter);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public static class DescargarImagen extends AsyncTask<String, Void, String> {
        public WeakReference<Context> context;

        public DescargarImagen(Context context){
            this.context = new WeakReference<>(context);
        }

        public String doInBackground (String... params){
            if (CardContentFragment.sistema==0){
                get_url="https://pcspucv.cl/tp/extraccion.php";
            }else if(CardContentFragment.sistema==1){
                get_url="https://pcspucv.cl/gsi/extraccion.php";
            }else{
                get_url="https://pcspucv.cl/tp/extraccion.php";
            }
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
                resultado=resultado.replace("\"","");
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
