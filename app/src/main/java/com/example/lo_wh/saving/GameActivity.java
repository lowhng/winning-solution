package com.example.lo_wh.saving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;


public class GameActivity extends AppCompatActivity {

    ImageView mImage_in;
    ImageView mImage_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mImage_in = findViewById(R.id.inside_imageview);
        mImage_out = findViewById(R.id.outside_imageview);

        mImage_in.setAdjustViewBounds(true);
        mImage_in.setScaleType(ImageView.ScaleType.CENTER_CROP);



    }
}
