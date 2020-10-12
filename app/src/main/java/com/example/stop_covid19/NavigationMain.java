package com.example.stop_covid19;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stop_covid19.Databasehelper.UserHelperClass;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.PieChart;

public class NavigationMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;

   private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference, firstdatabase;
    private Dialog MyDialog;
    TextView yes,no;
    private long backpress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

       toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //toolbar.setNavigationIcon(R.drawable.ic_menu_icon_nav);
       // getSupportActionBar().setIcon(R.drawable.ic_menu_icon_nav);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon_nav);

        navigationView.setNavigationItemSelectedListener(this);


        //navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseDatabase=FirebaseDatabase.getInstance();


    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
     */

    @Override
    public void onBackPressed() {

        if (backpress + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else
        {
            Toast.makeText(getBaseContext(),"Press back again to exit!",Toast.LENGTH_LONG).show();
        }

        backpress = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {

            case R.id.nav_home:
                Intent intent = new Intent(NavigationMain.this,NavigationMain.class);
                startActivity(intent);
                break;

            case R.id.nav_mycountry:
                Intent intent2 = new Intent(NavigationMain.this,MyCountry.class);
                startActivity(intent2);
                break;

            case R.id.nav_mystate:
                Intent intent3 = new Intent(NavigationMain.this,Mystate.class);
                startActivity(intent3);
                break;

            case R.id.nav_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "App ");
                String sAux = "App Features : \n 1 - Firebase Login & Register. \n 2 - Phone no OTP Authenticaiotn. \n 3 - Global Covid-19 Stats. \n 4 - Worldwide Affected Countries Covid-19 Stats. \n 5 - India's states Covid-19 Stats. \n\n Download the app throught the below link. \n";
                sAux = sAux + "https://drive.google.com/file/d/1qrQZNV8XVNltXl15uFRakusfbHBKMJHn/view?usp=sharing";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share"));
                break;

            case R.id.nav_about:
                Intent intent4 = new Intent(NavigationMain.this, AboutApp.class);
                startActivity(intent4);
                break;

            case R.id.nav_logout:
                Logout();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        MyCustomdialog();
    }

    private void MyCustomdialog() {

        MyDialog = new Dialog(NavigationMain.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_logout_dialog);

        yes = MyDialog.findViewById(R.id.yes_id);
        no = MyDialog.findViewById(R.id.no_id);

        yes.setEnabled(true);
        no.setEnabled(true);

        MyDialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(NavigationMain.this, LoginScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You Say No to Log Out",Toast.LENGTH_LONG).show();
                MyDialog.cancel();
            }
        });



    }

}