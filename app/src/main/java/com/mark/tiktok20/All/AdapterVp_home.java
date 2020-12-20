package com.mark.tiktok20.All;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.User_like;



import java.util.List;

public class AdapterVp_home extends RecyclerView.Adapter<AdapterVp_home.SliderViewHolder>{
    private List<ModelRv_home> modelRvList;
    private Context context;
    private DatabaseReference userRef,reftemp,userRefSetter;
    private FirebaseDatabase database;
    SimpleExoPlayer prev_player;
    private ViewPager2 viewPager2;
    SimpleExoPlayer player;
    String current_user;

    BroadcastReceiver broadcastReceiver;

    boolean is_visible_to_user;
    private String shorten_url;
    private int set_play = 0;
    private int glob_pos=0;
    private FirebaseAuth mAuth;
    String uid;
    String userId;
    public AdapterVp_home( Context context,List<ModelRv_home> modelRvList,ViewPager2 viewPager2) {
        this.modelRvList = modelRvList;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_feed,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        final Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        userId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("usersposts").child(userId);
        userRefSetter = FirebaseDatabase.getInstance().getReference("users");
        holder.feedvideo.setVideoPath(modelRvList.get(position).getVideo());
        Log.e("Url",modelRvList.get(position).getVideo());
        holder.pause.setVisibility(View.GONE);

        /*holder.thumbnail.setVisibility(View.VISIBLE);
        Glide
                .with(context)
                .load(modelRvList.get(position).getVideo())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.thumbnail);*/

        /*holder.feedvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.progressBar.setVisibility(View.GONE);
                holder.thumbnail.setVisibility(View.GONE);
                Log.e("Prepared", String.valueOf(position));
                mp.start();
                float videoRatio = mp.getVideoWidth()/(float)mp.getVideoHeight();
                float screenRatio = holder.feedvideo.getWidth()/(float)holder.feedvideo.getHeight();
                float scale = videoRatio/screenRatio;
                if (scale>=1f){
                    holder.feedvideo.setScaleX(scale);
                }else {
                    holder.feedvideo.setScaleY(1f/scale);
                }
            }
        });
        holder.feedvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        holder.feedvideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (holder.feedvideo.isPlaying()){
                    holder.feedvideo.pause();
                    holder.pause.setVisibility(View.VISIBLE);
                }else {
                    holder.feedvideo.start();
                    holder.pause.setVisibility(View.GONE);
                }
                return false;
            }
        });*/

        viewPager2.registerOnPageChangeCallback(new OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Musica"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(modelRvList.get(position).getVideo()));
        player.prepare(videoSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        holder.playerView.setPlayer(player);
        player.setPlayWhenReady(true);





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
                                if (ds.child("posturl").getValue().toString().equals(modelRvList.get(position).video)){
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
                                            vibe.vibrate(100);
                                        }
                                    }
                                    else {
                                        String temp = ds.getKey();
                                        User_like like = new User_like(userId);
                                        Log.e("Sample","Reached");
                                        reftemp.child(temp).child("likes").push().setValue(like);
                                        vibe.vibrate(100);
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
                    downloadFile(modelRvList.get(position).video);

            }
        });



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Watch Funny Videos On Musica-The Indian Short Video App : "+ modelRvList.get(position).getVideo()+" Get Now In Play Store : http://bit.do/musicashortvideo";
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
                        if (ds.child("posturl").getValue().toString().equals(modelRvList.get(position).video)){
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

    private void setdata(final String user,@NonNull final SliderViewHolder holder){
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

    @Override
    public int getItemCount() {
        return modelRvList.size();
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

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView profilepic,likepost,rotate_cd,download,share,thumbnail,pause;
        private VideoView feedvideo;
        public TextView username;
        //private JCVideoPlayerStandard jcVideoPlayerStandard;
        private PlayerView playerView;


        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            profilepic = itemView.findViewById(R.id.profile_postclick);
            feedvideo = itemView.findViewById(R.id.feed_video);
            username = itemView.findViewById(R.id.username_post);
            likepost = itemView.findViewById(R.id.like_post);
            thumbnail = itemView.findViewById(R.id.thumb);


            rotate_cd = itemView.findViewById(R.id.music_cd);
            download = itemView.findViewById(R.id.download_post);
            //jcVideoPlayerStandard = itemView.findViewById(R.id.video_player);
            share = itemView.findViewById(R.id.share_post);
        }

    }
}
