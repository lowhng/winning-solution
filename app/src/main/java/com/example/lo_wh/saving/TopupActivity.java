package com.example.lo_wh.saving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TopupActivity extends AppCompatActivity {

    float savingsBalance;
    long creditBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        if(sharedPref!=null){
            savingsBalance = sharedPref.getFloat("savingsBalance", 1080.0f);
            creditBalance = sharedPref.getLong("creditBalance", 0l);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        Toolbar myToolbar = findViewById(R.id.topup_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EditText txt1 = findViewById(R.id.txt_sn1);
        EditText txt2 = findViewById(R.id.txt_sn2);
        EditText txt3 = findViewById(R.id.txt_sn3);

        txt1.addTextChangedListener(new onTextChange());
        txt2.addTextChangedListener(new onTextChange());
        txt3.addTextChangedListener(new onTextChange());

        final Button button = findViewById(R.id.btn_topup);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Boolean validSerialNumber = false;
                Boolean topUpState = false;
                int topUpCredits = 0;
                float topUpSavings = 0.0f;

                EditText txtsn1 = findViewById(R.id.txt_sn1);
                EditText txtsn2 = findViewById(R.id.txt_sn2);
                EditText txtsn3 = findViewById(R.id.txt_sn3);
                EditText txtpin = findViewById(R.id.txt_pin);

                if(txtsn1.getText().length()==5
                        && txtsn2.getText().length()==5
                        && txtsn3.getText().length()==5){
                    String serialNumber = txtsn1.getText().toString() + txtsn2.getText().toString() + txtsn3.getText().toString();

                    Log.d("TopUp",serialNumber);
                    switch(serialNumber){
                        case "000000000000010":
                            validSerialNumber = true;
                            topUpCredits = 1000;
                            topUpSavings = 10.0f;
                            break;
                        case "000000000000020":
                            validSerialNumber = true;
                            topUpCredits = 2000;
                            topUpSavings = 20.0f;
                            break;
                        case "000000000000050":
                            validSerialNumber = true;
                            topUpCredits = 5000;
                            topUpSavings = 50.0f;
                            break;
                    }
                }

                if(txtpin.getText().toString().equals("123456") && validSerialNumber){
                    topUpState = true;
                }

                final AlertDialog alertDialog = new AlertDialog.Builder(TopupActivity.this).create();

                if(topUpState){
                    creditBalance += topUpCredits;
                    savingsBalance += topUpSavings;
                    saveBalance();
                    alertDialog.setTitle("Top Up Successful");
                    alertDialog.setMessage("Congratulations! " + topUpCredits + " credits has been added to your account!");
                }else{
                    alertDialog.setTitle("Top Up Failed");
                    alertDialog.setMessage("Please check if Serial Number and PIN Code is correct");
                }

                final Boolean dialogRedirect = topUpState;
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(dialogRedirect){
                                    finish();
                                }else{
                                    alertDialog.dismiss();
                                }
                            }
                        });
                alertDialog.show();
            }
        });

        findViewById(R.id.lbl_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtsn1 = findViewById(R.id.txt_sn1);
                EditText txtsn2 = findViewById(R.id.txt_sn2);
                EditText txtsn3 = findViewById(R.id.txt_sn3);
                EditText txtpin = findViewById(R.id.txt_pin);

                txtsn1.setText("00000");
                txtsn2.setText("00000");
                txtsn3.setText("00050");
                txtpin.setText("123456");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topup, menu);
        return true;
    }

    private final class onTextChange implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){

            //Check length
            TextView curTextView = (TextView)getCurrentFocus();
            if(curTextView!=null){
                if(curTextView.getText().toString().length() == 5){
                    //Move to next field
                    View nextTextView = curTextView.focusSearch(View.FOCUS_RIGHT);
                    if(nextTextView != null){
                        nextTextView.requestFocus();
                    }
                }
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
            //No Code
        }

        @Override
        public void afterTextChanged(Editable s){
            //No Code
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

        //Check Shared Preferences
        SharedPreferences sharedPrefCheck = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        if(sharedPrefCheck!=null){
            Log.d("SharedPrefSaveCheck", Float.toString(sharedPrefCheck.getFloat("savingsBalance", 1080.0f)));
            Log.d("SharedPrefSaveCheck", Long.toString(sharedPrefCheck.getLong("accountBalance",0l)));
        }
    }
}
