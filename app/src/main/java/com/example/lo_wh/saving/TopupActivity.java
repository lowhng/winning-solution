package com.example.lo_wh.saving;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.topup_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Button button = findViewById(R.id.btn_topup);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Boolean validSerialNumber = false;
                Boolean topUpState = false;
                int topUpCredits = 0;
                EditText txtsn1 = findViewById(R.id.txt_sn1);
                EditText txtsn2 = findViewById(R.id.txt_sn2);
                EditText txtsn3 = findViewById(R.id.txt_sn3);
                EditText txtpin = findViewById(R.id.txt_pin);

                if(txtsn1.getText().length()==5
                        && txtsn2.getText().length()==5
                        && txtsn3.getText().length()==5){
                    String serialNumber = txtsn1.getText().toString() + txtsn2.getText().toString() + txtsn3.getText().toString();

                    System.out.println(serialNumber);
                    if(serialNumber.equals("000000000000010")){
                        validSerialNumber = true;
                        topUpCredits = 1000;
                    }else if(serialNumber.equals("000000000000020")){
                        validSerialNumber = true;
                        topUpCredits = 2000;
                    }else if(serialNumber.equals("000000000000050")){
                        validSerialNumber = true;
                        topUpCredits = 5000;
                    }
                }

                if(txtpin.getText().toString().equals("123456")){
                    topUpState = true;
                }

                final AlertDialog alertDialog = new AlertDialog.Builder(TopupActivity.this).create();

                if(topUpState){
                    alertDialog.setTitle("Top Up Successful");
                    alertDialog.setMessage("Congratulations! " + topUpCredits + " credits has been added to your account!");
                }else{
                    alertDialog.setTitle("Top Up Failed");
                    alertDialog.setMessage("Please check if Serial Number is correct");
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topup, menu);
        return true;
    }


}
