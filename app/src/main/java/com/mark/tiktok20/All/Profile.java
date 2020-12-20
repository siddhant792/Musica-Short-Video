package com.mark.tiktok20.All;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.ModelThumbnail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends Fragment {
    private FirebaseDatabase database;
    String userId;
    RelativeLayout progressbar_layout;
    private FirebaseAuth mAuth;
    RelativeLayout own_uploads,liked_ones;
    ImageView uploads,liked;
    Adapter_profile adapter;
    ImageView profilepic;
    private RecyclerView recyclerView;
    private DatabaseReference userRef,userRef2,userRef3;
    private StorageReference storageReference;
    TextView bio,nameHeader,username,followers,following,likes;
    ImageView morebtn;
    public Profile() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.recyclerView_profile);
        bio = view.findViewById(R.id.bio_text);
        nameHeader = view.findViewById(R.id.name_header);
        uploads = view.findViewById(R.id.temp3);
        liked = view.findViewById(R.id.temp4);
        username = view.findViewById(R.id.username);
        followers = view.findViewById(R.id.followers_number);
        following = view.findViewById(R.id.following_number);
        userRef = FirebaseDatabase.getInstance().getReference("usersposts");
        userRef2 = FirebaseDatabase.getInstance().getReference("follow");
        storageReference = FirebaseStorage.getInstance().getReference().child(userId);
        progressbar_layout = view.findViewById(R.id.progress_bar_screen);
        profilepic = view.findViewById(R.id.profile_image);
        likes = view.findViewById(R.id.likes_number);
        morebtn = view.findViewById(R.id.morebutton);
        progressbar_layout.setVisibility(View.VISIBLE);
        setRecyclerView2();
        setUserData();
        own_uploads = view.findViewById(R.id.profile_uploads);
        liked_ones = view.findViewById(R.id.profile_liked);
        own_uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerView2();
            }
        });
        liked_ones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup_liked_data();
            }
        });
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), morebtn);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"Clicked" + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Edit Profile")){
                            Intent i = new Intent(getContext(),Edit_Profile.class);
                            startActivity(i);
                        }
                        if (item.getTitle().equals("Log Out")){
                            mAuth.signOut();
                            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(),Register_user.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        return view;
    }

    private void setup_liked_data(){
        final List<ModelThumbnail> modelRvList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reef  =database.getReference("usersposts");
        reef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelRvList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (!ds.getKey().equals(userId)){
                        for (DataSnapshot dsa : ds.getChildren()){
                            for (DataSnapshot temp : dsa.getChildren()){
                                if (temp.getKey().equals("likes")){
                                    for (DataSnapshot temp1 : temp.getChildren()){
                                        if (temp1.child("userid").getValue().toString().equals(userId)){
                                            try {
                                                modelRvList.add(new ModelThumbnail(dsa.child("thumburl").getValue().toString(),dsa.child("posturl").getValue().toString()));
                                                adapter.notifyDataSetChanged();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new Adapter_profile(getContext(),modelRvList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        uploads.setColorFilter(ContextCompat.getColor(getContext(), R.color.myclr1), android.graphics.PorterDuff.Mode.MULTIPLY);
        liked.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
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
                    if (userId.equals(ds.getKey())){
                    for (DataSnapshot dsa : ds.getChildren()){
                        try {
                                modelRvList.add(new ModelThumbnail(dsa.child("thumburl").getValue().toString(),dsa.child("posturl").getValue().toString()));
                                adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new Adapter_profile(getContext(),modelRvList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        uploads.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        liked.setColorFilter(ContextCompat.getColor(getContext(), R.color.myclr1), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setUserData(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.getKey().equals(userId)){
                        RequestOptions requestOptions2 = new RequestOptions()
                                .circleCrop()
                                .override(200,200);
                        nameHeader.setText(ds.child("name").getValue().toString());
                        bio.setText(ds.child("bio").getValue().toString());
                        username.setText("@"+ds.child("username").getValue().toString());
                        //followers.setText(ds.child("followers").getValue().toString());
                        //following.setText(ds.child("following").getValue().toString());
                        //likes.setText(ds.child("likes").getValue().toString());
                        Glide
                                .with(getContext())
                                .load(ds.child("profileurl").getValue().toString())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOptions2)
                                .into(profilepic);
                        progressbar_layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        userRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
                        for (DataSnapshot sn: zoneSnapshot.getChildren()){
                            if (sn.getKey().equals("following")){

                                    following.setText(String.valueOf(sn.getChildrenCount()));
                            }else {
                                following.setText("0");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
                        count++;
                        int total=0;
                        for(DataSnapshot ds : zoneSnapshot.getChildren()){
                            for (DataSnapshot dss : ds.getChildren()){
                                total  = (int) (total+dss.getChildrenCount());
                            }
                        }
                        likes.setText(String.valueOf(total));
                    }
                }
                if (count==0){
                    likes.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

}