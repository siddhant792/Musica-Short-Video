package com.mark.tiktok20.All;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.User_like;
import com.mark.tiktok20.models.Visibility;

import java.util.ArrayList;

public class Adapter_SnapHelper extends RecyclerView.Adapter<Adapter_SnapHelper.SnapHelper_ViewHolder> {

    public Context context;
    private ArrayList<ModelRv_home> datalist;
    private DatabaseReference userRef,reftemp,userRefSetter;
    private FirebaseDatabase database;

    private FirebaseAuth mAuth;
    String userId;

    public Adapter_SnapHelper(Context context, ArrayList<ModelRv_home> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public SnapHelper_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_feed,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT));
        Adapter_SnapHelper.SnapHelper_ViewHolder viewHolder = new Adapter_SnapHelper.SnapHelper_ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SnapHelper_ViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();


        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);


        Visibility visibility = new Visibility();
        final Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        userId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("usersposts").child(userId);
        userRefSetter = FirebaseDatabase.getInstance().getReference("users");

        database = FirebaseDatabase.getInstance();
        final DatabaseReference reef  =database.getReference("usersposts");
        holder.likepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                            reftemp = zoneSnapshot.getRef();
                            for (DataSnapshot ds: zoneSnapshot.getChildren()){
                                if (ds.child("posturl").getValue().toString().equals(datalist.get(position).video)){
                                    if (ds.getChildrenCount() > 1){
                                        int count =0;
                                        for (DataSnapshot temp1 : ds.getChildren()){
                                            for (DataSnapshot temp2 : temp1.getChildren()){
                                                if (temp2.child("userid").getValue().toString().equals(userId)){
                                                    Log.e("User","hello");
                                                    DatabaseReference reftemp = temp2.child("userid").getRef();
                                                    reftemp.removeValue();
                                                    count++;
                                                }
                                            }
                                        }
                                        if (count ==0){
                                            String temp = ds.getKey();
                                            User_like like = new User_like(userId);
                                            Log.e("Sample","Reached");
                                            reftemp.child(temp).child("likes").push().setValue(like);
                                            vibe.vibrate(50);
                                            holder.likepost.startAnimation(myAnim);

                                        }
                                    }
                                    else {
                                        String temp = ds.getKey();
                                        User_like like = new User_like(userId);
                                        Log.e("Sample","Reached");
                                        reftemp.child(temp).child("likes").push().setValue(like);
                                        vibe.vibrate(50);
                                        holder.likepost.startAnimation(myAnim);
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        holder.rotate_cd.startAnimation(rotate);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(datalist.get(position).video);

            }
        });



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Watch Funny Videos On Musica-The Indian Short Video App : "+ datalist.get(position).getVideo()+" Get Now In Play Store : http://bit.do/musicashortvideo";
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                context.startActivity(Intent.createChooser(intent,"Share Using :"));
            }
        });

        reef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot ds: zoneSnapshot.getChildren()){
                        if (ds.child("posturl").getValue().toString().equals(datalist.get(position).video)){
                            setdata(zoneSnapshot.getKey(),holder);
                            if (ds.getChildrenCount() > 1){
                                for (DataSnapshot temp1 : ds.getChildren()){
                                    for (DataSnapshot temp2 : temp1.getChildren()){
                                        if (temp2.child("userid").getValue().toString().equals(userId)){
                                            holder.likepost.setImageResource(R.drawable.heart_liked);
                                            break;
                                        }
                                        else {
                                            holder.likepost.setImageResource(R.drawable.heart_nolike);
                                        }
                                    }
                                }
                            }
                            else {
                                holder.likepost.setImageResource(R.drawable.heart_nolike);
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setdata(final String user,@NonNull final Adapter_SnapHelper.SnapHelper_ViewHolder holder){
        userRefSetter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.getKey().equals(user)){
                        RequestOptions requestOptions2 = new RequestOptions()
                                .circleCrop();
                        holder.username.setText("@"+ds.child("username").getValue().toString());
                        Glide
                                .with(context)
                                .load(ds.child("profileurl").getValue().toString())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOptions2)
                                .into(holder.profilepic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Other_User_Profile.class);
                intent.putExtra("userid",user);
                context.startActivity(intent);
            }
        });

    }
    public void downloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download Video");
        request.setDescription("Downloading Video...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()+".mp4");
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(context, "Downloading..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class SnapHelper_ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profilepic,likepost,rotate_cd,download,share,thumbnail;
        private TextView username;
        //private JCVideoPlayerStandard jcVideoPlayerStandard;
        private PlayerView playerView;

        public SnapHelper_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilepic = itemView.findViewById(R.id.profile_postclick);
            username = itemView.findViewById(R.id.username_post);
            likepost = itemView.findViewById(R.id.like_post);
            thumbnail = itemView.findViewById(R.id.thumb);
            rotate_cd = itemView.findViewById(R.id.music_cd);
            download = itemView.findViewById(R.id.download_post);
            share = itemView.findViewById(R.id.share_post);
        }
    }

}
