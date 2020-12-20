package com.mark.tiktok20.All;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mark.tiktok20.R;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.ArrayList;
import java.util.Collections;


public class Home extends Fragment implements Player.EventListener {
    private ViewPager2 viewPager2;
    private DatabaseReference userRef,userRef_posts;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    CircularFillableLoaders circularFillableLoaders;

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Context context;
    Boolean isScrolling = false;
    int firstVisibleItem = 0, visibleItemCount, totalItemCount = 0, lastVisibleItem = 0;
    int currentPage = -1;
    private int currentItems = 1;
    private ProgressBar p_bar;
    SimpleExoPlayer[] player_array = new SimpleExoPlayer[20000];
    View layout;
    PlayerView playerView;
    int x =0;
    int y=0;
    SimpleExoPlayer privious_player;

    int page_global=0;
    ArrayList<ModelRv_home> modelRvList = new ArrayList<>();
    boolean is_visible_to_user;

    String userId;
    public Home() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        userRef_posts = FirebaseDatabase.getInstance().getReference("posts");
        //viewPager2 = view.findViewById(R.id.viewPager);
        context = getContext();

        recyclerView = view.findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        p_bar = view.findViewById(R.id.p_bar);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                totalItemCount = layoutManager.getItemCount();

                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();


                if (page_no != currentPage) {
                    currentPage = page_no;

                    System.out.println("currentPage" + currentPage);
                    Log.e("Current page", String.valueOf(currentPage));
                    Release_Privious_Player();

                    Set_Player(currentPage);

                }
            }
        });

        circularFillableLoaders = view.findViewById(R.id.yourCircularFillableLoaders);
        circularFillableLoaders.setVisibility(View.VISIBLE);
        setRecyclerView2();


        return view;
    }



    private void setRecyclerView2(){
        final Query query = userRef_posts.orderByChild("counter");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelRvList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    try {
                        modelRvList.add(new ModelRv_home(R.drawable.default_picc,ds.child("url").getValue().toString(),"@loading"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Collections.reverse(modelRvList);
                circularFillableLoaders.setVisibility(View.GONE);
                //viewPager2.setAdapter(new AdapterVp_home(getContext(),modelRvList,viewPager2));
                Adapter_SnapHelper adapter_snapHelper = new Adapter_SnapHelper(getContext(),modelRvList);
                recyclerView.setAdapter(adapter_snapHelper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
        modelRvList.add(new ModelRv_home(R.drawable.default_picc,"https://bbyte.in/upload/video/5efacd34eaba3.mp4","@loading"));
       // viewPager2.setAdapter(new AdapterVp_home(getContext(),modelRvList,viewPager2));


        Adapter_SnapHelper adapter_snapHelper = new Adapter_SnapHelper(getContext(),modelRvList);
        recyclerView.setAdapter(adapter_snapHelper);*/
    }



    public void Set_Player(final int currentPage) {

        try {

            Already_created_player();
            privious_player = player_array[currentPage];
            final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
            playerView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

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
                        if (!player_array[currentPage].getPlayWhenReady()) {
                            privious_player.setPlayWhenReady(true);
                        } else {
                            privious_player.setPlayWhenReady(false);
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
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
        }
    }


    public void Release_Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }


    public void Already_created_player(){

        Log.e("Page", String.valueOf(currentPage));
        if (currentPage==0){
            player_array[currentPage] = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "Bbyte"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(modelRvList.get(currentPage).getVideo()));
            player_array[currentPage].prepare(videoSource);
            player_array[currentPage].setRepeatMode(Player.REPEAT_MODE_ALL);
            player_array[currentPage].addListener(this);
            layout = layoutManager.findViewByPosition(currentPage);
            playerView= layout.findViewById(R.id.playerview);
            playerView.setPlayer(player_array[currentPage]);
            player_array[currentPage].setPlayWhenReady(true);
            player_array[currentPage+1] = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
            DataSource.Factory dataSourceFactory2 = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "Musica"));
            MediaSource videoSource2 = new ExtractorMediaSource.Factory(dataSourceFactory2)
                    .createMediaSource(Uri.parse(modelRvList.get(currentPage+1).getVideo()));
            player_array[currentPage+1] .prepare(videoSource2);
        }

        if (currentPage>0) {

            if (page_global<currentPage){

                Log.e("Entered","scrolled up");

                    Log.e("Treated as normal","true");
                    player_array[currentPage+1] = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
                    player_array[currentPage+1].prepare(new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context,
                            Util.getUserAgent(context, "Musica")))
                            .createMediaSource(Uri.parse(modelRvList.get(currentPage+1).getVideo())));
                Log.e("Next Player Ready","True");
                player_array[currentPage-1].release();
                player_array[currentPage] .setRepeatMode(Player.REPEAT_MODE_ALL);
                player_array[currentPage] .addListener(this);
                layout = layoutManager.findViewByPosition(currentPage);
                playerView= layout.findViewById(R.id.playerview);
                playerView.setPlayer(player_array[currentPage] );
                player_array[currentPage] .setPlayWhenReady(true);

            }else if(page_global>currentPage){
                Log.e("Entered","scrolled down");

                player_array[currentPage] = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "Musica"));
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(modelRvList.get(currentPage).getVideo()));
                player_array[currentPage].prepare(videoSource);
                player_array[currentPage].setRepeatMode(Player.REPEAT_MODE_ALL);
                player_array[currentPage].addListener(this);
                layout = layoutManager.findViewByPosition(currentPage);
                playerView= layout.findViewById(R.id.playerview);
                playerView.setPlayer(player_array[currentPage]);
                player_array[currentPage].setPlayWhenReady(true);
                player_array[currentPage+1].release();
                player_array[currentPage+1] = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
                player_array[currentPage+1].prepare(new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "Musica")))
                        .createMediaSource(Uri.parse(modelRvList.get(currentPage+1).getVideo())));
            }
        }
        page_global = currentPage;

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
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            p_bar.setVisibility(View.GONE);
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

    public void onClickfromActivity(){
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
        }
    }
    public void onClickfromActivity2(){
        if (privious_player != null) {
            privious_player.setPlayWhenReady(true);
        }
    }
}
