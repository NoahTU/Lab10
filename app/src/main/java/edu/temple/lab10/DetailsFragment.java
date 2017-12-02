package edu.temple.lab10;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xxnoa_000 on 11/16/2017.
 */

public class DetailsFragment extends Fragment {

    TextView textView;

    View RootView;
    ImageView ImageView;

    String name= "";
    String price= "";
    String sy= "";

    Context te;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView= inflater.inflate(R.layout.fragment_details, container, false);

        ImageView= (ImageView) RootView.findViewById(R.id.imageView);

        textView=  (TextView)RootView.findViewById(R.id.details);

        /*new DownloadImageTask((ImageView) RootView.findViewById(R.id.imageView)).execute("https://finance.google.com/finance/getchart?p=5d&q="+sy);
        textView.setText(name+"\n$"+price);*/

        return RootView;
    }

    void setMessage(String message, int ppos, Context c) throws JSONException {

        System.out.println("End?: "+message);

        te=c;


        JSONArray jarray = new JSONArray(readFromFile());
        JSONObject company = (JSONObject) jarray.get(ppos);
        name= company.optString("Name");
        price= company.optString("LastPrice");
        sy= company.optString("Symbol");

        new DownloadImageTask(ImageView).execute("https://finance.google.com/finance/getchart?p=5d&q="+sy);
        textView.setText(name+"\n$"+price);


        //json functions
        //http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=<string>
    }

    public String readFromFile() {

        /*String ret = "";

        try {

            File mFolder = new File("/data/user/0/edu.temple.lab10/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");
            InputStream inputStream = getContext().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(new FileReader(imgFile));
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        System.out.println("The String: "+ret);
        return ret;*/



        String ret = "";

        try {
            InputStream inputStream = te.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
