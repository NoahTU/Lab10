package edu.temple.lab10;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavFragment.NavInterface{

    boolean twoPanes;
    private boolean connected;

    final Context con = this;
    private Button button;
    private EditText result;

    final Handler ha=new Handler();

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    List<String> listItems= Collections.synchronizedList(new ArrayList<String>());


    Handler mHandler = new Handler();
    NavFragment nav = new NavFragment();
    FragmentManager fragmentManager = getFragmentManager();
    DetailsFragment dets = new DetailsFragment();

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

        /*Intent intent = new Intent(MainActivity.this,RS.getClass());
        startService(intent);
        bindService(intent, connection, Context.BIND_ADJUST_WITH_ACTIVITY | Context.BIND_AUTO_CREATE);
        System.out.println("Service Bound");*/

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
            dets = new DetailsFragment();
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


        File mmFolder = new File("/data/user/0/edu.temple.lab10/files");
        File immgFile = new File(mFolder.getAbsolutePath() + "/configg.txt");
        if (!mmFolder.exists()) {
            mmFolder.mkdir();
        }
        if (!immgFile.exists()) {
            try {
                immgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Intent stockQuoteIntent = new Intent(MainActivity.this, RS.getClass());
        //stockQuoteIntent.putExtra("stock_symbols", listItems);
       // startService(stockQuoteIntent);

        System.out.println("Mark: "+listItems.isEmpty());

            System.out.println("LOOP IS ENABLED");
            /*ha.postDelayed(new Runnable() {

                @Override
                public void run() {

                    // should be loop for every 60 seconds, don't forget if null
                    if(!listItems.isEmpty()) {
                        RS.setSymbol(listItems);
                        System.out.println("Updated");
                    }
                    ha.postDelayed(this, 20000);
                }
            }, 20000);*/

        /*final List li=listItems;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        System.out.println("LOOP Running");
                        Thread.sleep(60000);
                        System.out.println("LOOP Running2");
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                System.out.println("list item: "+listItems);

                                System.out.println("list itemS: "+li);
                                //System.out.println("Currently in RS: "+RS.getterSymbol());
                                //RS.setSymbol(RS.getterSymbol());
                                RS.go();
                                //RS.timedL();
                                System.out.println("Updated");
                                Toast.makeText(getApplicationContext(), getString(R.string.up),
                                        Toast.LENGTH_SHORT).show();
                                // Write your code here to update the UI.
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();*/



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

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("STARTED");
        Intent intent = new Intent(MainActivity.this,RS.getClass());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //Context.BIND_ADJUST_WITH_ACTIVITY |
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            RefreshService.refreshBinder binder = (RefreshService.refreshBinder) service;
            RS = binder.getService();
            connected = true;
            System.out.println("Initial Connection: "+connected);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            connected = false;
        }
    };

    /*@Override
    public void onPause(){
        System.out.println("Pause Connection: "+connected);
        if(connected){

            unbindService(connection);

        }

        //Intent intent = new Intent(MainActivity.this,RefreshService.class);
        //bindService(intent, connection, Context.BIND_ADJUST_WITH_ACTIVITY | Context.BIND_AUTO_CREATE);
        super.onPause();
    }*/

    @Override
    public void onStop(){
        System.out.println("Stop Connection: "+connected);
        super.onStop();
        unbindService(connection);


        //Intent intent = new Intent(MainActivity.this,RefreshService.class);
        //bindService(intent, connection, Context.BIND_ADJUST_WITH_ACTIVITY | Context.BIND_AUTO_CREATE);

    }

   /* @Override
    public void onResume(){
        System.out.println("Resume Connection: "+connected);
        if(connected){


        }
        else{
            Intent intent = new Intent(MainActivity.this,RefreshService.class);
            bindService(intent, connection, Context.BIND_ADJUST_WITH_ACTIVITY | Context.BIND_AUTO_CREATE);
        }

        super.onResume();
    }

    @Override
    public void onDestroy(){
        System.out.println("Connection: "+connected);
        if(connected){
            System.out.println("Unbound");
            System.out.println("Unbound");
            System.out.println("Unbound");
            System.out.println("Unbound");
            System.out.println("Unbound");
            unbindService(connection);
       }
        System.out.println("NOOOOO");
        System.out.println("NOOOOO");
        System.out.println("NOOOOO");
        System.out.println("NOOOOO");
        super.onDestroy();
    }*/

    private void doTransition(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_nav, new DetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void updateL(String message){
        if (listItems.contains(message.toLowerCase())) {
            //listItems.add(temp);
        }
        else if(listItems.contains(message.toUpperCase())){

        }
        else if(listItems.contains(message)){

        }
        else{
            listItems.add(message);
            RS.setter(listItems);
            //listItems.n
            //listItems.notifyAll();
        }
        //listItems.add(message);
        System.out.println("THE List Items: "+listItems);
    }

    public List getL(){
        return listItems;
    }


    public void acceptMessage(String message, int pos) throws JSONException {
        System.out.println("From Activity:  "+message);
        /*if (listItems.contains(message.toLowerCase())) {
            //listItems.add(temp);
        }
        else if(listItems.contains(message.toUpperCase())){

        }
        else if(listItems.contains(message)){

        }
        else{
            listItems.add(message);
        }*/
        //listItems.add(message);
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
            //S.setSymbol(listItems);
            receiver.setMessage(message, pos, con);
        }
        else{
            dets.setMessage(message, pos, con);
        }
    }


}
