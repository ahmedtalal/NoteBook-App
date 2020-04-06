package com.example.notebook.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.Adapters.SettingAdapter;
import com.example.notebook.Models.SettingModel;
import com.example.notebook.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity{

    @BindView(R.id.toolbarBacksettings_id)
    Toolbar toolbarBacksettingsId;
    @BindView(R.id.settingsItems_id)
    RecyclerView settingsItemsId;
    private SettingAdapter adapter ;
    private ArrayList<SettingModel> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarBacksettingsId);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarBacksettingsId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext() , MainActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
        list  = new ArrayList<>();
        list.add(new SettingModel("Forgot Paaword" , R.drawable.ic_forgot)) ;
        list.add(new SettingModel("Tell a friend" , R.drawable.ic_share)) ;
        list.add(new SettingModel("Rate Us" , R.drawable.ic_rate)) ;
        list.add(new SettingModel("Contact US" , R.drawable.ic_contactus)) ;
        list.add(new SettingModel("About Us" , R.drawable.ic_aboutus)) ;
        list.add(new SettingModel("Privacy Policy" , R.drawable.ic_privacy)) ;
        adapter =  new SettingAdapter(list , getApplicationContext()) ;
        GridLayoutManager linearLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
        settingsItemsId.setLayoutManager(linearLayoutManager);
        settingsItemsId.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(getApplicationContext() , MainActivity.class) ;
        startActivity(intent);
        finishAffinity();
    }
}
