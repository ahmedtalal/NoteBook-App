package com.example.notebook.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.notebook.Models.Users;
import com.example.notebook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateprofileActivity extends AppCompatActivity {

    @BindView(R.id.updateToolbar_id)
    Toolbar updateToolbarId;
    @BindView(R.id.profilePhoto)
    CircleImageView profilePhoto;
    @BindView(R.id.camer_ID)
    ImageView camerID;
    @BindView(R.id.profileName_ID)
    EditText profileNameID;
    @BindView(R.id.profileEmail_ID)
    EditText profileEmailID;

    private static final int RN_PHOTO = 2;
    @BindView(R.id.editBtn)
    ImageButton editBtn;
    private Uri imageUri;
    private Uri imageUrires;
    private FirebaseStorage fbs;
    private StorageReference storageReference;
    private FirebaseUser fUser;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        ButterKnife.bind(this);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fbs = FirebaseStorage.getInstance();
        storageReference = fbs.getReference().child("UriImages");

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        profileEmailID.setText(email);
        DatabaseReference refro  = FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()) ;
        refro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users usermodels = dataSnapshot.getValue(Users.class) ;
                profileNameID.setText(usermodels.getUserName());
                if (usermodels.getPhoto().equals("null")) {
                    Glide.with(UpdateprofileActivity.this)
                            .load(R.drawable.ic_person)
                            .placeholder(R.drawable.ic_person)
                            .into(profilePhoto);
                }else {
                    Picasso.get().load(usermodels.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(profilePhoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(usermodels.getPhoto())
                                    .placeholder(R.drawable.ic_person)
                                    .error(R.drawable.ic_person)
                                    .into(profilePhoto);
                        }
                    });
                    imageUrires = Uri.parse(usermodels.getPhoto());
                    imageUri = Uri.parse(usermodels.getPhoto());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setSupportActionBar(updateToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateprofileActivity.this, MainActivity.class));
                finish();
            }
        });
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = profileNameID.getText().toString();
                String email = profileEmailID.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    profileNameID.setError("userName is required");
                    profileNameID.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    profileEmailID.setError("Email is required");
                    profileEmailID.requestFocus();
                    return;
                }
                if (imageUri.getPath().equals("null")) {
                    Toast.makeText(UpdateprofileActivity.this, "Please Enter your profile photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(UpdateprofileActivity.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                uploadPhoto(username, email, imageUri);
            }
        });

    }

    private void openGallary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "completed action"), RN_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RN_PHOTO && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Glide.with(UpdateprofileActivity.this)
                    .load(imageUri)
                    .into(profilePhoto);
        }

    }

    private void uploadPhoto(String username, String email, Uri imageUri) {
        if (imageUri.equals(imageUrires)){
            saveInfo(username, email, imageUrires.toString());
        }else {
            UploadTask uploadTask;
            final StorageReference rfs = storageReference.child(imageUri.getPath());
            uploadTask = rfs.putFile(imageUri);
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    return rfs.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri retrieveUriImage = task.getResult();
                    String converImage = retrieveUriImage.toString();
                    saveInfo(username, email, converImage);
                }
            });
        }
    }

    private void saveInfo(String username, String email, String converImage) {
        DatabaseReference refro = FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid());
        Users users = new Users(username , email , converImage) ;
        refro.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(UpdateprofileActivity.this , MainActivity.class) ;
        startActivity(intent);
        finishAffinity();
    }
}
