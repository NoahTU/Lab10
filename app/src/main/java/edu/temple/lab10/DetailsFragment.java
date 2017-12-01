package edu.temple.lab10;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        textView=  (TextView)RootView.findViewById(R.id.details);
        return RootView;
    }

    void setMessage(String message, int ppos) throws JSONException {

        System.out.println("End?: "+message);
        //textView.setText(message);
        /*while(!readFromFile().equals("[")){
            System.out.println("BLANK");
        }*/
        int test=0;
        while(test<1000){
            System.out.println("FINAL #"+test+": " + readFromFile());
            test++;
        }


        JSONArray jarray = new JSONArray(readFromFile());
        JSONObject company = (JSONObject) jarray.get(ppos);
        String name= company.optString("Name");

        textView.setText(name);


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
            InputStream inputStream = getContext().openFileInput("config.txt");

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
