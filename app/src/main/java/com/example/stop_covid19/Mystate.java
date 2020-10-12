package com.example.stop_covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stop_covid19.ModelHelper.CountryModel;
import com.example.stop_covid19.ModelHelper.MyCustomAdapter;
import com.example.stop_covid19.ModelHelper.MyStateAdapter;
import com.example.stop_covid19.ModelHelper.StatesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Mystate extends AppCompatActivity {

TextView txt_date, txt_time, txt_cases, txt_deaths, txt_recovered;
String str_date_time, str_cases, str_deaths, str_recovered;
ListView listview_states;
SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout progressDialog;


    public static List<StatesModel> statesModelList = new ArrayList<>();
    StatesModel statesModel;
    MyStateAdapter myStateAdapter;
    ImageView back, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystate);

        //Initialise
        Init();

        fetchData();

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


       /* swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }); */
    }

    private void fetchData() {

        progressDialog.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://api.covid19india.org/data.json";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray_allstate = null;

                try {
                    jsonArray_allstate = response.getJSONArray("statewise");
                    JSONObject data_india = jsonArray_allstate.getJSONObject(0);

                    str_cases = data_india.getString("confirmed");
                    str_deaths = data_india.getString("deaths");
                    str_recovered = data_india.getString("recovered");
                    str_date_time = data_india.getString("lastupdatedtime");

                    for (int i=1; i < jsonArray_allstate.length();i++ ) {

                        JSONObject states_data = jsonArray_allstate.getJSONObject(i);

                        String deaths = states_data.getString("deaths");
                        String confirmcases = states_data.getString("confirmed");
                        String recovered = states_data.getString("recovered");
                        String state = states_data.getString("state");



                        statesModel = new StatesModel(deaths, confirmcases, recovered, state);
                        statesModelList.add(statesModel);
                    }

                    myStateAdapter = new MyStateAdapter(Mystate.this, statesModelList);
                    listview_states.setAdapter(myStateAdapter);

                    Handler delaytoshowprogress = new Handler();

                    delaytoshowprogress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //setting text in the textview

                            txt_cases.setText(NumberFormat.getInstance().format(Integer.parseInt(str_cases)));
                            txt_deaths.setText(NumberFormat.getInstance().format(Integer.parseInt(str_deaths)));
                            txt_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                            txt_date.setText(FormatDate(str_date_time,1));
                            txt_time.setText(FormatDate(str_date_time,2));

                            progressDialog.setVisibility(View.GONE);

                        }
                    },1000);


                } catch (JSONException e) {

                    progressDialog.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                );

        requestQueue.add(request);
    }

    public String FormatDate(String date, int testcases){

        Date mDate = null;
        String dateFormat;

       try {
           mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);

           if (testcases == 0){
               dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
               return dateFormat;
           }
           else if (testcases == 1){
               dateFormat = new SimpleDateFormat("dd MMM yyyy").format(mDate);
               return dateFormat;
           }
           else if (testcases == 2){
               dateFormat = new SimpleDateFormat("hh:mm a").format(mDate);
               return dateFormat;
           }
           else {
               Log.d("error", "Wrong input! Choose from 0 to 2");
               return "Error";
           }

       }
       catch (ParseException e){

           e.printStackTrace();
           return date;
       }

    }


    private void Init() {

        txt_date = findViewById(R.id.textdate);
        txt_time = findViewById(R.id.texttime);
        txt_cases = findViewById(R.id.confirm_cases_india);
        txt_deaths = findViewById(R.id.deaths_india);
        txt_recovered = findViewById(R.id.recovered_india);
        listview_states = findViewById(R.id.listview_mystate);
        back = findViewById(R.id.back_arrow_mystate);
        refresh = findViewById(R.id.refresh_mystate);
        progressDialog = findViewById(R.id.progress_dialog);
        //swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

    }
}