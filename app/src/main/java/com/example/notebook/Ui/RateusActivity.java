package com.example.notebook.Ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateusActivity extends AppCompatActivity {

    @BindView(R.id.rateToolbar_id)
    Toolbar rateToolbarId;
    @BindView(R.id.rate_id)
    RatingBar rateId;
    @BindView(R.id.rateBtn_ID)
    Button rateBtnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);
        ButterKnife.bind(this);
        setSupportActionBar(rateToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rateToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RateusActivity.this, SettingsActivity.class));
                finish();
            }
        });

        rateBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW) ;
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.example.notebook.Ui")) ;
                Log.v("Uri" , "https://play.google.com/store/apps/details?id=com.example.notebook.Ui") ;
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RateusActivity.this , SettingsActivity.class));
        finish();
    }
}
