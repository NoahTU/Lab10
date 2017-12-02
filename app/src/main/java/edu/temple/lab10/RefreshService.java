package edu.temple.lab10;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by xxnoa_000 on 11/28/2017.
 */

public class RefreshService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    ArrayList<String> symbol;

    public void setSymbol(ArrayList<String> s) {
        this.symbol = s;
        //System.out.println("THE MARK 5");
        getQuote(symbol);

    }

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //go through file and get jobject symbols

        //String[] array=intent.getStringArray("stock_symbol");
        System.out.println("THE MARK 2");
        getQuote(intent.getStringArrayListExtra("stock_symbol"));
    }


    public void getQuote(final ArrayList<String> symbol) {

        //URL stockQuoteUrl;
        System.out.println("THE MARK 1");

        writeToFile("[");
        Thread t = new Thread() {
            @Override
            public void run() {
                URL stockQuoteUrl;
                //writeToFile("[");
                int temp = 0;
                String finalresponse="[";
                for (int i = 0; i < symbol.size(); i++) {

                    try {
                        //System.out.println(symbol[i]);
                        if (i == 0) {
                            //writeToFile("[");
                            //System.out.println("WROTE 1");
                        }
                        System.out.println("Current Symbol: " + symbol.get(i).toUpperCase());

                        stockQuoteUrl = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + symbol.get(i).toUpperCase());

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        stockQuoteUrl.openStream()));

                        String response = "", tmpResponse;

                        tmpResponse = reader.readLine();
                        while (tmpResponse != null) {
                            response = response + tmpResponse;
                            tmpResponse = reader.readLine();
                        }

                        JSONObject stockObject = new JSONObject(response);
                        System.out.println("THE STOCK: " + stockObject.toString());
                        //writeToFile(stockObject.toString());
                        finalresponse= finalresponse+stockObject.toString();
                        Log.d("Saved stock data", stockObject.toString());
                        temp = i + 1;
                        if (temp < symbol.size()) {
                            finalresponse=finalresponse+",";
                            System.out.println("WROTE MORE");
                            //writeToFile(",");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finalresponse=finalresponse+"]";
                System.out.println("What it should be: "+finalresponse);
                writeToFile(finalresponse);
                //System.out.println("WROTE 2");


                //
            }
        };
        t.start();

        //writeToFile("]");
        //System.out.println("FINAL:" + readFromFile());

    }

    private void writeToFile(String data) {
        try {
            File mFolder = new File("/data/user/0/edu.temple.lab10/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");

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

            File mFolder = new File("/data/user/0/edu.temple.lab10/files");
            File imgFile = new File(mFolder.getAbsolutePath() + "/config.txt");
           InputStream inputStream = this.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(new FileReader(imgFile));
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        System.out.println("The String: " + ret);
        return ret;

    }
}
