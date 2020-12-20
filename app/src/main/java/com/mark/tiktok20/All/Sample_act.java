package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.mark.tiktok20.R;

public class Sample_act extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_act);
        img = findViewById(R.id.temp_img);
        String path = getIntent().getStringExtra("video_path");
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
        img.setImageBitmap(bMap);
    }
}