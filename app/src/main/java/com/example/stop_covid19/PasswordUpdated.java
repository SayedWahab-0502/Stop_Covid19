package com.example.stop_covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PasswordUpdated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_updated);
    }


    public void BacktoLoginscreen(View view){
        //startActivity(new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }
}