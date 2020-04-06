package com.example.notebook.Ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutusActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.aboutUsToolbar_id)
    Toolbar aboutUsToolbarId;
    @BindView(R.id.facebook_id)
    CircleImageView facebookId;
    @BindView(R.id.google_id)
    CircleImageView googleId;
    @BindView(R.id.youtube_id)
    CircleImageView youtubeId;
    @BindView(R.id.twitter_id)
    CircleImageView twitterId;
    @BindView(R.id.github_id)
    CircleImageView githubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        setSupportActionBar(aboutUsToolbarId);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aboutUsToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        facebookId.setOnClickListener(this::onClick);
        googleId.setOnClickListener(this::onClick);
        youtubeId.setOnClickListener(this::onClick);
        twitterId.setOnClickListener(this::onClick);
        githubId.setOnClickListener(this::onClick);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.facebook_id :
                String link = "https://www.facebook.com/ahmd.talal.5?ref=bookmarks";
                goToUrl(link);
                break;
            case R.id.google_id :
                String link1 = "https://mail.google.com/mail/u/0/#inbox";
                goToUrl(link1);
                break;
            case R.id.youtube_id :
                String link2 = "https://www.youtube.com/channel/UCxjsmxYugcfGrjRaP2gteiQ?view_as=subscriber";
                goToUrl(link2);
                break;
            case R.id.github_id :
                String link3 = "https://github.com/ahmedtalal?tab=repositories";
                goToUrl(link3);
                break;
            case R.id.twitter_id :
                String link4 = "https://twitter.com/BashMobarmg?s=08";
                goToUrl(link4);
        }

    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        //launchBrowser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchBrowser);
    }

}
