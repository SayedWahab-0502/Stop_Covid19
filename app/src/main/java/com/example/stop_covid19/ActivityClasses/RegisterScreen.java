package com.example.stop_covid19.ActivityClasses;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.stop_covid19.Databasehelper.CheckInternet;
import com.example.stop_covid19.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class RegisterScreen extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    RelativeLayout progressDialog;

    AppCompatButton signup_btn;
    TextInputLayout firstname_inputlayout, lastname_inputlayout, email_inputlayout, phoneno_inputlayout, password_inputlayout, confirmpassword_inputlayout;
    //private static final String TAG = "Logging Example";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);


        progressDialog = findViewById(R.id.progress_dialog);
        firstname_inputlayout = findViewById(R.id.signup_firstname);
        lastname_inputlayout = findViewById(R.id.signup_lastname);
        email_inputlayout = findViewById(R.id.signup_email);
        phoneno_inputlayout = findViewById(R.id.signup_phoneno);
        password_inputlayout = findViewById(R.id.signup_password);
        confirmpassword_inputlayout = findViewById(R.id.signup_confirmpassword);
        countryCodePicker = findViewById(R.id.ccp);

        signup_btn = findViewById(R.id.signup_button);


    }


    public void signUp(View view) {


        //check internet connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }

        if (!validateFirstname() || !validateLastname() || !validateEmailid() || !validatePhoneno() || !validatePassword() || !validateConfirmPassword() || !matchPassword()) {
            return;
        }


        progressDialog.setVisibility(View.VISIBLE);

        //validation succedded and now move to next screen to verify phone number and save data

        String _firstname = firstname_inputlayout.getEditText().getText().toString().trim();
        String  _lastname = lastname_inputlayout.getEditText().getText().toString().trim();
        String  _email = email_inputlayout.getEditText().getText().toString().trim();
        String  _password = password_inputlayout.getEditText().getText().toString().trim();

       // String _confirmpassword = confirmpassword_inputlayout.getEditText().getText().toString().trim();

        String  _getUserEnteredPhoneNumber = phoneno_inputlayout.getEditText().getText().toString().trim();

        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {                //if there is 0 at the start this condition will detect and remove it
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }


       String _phoneno = "+" + countryCodePicker.getSelectedCountryCode() + _getUserEnteredPhoneNumber;   // + , countrycode_fullno(92) , user no


        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        intent.putExtra("firstname", _firstname);
        intent.putExtra("lastname", _lastname);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        intent.putExtra("phoneno", _phoneno);

        intent.putExtra("Whattodo","createNewUser");  //this disitnguish if the user is coming from signup screen or update password
        startActivity(intent);

        progressDialog.setVisibility(View.GONE);


    }




    private void showCustomDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterScreen.this);
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
    // Validation Functions for each fields


    private boolean validateFirstname() {

        String val = firstname_inputlayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";
        if (val.isEmpty()) {
            firstname_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (val.length() > 10) {
            firstname_inputlayout.setError("Firstnmae is too long!");
            return false;
        } else if (!val.matches(checkspaces)) {
            firstname_inputlayout.setError("No white spaces are allowed!");
            return false;
        }
        else {
            firstname_inputlayout.setError(null);
            firstname_inputlayout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateLastname() {

        String val = lastname_inputlayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";
        if (val.isEmpty()) {
            lastname_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (val.length() > 10) {
            lastname_inputlayout.setError("Lastnmae is too long!");
            return false;
        } else if (!val.matches(checkspaces)) {
            lastname_inputlayout.setError("No white spaces are allowed!");
            return false;
        } else {
            lastname_inputlayout.setError(null);
            lastname_inputlayout.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validateEmailid() {

        String val = email_inputlayout.getEditText().getText().toString().trim();
        String checkemail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (!val.matches(checkemail)) {
            email_inputlayout.setError("Invalid email!");
            return false;
        } else {
            email_inputlayout.setError(null);
            email_inputlayout.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validatePhoneno() {
        String val = phoneno_inputlayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";

        if (val.isEmpty()) {
            phoneno_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (val.length() > 10) {
            phoneno_inputlayout.setError("Enter a valid phone no!");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneno_inputlayout.setError("No white spaces are allowed!");
            return false;
        } else {
            phoneno_inputlayout.setError(null);
            phoneno_inputlayout.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validatePassword() {

        String val = password_inputlayout.getEditText().getText().toString().trim();
        String checkpassword = "^" +
                //"(?=.*[0-9])" +    // at least 1 digit
                //"(?=.*[a-z])" +      // at least 1 lowercase letter
                //"(?=.*[A-Z])" +         // at least 1 uppercase letter

                "(?=.*[a-zA-Z])" +         //any letter
                "(?=.*[@#$%^&+=])" +      // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{4,}" +                // at least 4 character
                "$";

        if (val.isEmpty()) {
            password_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (!val.matches(checkpassword)) {
            password_inputlayout.setError("Password should contain 1 special character & at least 4 character!");
            return false;
        } else {
            password_inputlayout.setError(null);
            password_inputlayout.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validateConfirmPassword() {

        String val = confirmpassword_inputlayout.getEditText().getText().toString().trim();
        String checkpassword = "^" +
                //"(?=.*[0-9])" +    // at least 1 digit
                //"(?=.*[a-z])" +      // at least 1 lowercase letter
                //"(?=.*[A-Z])" +         // at least 1 uppercase letter

                "(?=.*[a-zA-Z])" +         //any letter
                "(?=.*[@#$%^&+=])" +      // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{4,}" +                // at least 4 character
                "$";

        if (val.isEmpty()) {
            confirmpassword_inputlayout.setError("Fields cannot be empty");
            return false;
        } else if (!val.matches(checkpassword)) {
            confirmpassword_inputlayout.setError("Password should contain at least 1 special character & 4 character!");
            return false;
        } else {
            confirmpassword_inputlayout.setError(null);
            confirmpassword_inputlayout.setErrorEnabled(false);
            return true;
        }

    }


    private boolean matchPassword() {

        String val = password_inputlayout.getEditText().getText().toString().trim();
        String val2= confirmpassword_inputlayout.getEditText().getText().toString().trim();

        if (!val.equals(val2)) {
            Toast.makeText(this, "Password & ConfirmPassword does not match!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}