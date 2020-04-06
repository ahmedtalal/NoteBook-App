package com.example.notebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.Models.NoteModel;
import com.example.notebook.R;
import com.example.notebook.Ui.EditnoteActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> implements Filterable {

    private List<NoteModel> lists  ;
    private Context getContext ;
    private List<NoteModel> notesListAll ;

    public NotesAdapter(List<NoteModel> lists, Context getContext) {
        this.lists = lists;
        this.getContext = getContext;
        notesListAll =  new ArrayList<>(lists) ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext).inflate(R.layout.note_items , parent , false) ;
        MyViewHolder myViewHolder = new MyViewHolder(view );
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteModel note = lists.get(position) ;
        holder.noteText.setText(note.getNote());
        holder.dateText.setText(note.getDate());
        if (note.getFavorites() == false){
            holder.favoriteImage.setImageResource(R.drawable.ic_unfavorite);
        }else
        {
            holder.favoriteImage.setImageResource(R.drawable.ic_star);
        }
        if (note.getTag().equals("Work")){
            holder.tageImage.setImageResource(R.drawable.ic_tag33);
        }else if (note.getTag().equals("Personal")){
            holder.tageImage.setImageResource(R.drawable.ic_tag11);
        }else if (note.getTag().equals("Travel")){
            holder.tageImage.setImageResource(R.drawable.ic_tagg);
        }else if (note.getTag().equals("Life")){
            holder.tageImage.setImageResource(R.drawable.ic_tag22);
        }else if (note.getTag().equals("Untagged")){
            holder.tageImage.setImageResource(R.drawable.ic_tag_4);
        }
        String notes = lists.get(position).getNote() ;
        String date = lists.get(position).getDate() ;
        boolean favorite = lists.get(position).getFavorites() ;
        String tag = lists.get(position).getTag() ;
        String id = lists.get(position).getId() ;
        holder.ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext , EditnoteActivity.class) ;
                intent.putExtra("notes" , notes) ;
                intent.putExtra("date" , date) ;
                intent.putExtra("favorite" ,favorite) ;
                intent.putExtra("tag" , tag) ;
                intent.putExtra("id" , id) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                getContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter =  new Filter() {
        // run on background thread ;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NoteModel> filterList = new ArrayList<>() ;
            if (constraint.toString().isEmpty()){
                filterList.addAll(notesListAll) ;
            }else {
                for (NoteModel notes : notesListAll){
                    if (notes.getNote().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterList.add(notes) ;
                    }
                }
            }
            FilterResults results =  new FilterResults() ;
            results.values = filterList ;
            return results;
        }

        // run on a UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lists.clear();
            lists.addAll((Collection<? extends NoteModel>) results.values) ;
            notifyDataSetChanged();
        }
    } ;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView tageImage , favoriteImage ;
        public TextView noteText , dateText ;
        public MaterialRippleLayout ripple ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tageImage = itemView.findViewById(R.id.tageImage_id) ;
            favoriteImage = itemView.findViewById(R.id.favorite_id) ;
            noteText = itemView.findViewById(R.id.noteText_id) ;
            dateText = itemView.findViewById(R.id.dateText_id) ;
            ripple = itemView.findViewById(R.id.note_ripple_id) ;
        }
    }
}
