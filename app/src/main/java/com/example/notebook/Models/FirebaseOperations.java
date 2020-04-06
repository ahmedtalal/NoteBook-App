package com.example.notebook.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.notebook.Ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseOperations {
    private FirebaseUser fUser ;
    private DatabaseReference dbRef ;
    private Context getContext ;
    private ArrayList<NoteModel> noteList ;

    public FirebaseOperations(Context context ) {
        this.getContext = context ;
        fUser = FirebaseAuth.getInstance().getCurrentUser() ;
        dbRef = FirebaseDatabase.getInstance().getReference() ;
        this.noteList = new ArrayList<>() ;
    }

    // insert data into firebase
    public boolean WriteData(NoteModel model , String child){
        if (model == null){
            return false;
        }else
        {
            dbRef.child(child).child(fUser.getUid()).child(model.getId()).setValue(model) ;
        }
        return true ;
    }
    // read data from firebase
    public ArrayList<NoteModel> ReadData(String child){
        dbRef.child(child).child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            NoteModel noteModel = new NoteModel();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    noteModel = snapshot.getValue(NoteModel.class);
                    noteList.add(noteModel) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext , databaseError.getMessage() , Toast.LENGTH_LONG).show();
            }
        }) ;
        return noteList ;
    }
}
