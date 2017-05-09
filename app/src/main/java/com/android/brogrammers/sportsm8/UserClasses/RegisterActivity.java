package com.android.brogrammers.sportsm8.UserClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.brogrammers.sportsm8.MainActivity;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RegisterActivity extends AppCompatActivity implements UIthread {

    private static final String TAG = "";
    protected EditText username;
    private EditText password;
    private EditText email;
    protected String enteredUsername;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.eUsername);
        password = (EditText) findViewById(R.id.ePassword);
        email = (EditText) findViewById((R.id.eEmail));

        mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                String enteredEmail = email.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("") || enteredEmail.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Username, password, and email required", Toast.LENGTH_LONG).show();
                    return;
                }

                //request server authentication
                register();
                break;
            case R.id.cancel_button:
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void register() {
        enteredUsername = username.getText().toString();
        final String enteredPassword = password.getText().toString();
        final String enteredEmail = email.getText().toString();
        mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            syncDatabases(enteredUsername,enteredPassword,enteredEmail);
                        }

                        // ...
                    }
                });


    }
    public void syncDatabases(String enteredUsername,String enteredPassword,String enteredEmail){
        String[] params = {"IndexAccounts.php", "function", "createNewAccount", "email", enteredUsername, "password", enteredPassword, "email", enteredEmail};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
    }


    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            RegisterActivity.this.finish();


    }


}

