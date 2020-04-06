package com.example.notebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.notebook.Models.SettingModel;
import com.example.notebook.R;
import com.example.notebook.Ui.AboutusActivity;
import com.example.notebook.Ui.ContactusActivity;
import com.example.notebook.Ui.ForgotpasswordActivity;
import com.example.notebook.Ui.RateusActivity;
import com.example.notebook.Ui.ShareappActivity;
import com.example.notebook.Ui.privacyActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.myViewHolder> {
    private ArrayList<SettingModel> list ;
    private Context context ;

    public SettingAdapter(ArrayList<SettingModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_items , parent , false) ;
        myViewHolder myViewHolder = new myViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        SettingModel model = list.get(position) ;
        holder.image.setImageResource(model.getImg());
        holder.text.setText(model.getName());
        holder.ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.text.getText().equals("Tell a friend")){
                    Intent intent = new Intent(context , ShareappActivity.class) ;
                    context.startActivity(intent);
                }else if (holder.text.getText().equals("Rate Us")){
                    Intent intent = new Intent(context , RateusActivity.class) ;
                    context.startActivity(intent);
                }else if (holder.text.getText().equals("Privacy Policy")){
                    Intent intent = new Intent(context , privacyActivity.class) ;
                    context.startActivity(intent);
                }else if (holder.text.getText().equals("Contact US")){
                    Intent intent = new Intent(context , ContactusActivity.class) ;
                    context.startActivity(intent);
                }else if (holder.text.getText().equals("Forgot Paaword")){
                    Intent intent = new Intent(context , ForgotpasswordActivity.class) ;
                    intent.putExtra("code" , "returned") ;
                    context.startActivity(intent);
                }else if (holder.text.getText().equals("About Us")){
                    Intent intent = new Intent(context , AboutusActivity.class) ;
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView image ;
        public TextView text ;
        public MaterialRippleLayout ripple ;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.settingImg_id) ;
            text = itemView.findViewById(R.id.settingText_id) ;
            ripple =  itemView.findViewById(R.id.ripple_settings_id) ;
        }
    }
}
