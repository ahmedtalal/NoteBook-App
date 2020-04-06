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

import com.example.notebook.Models.Users;
import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.user_register_id)
    EditText userRegisterId;
    @BindView(R.id.email_register_id)
    EditText emailRegisterId;
    @BindView(R.id.password_register_id)
    EditText passwordRegisterId;
    @BindView(R.id.repassword_register_id)
    EditText repasswordRegisterId;
    @BindView(R.id.registerButton)
    Button registerButton;

    private Users users;
    private FirebaseAuth muAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase fDB ;
    private DatabaseReference rDB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        muAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance() ;
        rDB = fDB.getReference() ;

        registerButton.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            users = new Users(
                    userRegisterId.getText().toString(),
                    emailRegisterId.getText().toString(),
                    "null" ,
                    passwordRegisterId.getText().toString(),
                    repasswordRegisterId.getText().toString()
            );

            checkUser(users);
        }
    }

    public void checkUser( Users object) {
        int passLength = object.getPassword().length();
        if (object.getUserName().equals("")) {
            userRegisterId.setError("username  is required");
            userRegisterId.requestFocus();
            return;
        }
        if (object.getUserName().length() < 5) {
            userRegisterId.setError("the length must be greater than 5 letters");
            userRegisterId.requestFocus();
            return;
        }
        if (object.getEmail().equals("")) {
            emailRegisterId.setError("email is required");
            emailRegisterId.requestFocus();
            return;
        }
        if (object.getPassword().equals("")) {
            passwordRegisterId.setError("password is required");
            passwordRegisterId.requestFocus();
            return;
        }
        if (passLength < 8) {
            passwordRegisterId.setError("the length must be greater than 8 letters");
            passwordRegisterId.requestFocus();
            return;
        }
        if (object.getConfirmPassword().equals("")) {
            repasswordRegisterId.setError("confirm password is required");
            repasswordRegisterId.requestFocus();
            return;
        }
        if (!object.getConfirmPassword().equals(object.getPassword())) {
            Toast.makeText(getApplicationContext(), "Not matching between password and confirm password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        createAccount(users);

    }

    private void createAccount(Users users) {
        muAuth.createUserWithEmailAndPassword(users.getEmail(), users.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid() ;
                            saveUserInfo(users , userID) ;

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void saveUserInfo(Users users, String userID) {
        Users usersModel = new Users(users.getUserName() , users.getEmail() , "null") ;
        rDB.child("Users").child(userID).setValue(usersModel );
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        finish();
    }

    public void re_login_id(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


}
