package com.example.stop_covid19.ActivityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stop_covid19.R;

import java.text.NumberFormat;

public class DetailCountry extends AppCompatActivity {


    private int positionCountry;

    TextView country_txt, cases_txt, todaycases_txt, deaths_txt, today_deaths_txt, recovered_txt, active_txt, population_txt, continent_txt;
    ImageView back, refresh;
    RelativeLayout progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_country);


      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

        Init();   //initialization

     funcTion();



   /*  refresh.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             funcTion();
         }
     });

    */


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void funcTion() {

        progressDialog.setVisibility(View.VISIBLE);



        Intent intent = getIntent();
        positionCountry = intent.getIntExtra("position", 0);

        country_txt.setText(MyCountry.countryModelList.get(positionCountry).getCountry());
        cases_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getCases())));
        todaycases_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getToday_cases())));
        deaths_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getDeaths())));
        today_deaths_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getToday_deaths())));
        recovered_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getRecovered())));
        active_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getActive())));
        population_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(MyCountry.countryModelList.get(positionCountry).getPopulation())));
        continent_txt.setText(MyCountry.countryModelList.get(positionCountry).getContinent());




        progressDialog.setVisibility(View.GONE);
    }

    private void Init() {

        country_txt = findViewById(R.id.text_countrydt);
        cases_txt = findViewById(R.id.text_casesdt);
        todaycases_txt = findViewById(R.id.text_todaycasesdt);
        deaths_txt = findViewById(R.id.text_deathsdt);
        today_deaths_txt = findViewById(R.id.text_todaydeathsdt);
        recovered_txt = findViewById(R.id.text_recovereddt);
        active_txt = findViewById(R.id.text_activedt);
        population_txt = findViewById(R.id.text_populationdt);
        continent_txt = findViewById(R.id.text_continentdt);
        back = findViewById(R.id.back_arrow_detail);
       // refresh = (ImageView) findViewById(R.id.refresh_detail);
        progressDialog= findViewById(R.id.progress_dialog);
    }


}