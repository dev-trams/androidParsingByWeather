package com.example.prantice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private Context context;

    public ImageAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000);
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
}
