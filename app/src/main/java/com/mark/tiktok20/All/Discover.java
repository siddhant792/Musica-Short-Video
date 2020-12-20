package com.mark.tiktok20.All;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.User_search;

public class Discover extends Fragment {
    private RecyclerView recyclerView;
    Adapter_profile adapter;
    AutoCompleteTextView searchbar;
    ImageView search;
    private FirebaseDatabase database;
    String userId;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions<User_search> options;
    private FirebaseRecyclerAdapter<User_search,myViewHolder> adapter2;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    public Discover() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        searchbar = view.findViewById(R.id.searchbar);
        recyclerView = view.findViewById(R.id.recyclerView_profile);
        search = view.findViewById(R.id.serachicon);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchbar.getText().toString();
                searchUser(text);
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchbar.getText().toString();
                searchUser(text);
            }
        });
        String text = "";
        searchUser(text);
        return view;
    }

    private void searchUser(String keyword) {
        Query query = userRef.orderByChild("username").startAt(keyword).endAt(keyword + "uf8ff");
        options = new FirebaseRecyclerOptions.Builder<User_search>().setQuery(query,User_search.class).build();
        adapter2 = new FirebaseRecyclerAdapter<User_search, myViewHolder>(options) {
            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
                View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_serch,parent,false);
                return new myViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull User_search model) {
                holder.namee.setText(model.getName());
                holder.usernamee.setText(model.getUsername());
                RequestOptions requestOptions2 = new RequestOptions()
                        .circleCrop();
                Glide
                        .with(getContext())
                        .load(model.getProfileurl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOptions2)
                        .into(holder.profilepic);

                final String userid  = model.getUid();

                holder.clickable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(),userid, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),Other_User_Profile.class);
                        intent.putExtra("userid",userid);
                        startActivity(intent);
                    }
                });

            }
        };
        adapter2.startListening();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter2);
    }
}