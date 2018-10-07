package com.example.lo_wh.saving;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    float savingsBalance;
    long creditBalance;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh();


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
                Intent stats = new Intent(MainActivity.this,StatsActivity.class);
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

    @Override
    protected void onStart(){
        super.onStart();
        refresh();
    }

    private void refresh(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        float savingsBalance;
        long creditBalance;
        if(sharedPref!=null){
            savingsBalance = sharedPref.getFloat("savingsBalance", 1080.0f);
            Log.d("LoadBalance", "Savings Balance: " + savingsBalance);
            creditBalance = sharedPref.getLong("creditBalance", 0l);
            Log.d("LoadBalance", "Credit Balance: " + creditBalance);
        }else{
            savingsBalance = 100.0f;
            creditBalance = 0l;
        }

        TextView balance = findViewById(R.id.info_main);
        BigDecimal displayBalance = new BigDecimal(savingsBalance);
        displayBalance = displayBalance.setScale(2,BigDecimal.ROUND_HALF_UP);
        balance.setText("Savings: RM" + displayBalance.toString());
    }

    private void saveBalance(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.remove("savingsBalance").commit();
        sharedPrefEditor.remove("creditBalance").commit();
        Log.d("SaveBalance", "Savings Balance: " + savingsBalance);
        Log.d("SaveBalance", "Credit Balance: " + creditBalance);
        sharedPrefEditor.putFloat("savingsBalance", savingsBalance);
        sharedPrefEditor.putLong("creditBalance", creditBalance);
        sharedPrefEditor.commit();

        //Check Shared Preferences
        SharedPreferences sharedPrefCheck = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        if(sharedPrefCheck!=null){
            Log.d("SharedPrefSaveCheck", Float.toString(sharedPrefCheck.getFloat("savingsBalance", 1080.0f)));
            Log.d("SharedPrefSaveCheck", Long.toString(sharedPrefCheck.getLong("accountBalance",0l)));
        }
    }
}
