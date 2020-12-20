package com.mark.tiktok20.All;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.mark.tiktok20.R;

public class Explore extends Fragment {

    ImageView wallpaper,careyourself;
    Button install_wa,install_cy;

    public Explore() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        wallpaper = view.findViewById(R.id.wallpaper);
        careyourself = view.findViewById(R.id.careyourself);
        install_wa = view.findViewById(R.id.install_wa);
        install_cy = view.findViewById(R.id.install_cy);
        Glide
                .with(getContext())
                .load("https://i.ibb.co/Hq0SHyH/Wa.png")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(wallpaper);
        Glide
                .with(getContext())
                .load("https://i.ibb.co/y40j4x5/Cy.png")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(careyourself);
        install_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mark.wallpaperarena&hl=en"));
                startActivity(i);
            }
        });

        install_cy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.bharat.care&hl=en"));
                startActivity(i);
            }
        });

        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mark.wallpaperarena&hl=en"));
                startActivity(i);
            }
        });

        careyourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.bharat.care&hl=en"));
                startActivity(i);
            }
        });

        return view;
    }

}