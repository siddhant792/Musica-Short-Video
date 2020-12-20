package com.mark.tiktok20.All;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.ModelThumbnail;

import java.util.HashMap;
import java.util.List;


public class
Adapter_profile extends RecyclerView.Adapter<Adapter_profile.ViewHolder>{
    private List<ModelThumbnail> modelRvList;
    private Context context;
    public Adapter_profile(Context context, List<ModelThumbnail> modelRvList) {
        this.modelRvList = modelRvList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop();

        Glide
                .with(context)
                .load(modelRvList.get(position).getThumb())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.Image);


        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Single_Video_Open.class);
                intent.putExtra("Url_value",modelRvList.get(position).getPost());
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return modelRvList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.img_item);
            AppCompatActivity activity = (AppCompatActivity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            /*int devicew = metrics.widthPixels;
            int deviceh = metrics.heightPixels;
            int cw = (int) (devicew/2.27);
            int ch = (int) (deviceh/2.27);
            Image.getLayoutParams().height = ch;
            Image.getLayoutParams().width = cw;
            Image.requestLayout();*/
        }

    }


}
