package com.example.notebook.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notebook.Adapters.NotesAdapter;
import com.example.notebook.Models.FirebaseOperations;
import com.example.notebook.Models.NoteModel;
import com.example.notebook.Models.Users;
import com.example.notebook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    @BindView(R.id.toolbar_id)
    Toolbar toolbarId;
    @BindView(R.id.search_id)
    SearchView searchId;
    @BindView(R.id.layoutType_id)
    ImageView layoutTypeId;
    @BindView(R.id.recyclerItem_id)
    RecyclerView recyclerItemId;
    @BindView(R.id.addItem_id)
    LinearLayout addItemId;
    @BindView(R.id.navigation_id)
    NavigationView navigationId;
    @BindView(R.id.drawer_id)
    DrawerLayout drawerId;
    @BindView(R.id.no_note_id)
    LinearLayout noNoteId;

    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth muAuth;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseUser fUser ;
    private DatabaseReference dbRef ;
    private ArrayList<NoteModel> noteList ;
    private ArrayList<NoteModel> travelList ;
    private ArrayList<NoteModel> personalList ;
    private ArrayList<NoteModel> workList ;
    private ArrayList<NoteModel> lifeList ;
    private ArrayList<NoteModel> untaggedList ;
    private ArrayList<NoteModel> favoritesList ;
    private ArrayList<NoteModel> allNotes ;
    private NotesAdapter notesAdapter ;
    private ProgressDialog progressDialog;
    private int convertLayout = 0 ;
    private int convertSpectialLayout = 0 ;
    TextView emailTextView ;
    CircleImageView cirle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationId.setItemIconTintList(null);
        mToggle = new ActionBarDrawerToggle(this, drawerId, toolbarId, R.string.close, R.string.open);
        drawerId.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationId.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        muAuth = FirebaseAuth.getInstance();
        addItemId.setOnClickListener(this::onClick);
        noteList = new ArrayList<>() ;
        travelList =  new ArrayList<>();
        personalList = new ArrayList<>() ;
        workList =  new ArrayList<>() ;
        lifeList = new ArrayList<>() ;
        untaggedList = new ArrayList<>() ;
        favoritesList =  new ArrayList<>() ;
        allNotes = new ArrayList<>() ;
        fUser = FirebaseAuth.getInstance().getCurrentUser() ;
        dbRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(fUser.getUid()) ;
        dbRef.keepSynced(true);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        retriveData();
        layoutTypeId.setOnClickListener(this::onClick);
        searchId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        checkSignIn();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.logout_id:
                muAuth.signOut();
                Intent intent = new Intent(MainActivity.this, ScreenActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.allNotes_ID:
                String allNotes = "allNotes" ;
                retriveSpecialData(allNotes) ;
                toolbarId.setTitle("All Notes");
                convertSpectialLayout = 0 ;
                break;
            case R.id.favorites_id:
                //Toast.makeText(getApplicationContext(), "this is favorites notes", Toast.LENGTH_LONG).show();
                toolbarId.setTitle("Favorites");
                String favorite = "favorites" ;
                retriveSpecialData(favorite) ;
                break;
            case R.id.travel_id:
               // Toast.makeText(getApplicationContext(), "this is travel notes", Toast.LENGTH_LONG).show();
                String travel = "Travel" ;
                retriveSpecialData(travel) ;
                toolbarId.setTitle("Travel");
                break;
            case R.id.personal_id:
                //Toast.makeText(getApplicationContext(), "this is personal notes", Toast.LENGTH_LONG).show();
                String personal = "Personal" ;
                retriveSpecialData(personal) ;
                toolbarId.setTitle("Personal");
                break;
            case R.id.life_id:
                //Toast.makeText(getApplicationContext(), "this is life notes", Toast.LENGTH_LONG).show();
                String life = "Life" ;
                retriveSpecialData(life) ;
                toolbarId.setTitle("Life");
                break;
            case R.id.work_id:
               // Toast.makeText(getApplicationContext(), "this is work notes", Toast.LENGTH_LONG).show();
                String work = "Work" ;
                retriveSpecialData(work) ;
                toolbarId.setTitle("Work");
                break;
            case R.id.untagged_id :
                String untagged = "untagged" ;
                retriveSpecialData(untagged) ;
                toolbarId.setTitle("Untagged");
                break;
            case R.id.setting_id:
                Intent intent1 = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
        return true;
    }


    private void retriveData() {
        noteList.clear();
        workList.clear();
        travelList.clear();
        personalList.clear();
        lifeList.clear();
        untaggedList.clear();
        allNotes.clear();
        favoritesList.clear();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    NoteModel noteModel = snapshot.getValue(NoteModel.class);
                    noteList.add(noteModel) ;
                    allNotes.add(noteModel );
                    if (noteModel.getTag().equals("Travel")){
                        travelList.add(noteModel);
                    }
                    if (noteModel.getTag().equals("Personal")){
                        personalList.add(noteModel) ;
                    }
                    if (noteModel.getTag().equals("Life")){
                        lifeList.add(noteModel) ;
                    }
                    if (noteModel.getTag().equals("Work")){
                        workList.add(noteModel) ;
                    }
                    if (noteModel.getFavorites() == true){
                        favoritesList.add(noteModel) ;
                    }
                    if (noteModel.getTag().equals("Untagged")){
                        untaggedList.add(noteModel) ;
                    }
                }
                notesAdapter = new NotesAdapter(noteList, getApplicationContext());
                if (notesAdapter.getItemCount() != 0){
                    noNoteId.setVisibility(View.INVISIBLE);
                    if (convertLayout == 0){
                        gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                        recyclerItemId.setLayoutManager(gridLayoutManager);
                        recyclerItemId.setAdapter(notesAdapter);
                    }else if (convertLayout == 1){
                        linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                        linearLayoutManager.setStackFromEnd(false);
                        recyclerItemId.setLayoutManager(linearLayoutManager);
                        recyclerItemId.setAdapter(notesAdapter);
                    }

                    progressDialog.dismiss();
                }else {
                    noNoteId.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext() , databaseError.getMessage() , Toast.LENGTH_LONG).show();
            }
        }) ;

    }

    private void retriveSpecialData(String type) {
        if (type.equals("Travel")){
            convertSpectialLayout = 1 ;
            if (travelList.isEmpty()){
                recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(travelList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("Personal")){
            convertSpectialLayout =  2 ;
            if (personalList.isEmpty()){
                recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(personalList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("Work")){
            convertSpectialLayout = 3 ;
            if (workList.isEmpty()){
                recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(workList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("Life")){
            convertSpectialLayout = 4 ;
            if (lifeList.isEmpty()){
                recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(lifeList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("favorites")){
            convertSpectialLayout = 5 ;
            if (favoritesList.isEmpty()){
               recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(favoritesList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("untagged")){
            convertSpectialLayout = 6 ;
            if (untaggedList.isEmpty()){
                recyclerItemId.setAdapter(null);
            }
            notesAdapter = new NotesAdapter(untaggedList, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        if (type.equals("allNotes")){
            notesAdapter = new NotesAdapter(allNotes, getApplicationContext());
            if (notesAdapter.getItemCount() != 0){
                noNoteId.setVisibility(View.INVISIBLE);
                if (convertLayout == 0){
                    gridLayoutManager =  new GridLayoutManager(getApplicationContext() , 2 ) ;
                    recyclerItemId.setLayoutManager(gridLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }else if (convertLayout == 1){
                    linearLayoutManager =  new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false) ;
                    linearLayoutManager.setStackFromEnd(false);
                    recyclerItemId.setLayoutManager(linearLayoutManager);
                    recyclerItemId.setAdapter(notesAdapter);
                }

                progressDialog.dismiss();
            }else {
                noNoteId.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addItem_id:
                Intent intent = new Intent(getApplicationContext(), AddnoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.layoutType_id :
                if (convertSpectialLayout == 1){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("Travel");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("Travel");
                    }
                }else if (convertSpectialLayout == 2){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("Personal");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("Personal");
                    }
                }else if (convertSpectialLayout == 3){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("Work");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("Work");
                    }
                }else if (convertSpectialLayout == 4){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("Life");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("Life");
                    }
                }else if (convertSpectialLayout == 5){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("favorites");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("favorites");
                    }
                }else if (convertSpectialLayout == 6){
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveSpecialData("untagged");
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveSpecialData("untagged");
                    }
                } else {
                    if (convertLayout == 0) {
                        convertLayout = 1;
                        layoutTypeId.setImageResource(R.drawable.ic_itemlinear);
                        retriveData();
                    }else if (convertLayout == 1){
                        convertLayout = 0;
                        layoutTypeId.setImageResource(R.drawable.ic_view_module);
                        retriveData();
                    }
                }
                break;


        }
    }

    private void checkSignIn() {
        DatabaseReference refro  = FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()) ;
        emailTextView = navigationId.getHeaderView(0).findViewById(R.id.headerEmail_id) ;
        cirle = navigationId.getHeaderView(0).findViewById(R.id.headerPhoto_id) ;
        Button updateBtn = navigationId.getHeaderView(0).findViewById(R.id.updateProfoBtn_id) ;
        if (fUser != null){
            refro.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users usermodels = dataSnapshot.getValue(Users.class) ;
                    emailTextView.setText(usermodels.getEmail());
                    if (usermodels.getPhoto().equals("null")){
                        Glide.with(MainActivity.this)
                                .load(R.drawable.ic_person)
                                .placeholder(R.drawable.ic_person)
                                .into(cirle);
                    }else {
                        Picasso.get().load(usermodels.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(cirle, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get()
                                        .load(usermodels.getPhoto())
                                        .placeholder(R.drawable.ic_person)
                                        .error(R.drawable.ic_person)
                                        .into(cirle);
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emailTextView = navigationId.getHeaderView(0).findViewById(R.id.headerEmail_id) ;
                    emailTextView.setText("Email");

                }
            });
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(MainActivity.this , UpdateprofileActivity.class) ;
                    intent.putExtra("email" , emailTextView.getText().toString()) ;
                    startActivity(intent);
                    finishAffinity();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

}
