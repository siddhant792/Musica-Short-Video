package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mark.tiktok20.R;

public class Terms_Conditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__conditions);

        TextView text = findViewById(R.id.textView3);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(R.string.policy);
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Terms_Conditions.this.finish();
            }
        });

    }
}