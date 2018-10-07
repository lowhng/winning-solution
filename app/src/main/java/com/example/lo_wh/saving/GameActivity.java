package com.example.lo_wh.saving;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GameActivity extends AppCompatActivity {

    final String imagePrefix = "white_level";
    final String viewPrefix = "outside_imageview";
    ImageView mImage_in;
    HashMap<String,Integer> hashMap;
    float savingsBalance;
    long creditBalance;
    ImageButton refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        refresh();

        hashMap = new HashMap<>();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("gameHashMap", Context.MODE_PRIVATE);
        try {
            if (sharedPref != null) {
                Log.d("InitGame","Shared Preferences Found!");
                JSONObject jsonHashMap = new JSONObject(sharedPref.getString("hashMap", (new JSONObject()).toString()));
                printJsonHashMap(jsonHashMap);
                Iterator<String> keyIterator = jsonHashMap.keys();
                while(keyIterator.hasNext()){
                    String currentKey = keyIterator.next();
                    hashMap.put(currentKey,jsonHashMap.getInt(currentKey));
                }
            } else {
                Log.d("InitGame","Shared Preferences Not Found!");
                hashMap = null;
            }
        }catch(JSONException e){
            e.printStackTrace();
            hashMap = null;
        }catch(Exception e){
            e.printStackTrace();
            hashMap = null;
        }
        if (hashMap == null || hashMap.size() == 0){
            initHashMap();
        }else{
            initialRender();
        }

        mImage_in = findViewById(R.id.inside_imageview);
        mImage_in.setAdjustViewBounds(true);
        mImage_in.setScaleType(ImageView.ScaleType.CENTER_CROP);

        refreshBtn = findViewById(R.id.btn_refresh);

        findViewById(R.id.btn_buy).setOnClickListener(new onClickListener());
        refreshBtn.setOnClickListener(new onClickUpdateListener());

        //Drop Listener
        findViewById(R.id.outside_imageview1).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview2).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview3).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview4).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview5).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview6).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview7).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview8).setOnDragListener(new dragEventListener());
        findViewById(R.id.outside_imageview9).setOnDragListener(new dragEventListener());
    }

    private void initHashMap(){
        //Initialize HashMap for first time players
        hashMap = new HashMap<String,Integer>()
        {{
            put("1",0);
            put("2",0);
            put("3",0);
            put("4",0);
            put("5",0);
            put("6",0);
            put("7",0);
            put("8",0);
            put("9",0);
        }};
    }

    private void initialRender(){
        Log.d("Map Render","Running initial render...");
        //First render of saved game
        Iterator hashMapIterator = hashMap.entrySet().iterator();
        while(hashMapIterator.hasNext()){
            Map.Entry<String, Integer> keyValuePair = (Map.Entry<String, Integer>)hashMapIterator.next();

            String curImageViewIdString = viewPrefix + keyValuePair.getKey();
            int curImageViewId = getResources().getIdentifier(curImageViewIdString,"id", GameActivity.this.getPackageName());
            ImageView curImageView = findViewById(curImageViewId);

            if(keyValuePair.getValue() != 0){
                String curImageIdString = imagePrefix + keyValuePair.getValue().toString();
                int curImageId = getResources().getIdentifier(curImageIdString,"drawable", GameActivity.this.getPackageName());
                curImageView.setImageResource(curImageId);
                curImageView.setOnTouchListener(new dragTouchListener());
            }
        }
    }

    private final class onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            if(creditBalance>=5000) {
                final AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Confirm Action");
                alertDialog.setMessage("Exchange 5000 coins for 1 building?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go Back",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                boolean placed = false;
                                Integer i = 1;
                                for (; i <= hashMap.size() && !placed; i++) {
                                    String intConv = i.toString();
                                    Log.d("NewBuildingPlacement", "Iterating " + intConv);
                                    Log.d("NewBuildingPlacement", "Value = " + hashMap.get(intConv).toString());
                                    if (hashMap.get(intConv) == 0) {
                                        placed = true;

                                        creditBalance -= 5000;

                                        Integer tempVal = hashMap.get(intConv);
                                        tempVal++;
                                        hashMap.put(intConv, tempVal);

                                        String newBuildingViewName = "outside_imageview" + intConv;
                                        Log.d("NewBuildingPlacement", "Found empty lot " + newBuildingViewName);
                                        int id = getResources().getIdentifier(newBuildingViewName, "id", GameActivity.this.getPackageName());

                                        ImageView newBuildingView = findViewById(id);
                                        //Set Level 1
                                        newBuildingView.setImageResource(R.drawable.white_level1);

                                        //Set Draggable
                                        newBuildingView.setOnTouchListener(new dragTouchListener());

                                        //Save Results to Shared Preferences
                                        saveResults();
                                        saveBalance();
                                        refresh();
                                    }
                                }
                                if (!placed) {
                                    //No space
                                    Toast noSpaceToast = Toast.makeText(getApplicationContext(),
                                            "No space remaining for new buildings, please merge existing buildings to continue",
                                            Toast.LENGTH_LONG);
                                    noSpaceToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    noSpaceToast.show();
                                }
                            }
                        });

                alertDialog.show();
            }else {
                Toast insufficientFundsToast = Toast.makeText(getApplicationContext(),
                        "Insufficient Balance to Purchase New Building",
                        Toast.LENGTH_SHORT);
                insufficientFundsToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                insufficientFundsToast.show();
            }
        }
    }

    private final class onClickUpdateListener implements View.OnClickListener{

        @Override
        public void onClick(View v){
            long totalIncome = 0l;
            Iterator hashMapIterator = hashMap.entrySet().iterator();
            while(hashMapIterator.hasNext()){
                Map.Entry<String,Integer> keyValuePair = (Map.Entry<String, Integer>)hashMapIterator.next();
                double multiplier = (double)keyValuePair.getValue();
                Log.d("Income", "Multiplier: " + multiplier);
                double specificIncome = 0d;
                if(multiplier != 0d){
                    specificIncome = 100 * Math.pow(3, multiplier);
                }
                Log.d("Income", "Specific Income: " + specificIncome);
                totalIncome += (long)specificIncome;
                Log.d("Income","Subtotal Income: " + totalIncome);
            }
            creditBalance += totalIncome;
            saveBalance();
            refresh();
        }
    }

    private final class dragTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            } else {
                return false;
            }
        }

    }

    protected class dragEventListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event){

            final int action = event.getAction();

            switch(action){
                case DragEvent.ACTION_DROP:
                    //Get required information
                    ImageView movedView = (ImageView)event.getLocalState();
                    String movedViewName = getResources().getResourceName(movedView.getId());
                    String movedViewNumber = movedViewName.substring(movedViewName.indexOf(viewPrefix) + viewPrefix.length());

                    ImageView targetView = (ImageView) v;
                    String targetViewName = getResources().getResourceName(targetView.getId());
                    String targetViewNumber = targetViewName.substring(targetViewName.indexOf(viewPrefix) + viewPrefix.length());

                    Log.d("DragEvent","Moving " + viewPrefix + movedViewNumber);
                    Log.d("DragEvent","Dropped on " + viewPrefix + targetViewNumber);

                    //Check if moved and target are at same level
                    int movedLevel = hashMap.get(movedViewNumber);
                    int targetLevel = hashMap.get(targetViewNumber);

                    if(movedLevel == targetLevel){
                        //Same level, add target level
                        targetLevel++;
                        hashMap.put(targetViewNumber, targetLevel);

                        //Display new level
                        int targetImageId = getResources().getIdentifier(imagePrefix + targetLevel,"drawable", GameActivity.this.getPackageName());
                        targetView.setImageResource(targetImageId);

                        //Reset moved level
                        hashMap.put(movedViewNumber,0);

                        //Display blank
                        movedView.setImageResource(android.R.color.transparent);

                        //Remove drag listener for moved view
                        movedView.setOnTouchListener(null);

                        //Save to Shared Preferences
                        saveResults();

                    }else if (targetLevel == 0){
                        //Target Level 0, Move Building
                        targetLevel = movedLevel;
                        hashMap.put(targetViewNumber, targetLevel);

                        //Display new level
                        int targetImageId = getResources().getIdentifier(imagePrefix + targetLevel,"drawable", GameActivity.this.getPackageName());
                        targetView.setImageResource(targetImageId);

                        //Set listener (in case of level 0 -> 1
                        targetView.setOnTouchListener(new dragTouchListener());

                        //Reset moved level
                        hashMap.put(movedViewNumber,0);

                        //Display blank
                        movedView.setImageResource(android.R.color.transparent);

                        //Remove drag listener for moved view
                        movedView.setOnTouchListener(null);

                        //Save to Shared Preferences
                        saveResults();

                    }else{
                        Toast incorrectLevelToast = Toast.makeText(getApplicationContext(),
                                "Buildings must be of same level to merge",
                                Toast.LENGTH_SHORT);
                        incorrectLevelToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        incorrectLevelToast.show();
                    }
            }
            return true;
        }
    }

    private void saveResults(){
        Log.d("SaveHashMap","Attempting to save results!");
        //Save results to Shared Preferences
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("gameHashMap", Context.MODE_PRIVATE);
        JSONObject jsonHashMap = new JSONObject(hashMap);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.remove("hashMap").commit();
        sharedPrefEditor.putString("hashMap", jsonHashMap.toString());
        sharedPrefEditor.commit();

        //DEBUG: Check results
        SharedPreferences sharedPrefCheck = getApplicationContext().getSharedPreferences("gameHashMap", Context.MODE_PRIVATE);
        try {
            JSONObject jsonHashMapCheck = new JSONObject(sharedPrefCheck.getString("hashMap", (new JSONObject()).toString()));
            printJsonHashMap(jsonHashMapCheck);
        }catch (JSONException e){
            e.printStackTrace();
            Log.d("SaveHashMap","JSON Check Failed");
        }
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
    }

    private void printJsonHashMap(JSONObject jsonHashMap){
        Log.d("JSONHashMap","===========Check============");
        Log.d("JSONHashMap", jsonHashMap.toString());
        Log.d("JSONHashMap","==========End=Check=========");
    }

    private void refresh(){
        SharedPreferences sharedPrefBalance = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        if(sharedPrefBalance!=null){
            savingsBalance = sharedPrefBalance.getFloat("savingsBalance", 100.0f);
            creditBalance = sharedPrefBalance.getLong("creditBalance", 0l);
        }

        TextView txtCredit = findViewById(R.id.txt_credit);
        txtCredit.setText(Long.toString(creditBalance));
    }
}
