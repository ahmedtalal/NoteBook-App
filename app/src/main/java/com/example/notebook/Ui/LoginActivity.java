package com.example.notebook.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.Models.Users;
import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email_login_id)
    EditText emailLoginId;
    @BindView(R.id.password_login_id)
    EditText passwordLoginId;
    @BindView(R.id.loginButton)
    Button loginButton;

    private ProgressDialog progressDialog;
    private FirebaseAuth muAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getTheme().applyStyle(R.style.AppTheme2 , true);
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        muAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this::onClick);

    }

    public void forgotPass_id(View view) {
        // here set forgot password action
        Intent intent =  new Intent(LoginActivity.this , ForgotpasswordActivity.class) ;
        intent.putExtra("code" , "returned1") ;
        startActivity(intent);
    }

    public void register_id(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            CheckCurrentUsr(emailLoginId.getText().toString() , passwordLoginId.getText().toString());

        }
    }

    private void CheckCurrentUsr(String email , String password) {
        if (email.equals("")) {
            emailLoginId.setError("email is required");
            emailLoginId.requestFocus();
            return;
        }
        if (password.equals("")) {
            passwordLoginId.setError("password is required");
            passwordLoginId.requestFocus();
            return;
        }
        if (password.length() < 8) {
            passwordLoginId.setError("the length must be greater than 8 letters");
            passwordLoginId.requestFocus();
            return;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getCurrentUser(email , password);
    }

    private void getCurrentUser(String email , String password) {
        muAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}
