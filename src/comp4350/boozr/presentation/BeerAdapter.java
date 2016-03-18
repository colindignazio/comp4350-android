package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import comp4350.boozr.R;

import java.util.List;


/**
 * Created by David on 15/03/2016.
 */
public class BeerAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    JSONArray data = null;

    public BeerAdapter(Context context, int layoutResourceId, JSONArray data, List<String> beerArrayList){
        super(context, layoutResourceId, beerArrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    static class BeerHolder
    {
    	TextView txtId;
        TextView txtName;
        TextView txtBrewery;
        TextView txtType;
        TextView txtAlc;
        TextView txtRating;
        TextView txtPrice;

    }
    
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        View row = convertView;
        BeerHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BeerHolder();
            
            holder.txtId = (TextView)row.findViewById(R.id.drink_id);
            holder.txtName = (TextView)row.findViewById(R.id.beer_name);
            holder.txtBrewery = (TextView)row.findViewById(R.id.brewery_name);
            holder.txtType = (TextView)row.findViewById(R.id.beer_type);
            holder.txtAlc = (TextView)row.findViewById(R.id.beer_alc);
            holder.txtRating = (TextView)row.findViewById(R.id.beer_rating);
            holder.txtPrice = (TextView)row.findViewById(R.id.beer_price);


            row.setTag(holder);
        }
        else
        {
            holder = (BeerHolder)row.getTag();
        }

        try {
            JSONObject beer = data.getJSONObject(position);
            holder.txtId.setText(beer.getString("Beer_id"));
            holder.txtName.setText(beer.getString("Name"));
            holder.txtBrewery.setText(beer.getString("Brewery"));
            holder.txtType.setText(beer.getString("Type"));
            holder.txtAlc.setText(beer.getString("Alcohol_By_Volume"));
            holder.txtRating.setText(beer.getString("Rating"));
            holder.txtPrice.setText(beer.getString("AvgPrice"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }


}
