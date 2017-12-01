package edu.temple.lab10;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavFragment.NavInterface{

    boolean twoPanes;

    final Context context = this;
    private Button button;
    private EditText result;

    final Handler ha=new Handler();

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    NavFragment nav = new NavFragment();
    FragmentManager fragmentManager = getFragmentManager();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    RefreshService RS= new RefreshService();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = new Bundle();
        bundle.putString("newStock", "");
        //NavFragment fragobj = new NavFragment();
        //nav.setArguments(bundle);

        //  Determine if only one or two panes are visible
        twoPanes = (findViewById(R.id.fragment_details) != null);

        System.out.println(twoPanes);
        System.out.println(twoPanes);
        //  Load navigation fragment by default
        //NavFragment nav = new NavFragment();
        //FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_nav, nav);
        nav.setArguments(bundle);
        fragmentTransaction.commit();
        getFragmentManager()
                .executePendingTransactions();

        /*
         *  Check if details pain is visible in current layout (e.g. large or landscape)
         *  and load fragment if true.
         */
        if (twoPanes){
            DetailsFragment dets = new DetailsFragment();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_details, dets);
            fragmentTransaction.commit();
        }




        //File mFolder = new File(getFilesDir() + "/sample");
        File mFolder = new File("/data/user/0/edu.temple.lab10/files");
        File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        if (!imgFile.exists()) {
            try {
                imgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent stockQuoteIntent = new Intent(MainActivity.this, RS.getClass());
        stockQuoteIntent.putStringArrayListExtra("stock_symbols", listItems);
        startService(stockQuoteIntent);

        System.out.println("Mark: "+listItems.isEmpty());
        if(!listItems.isEmpty()) {
            System.out.println("LOOP IS ENABLED");
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //call function
                    try {
                        PrintWriter writer = new PrintWriter("config.txt");
                        writer.print("");
                        writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // should be loop for every 60 seconds, don't forget if null
                    RS.setSymbol(listItems);

                    ha.postDelayed(this, 60000);
                }
            }, 60000);
        }

        /*File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "config.txt");*/


        /////////////////////////////////////////////////////////////////////////////////////
        /*try{
        PrintWriter writer = new PrintWriter("config.txt");
        writer.print("");
        writer.close();} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // should be loop for every 60 seconds, don't forget if null
        int i=0;
        String blank="";
        Intent stockQuoteIntent = new Intent(MainActivity.this, RefreshService.class);
        //stockQuoteIntent.putExtra("stock_symbol"+Integer.toString(i), blank);
        startService(stockQuoteIntent);*/
        ////////////////////////////////////////////////////////////////////////////////////////



       /* adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(adapter);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        lv.setEmptyView(emptyText);


        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        String ChoreString = String.valueOf(parent.getItemAtPosition(position));


                    }
                }

        );*/



        /*FloatingActionButton myFab = (FloatingActionButton) (findViewById(R.id.floatingActionButton));
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(v, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                System.out.println("Action buttone works");

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        System.out.println(userInput.getText());
                                        //lv.add(userInput.getText());
                                        listItems.add(userInput.getText().toString());
                                        Bundle bundle = new Bundle();

                                        bundle.putString("newStock", userInput.getText().toString());

                                        // set Fragmentclass Arguments
                                        NavFragment fragobj = (NavFragment) getFragmentManager().findFragmentById(R.id.fragment_nav);

                                        //fragobj.getArguments().putString("newStock", userInput.getText().toString());

                                        //fragobj.setUpLayout();

                                        fragobj.setArguments(bundle);
                                        //adapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel",
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
        });*/
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.switch_fragments) {
            //  Only display switch action if in single pane mode
            if (!twoPanes) {
                doTransition();
            } else {
                Toast.makeText(MainActivity.this, "Action Disabled", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void doTransition(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_nav, new DetailsFragment())
                .addToBackStack(null)
                .commit();
    }


    public void acceptMessage(String message, int pos) throws JSONException {
        System.out.println("From Activity:  "+message);
        listItems.add(message);
        //get detail to set that information
        DetailsFragment receiver= new DetailsFragment();

        if (!twoPanes) {
            getFragmentManager()
                    .beginTransaction()
                    // .add(R.id.fragment_details, receiver)
                    .replace(R.id.fragment_nav, receiver)
                    .addToBackStack(null)
                    .commit();
            getFragmentManager()
                    .executePendingTransactions();


            //Intent stockQuoteIntent = new Intent(MainActivity.this, RefreshService.class);
            //stockQuoteIntent.putStringArrayListExtra("stock_symbols", listItems);
            System.out.println("THE MARK 3");
            //startService(stockQuoteIntent);
            RS.setSymbol(listItems);
            receiver.setMessage(message, pos);
        }
    }


}
