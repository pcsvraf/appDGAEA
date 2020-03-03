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
    public static String[] nuevo;
    public static List<String>  mStringList;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        new DescargarImagen(tabla.this).execute("","");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        MainActivity2.Adapter adapter = new MainActivity2.Adapter(getSupportFragmentManager());
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
                    mStringList= Arrays.asList(inputLine);
                }

                System.out.println(response.toString());
                //System.out.println(((Object)response).getClass().getSimpleName());
                resultado=response.toString();
                nuevo=Arrays.copyOf(mStringList.toArray(), mStringList.size(), String[].class);
                System.out.println(Arrays.toString(nuevo));
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