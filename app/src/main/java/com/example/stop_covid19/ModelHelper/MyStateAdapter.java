package com.example.stop_covid19.ModelHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.stop_covid19.R;

import java.text.NumberFormat;
import java.util.List;

public class MyStateAdapter extends ArrayAdapter<StatesModel> {

    private Context context;
    private List<StatesModel> statesModelsList;  // List of type StateModel

    public MyStateAdapter(@NonNull Context context,List<StatesModel> statesModelsList) {
        super(context, R.layout.mystate_custom_items,statesModelsList);

        this.context = context;
        this.statesModelsList = statesModelsList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mystate_custom_items, null, true);
        TextView txt_deaths = view.findViewById(R.id.deaths_india_item);
        TextView txt_confirmcases = view.findViewById(R.id.confirm_cases_india_item);
        TextView txt_recovered = view.findViewById(R.id.recovered_india_item);
        TextView txt_statename = view.findViewById(R.id.states_name);



        txt_deaths.setText(NumberFormat.getInstance().format(Integer.parseInt(statesModelsList.get(position).getDeaths())));
        txt_confirmcases.setText(NumberFormat.getInstance().format(Integer.parseInt(statesModelsList.get(position).getConfirmed_cases())));
        txt_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(statesModelsList.get(position).getRecovered())));
        txt_statename.setText(statesModelsList.get(position).getStatename());

        return view;
    }

    @Override
    public int getCount() {
        return statesModelsList.size();
    }

    @Nullable
    @Override
    public StatesModel getItem(int position) {
        return statesModelsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
