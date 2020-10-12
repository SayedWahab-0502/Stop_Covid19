package com.example.stop_covid19.ActivityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.stop_covid19.Databasehelper.CheckInternet;
import com.example.stop_covid19.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPassword extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneno_textinputlayout;
    AppCompatButton next_button;
     RelativeLayout progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        countryCodePicker= findViewById(R.id.country_code_picker_forgotpassword);
        phoneno_textinputlayout= findViewById(R.id.forgotpassword_phoneno);
        next_button= findViewById(R.id.next_button);
        progressDialog= findViewById(R.id.progress_dialog);

    }


    public void Verifyphoneno(View  view){

        //check internet connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }

        //validate phoneno
        if (!validatePhoneno()){
            return;
        }

        progressDialog.setVisibility(View.VISIBLE);

        String _phone_no = phoneno_textinputlayout.getEditText().getText().toString().trim();

        if (_phone_no.charAt(0) == '0') {                //if there is 0 at the start this condition will detect and remove it
            _phone_no = _phone_no.substring(1);
        }

        final String final_phoneno = "+" + countryCodePicker.getSelectedCountryCode() + _phone_no;   // + , countrycode_fullno(92) , user no


        //check whether user exist or not in database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneno").equalTo(final_phoneno);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    phoneno_textinputlayout.setError(null);
                    phoneno_textinputlayout.setErrorEnabled(false);
                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneno", final_phoneno);

                    //we are calling the same VerifyOTP screen for creating new user & updating password....to distinguish we are adding the below code
                    intent.putExtra("Whattodo","updateData");  //this disitnguish if the user is coming from update password
                    startActivity(intent);
                    finish();

                    progressDialog.setVisibility(View.GONE);
                }
                else {
                    progressDialog.setVisibility(View.GONE);

                    phoneno_textinputlayout.setError("No such user exist!");
                    phoneno_textinputlayout.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validatePhoneno(){
        String val = phoneno_textinputlayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,10}\\z";

        if (val.isEmpty()) {
            phoneno_textinputlayout.setError("Fields cannot be empty");
            return false;
        } else if (val.length() > 10) {
            phoneno_textinputlayout.setError("Enter a valid phone no!");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneno_textinputlayout.setError("No white spaces are allowed!");
            return false;
        } else {
            phoneno_textinputlayout.setError(null);
            phoneno_textinputlayout.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustomDialog() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ForgotPassword.this);
        alertdialog.setMessage("Please connect to the internet to proceed furthur")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();
            }
        });
    }


}