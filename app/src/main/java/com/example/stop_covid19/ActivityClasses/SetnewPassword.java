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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetnewPassword extends AppCompatActivity {

    TextInputLayout newpassword, confirmpassword;
    AppCompatButton updatepassword_button;
    RelativeLayout progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setnew_password);

        newpassword= findViewById(R.id.update_newpassword);
        confirmpassword= findViewById(R.id.update_confirmpassword);

        updatepassword_button= findViewById(R.id.update_password_button);
       // progressDialog= findViewById(R.id.progress_dialog);
    }


    /*
    Update User password
    on Button Click
     */
    public void setNewPassword(View view){

        //check internet connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }


        //validation of password & confirm_password
        if (!validatePassword() || !validateConfirmPassword() || !matchPassword()){
            return;
        }

       // progressDialog.setVisibility(View.VISIBLE);

        //Get data from fields

        String _newpassword = newpassword.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneno");


        //Update data in firebase and in Sessions

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(_newpassword);

        startActivity(new Intent(getApplicationContext(), PasswordUpdated.class));
        Toast.makeText(SetnewPassword.this, "Password Updated Successfully", Toast.LENGTH_LONG).show();
        finish();

       // progressDialog.setVisibility(View.GONE);
    }


    private boolean validatePassword() {

        String val = newpassword.getEditText().getText().toString().trim();
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
            newpassword.setError("Fields cannot be empty");
            return false;
        } else if (!val.matches(checkpassword)) {
            newpassword.setError("Password should contain 1 special character & at least 4 character!");
            return false;
        } else {
            newpassword.setError(null);
            newpassword.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validateConfirmPassword() {

        String val = confirmpassword.getEditText().getText().toString().trim();
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
            confirmpassword.setError("Fields cannot be empty");
            return false;
        } else if (!val.matches(checkpassword)) {
            confirmpassword.setError("Password should contain at least 1 special character & 4 character!");
            return false;
        } else {
            confirmpassword.setError(null);
            confirmpassword.setErrorEnabled(false);
            return true;
        }

    }


    private boolean matchPassword() {

        String val = newpassword.getEditText().getText().toString().trim();
        String val2= confirmpassword.getEditText().getText().toString().trim();

        if (!val.equals(val2)) {
            Toast.makeText(this, "Password & ConfirmPassword does not match!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    private void showCustomDialog() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(SetnewPassword.this);
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