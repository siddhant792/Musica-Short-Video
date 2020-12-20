package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.Sounds_model;

import java.io.File;
import java.util.ArrayList;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;
import pl.droidsonroids.gif.GifImageView;

public class Sounds extends AppCompatActivity {
    RecyclerView recyclerView;

    private StorageReference storageReference;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    String userId;

    ArrayList<Sounds_model> modelRvList = new ArrayList<>();
    private Adapter_sound.RecyclerViewClick listener;
    View prev_click = null;
    String des_path = "/storage/emulated/0/Musica_Cache/";
    String file_name;
    DownloadRequest prDownloader;
    private static final int DIALOUGE = 1;
    ProgressBar progressBar;
    ImageView back;
    DialogManager dialogManager;

    String sound_name;

    SimpleExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds);
        recyclerView = findViewById(R.id.recyclerView_sounds);
        onClicklistener();
        back = findViewById(R.id.back_img);
        progressBar = findViewById(R.id.loading_sounds);
        progressBar.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        File file = new File(des_path);
        if (!file.exists()){
            file.mkdir();
        }
        dialogManager = new DialogManager();
        PopupDialog.create(Sounds.this)
                .manageMe(dialogManager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Loading..."));
        file_name = String.valueOf(System.currentTimeMillis()) + ".aac";

        userRef = FirebaseDatabase.getInstance().getReference("adminsongs");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelRvList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    modelRvList.add(new Sounds_model(ds.child("name").getValue().toString(),ds.child("url").getValue().toString()));
                }
                Adapter_sound adapter = new Adapter_sound(Sounds.this,modelRvList,listener);
                recyclerView.setLayoutManager(new GridLayoutManager(Sounds.this,1, LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }
    private void onClicklistener() {
        listener = new Adapter_sound.RecyclerViewClick() {
            @Override
            public void onClick(View v, int position, GifImageView playing, ImageView select_this_btn,String name) {
                if (player!=null && prev_click != null){
                    player.release();
                    prev_click.findViewById(R.id.song_playing).setVisibility(View.GONE);
                    prev_click.findViewById(R.id.select_this_btn).setVisibility(View.GONE);
                }
                sound_name = name;
                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                player = ExoPlayerFactory.newSimpleInstance(Sounds.this, trackSelector);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Sounds.this,
                        Util.getUserAgent(Sounds.this, "Musica"));
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(modelRvList.get(position).getSound_url()));
                player.prepare(videoSource);
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                player.setPlayWhenReady(true);
                prev_click = v;
                select_this_btn.setVisibility(View.VISIBLE);
                playing.setVisibility(View.VISIBLE);
                select_this_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogManager.show(DIALOUGE);
                        prDownloader = PRDownloader.download(modelRvList.get(position).getSound_url(),des_path,file_name)
                                .build()
                                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                    @Override
                                    public void onStartOrResume() {

                                    }
                                })
                                .setOnPauseListener(new OnPauseListener() {
                                    @Override
                                    public void onPause() {

                                    }
                                })
                                .setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel() {

                                    }
                                })
                                .setOnProgressListener(new OnProgressListener() {
                                    @Override
                                    public void onProgress(Progress progress) {

                                    }
                                });
                        prDownloader.start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                dialogManager.destroy();
                                Intent output = new Intent();
                                output.putExtra("url",des_path+file_name);
                                output.putExtra("name",sound_name);
                                Sounds.this.setResult(RESULT_OK, output);
                                Sounds.this.finish();
                            }

                            @Override
                            public void onError(Error error) {
                                dialogManager.destroy();
                            }
                        });

                    }
                });
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player!=null){
            player.release();
        }
    }
}