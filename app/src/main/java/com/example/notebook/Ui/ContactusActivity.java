package com.example.notebook.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebook.Models.SendingMail;
import com.example.notebook.R;

import javax.mail.Transport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactusActivity extends AppCompatActivity {

    @BindView(R.id.contactToolbar_id)
    Toolbar contactToolbarId;
    @BindView(R.id.emailField_ID)
    EditText emailFieldID;
    @BindView(R.id.messageField)
    EditText messageField;
    @BindView(R.id.sendBtn_ID)
    Button sendBtnID;
    @BindView(R.id.subjectField_ID)
    EditText subjectFieldID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        ButterKnife.bind(this);
        setSupportActionBar(contactToolbarId);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sendBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailfield = emailFieldID.getText().toString().trim();
                String message = messageField.getText().toString().trim();
                String subject = subjectFieldID.getText().toString().trim() ;
                if (TextUtils.isEmpty(emailfield)) {
                    emailFieldID.setError("the email field is required");
                    emailFieldID.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(message)) {
                    messageField.setError("the message field is required");
                    messageField.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(subject)) {
                    messageField.setError("the subject field is required");
                    messageField.requestFocus();
                    return;
                }
                SendingMail sending = new SendingMail(emailfield , subject , message , ContactusActivity.this) ;
                sending.sendMail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContactusActivity.this, SettingsActivity.class));
    }
}
