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
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticion);
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        textInputEmail = (TextInputLayout) findViewById(R.id.text_input_layout_email);
        textInputPassword = (TextInputLayout) findViewById(R.id.text_input_layout_pass);
        Button boton_aprobar= findViewById(R.id.buttonAprobar);
        Button rechazar= findViewById(R.id.buttonRechazar);

        boton_aprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=editTextEmail.getText().toString();
                String password= editTextPassword.getText().toString();
                new DescargarImagen(peticion.this).execute(email, password);
            }
        });


    }

    public static class DescargarImagen extends AsyncTask<String, Void, String>{
        private WeakReference<Context> context;

        public DescargarImagen(Context context){
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String... params){
            String registrar_url="https://pcspucv.cl/tp/registro.php";
            String get_url="https://pcspucv.cl/tp/extraccion.php";
            String resultado;

            try{

                URL url=new URL(registrar_url);
                HttpsURLConnection httpsURLConnection= (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream= httpsURLConnection.getOutputStream();
                BufferedWriter  bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

                String email= params[0];
                String password= params[1];

                String data= URLEncoder.encode("email", "UTF-8")+"="+ URLEncoder.encode(email, "UTF-8")
                        + "&"+ URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8");

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

    public void validate(View view) {
        String mailError = null;
        if (TextUtils.isEmpty(editTextEmail.getText())) {
            mailError = getString(R.string.mandatory);
        }
        toggleTextInputLayoutError(textInputEmail, mailError);

        String passError = null;
        if (TextUtils.isEmpty(editTextPassword.getText())) {
            passError = getString(R.string.mandatory);
        }
        toggleTextInputLayoutError(textInputPassword, passError);

        clearFocus();
    }

    /**
     * Display/hides TextInputLayout error.
     *
     * @param msg the message, or null to hide
     */
    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}
