package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.User;

public class Edit_Profile extends AppCompatActivity{
    EditText username;
    EditText name;
    RadioGroup grp;
    String gender;
    EditText bio;
    TextView nextbtn,gender_warning;
    ImageView profilepic;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    Vibrator vibe;
    private FirebaseDatabase database;
    boolean checked;
    String uploadpicurl;
    private StorageReference storageReference;
    Uri Imageuri;
    RadioButton male,female,others;
    RelativeLayout progress;
    String userId;
    String profile_url;
    private static final int ImageBack = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        username = findViewById(R.id.enterusername);
        name = findViewById(R.id.entername);
        mAuth = FirebaseAuth.getInstance();
        progress = findViewById(R.id.progress_bar_screen);
        progress.setVisibility(View.VISIBLE);
        userId = mAuth.getCurrentUser().getUid();
        nextbtn = findViewById(R.id.nextbtn);
        male = findViewById(R.id.male);
        gender_warning = findViewById(R.id.gender_warning);
        female = findViewById(R.id.female);
        others = findViewById(R.id.others);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        profilepic = findViewById(R.id.profile_image);
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_pic");
        bio = findViewById(R.id.bio);
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        gender_warning.setVisibility(View.GONE);
        grp = findViewById(R.id.tempm);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserData();
            }
        });


        final RequestOptions requestOptions2 = new RequestOptions()
                .circleCrop();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
                        username.setText(zoneSnapshot.child("username").getValue().toString());
                        name.setText(zoneSnapshot.child("name").getValue().toString());
                        Glide
                                .with(Edit_Profile.this)
                                .load(zoneSnapshot.child("profileurl").getValue().toString())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOptions2)
                                .into(profilepic);
                        profile_url = zoneSnapshot.child("profileurl").getValue().toString();
                        bio.setText(zoneSnapshot.child("bio").getValue().toString());
                        if (zoneSnapshot.child("gender").getValue().toString().equals("Male")){
                            male.setChecked(true);
                            checked = true;
                        }
                        else if (zoneSnapshot.child("gender").getValue().toString().equals("Female")){
                            female.setChecked(true);
                            checked = true;
                        }
                        else if (zoneSnapshot.child("gender").getValue().toString().equals("Others")){
                            others.setChecked(true);
                            checked = true;
                        }
                        progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private void sendUserData() {

        String usernamefinal = username.getText().toString();
        String namefinal = name.getText().toString();
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        String biofinal = bio.getText().toString();
        if (biofinal.length() > 70){
            vibe.vibrate(60);
        }else {
            String Id = userRef.push().getKey();
            if (checked){
                if (uploadpicurl != null){
                    User user = new User(namefinal,usernamefinal,gender,biofinal,uploadpicurl,userId,0,0,0);
                    userRef.child(userId).setValue(user);
                }
                else {
                    User user = new User(namefinal,usernamefinal,gender,biofinal,profile_url,userId,0,0,0);
                    userRef.child(userId).setValue(user);
                }
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                gender_warning.setVisibility(View.VISIBLE);
                vibe.vibrate(60);
            }
        }

    }

    public void onRadioButtonClicked(View view) {
        checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.male:
                gender = "Male";
                break;
            case R.id.female:
                gender = "Female";
                break;
            case R.id.others:
                gender = "Others";
                break;
        }
    }

    public void uploadimage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),ImageBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageBack && resultCode == RESULT_OK){
            Imageuri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Imageuri);
                uploadpicture();
                RequestOptions requestOptions2 = new RequestOptions()
                        .circleCrop();
                Glide
                        .with(this)
                        .load(bitmap)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOptions2)
                        .into(profilepic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadpicture(){
        StorageReference sample = storageReference.child(userId);
        final StorageReference Imagename = sample.child("picture");
        Imagename.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadpicurl = String.valueOf(uri);
                    }
                });
            }
        });
    }
}