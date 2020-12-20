package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

/*import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;*/

public class Single_Video_Open extends AppCompatActivity implements Player.EventListener {

    ProgressBar progressBar;
    private DatabaseReference reftemp;
    VideoView videoView;
    ImageView like,download,share,music_cd;
    private FirebaseAuth mAuth;
    PlayerView playerView;
    ImageView back;
    SimpleExoPlayer player;
    String url;
    String userId;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single__video__open);
        url = getIntent().getStringExtra("Url_value");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progabar);
        download = findViewById(R.id.download_post);
        music_cd = findViewById(R.id.music_cd);
        share = findViewById(R.id.share_post);
        back = findViewById(R.id.back_button);

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        music_cd.startAnimation(rotate);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(url);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Watch Funny Videos On Musica-The Indian Short Video App : "+url+" Get Now In Play Store : http://bit.do/musicashortvideo";
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                startActivity(Intent.createChooser(intent,"Share Using :"));
            }
        });
        userId = mAuth.getCurrentUser().getUid();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.e("ID",userId);
        like = findViewById(R.id.like_post);
        videoView = findViewById(R.id.feed_video);
        final Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        /*videoView.setVideoPath(url);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.start();
                float videoRatio = mp.getVideoWidth()/(float)mp.getVideoHeight();
                float screenRatio = videoView.getWidth()/(float)videoView.getHeight();
                float scale = videoRatio/screenRatio;
                if (scale>=1f){
                    videoView.setScaleX(scale);
                }else {
                    videoView.setScaleY(1f/scale);
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });*/

        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Musica"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url));
        player.prepare(videoSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);
        playerView= findViewById(R.id.playerview);
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);

        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Single_Video_Open.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {

                        }
                    }

                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if (!player.getPlayWhenReady()) {
                        player.setPlayWhenReady(true);
                    } else {
                        player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        final Animation myAnim = AnimationUtils.loadAnimation(Single_Video_Open.this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        database = FirebaseDatabase.getInstance();
        final DatabaseReference reef  =database.getReference("usersposts");
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                            reftemp = zoneSnapshot.getRef();
                            for (DataSnapshot ds: zoneSnapshot.getChildren()){
                                if (ds.child("posturl").getValue().toString().equals(url)){
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
                                            User_like like2 = new User_like(userId);
                                            Log.e("Sample","Reached");
                                            reftemp.child(temp).child("likes").push().setValue(like2);
                                            vibe.vibrate(100);
                                            like.startAnimation(myAnim);
                                        }
                                    }
                                    else {
                                        String temp = ds.getKey();
                                        User_like like2 = new User_like(userId);
                                        Log.e("Sample","Reached");
                                        reftemp.child(temp).child("likes").push().setValue(like2);
                                        vibe.vibrate(100);
                                        like.startAnimation(myAnim);
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


        reef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot ds: zoneSnapshot.getChildren()){
                        if (ds.child("posturl").getValue().toString().equals(url)){
                            if (ds.getChildrenCount() > 1){
                                for (DataSnapshot temp1 : ds.getChildren()){
                                    for (DataSnapshot temp2 : temp1.getChildren()){
                                        if (temp2.child("userid").getValue().toString().equals(userId)){
                                            like.setImageResource(R.drawable.heart_liked);
                                            break;
                                        }
                                        else {
                                            like.setImageResource(R.drawable.heart_nolike);
                                        }
                                    }
                                }
                            }
                            else {
                                like.setImageResource(R.drawable.heart_nolike);
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
    public void downloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download Video");
        request.setDescription("Downloading Video...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()+".mp4");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(this, "Downloading..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}