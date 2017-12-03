package edu.temple.lab10;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xxnoa_000 on 11/16/2017.
 */

public class NavFragment extends Fragment {

    TextView mTextView;

    RefreshService RS= new RefreshService();
    NavInterface activty;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    //ArrayList<String> listItems=new ArrayList<String>();
    List<String> listItems= Collections.synchronizedList(new ArrayList<String>());

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    String ns;

    public static NavFragment newInstance() {
        return new NavFragment();
    }

    public NavFragment() {
        // Required empty public constructor
        super();
        // Just to be an empty Bundle. You can use this later with getArguments().set...
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Activity c) {
        super.onAttach(c);
        activty = (NavInterface) c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            String temp="";
            JSONArray jarray = new JSONArray(readFromFile());

            if(!jarray.isNull(0)) {
                System.out.println("length: "+jarray.length());
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject sym = (JSONObject) jarray.get(i);
                    temp= sym.optString("Symbol");
                    //System.out.println("Current Temp: "+temp);
                    //System.out.println("LOADING: "+listItems.contains(temp.toLowerCase()));
                    System.out.println("LOADING2: "+listItems.contains(temp));
                    //System.out.println("LOADING3: "+listItems.contains(temp.toUpperCase()));
                    if (listItems.contains(temp.toLowerCase())) {
                        //listItems.add(temp);
                    }
                    else if(listItems.contains(temp.toUpperCase())){

                    }
                    else if(listItems.contains(temp)){

                    }
                    else{
                        listItems.add(temp);
                        activty.updateL(temp);
                    }
                }
                //System.out.println("Nav's array: "+listItems);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        /*ns = getArguments().getString("newStock");
        if (!ns.equals(null)&&!ns.equals("")){
            listItems.add(ns);
            adapter.notifyDataSetChanged();
        }
        else if(ns.equals("")){
            System.out.println("ns is null");
        }
        else{
            System.out.println("ns is null");
        }*/
        //listItems.add(ns);
        View view=inflater.inflate(R.layout.fragment_nav, container, false);
       // mTextView = (TextView) view.findViewById(R.id.textView1);



        FloatingActionButton myFab = (FloatingActionButton) (view.findViewById(R.id.floatingActionButton));
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(v, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                //System.out.println("Action button works");

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        System.out.println(userInput.getText());
                                        //lv.add(userInput.getText());
                                        listItems.add(userInput.getText().toString());
                                        activty.updateL(userInput.getText().toString());
                                        RS.setSymbol(listItems);
                                        writeToFile(listItems.toString());
                                        Bundle bundle = new Bundle();

                                        //*bundle.putString("newStock", userInput.getText().toString());

                                        // set Fragmentclass Arguments
                                        //*NavFragment fragobj = (NavFragment) getFragmentManager().findFragmentById(R.id.fragment_nav);

                                        //fragobj.getArguments().putString("newStock", userInput.getText().toString());

                                        //fragobj.setUpLayout();

                                       //* fragobj.setArguments(bundle);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


        adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setAdapter(adapter);
        TextView emptyText = (TextView)view.findViewById(android.R.id.empty);
        lv.setEmptyView(emptyText);
       // listItems.add(ns);


        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        String ChoreString = String.valueOf(parent.getItemAtPosition(position));

                        System.out.println("What was picked: "+ChoreString);
                        try {
                            System.out.println("position: "+position);
                            activty.acceptMessage(ChoreString, position);
                            //System.out.println("position: "+position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

        );

        return view;
    }


    public interface NavInterface {
        public void acceptMessage(String message, int position) throws JSONException;
        public void updateL(String message);
    }


    private void writeToFile(String data) {
        try {
            System.out.println("DATA: "+data);
            File mFolder = new File("/data/user/0/edu.temple.lab10/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/configg.txt");

            FileOutputStream fOut = new FileOutputStream(imgFile, false);/// may have to delete false
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            //myOutWriter.append(data);
            myOutWriter.write(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();

            /*FileOutputStream outputStreamWriter = this.openFileOutput("config.txt", Context.MODE_PRIVATE);
            outputStreamWriter.write(data);
            outputStreamWriter.close();*/
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }



    public String readFromFile() {

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