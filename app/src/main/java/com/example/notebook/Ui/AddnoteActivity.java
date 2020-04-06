package com.example.notebook.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.Models.FirebaseOperations;
import com.example.notebook.Models.NoteModel;
import com.example.notebook.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddnoteActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbarBack_id)
    Toolbar toolbarBackId;
    @BindView(R.id.addTag_id)
    TextView addTagId;
    @BindView(R.id.setTime_id)
    TextView setTimeId;
    @BindView(R.id.noteWords_id)
    EditText noteWordsId;
    @BindView(R.id.counter_id)
    TextView counterId;
    @BindView(R.id.rightAddNote_id)
    ImageView rightAddNoteId;
    @BindView(R.id.img_tag_id)
    ImageView imgTagId;
    @BindView(R.id.linear3)
    LinearLayout linear3;

    private ProgressDialog progressDialog;
    private boolean checked = false ;
    private DatabaseReference reference ;
    private FirebaseUser user ;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarBackId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser() ;
        reference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()) ;

        toolbarBackId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        rightAddNoteId.setOnClickListener(this::onClick);
        addTagId.setOnClickListener(this::onClick);
        setTimeId.setText(getTimeDate());
        noteWordsId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = noteWordsId.length();
                String convert = String.valueOf(length);
                    counterId.setText(convert);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightAddNote_id:
                // here set action
                if (checked == false){
                    Toast.makeText(getApplicationContext() , "Please enter your tag" , Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog = new ProgressDialog(AddnoteActivity.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                NoteModel noteModel = new NoteModel
                        (
                                noteWordsId.getText().toString() ,
                                addTagId.getText().toString() ,
                                setTimeId.getText() .toString(),
                                false ,
                                reference.push().getKey()
                        ) ;
                addNote(noteModel) ;
                break;
            case R.id.addTag_id:
                BottomSheetDialog bottom = new BottomSheetDialog(AddnoteActivity.this , R.style.BottomSheetDialogThem);
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
                        addTagId.setText("Travel");
                        imgTagId.setImageResource(R.drawable.ic_tagg);
                        bottom.cancel();
                        checked =  true ;
                    }
                });

                personal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTagId.setText("Personal");
                        imgTagId.setImageResource(R.drawable.ic_tag11);
                        bottom.cancel();
                        checked =  true ;
                    }
                });
                life.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTagId.setText("Life");
                        imgTagId.setImageResource(R.drawable.ic_tag22);
                        bottom.cancel();
                        checked =  true ;
                    }
                });
                work.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTagId.setText("Work");
                        imgTagId.setImageResource(R.drawable.ic_tag33);
                        bottom.cancel();
                        checked =  true ;
                    }
                });
                untagged.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTagId.setText("Untagged");
                        imgTagId.setImageResource(R.drawable.ic_tag_4);
                        bottom.cancel();
                        checked =  true ;
                    }
                });
                bottom.setContentView(vv);
                bottom.show();
                break;

        }
    }

    private void addNote(NoteModel noteModel) {
        FirebaseOperations operations =  new FirebaseOperations(getApplicationContext()) ;
        boolean result = operations.WriteData(noteModel , "Notes");
        if (result){
            progressDialog.dismiss();
            Intent intent =  new Intent(getApplicationContext() , MainActivity.class) ;
            startActivity(intent);
            finish();
        }else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private String getTimeDate() {
        String currentDateandTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        return currentDateandTime;
    }
}
