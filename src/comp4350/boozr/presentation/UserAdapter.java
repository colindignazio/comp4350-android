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
public class UserAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    JSONArray data = null;

    public UserAdapter(Context context, int layoutResourceId, JSONArray data, List<String> userArrayList) throws JSONException {
        super(context, layoutResourceId, userArrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    static class UserHolder
    {
    	TextView txtId;
        TextView txtName;
        TextView txtEmail;
        TextView txtLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        View row = convertView;
        UserHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserHolder();

            holder.txtId = (TextView)row.findViewById(R.id.user_id);
            holder.txtName = (TextView)row.findViewById(R.id.user_name);
            holder.txtEmail = (TextView)row.findViewById(R.id.user_email);
            holder.txtLocation = (TextView)row.findViewById(R.id.user_location);
            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder)row.getTag();
        }

        try {
            JSONObject user = data.getJSONObject(position);
            holder.txtId.setText(user.getString("User_id"));
            holder.txtName.setText(user.getString("User_name"));
            holder.txtEmail.setText(user.getString("User_email"));
            holder.txtLocation.setText(user.getString("User_location"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }

}
