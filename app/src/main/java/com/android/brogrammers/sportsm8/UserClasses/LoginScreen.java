package com.android.brogrammers.sportsm8.UserClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;

import com.android.brogrammers.sportsm8.MainActivity;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.android.brogrammers.sportsm8.TestDirectory.BottomNavBar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, UIthread, View.OnClickListener {

    private static final String TAG = "Tag";
    private static final int RC_SIGN_IN = 9001;
    protected EditText email;
    private EditText password;
    protected String enteredEmail;
    private String dbReturn;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        email = (EditText) findViewById(R.id.eEmail);
        password = (EditText) findViewById(R.id.ePassword);

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("email", mAuth.getCurrentUser().getEmail());
                    editor.apply();
                    progressDialog.dismiss();
                    System.out.println("session continued");
                    //Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    startActivity(intent);
                    LoginScreen.this.finish();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        //try to read local database if a user is already logged in
        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String loginJson = sharedPrefs.getString("islogin", "");

       /* if (loginJson.equalsIgnoreCase("1")) {
            System.out.println("session continued");
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            LoginScreen.this.finish();
        }*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Logging in");
    }

    @Override
    public void updateUI(String xyz) {
        /*try {
            ArrayList<Information> info = Database.jsonToArrayList(dbReturn);
            System.out.println("UPDATING UI");
            if (info.get(0).success == 0) {
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                LoginScreen.this.finish();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void login() {
        enteredEmail = email.getText().toString();
        String enteredPassword = password.getText().toString();

        if (enteredEmail.equals("") || enteredPassword.equals("")) {
            Toast.makeText(LoginScreen.this, "email and password required", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void updateUI() {

    /*    SharedPreferences sharedPrefs = getSharedPreferences("IndexAccounts", Context.MODE_PRIVATE);
        String loginJson = sharedPrefs.getString("IndexAccountsloginAccountJSON", "");

        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(loginJson);
            int success = (int) jsonObject.get("success");

            System.out.println("UPDATING UI");
            if (success == 1) {

                //save login information locally for session management
                sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("email", enteredEmail);
                editor.putString("islogin", "1");
                editor.apply();

                //start home screen in case of successful login
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            } else if (success == 0) {
                Toast.makeText(LoginScreen.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(LoginScreen.this, "Connection Problem", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

            /*
            IN CASE OF ParseException e
            } else if (info.get(0).success.equalsIgnoreCase("exception") || info.get(0).success.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(LoginScreen.this, "Connection Problem", Toast.LENGTH_LONG).show();
            }
            */

//        JSONParser parser = new JSONParser();
//        JSONObject json = null;
//        String success = "";
//        try {
//            json = (JSONObject) parser.parse(loginJson);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        success = Long.toString((Long)json.get("success"));
//
//        if (success.equalsIgnoreCase("1")) {
//
//            //save login information locally for session management
//            sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putString("email", enteredEmail);
//            editor.apply();
//
//            //start home screen in case of successful login
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            this.finish();
//        } else if (success.equalsIgnoreCase("0")) {
//            Toast.makeText(LoginScreen.this, "Invalid email or password", Toast.LENGTH_LONG).show();
//        } else if (success.equalsIgnoreCase("exception") || success.equalsIgnoreCase("unsuccessful")) {
//            Toast.makeText(LoginScreen.this, "Connection Problem", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton: {
                login();
                break;
            }
            case R.id.registerButton: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            case R.id.sign_in_button:
                progressDialog.show();
                signIn();
                break;
            default: {
                break;
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        progressDialog.hide();
                        syncDatabases(acct);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public void syncDatabases(GoogleSignInAccount account) {
        String[] params = {"IndexAccounts.php", "function", "createNewAccount", "username", account.getDisplayName(), "password", account.getId(), "email", account.getEmail()};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public static String getRealEmail(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getEmail();
    }


    public static String getEmailAdress(Context context) {

        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        return sharedPrefs.getString("email","");
        //TODO: USE THIS CODE
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        return mAuth.getCurrentUser().getEmail();


    }
}