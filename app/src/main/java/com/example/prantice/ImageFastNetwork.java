package com.example.prantice;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

public class ImageFastNetwork {
    private Context context;

    public ImageFastNetwork(Context context) {
        this.context = context;
    }
    public void request(String url, ImageView imageView) {
        AndroidNetworking.initialize(context);
        AndroidNetworking.get(url).build().getAsBitmap(new BitmapRequestListener() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }
}
