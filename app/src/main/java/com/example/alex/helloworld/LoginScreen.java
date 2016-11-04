package com.example.alex.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.os.AsyncTask;

import android.widget.EditText;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import android.app.ProgressDialog;
import android.net.Uri;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoginScreen extends AppCompatActivity{

    protected EditText username;
    private EditText password;
    protected String enteredUsername;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        username = (EditText)findViewById(R.id.eUsername);
        password = (EditText)findViewById(R.id.ePassword);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        Button registerButton = (Button)findViewById(R.id.registerButton);
    }
    public void buttonClick(View v){
        switch (v.getId()){
            case R.id.loginButton: {
                enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("")) {
                    Toast.makeText(LoginScreen.this, "Username and password required", Toast.LENGTH_LONG).show();
                    return;
                }
                //request server authentification
                new AsyncLogin().execute(enteredUsername, enteredPassword);
                break;
            }
            case R.id.registerButton: {
                Intent intent = new Intent(LoginScreen.this, RegisterActivity.class);
                startActivity(intent);
                LoginScreen.this.finish();
                break;
            }
            default: {
                break;
            }
        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String>{
        ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //this method will be running on the UI thread
            progressDialog.setMessage("\tLoading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            try{
                url = new URL("http://10.0.2.2:8080/android_user" +
                        "_api/Backend/index.php");
            }
            catch(MalformedURLException e){
                e.printStackTrace();
                System.out.println("URL not found");
                return "exception";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout((READ_TIMEOUT));
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                //setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                //Open connection for sending data

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch(IOException e1){
                e1.printStackTrace();
                System.out.println("URL not found");
                return "exception";
            }
            try{
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder successString = new StringBuilder();
                String line;
                String success ="";

                while ((line = reader.readLine()) != null) {
                    successString.append(line);
                    System.out.println(successString.toString());
                }
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(successString.toString());

                    //the json.get seems to be causing the trouble
                    success = Long.toString((Long) json.get("success"));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return success;
            }
            catch(IOException e2) {
                e2.printStackTrace();
                return "exception";
            }
            finally{
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String success){
            //this is running on the UI thread
            progressDialog.dismiss();

            if(success.equalsIgnoreCase("1")){
                Intent intent = new Intent(LoginScreen.this, Home.class);
                startActivity(intent);
                LoginScreen.this.finish();

            }
            else if(success.equalsIgnoreCase("")){
                Toast.makeText(LoginScreen.this, "Invalid username or password", Toast.LENGTH_LONG).show();

            }
            else if(success.equalsIgnoreCase("exception") || success.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(LoginScreen.this, "Connection Problem", Toast.LENGTH_LONG).show();
            }
        }
    }
}
