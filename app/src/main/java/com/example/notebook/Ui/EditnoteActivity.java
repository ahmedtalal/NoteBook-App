package com.example.notebook.Ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.Models.NoteModel;
import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditnoteActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbarBack1_id)
    Toolbar toolbarBack1Id;
    @BindView(R.id.rightEditNote_id)
    ImageView rightEditNoteId;
    @BindView(R.id.editTag_id)
    TextView editTagId;
    @BindView(R.id.getTime_id)
    TextView getTimeId;
    @BindView(R.id.noteEditWords_id)
    EditText noteEditWordsId;
    @BindView(R.id.shareLayout_id)
    LinearLayout shareLayoutId;
    @BindView(R.id.favoriteLayout_id)
    LinearLayout favoriteLayoutId;
    @BindView(R.id.deleteLayout_id)
    LinearLayout deleteLayoutId;
    @BindView(R.id.img_tag1_id)
    ImageView imgTag1Id;
    @BindView(R.id.imgFavorte_id)
    ImageView imgFavorteId;
    @BindView(R.id.textFavorite_id)
    TextView textFavoriteId;

    private FirebaseDatabase fDB ;
    private DatabaseReference rDB ;
    private FirebaseUser fUser;
    private NoteModel noteModel ;
    private String id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarBack1Id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fDB = FirebaseDatabase.getInstance() ;
        fUser = FirebaseAuth.getInstance().getCurrentUser() ;
        rDB = fDB.getReference().child("Notes").child(fUser.getUid()) ;

        toolbarBack1Id.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String note = intent.getStringExtra("notes");
        String date = intent.getStringExtra("date");
        String tag = intent.getStringExtra("tag");
        id = intent.getStringExtra("id") ;
        boolean favorite = intent.getBooleanExtra("favorite", false);
        getTimeId.setText(date);
        editTagId.setText(tag);
        noteEditWordsId.setText(note);
        setTag(tag) ;
        setFavorite(favorite) ;
        editTagId.setOnClickListener(this::onClick);
        rightEditNoteId.setOnClickListener(this::onClick);
        favoriteLayoutId.setOnClickListener(this::onClick);
        shareLayoutId.setOnClickListener(this::onClick);
        deleteLayoutId.setOnClickListener(this::onClick);




    }

    private void setFavorite(boolean favorite) {
        if (favorite == true){
            imgFavorteId.setImageResource(R.drawable.ic_star);
            textFavoriteId.setText("Favorite");
        }
    }

    private void setTag(String tag) {
        switch (tag) {
            case "Travel":
                imgTag1Id.setImageResource(R.drawable.ic_tagg);
                break;
            case "Personal":
                imgTag1Id.setImageResource(R.drawable.ic_tag11);
                break;
            case "Life":
                imgTag1Id.setImageResource(R.drawable.ic_tag22);
                break;
            case "Work":
                imgTag1Id.setImageResource(R.drawable.ic_tag33);
                break;
            case "Untagged":
                imgTag1Id.setImageResource(R.drawable.ic_tag_4);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editTag_id :
                editTage() ;
                break;
            case R.id.rightEditNote_id :
                updateNoteInfo() ;
                break;
            case R.id.favoriteLayout_id :
                checkFavoriteMode() ;
                break;
            case R.id.shareLayout_id :
                shareNote() ;
                break;
            case R.id.deleteLayout_id :
                deleteNote() ;
                break;

        }
    }

    private void updateNoteInfo() {
        boolean result = false;
        if (textFavoriteId.getText().equals("Favorite")){
            result = true ;
        }else if (textFavoriteId.getText().equals("Unfavorite")){
            result = false ;
        }
        noteModel = new NoteModel
                (
                        noteEditWordsId.getText().toString() ,
                        editTagId.getText().toString() ,
                        getTimeId.getText().toString() ,
                        result ,
                        id
                ) ;
        HashMap<String , Object> hashMap = new HashMap<>() ;
        hashMap.put("date" , noteModel.getDate()) ;
        hashMap.put("favorites" , noteModel.getFavorites()) ;
        hashMap.put("id" , noteModel.getId()) ;
        hashMap.put("note" , noteModel.getNote()) ;
        hashMap.put("tag" , noteModel.getTag()) ;
        String key = rDB.getRef().child(noteModel.getId()).getKey() ;
        rDB.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class) ;
                    startActivity(intent);
                }
            }
        });

    }

    private void deleteNote() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setIcon(R.drawable.ic_question) ;
        builder.setTitle("Delete Note!") ;
        builder.setMessage("Are you sure , you want to delete note") ;
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key = rDB.getRef().child(id).getKey() ;
                rDB.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext() , MainActivity.class) ;
                            startActivity(intent);
                        }
                    }
                });
            }
        }) ;
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true) ;
            }
        }) ;
        AlertDialog alertDialog = builder.create() ;
        alertDialog.show();
    }

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, noteEditWordsId.getText().toString());
        Intent shareIntent = Intent.createChooser(sendIntent, "Share your notes with your friends");
        startActivity(shareIntent) ;
    }

    private void checkFavoriteMode() {
      if (textFavoriteId.getText().equals("Favorite")){
          textFavoriteId.setText("Unfavorite");
          imgFavorteId.setImageResource(R.drawable.ic_favorite);
      }else if (textFavoriteId.getText().equals("Unfavorite")){
          textFavoriteId.setText("Favorite");
          imgFavorteId.setImageResource(R.drawable.ic_star);
      }
    }


    private void editTage() {
        BottomSheetDialog bottom = new BottomSheetDialog(EditnoteActivity.this , R.style.BottomSheetDialogThem);
        View vv = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.bottomsheet_activity,findViewById(R.id.bottomSheet_id)) ;
        bottom.setCanceledOnTouchOutside(false);
        MaterialRippleLayout cacelBtn = vv.findViewById(R.id.canceled_id);
        RadioButton travel = vv.findViewById(R.id.traveled_id);
        RadioButton personal = vv.findViewById(R.id.personaled_id);
        RadioButton life = vv.findViewById(R.id.lifed_id);
        RadioButton work = vv.findViewById(R.id.worked_id);
        RadioButton untagged = vv.findViewById(R.id.untagged_id);
        cacelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom.cancel();
            }
        });
        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagId.setText("Travel");
                imgTag1Id.setImageResource(R.drawable.ic_tagg);
                bottom.cancel();
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagId.setText("Personal");
                imgTag1Id.setImageResource(R.drawable.ic_tag11);
                bottom.cancel();
            }
        });
        life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagId.setText("Life");
                imgTag1Id.setImageResource(R.drawable.ic_tag22);
                bottom.cancel();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagId.setText("Work");
                imgTag1Id.setImageResource(R.drawable.ic_tag33);
                bottom.cancel();
            }
        });
        untagged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagId.setText("Untagged");
                imgTag1Id.setImageResource(R.drawable.ic_tag_4);
                bottom.cancel();
            }
        });
        bottom.setContentView(vv);
        bottom.show();
    }
}
