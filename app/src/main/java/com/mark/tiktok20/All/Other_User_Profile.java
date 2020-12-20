package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.FollowSystem;
import com.mark.tiktok20.models.ModelThumbnail;

import java.util.ArrayList;
import java.util.List;

public class Other_User_Profile extends AppCompatActivity {

    TextView name,username,followers,following,likes,bio;
    ImageView profile_pic;
    String rec_id;
    RelativeLayout progressbar_layout;
    private FirebaseAuth mAuth;
    Adapter_profile adapter;
    Button follow;
    private FirebaseDatabase database;
    private DatabaseReference userRef,userRefposts,userReffollow;
    String userId;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other__user__profile);
        rec_id = getIntent().getStringExtra("userid");
        name = findViewById(R.id.name_header);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerView_profile);
        userRef = FirebaseDatabase.getInstance().getReference("follow");
        username = findViewById(R.id.username);
        followers = findViewById(R.id.followers_number);
        following = findViewById(R.id.following_number);
        likes = findViewById(R.id.likes_number);
        profile_pic = findViewById(R.id.profile_image);
        follow = findViewById(R.id.follow_button);
        bio = findViewById(R.id.bio_text);
        progressbar_layout = findViewById(R.id.progress_bar_screen);
        progressbar_layout.setVisibility(View.VISIBLE);
        userRefposts = FirebaseDatabase.getInstance().getReference("usersposts");
        userReffollow = FirebaseDatabase.getInstance().getReference("follow");
        setUserData();
        setRecyclerView2();
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FollowSystem followSystem = new FollowSystem(userId);
                final FollowSystem followSystem1 = new FollowSystem(rec_id);
               // userRef.child(userId).child("following").push().setValue(followSystem1);

                if (!rec_id.equals(userId)){
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int count2=0;
                            for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                                if (zoneSnapshot.getKey().equals(rec_id)){
                                    Log.e("Status","If");
                                    count2++;
                                    int temp = 0;
                                    for (DataSnapshot sn: zoneSnapshot.getChildren()){
                                        if (sn.getKey().equals("followers")){
                                            int count=0;
                                            temp++;
                                            for (DataSnapshot sb: sn.getChildren()){
                                                for (DataSnapshot sbo: sb.getChildren()){
                                                    if (sbo.getValue().toString().equals(userId)){
                                                        //userRef.child(rec_id).child("followers").push().setValue(followSystem);
                                                        DatabaseReference reftemp = sb.child("uid").getRef();
                                                        reftemp.removeValue();
                                                        count++;
                                                    }
                                                }
                                            }
                                            if (count==0){
                                                userRef.child(rec_id).child("followers").push().setValue(followSystem);
                                                Log.e("Count:","To be added");
                                            }
                                        }
                                        if (temp==0){
                                            userRef.child(rec_id).child("followers").push().setValue(followSystem);
                                        }
                                    }
                                }
                            }
                            if (count2==0){
                                userRef.child(rec_id).child("followers").push().setValue(followSystem);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int count2=0;
                            for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                                if (zoneSnapshot.getKey().equals(userId)){
                                    Log.e("Status","If");
                                    count2++;
                                    for (DataSnapshot sn: zoneSnapshot.getChildren()){
                                        if (sn.getKey().equals("following")){
                                            int count=0;
                                            for (DataSnapshot sb: sn.getChildren()){
                                                for (DataSnapshot sbo: sb.getChildren()){
                                                    if (sbo.getValue().toString().equals(rec_id)){
                                                        //userRef.child(rec_id).child("followers").push().setValue(followSystem);
                                                        DatabaseReference reftemp = sb.child("uid").getRef();
                                                        reftemp.removeValue();
                                                        count++;
                                                    }
                                                }
                                            }
                                            if (count==0){
                                                userRef.child(userId).child("following").push().setValue(followSystem1);
                                                Log.e("Count:","To be added");
                                            }
                                            else {
                                                Log.e("Count:","Alreday Presrent");
                                            }
                                        }
                                    }
                                }
                            }
                            if (count2==0){
                                userRef.child(userId).child("following").push().setValue(followSystem1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(Other_User_Profile.this, "Could not follow itself", Toast.LENGTH_SHORT).show();
                }
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
                        for (DataSnapshot sn: zoneSnapshot.getChildren()){
                            int count =0;
                            if (sn.getKey().equals("following")){
                                for (DataSnapshot sb: sn.getChildren()){
                                    for (DataSnapshot sbo: sb.getChildren()){
                                        if (sbo.getValue().toString().equals(rec_id)){
                                            follow.setText("UNFOLLOW");
                                            count++;
                                        }
                                    }
                                }
                            }
                            if (count==0){
                                follow.setText("FOLLOW");
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

    private void setUserData(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.getKey().equals(rec_id)){
                        RequestOptions requestOptions2 = new RequestOptions()
                                .circleCrop()
                                .override(200,200);
                        name.setText(ds.child("name").getValue().toString());
                        bio.setText(ds.child("bio").getValue().toString());
                        username.setText("@"+ds.child("username").getValue().toString());
                        followers.setText(ds.child("followers").getValue().toString());
                        following.setText(ds.child("following").getValue().toString());
                        likes.setText(ds.child("likes").getValue().toString());
                        Glide
                                .with(Other_User_Profile.this)
                                .load(ds.child("profileurl").getValue().toString())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOptions2)
                                .into(profile_pic);
                        progressbar_layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userReffollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x=0;
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(rec_id)){
                        x++;
                        for (DataSnapshot sn: zoneSnapshot.getChildren()){
                            if (sn.getKey().equals("following")){

                                following.setText(String.valueOf(sn.getChildrenCount()));
                            }else {
                                following.setText("0");
                            }
                        }
                    }
                }if (x==0){
                    following.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userReffollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x= 0;
                    for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                        if (zoneSnapshot.getKey().equals(rec_id)){
                            x++;
                            int count=0;
                            for (DataSnapshot sn: zoneSnapshot.getChildren()){
                                if (sn.getKey().equals("followers")){
                                    Log.e("Count temp:", String.valueOf(sn.getChildrenCount()));
                                    followers.setText(String.valueOf(sn.getChildrenCount()));
                                    count++;
                                }
                            }
                            if (count==0){
                                followers.setText("0");
                            }
                        }
                    }
                if(x==0) {
                    followers.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userRefposts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(rec_id)){
                        int total=0;
                        for(DataSnapshot ds : zoneSnapshot.getChildren()){
                            for (DataSnapshot dss : ds.getChildren()){
                                total  = (int) (total+dss.getChildrenCount());
                            }
                        }
                        likes.setText(String.valueOf(total));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });





    }


    private void setRecyclerView2(){
        final List<ModelThumbnail> modelRvList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reef  =database.getReference("usersposts");
        reef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelRvList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (rec_id.equals(ds.getKey())){
                        for (DataSnapshot dsa : ds.getChildren()){
                            try {
                                modelRvList.add(new ModelThumbnail(dsa.child("thumburl").getValue().toString(),dsa.child("posturl").getValue().toString()));
                                adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        Log.e("Stat","Not Found");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new Adapter_profile(this,modelRvList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);

    }

}