/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Provides UI for the view with Cards.
 */
public class CardContentFragment extends Fragment {
    public static String usuarios;//es la variable que tendra la info extraida de la bd
    public static String [] nuevito;
    public static int sistema;
    public static Boolean ingreso=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        new DescargarImagen().execute();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public static TextView name;
        public TextView description;
        public String alerta="No tiene permisos";
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sistema=getAdapterPosition();
                    if (getAdapterPosition()==0){
                        Context context = v.getContext();
                        Intent intent = new Intent(context, tabla.class);
                        intent.putExtra(tabla.EXTRA, getAdapterPosition());
                        context.startActivity(intent);
                    }else if (getAdapterPosition()==1){
                        if (ingreso==true){
                            Context context = v.getContext();
                            Intent intent = new Intent(context, tabla.class);
                            intent.putExtra(tabla.EXTRA, getAdapterPosition());
                            context.startActivity(intent);
                        }else {
                            final Context context = v.getContext();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Aviso");
                            builder.setMessage("No tiene los permisos para acceder al sistema");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                    Intent intent = new Intent(context, MainActivity2.class);
                                    intent.putExtra(tabla.EXTRA, getAdapterPosition());
                                    context.startActivity(intent);
                                }
                            }).show();
                            //builder.create();
                            //builder.show();

                            //Intent intent = new Intent(context, MainActivity2.class);
                            //intent.putExtra(tabla.EXTRA, getAdapterPosition());
                            //context.startActivity(intent);
                        }
                    }

                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button)itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    sistema=getAdapterPosition();
                    if (getAdapterPosition()==0){
                        Context context = v.getContext();
                        Intent intent = new Intent(context, tabla.class);
                        intent.putExtra(tabla.EXTRA, getAdapterPosition());
                        context.startActivity(intent);
                    }else if (getAdapterPosition()==1){
                        if (ingreso==true){
                            Context context = v.getContext();
                            Intent intent = new Intent(context, tabla.class);
                            intent.putExtra(tabla.EXTRA, getAdapterPosition());
                            context.startActivity(intent);
                        }else {
                            final Context context = v.getContext();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Aviso");
                            builder.setMessage("No tiene los permisos para acceder al sistema");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                    Intent intent = new Intent(context, MainActivity2.class);
                                    intent.putExtra(tabla.EXTRA, getAdapterPosition());
                                    context.startActivity(intent);
                                }
                            }).show();
                            //builder.create();
                            //builder.show();

                            //Intent intent = new Intent(context, MainActivity2.class);
                            //intent.putExtra(tabla.EXTRA, getAdapterPosition());
                            //context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 3;

        private final String[] mPlaces;
        private final String[] mPlaceDesc;
        private final Drawable[] mPlacePictures;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.places);
            mPlaceDesc = resources.getStringArray(R.array.place_desc);
            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    public static class DescargarImagen extends AsyncTask<String, Void, String> {

        public String doInBackground (String... params){
            String get_url="https://pcspucv.cl/gsi/usuarios.php";
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
                usuarios=response.toString();
                usuarios=usuarios.replace("[","");
                usuarios=usuarios.replace("]","");
                nuevito=usuarios.split(",");

                for (int i = 0; i < nuevito.length; i++) {
                    String email='"'+login.correo+'"';
                    System.out.println(email);
                    if (nuevito[i].equals(email)){
                        ingreso=true;
                        System.out.println("true");
                        break;
                    }else{
                        ingreso=false;
                        System.out.println("false");
                    }
                }
                //list2.ContentAdapter.mPlaces=nuevito;

                //se comenta para que no se cierre la conexion de los datos, y asi evitar retraso en mostrar
                //in.close();
                //httpsURLConnectionn.disconnect();

            }catch (MalformedURLException e){
                Log.d("MI APP", "se ha utilizado una url de formato incorrecto");
                usuarios ="Se ha producido un error";
            }catch (IOException e){
                Log.d("MI APP", "error inesperado");
                usuarios= "Se ha producido un error";
            }
            return usuarios;
        }
        //para mostrar resultado por pantalla
        /*public void onPostExecute(String resultado){
            Toast.makeText(context.get(),resultado, Toast.LENGTH_LONG).show();
        }*/

    }
}

