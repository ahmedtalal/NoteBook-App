package com.example.notebook.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotpasswordActivity extends AppCompatActivity {

    @BindView(R.id.passwordToolbar)
    Toolbar passwordToolbar;
    @BindView(R.id.sendEmail_ID)
    EditText sendEmailID;
    @BindView(R.id.resetpassword)
    Button resetpassword;
    @BindView(R.id.progress)
    ProgressBar progress;
    private String returned  = null ;
    private FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ButterKnife.bind(this);

        setSupportActionBar(passwordToolbar);
        getSupportActionBar().setTitle("Reset password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent() ;
        returned = intent.getStringExtra("code") ;

        passwordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returned.equals("returned")){
                    startActivity(new Intent(ForgotpasswordActivity.this , SettingsActivity.class));
                    finishAffinity();
                }else if (returned.equals("returned1")){
                    startActivity(new Intent(ForgotpasswordActivity.this , LoginActivity.class));
                    finishAffinity();
                }
            }
        });

        auth = FirebaseAuth.getInstance() ;
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recieveEmail = sendEmailID.getText().toString().trim() ;
                if (TextUtils.isEmpty(recieveEmail)){
                    sendEmailID.setError("this field is required");
                    sendEmailID.requestFocus() ;
                    return;
                }else {
                    progress.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(recieveEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext() , "Please check your email" , Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotpasswordActivity.this , LoginActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext() , task.getException().getMessage() , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (returned.equals("returned")){
            startActivity(new Intent(ForgotpasswordActivity.this , SettingsActivity.class));
            finish();
        }else if (returned.equals("returned1")){
            startActivity(new Intent(ForgotpasswordActivity.this , LoginActivity.class));
            finish();
        }
    }
}
