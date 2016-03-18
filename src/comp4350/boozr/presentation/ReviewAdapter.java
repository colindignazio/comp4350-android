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


public class ReviewAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    JSONArray data = null;

    public ReviewAdapter(Context context, int layoutResourceId, JSONArray data, List<String> reviewArrayList){
        super(context, layoutResourceId, reviewArrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    static class ReviewHolder
    {
    	TextView starRating;
    	TextView review;

    }
    
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        View row = convertView;
        ReviewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ReviewHolder();
            holder.starRating = (TextView)row.findViewById(R.id.ratingTextView);
            holder.review = (TextView)row.findViewById(R.id.reviewTextView);


            row.setTag(holder);
        }
        else
        {
            holder = (ReviewHolder)row.getTag();
        }

        try {
            JSONObject review = data.getJSONObject(position);
            holder.starRating.setText(review.getString("stars"));
            holder.review.setText(review.getString("review"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }


}
