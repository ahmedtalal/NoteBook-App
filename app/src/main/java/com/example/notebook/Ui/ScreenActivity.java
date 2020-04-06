package com.example.notebook.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScreenActivity extends AppCompatActivity {

    private MaterialRippleLayout login , regsiter ;
    private FirebaseUser user ;
    private FirebaseAuth muAth ;
    @Override
    protected void onStart() {
        super.onStart();
        muAth = FirebaseAuth.getInstance() ;
        user = muAth.getCurrentUser();
        if (user != null){
            Intent intent =  new Intent(getApplicationContext() , MainActivity.class) ;
            startActivity(intent);
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        login = findViewById(R.id.ripple_login_btn) ;
        regsiter = findViewById(R.id.ripple_register_btn) ;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ScreenActivity.this , LoginActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
        regsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ScreenActivity.this , RegisterActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
    }

    /// this method to prevent accure back action when user set click on back button on phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
