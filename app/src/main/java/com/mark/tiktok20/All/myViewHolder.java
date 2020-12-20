package com.mark.tiktok20.All;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.tiktok20.R;

public class myViewHolder extends RecyclerView.ViewHolder {
    ImageView profilepic;
    TextView usernamee;
    TextView namee;
    RelativeLayout clickable;
    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        profilepic = itemView.findViewById(R.id.profile_image_user_activity);
        usernamee = itemView.findViewById(R.id.text_username);
        namee = itemView.findViewById(R.id.text_name);
        clickable = itemView.findViewById(R.id.clickable_id);
    }
}
