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

import java.util.List;

import comp4350.boozr.R;

/**
 * Created by David on 15/03/2016.
 */
public class BeerAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    JSONArray data = null;

    public BeerAdapter(Context context, int layoutResourceId, JSONArray data, List<String> beerArrayList) throws JSONException {
        super(context, layoutResourceId, beerArrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    static class BeerHolder
    {
        TextView txtName;
        TextView txtBrewery;
        TextView txtType;
        TextView txtAlc;
        TextView txtrating;

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

            holder.txtName = (TextView)row.findViewById(R.id.user_name);
            holder.txtBrewery = (TextView)row.findViewById(R.id.user_email);
            holder.txtType = (TextView)row.findViewById(R.id.beer_type);
            holder.txtAlc = (TextView)row.findViewById(R.id.beer_alc);
            holder.txtrating = (TextView)row.findViewById(R.id.beer_rating);


            row.setTag(holder);
        }
        else
        {
            holder = (BeerHolder)row.getTag();
        }

        try {
            JSONObject beer = data.getJSONObject(position);
            holder.txtName.setText(beer.getString("Name"));
            holder.txtBrewery.setText(beer.getString("Brewery"));
            holder.txtType.setText(beer.getString("Type"));
            holder.txtAlc.setText(beer.getString("Alcohol_By_Volume"));
            holder.txtrating.setText(beer.getString("Rating"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }


}
