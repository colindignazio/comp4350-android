package comp4350.boozr.objects;

import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.util.*;
import android.os.AsyncTask;
import comp4350.boozr.business.APIUtils;

public class API extends AsyncTask<String, Void, String> {
    private String apiUrl = "http://54.200.14.217/?/";

    @Override
    protected String doInBackground(String... input) {
        // The input strings MUST contain the url first followed by name value pairs
        // A null result will be returned if you fail to pass the url as the first param
        if(input[0] == null || input[0].equals("") || !input[0].contains("/")) {
            //You did not supply a url as the first param
            return "URL FORMAT ERROR";
        } else if(input.length%2 != 1) {
            //You did not specify key-value pairs for every set
            //ie. you added a key without a value
            return "KEY-VALUE MISMATCH ERROR";
        } else {
            //The URL is at least a valid format
            String result = "";
            try {
                HashMap<String, String> temp = new HashMap<>();
                for(int i=1; i < input.length-1; i+=2) {
                    temp.put(input[i], input[i+1]);
                }
                result = this.sendRequest(input[0], temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public String sendRequest(String method, HashMap<String, String> params) {
        URL url;
        String response = "";
        try {
            url = new URL(this.apiUrl + method);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(APIUtils.paramsToString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            } else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
