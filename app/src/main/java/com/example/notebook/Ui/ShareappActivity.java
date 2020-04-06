package com.example.notebook.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebook.BuildConfig;
import com.example.notebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareappActivity extends AppCompatActivity {

    @BindView(R.id.shareToolbar_id)
    Toolbar shareToolbarId;
    @BindView(R.id.sharebutton_ID)
    Button sharebuttonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareapp);
        ButterKnife.bind(this);

        setSupportActionBar(shareToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        shareToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareappActivity.this, SettingsActivity.class));
                finish();
            }
        });

        sharebuttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "NoteBook App");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "choose one"));
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShareappActivity.this, SettingsActivity.class));
        finish();
    }
}
