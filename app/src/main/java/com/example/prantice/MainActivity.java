package com.example.prantice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String json = "";
    int type = 1;
    String url = "http://192.168.0.27:8887/weather.json";
    ArrayList<Weathers> weathers = new ArrayList<>();
    ListView listView;
    boolean trigger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("중간수행평가");
        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.text);
        listView = findViewById(R.id.list);
        Switch toggle = findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked) {
                    toggle.setText("Glide 사용");
                    trigger = true;
                } else {
                    toggle.setText("자체 통신 방법");
                    trigger = false;
                }
            }
        });
        ScrollView scrollView = findViewById(R.id.scroll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if(type == 1) {
                    JsonAsyncTask asyncTask = new JsonAsyncTask();
                    try {
                        json = asyncTask.execute(url).get();
                        textView.setText(json);
                    } catch (ExecutionException | InterruptedException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidNetworking.initialize(MainActivity.this);
                    AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            textView.setText(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

                }
            }
        });
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                ParsingJson parsingJson = new ParsingJson(MainActivity.this);
                if(type == 1) {
                    try {
                        weathers = parsingJson.onParsingJson(json);
                        CustomAdapter adapter = new CustomAdapter(MainActivity.this, weathers, type, trigger);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidNetworking.initialize(MainActivity.this);
                    AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ParsingJson parsingJson = new ParsingJson(MainActivity.this);
                            try {
                                weathers = parsingJson.onParsingJson(response);
                                CustomAdapter adapter = new CustomAdapter(MainActivity.this, weathers, type, trigger);
                                listView.setAdapter(adapter);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                type = 1;
                break;
            case R.id.item2:
                type = 2;
                break;
        }
        item.setChecked(true);
        return true;
    }
}