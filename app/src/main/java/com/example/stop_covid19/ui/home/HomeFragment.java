package com.example.stop_covid19.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stop_covid19.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class HomeFragment extends Fragment {

   // private HomeViewModel homeViewModel;

    TextView total_case_txt, recovered_txt, deaths_txt, active_txt, affectedcountries_txt;
    String total_case_str, recovered_str, deaths_str, active_str;
    SimpleArcLoader simpleArcLoader;
    PieChart pieChart;
    ScrollView scrollView;
    RelativeLayout progressDialog;

    AppCompatButton call_btn, sms_btn;
    private static final int REQUEST_CALL = 1;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);


        // declare variable and initialize layout ui with java

        total_case_txt = view.findViewById(R.id.total_cases_id);
        recovered_txt = view.findViewById(R.id.recovered_id);
        deaths_txt = view.findViewById(R.id.deaths_id);
        active_txt = view.findViewById(R.id.active_id);
        affectedcountries_txt = view.findViewById(R.id.affected_countries_id);

        simpleArcLoader = view.findViewById(R.id.loader_arc);
        pieChart = view.findViewById(R.id.piechart);
        scrollView = view.findViewById(R.id.scrollview_globaldata);
        progressDialog = view.findViewById(R.id.progress_dialog);
        call_btn = view.findViewById(R.id.callnow_button);
        sms_btn = view.findViewById(R.id.sms_button);

        fetchData();



        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+911123978046"));
                startActivity(intent);
            }
        });

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + "+91 9013353535");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);

            }
        });

        return view;
    }

    private void fetchData() {

        progressDialog.setVisibility(View.VISIBLE);

        String url = "https://disease.sh/v3/covid-19/all";

       // simpleArcLoader.start();


        //getting data from api using volley

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //getting the json response here

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            total_case_str = jsonObject.getString("cases");
                            recovered_str = jsonObject.getString("recovered");
                            deaths_str = jsonObject.getString("deaths");
                            active_str = jsonObject.getString("active");

                            total_case_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(total_case_str)));
                            recovered_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered_str)));
                            deaths_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(deaths_str)));
                            active_txt.setText(NumberFormat.getInstance().format(Integer.parseInt(active_str)));
                            affectedcountries_txt.setText(jsonObject.getString("affectedCountries"));

                            pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(total_case_str), Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered_str), Color.parseColor("#66BB6A")));
                            pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(deaths_str), Color.parseColor("#EF5350")));
                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active_str), Color.parseColor("#29B6F6")));

                            pieChart.startAnimation();

                           // simpleArcLoader.stop();
                            progressDialog.setVisibility(View.GONE);
                            //simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressDialog.setVisibility(View.GONE);
                            //simpleArcLoader.stop();
                            //simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //simpleArcLoader.stop();
                        //simpleArcLoader.setVisibility(View.GONE);
                        progressDialog.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }
}