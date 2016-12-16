package com.example.alex.helloworld.UserClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;

import com.example.alex.helloworld.MainActivity;
import com.example.alex.helloworld.databaseConnection.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoginScreen extends AppCompatActivity implements UIthread {

    protected EditText username;
    private EditText password;
    protected String enteredUsername;
    private String dbReturn;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        username = (EditText) findViewById(R.id.eUsername);
        password = (EditText) findViewById(R.id.ePassword);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        //try to read local database if a user is already logged in
        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String loginJson = sharedPrefs.getString("islogin", "");
        String prevUsername = sharedPrefs.getString("username", "");
        System.out.println("logged in user is: " + prevUsername);

        //better check?
        if (!prevUsername.equals("") && loginJson.equalsIgnoreCase("1")) {
            System.out.println("session continued");
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            LoginScreen.this.finish();
        }

    }

    public void buttonClick(View v) throws ExecutionException, InterruptedException {
        switch (v.getId()) {
            case R.id.loginButton: {
                enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("")) {
                    Toast.makeText(LoginScreen.this, "Username and password required", Toast.LENGTH_LONG).show();
                    return;
                }
                //request server authentication
                login();
                /*
                //request server authentification

                //new AsyncLogin().execute(enteredUsername, enteredPassword);
                String[] params = {"IndexAccounts.php", "function", "loginAccount", "username", enteredUsername, "password", enteredPassword};
                Database db = new Database(this, this.getApplicationContext());
                dbReturn = db.execute(params).get();
                System.out.println("CONNECTED TO DB");
                //
                //Getting nonsense from db currently
                //
                */
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

    @Override
    public void updateUI() {
        try {
            ArrayList<Information> info = Database.jsonToArrayList(dbReturn);
            System.out.println("UPDATING UI");
            if (info.get(0).success == 0) {
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                LoginScreen.this.finish();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();

        String[] params = {"IndexAccounts.php", "function", "loginAccount", "username", enteredUsername, "password", enteredPassword};
        Database db = new Database(this, this.getApplicationContext());
        db.execute(params);
    }

    @Override
    public void updateUI(String successString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        String success = "";
        try {
            json = (JSONObject) parser.parse(successString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        success = Long.toString((Long) json.get("success"));
        System.out.println("SUCCESS: " + success);

        if (success.equalsIgnoreCase("1")) {

            //save login information locally for session management
            //should the password be saved?
            SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("username", enteredUsername);
            editor.putString("email", "Korbi@korbi.de");
            // is already clear that success == 1
            editor.putString("islogin", success);
            editor.apply();

            //start home screen in case of successful login
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            LoginScreen.this.finish();
        } else if (success.equalsIgnoreCase("0")) {
            Toast.makeText(LoginScreen.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        } else if (success.equalsIgnoreCase("exception") || success.equalsIgnoreCase("unsuccessful")) {
            Toast.makeText(LoginScreen.this, "Connection Problem", Toast.LENGTH_LONG).show();
        }
    }

}