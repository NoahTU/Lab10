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

        //  Determine if only one or two panes are visible
        twoPanes = (findViewById(R.id.fragment_details) != null);

        System.out.println(twoPanes);
        System.out.println(twoPanes);

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


        System.out.println("Mark: "+listItems.isEmpty());

            System.out.println("LOOP IS ENABLED");
    }


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



    @Override
    public void onStop(){
        System.out.println("Stop Connection: "+connected);
        super.onStop();
        unbindService(connection);

    }



    private void doTransition(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_nav, new DetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void updateL(String message){
        if (listItems.contains(message.toLowerCase())) {

        }
        else if(listItems.contains(message.toUpperCase())){

        }
        else if(listItems.contains(message)){

        }
        else{
            listItems.add(message);
            RS.setter(listItems);

        }

        System.out.println("THE List Items: "+listItems);
    }



    public void acceptMessage(String message, int pos) throws JSONException {
        System.out.println("From Activity:  "+message);

        DetailsFragment receiver= new DetailsFragment();

        if (!twoPanes) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_nav, receiver)
                    .addToBackStack(null)
                    .commit();
            getFragmentManager()
                    .executePendingTransactions();

            System.out.println("THE MARK 3");
            receiver.setMessage(message, pos, con);
        }
        else{
            dets.setMessage(message, pos, con);
        }
    }


}
