package com.example.stop_covid19.ActivityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stop_covid19.ModelHelper.CountryModel;
import com.example.stop_covid19.ModelHelper.MyCustomAdapter;
import com.example.stop_covid19.R;
import com.google.android.material.textfield.TextInputEditText;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCountry extends AppCompatActivity {

    SimpleArcLoader simpleArcLoader;
    TextInputEditText textInputEditText;
    ListView listView;

    public static List<CountryModel> countryModelList = new ArrayList<>();
    CountryModel countryModel;
    MyCustomAdapter myCustomAdapter;
    ImageView back, refresh;
    RelativeLayout progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_country);

        simpleArcLoader= findViewById(R.id.loader_arc_mycountry);
        textInputEditText= findViewById(R.id.id_search);
        listView= findViewById(R.id.listview);
        back = findViewById(R.id.back_arrow_mycountry);
        refresh = findViewById(R.id.refresh_mycountry);
        progressDialog = findViewById(R.id.progress_dialog);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), DetailCountry.class).putExtra("position", position));
            }
        });
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();

            }
        });

    }

    private void fetchData() {

        progressDialog.setVisibility(View.VISIBLE);

        String url = "https://disease.sh/v3/covid-19/countries";

        //simpleArcLoader.start();

        //getting data from api using volley

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //getting the json response here


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i=0;i<jsonArray.length();i++ ){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String countryname = jsonObject.getString("country");
                                String cases = jsonObject.getString("cases");
                                String today_cases = jsonObject.getString("todayCases");
                                String deaths = jsonObject.getString("deaths");
                                String today_deaths = jsonObject.getString("todayDeaths");
                                String recovered = jsonObject.getString("recovered");
                                String active = jsonObject.getString("active");
                                String population = jsonObject.getString("population");
                                String continent = jsonObject.getString("continent");


                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagurl = object.getString("flag");


                                countryModel = new CountryModel(flagurl, countryname, cases, today_cases, deaths, today_deaths, recovered, active, population, continent);
                                countryModelList.add(countryModel);

                            }


                            myCustomAdapter = new MyCustomAdapter(MyCountry.this, countryModelList);
                            listView.setAdapter(myCustomAdapter);
                            //simpleArcLoader.stop();
                            progressDialog.setVisibility(View.GONE);

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                          //  simpleArcLoader.stop();
                            progressDialog.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // simpleArcLoader.stop();
                        progressDialog.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}