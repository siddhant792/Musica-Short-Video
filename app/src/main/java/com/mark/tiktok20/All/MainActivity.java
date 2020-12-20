package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mark.tiktok20.R;
import com.mark.tiktok20.ui.CameraActivity;

import me.shaohui.bottomdialog.BottomDialog;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    RelativeLayout container;
    RelativeLayout navbar;
    Home h = new Home();
    private long backpressedtime;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    Discover d = new Discover();
    Explore a = new Explore();
    Profile f = new Profile();
    private FirebaseAuth mAuth;
    ImageView home,discover,upload,activity,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        home = findViewById(R.id.home);
        discover = findViewById(R.id.search);
        navbar = findViewById(R.id.navbar);
        AnimationDrawable animationDrawable = (AnimationDrawable) navbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
        upload = findViewById(R.id.upload);
        activity = findViewById(R.id.activity);
        profile = findViewById(R.id.profile);
        if (savedInstanceState == null){
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container,h)
                    .add(R.id.container,d)
                    .add(R.id.container,a)
                    .add(R.id.container,f)
                    .show(h)
                    .hide(d)
                    .hide(a)
                    .hide(f)
                    .commit();
            home.setImageResource(R.drawable.home_active);
            discover.setImageResource(R.drawable.search_deactive);
            activity.setImageResource(R.drawable.activity_deactive);
            profile.setImageResource(R.drawable.profile_deactive);
        }
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .show(d)
                        .hide(h)
                        .hide(a)
                        .hide(f)
                        .commit();
                h.onClickfromActivity();
                home.setImageResource(R.drawable.home_deactive);
                discover.setImageResource(R.drawable.search_active);
                activity.setImageResource(R.drawable.activity_deactive);
                profile.setImageResource(R.drawable.profile_deactive);
                vibe.vibrate(60);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this,Upload_video.class);
                startActivity(intent);*/
                h.onClickfromActivity();
                BottomDialog.create(getSupportFragmentManager())
                        .setViewListener(new BottomDialog.ViewListener() {
                            @Override
                            public void bindView(View v) {
                                RelativeLayout record = v.findViewById(R.id.record_layout);
                                RelativeLayout upload = v.findViewById(R.id.upload_layout);
                                record.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(MainActivity.this, CameraActivity.class);
                                        startActivity(i);
                                    }
                                });
                                upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this,Upload_video.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        })
                        .setLayoutRes(R.layout.bottom_bar)
                        .setDimAmount(0.1f)
                        .setCancelOutside(true)
                        .setTag("BottomDialog")
                        .show();

            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .show(a)
                        .hide(d)
                        .hide(h)
                        .hide(f)
                        .commit();
                home.setImageResource(R.drawable.home_deactive);
                discover.setImageResource(R.drawable.search_deactive);
                activity.setImageResource(R.drawable.activity_active);
                profile.setImageResource(R.drawable.profile_deactive);
                vibe.vibrate(60);
                h.onClickfromActivity();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .show(f)
                        .hide(d)
                        .hide(a)
                        .hide(h)
                        .commit();
                home.setImageResource(R.drawable.home_deactive);
                discover.setImageResource(R.drawable.search_deactive);
                activity.setImageResource(R.drawable.activity_deactive);
                profile.setImageResource(R.drawable.profile_active);
                vibe.vibrate(60);
                h.onClickfromActivity();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .show(h)
                        .hide(d)
                        .hide(a)
                        .hide(f)
                        .commit();
                home.setImageResource(R.drawable.home_active);
                discover.setImageResource(R.drawable.search_deactive);
                activity.setImageResource(R.drawable.activity_deactive);
                profile.setImageResource(R.drawable.profile_deactive);
                vibe.vibrate(60);
                h.onClickfromActivity2();

            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);
            }
        }

    }



    @Override
    public void onBackPressed() {
        if (backpressedtime + 300 > System.currentTimeMillis()){
            MainActivity.this.finishAffinity();
        }
        else {
            Toast.makeText(getBaseContext(), "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        }
        backpressedtime = System.currentTimeMillis();
    }

    private void setupFirebase(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}





