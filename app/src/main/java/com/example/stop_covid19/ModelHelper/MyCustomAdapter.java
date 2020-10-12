package com.example.stop_covid19.ModelHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.stop_covid19.ActivityClasses.MyCountry;
import com.example.stop_covid19.R;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<CountryModel> {  //creating a adapter class of country model.

    private Context context;
    private List<CountryModel> countryModelList;  // List of type CountryModel
    private List<CountryModel> countryModelListFiltered;


    public MyCustomAdapter(@NonNull Context context, List<CountryModel> countryModelList) {
        super(context, R.layout.cutom_list_item, countryModelList);

        this.context = context;
        this.countryModelList = countryModelList;
        this.countryModelListFiltered = countryModelList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_list_item, null, true);
        TextView Country_name = view.findViewById(R.id.country_name);
        ImageView flag_image = view.findViewById(R.id.image_flag);



        Country_name.setText(countryModelListFiltered.get(position).getCountry());
        Glide.with(context).load(countryModelListFiltered.get(position).getFlag()).into(flag_image);

        return view;
    }

    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }

    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if ((constraint == null) || constraint.length() == 0)
                {
              filterResults.count = countryModelList.size();
              filterResults.values = countryModelList;
                }
                else{
                    List<CountryModel> resultsModel = new ArrayList<>();
                    String searchstr = constraint.toString().toLowerCase();

                for (CountryModel itemModel:countryModelList){
                    if (itemModel.getCountry().toLowerCase().contains(searchstr)){
                        resultsModel.add(itemModel);
                    }

                    filterResults.count = resultsModel.size();
                    filterResults.values = resultsModel;
                }

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryModelListFiltered = (List<CountryModel>) results.values;
                MyCountry.countryModelList = (List<CountryModel>) results.values;
                notifyDataSetChanged();

            }
        };

        return filter;
    }
}
