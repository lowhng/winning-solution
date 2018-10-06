package com.example.lo_wh.saving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button gameBtn = findViewById(R.id.game_Btn);
        Button statsBtn = findViewById(R.id.stats_Btn);
        Button topupBtn = findViewById(R.id.topup_Btn);

        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainActivity.this,GameActivity.class);
                startActivity(game);
            }
        });

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stats = new Intent(MainActivity.this,GameActivity.class);
                startActivity(stats);
            }
        });

        topupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topup = new Intent(MainActivity.this,TopupActivity.class);
                startActivity(topup);
            }
        });
    }
}
