package com.example.studyroomtest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyroomtest.R;
import com.example.studyroomtest.adapter.RecyclerAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerAdapter adapter;
    ArrayList<ArrayList<String>> list = new ArrayList<>();

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.rv);
        recyclerView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);

        if(!isNetworkAvailable()){
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            new DownloadFilesTask().execute();

            bt = findViewById(R.id.button);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.d(TAG,"Exception "+e.getMessage());
                    }
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    new DownloadFilesTask().execute();
                }
            });
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, ArrayList<ArrayList<String>>> {
        protected ArrayList<ArrayList<String>> doInBackground(URL... urls) {
            String inline = "";
            try {
                list.clear();
                URL url = new URL("https://api.stackexchange.com/2.2/search?order=desc&sort=activity&intitle=perl&site=stackoverflow");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int responsecode = conn.getResponseCode();

                if(responsecode==200) {

                    Scanner sc = new Scanner(url.openStream());
                    while (sc.hasNext()) {
                        inline += sc.nextLine();
                    }
                    sc.close();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                    return null;
                }

                list = parsingMethod(list, inline);
                conn.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return list;
        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(ArrayList<ArrayList<String>> result) {

            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            RecyclerView recyclerView = findViewById(R.id.rv);
            recyclerView.setVisibility(View.VISIBLE);

            TextView tv = findViewById(R.id.edit);
            String tagValue = tv.getText().toString();

            result = modifyResult(result,tagValue);

            if(result.size()==0){
                Toast.makeText(getApplicationContext(),"No Result found",Toast.LENGTH_LONG).show();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
            adapter = new RecyclerAdapter(getApplication(), result);
            recyclerView.setAdapter(adapter);
        }
    }

    private ArrayList<ArrayList<String>> parsingMethod(ArrayList<ArrayList<String>> list,String inline) throws ParseException {

        JSONParser parse = new JSONParser();
        JSONObject JsonObject1 = (JSONObject)parse.parse(inline);
        JSONArray jsonArray1 = (JSONArray) JsonObject1.get("items");

        for(int i=0;i<jsonArray1.size();i++){
            ArrayList<String> temp = new ArrayList<>();
            JSONObject JsonObject2 = (JSONObject)jsonArray1.get(i);
            JSONObject JsonArray2 = (JSONObject) JsonObject2.get("owner");


            temp.add(JsonObject2.get("title").toString());
            temp.add(JsonObject2.get("tags").toString());
            temp.add(JsonArray2.get("profile_image").toString());
            list.add(temp);
        }
        return list;

    }


    private ArrayList<ArrayList<String>> modifyResult(ArrayList<ArrayList<String>> result, String tagValue) {
        if(!tagValue.equals("")){
            for(int i=0;i<result.size();i++){
                String tag = result.get(i).get(1);
                String[] tagCheck = tagValue.split("\\s+");
                ArrayList<String> check = new ArrayList<>();
                for(int k=0;k<tag.length();k++){
                    if((tag.charAt(k)<='Z'&&tag.charAt(k)>='A')||(tag.charAt(k)>='a'&&tag.charAt(k)<='z')){
                        String str ="";
                        while(k<tag.length()&&(tag.charAt(k)<='Z'&&tag.charAt(k)>='A')||(tag.charAt(k)>='a'&&tag.charAt(k)<='z')){
                            str = str + tag.charAt(k);
                            k++;
                        }
                        check.add(str);
                    }
                }

                for(int j=0;j<tagCheck.length;j++){
                    if(!check.contains(tagCheck[j])){
                        result.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
        return result;
    }

}