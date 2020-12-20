package com.mark.tiktok20.All;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mark.tiktok20.R;

import java.util.List;


public class
Adapter_activity extends RecyclerView.Adapter<Adapter_activity.ViewHolder>{
    private List<ModelRv_activity> modelRvList;
    private Context context;
    public Adapter_activity(Context context, List<ModelRv_activity> modelRvList) {
        this.modelRvList = modelRvList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_activity,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        RequestOptions requestOptions2 = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .skipMemoryCache(true);

        Glide
                .with(context)
                .load(modelRvList.get(position).getVideo())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions2)
                .into(holder.imageprofile);
        Glide
                .with(context)
                .load(modelRvList.get(position).getVideo())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.Image);

        holder.text_activity.setText(modelRvList.get(position).getText());

        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return modelRvList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        ImageView imageprofile;
        TextView text_activity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.video_user_activity);
            imageprofile = itemView.findViewById(R.id.profile_image_user_activity);
            text_activity = itemView.findViewById(R.id.text_activity);
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
