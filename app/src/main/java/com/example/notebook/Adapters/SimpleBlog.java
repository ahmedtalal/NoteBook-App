package com.example.notebook.Adapters;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class SimpleBlog extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //enabling photo offline
        Picasso.Builder builder = new Picasso.Builder(this) ;
        builder.downloader(new OkHttp3Downloader(this , Integer.MAX_VALUE)) ;
        Picasso built = builder.build() ;
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
