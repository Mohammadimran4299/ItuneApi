package com.example.itunesearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView simpleGrid;
    Button button;
    EditText text;

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    ArrayList<DataArtist> dataArtists;
    private AdapterClass mGridAdapter;

    private static String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btnsearch);
        text = findViewById(R.id.search_input);
        dataArtists = new ArrayList<>();
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataArtists.clear();
                String s = text.getText().toString();
                url = "https://itunes.apple.com/search?term=" + s;
                new GetContacts().execute();
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("results");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String tt = c.getString("artistName");
                        String album = c.getString("collectionName");
                        String sName = c.getString("trackCensoredName");
                        String path = c.getString("artworkUrl100");
                        DataArtist item = new DataArtist();
                        item.setArtName(tt);
                        item.setSongName(sName);
                        item.setArtImage(path);
                        item.setArtalbum(album);
                        dataArtists.add(item);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error  " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error  " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "errors!", Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            mGridAdapter = new AdapterClass(MainActivity.this, R.layout.grid_item, dataArtists);
            simpleGrid.setAdapter(mGridAdapter);
            mGridAdapter.setGridData(dataArtists);


        }

    }

}
