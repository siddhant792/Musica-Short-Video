package com.mark.tiktok20.All;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.tiktok20.R;
import com.mark.tiktok20.models.Sounds_model;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Adapter_sound extends RecyclerView.Adapter<Adapter_sound.ViewHolder> {

    private ArrayList<Sounds_model> modelRvList;
    private Context context;
    private RecyclerViewClick listener;
    public Adapter_sound(Context context, ArrayList<Sounds_model> modelRvList,RecyclerViewClick listener) {
        this.modelRvList = modelRvList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_sound,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sound_name.setText(modelRvList.get(position).getSound_name());
    }

    @Override
    public int getItemCount() {
        return modelRvList.size();
    }

    public interface RecyclerViewClick{
        void onClick(View v,int position,GifImageView playing,ImageView select_sound_btn,String sound_name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView sound_name;
        GifImageView sound_playing;
        ImageView select_this_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sound_name = itemView.findViewById(R.id.sound_name);
            sound_playing = itemView.findViewById(R.id.song_playing);
            select_this_btn = itemView.findViewById(R.id.select_this_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition(),sound_playing,select_this_btn,modelRvList.get(getAdapterPosition()).getSound_name());
        }
    }
}
