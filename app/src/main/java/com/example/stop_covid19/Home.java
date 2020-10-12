package com.example.stop_covid19;

import android.app.Application;
import android.content.Intent;

import com.example.stop_covid19.ActivityClasses.NavigationMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {


    //No use of this class

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            startActivity(new Intent(Home.this, NavigationMain.class));
        }
    }
}
