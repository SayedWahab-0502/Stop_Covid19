package com.example.stop_covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stop_covid19.Databasehelper.CheckInternet;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class LoginScreen extends AppCompatActivity {

    Button button_login;

    TextView textView_signup, forgot_pssword;

    LinearLayout linearLayout;

    TextInputLayout phoneno_editext, password_editext;

    CountryCodePicker countryCodePicker;

    RelativeLayout progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        progressDialog = findViewById(R.id.progress_dialog);
        button_login = findViewById(R.id.login_button);
        textView_signup = findViewById(R.id.text_signup);
        forgot_pssword = findViewById(R.id.forgotpassword_text);
        linearLayout = findViewById(R.id.linear_logo_sigin);
        countryCodePicker = findViewById(R.id.ccp_login);

        phoneno_editext = findViewById(R.id.login_phoneno);
        password_editext = findViewById(R.id.editext_password_login);



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            startActivity(new Intent(LoginScreen.this,NavigationMain.class));
            finish();
        }


        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(linearLayout, "Logo");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginScreen.this, pairs);
                startActivity(intent, activityOptions.toBundle());

            }
        });

        forgot_pssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

    }

    public void letTheUserLoggedIn(View view) {

        //check internet connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }


        //validate username & password
        if (!validatePhoneno() || !validatePassword()) {
            return;
        }


        //get data

        progressDialog.setVisibility(View.VISIBLE);

        String _phoneno = phoneno_editext.getEditText().getText().toString().trim();
        if (_phoneno.charAt(0) == '0') {                //if there is 0 at the start this condition will detect and remove it
            _phoneno = _phoneno.substring(1);
        }


        final String _complete_phoneno = "+" + countryCodePicker.getSelectedCountryCode() + _phoneno;   // + , countrycode_fullno(92) , user no
        final String _password = password_editext.getEditText().getText().toString().trim();

        //Firebase Databasequery

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneno").equalTo(_complete_phoneno);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if we recieve the data successfully
                if (snapshot.exists()) {

                    phoneno_editext.setError(null);
                    phoneno_editext.setErrorEnabled(false);


                    String systemPassword = snapshot.child(_complete_phoneno).child("password").getValue(String.class);

                    if (systemPassword.equals(_password)) {

                        password_editext.setError(null);
                        phoneno_editext.setErrorEnabled(false);

                        progressDialog.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), NavigationMain.class));
                        finish();
                        Toast.makeText(LoginScreen.this, "SUCCESS!", Toast.LENGTH_LONG).show();


                    } else {
                        progressDialog.setVisibility(View.GONE);
                        Toast.makeText(LoginScreen.this, "Password does not match!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    progressDialog.setVisibility(View.GONE);
                    Toast.makeText(LoginScreen.this, "No such user exist!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.setVisibility(View.GONE);
                Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();  //if something went wrong and we didn't recieve the data
            }
        });
    }



    private void showCustomDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginScreen.this);
        alertDialog.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            startActivity( new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }



    /*
    Validate  Function
     */

    private boolean validatePhoneno() {
        String val = phoneno_editext.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";

        if (val.isEmpty()) {
            phoneno_editext.setError("Fields cannot be empty");
            return false;
        } else if (val.length() > 10) {
            phoneno_editext.setError("Enter a valid phone no!");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneno_editext.setError("No white spaces are allowed!");
            return false;
        } else {
            phoneno_editext.setError(null);
            phoneno_editext.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {

        String val = password_editext.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password_editext.setError("Fields cannot be empty");
            return false;
        } else {
            password_editext.setError(null);
            password_editext.setErrorEnabled(false);
            return true;
        }

    }


}