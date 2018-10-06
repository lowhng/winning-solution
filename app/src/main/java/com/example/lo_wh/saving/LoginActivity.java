package com.example.lo_wh.saving;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.password);

                if ((username.getText().toString().contentEquals("username")) &&
                        (password.getText().toString().contentEquals("password")))
                {
                    Intent main = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(main);
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Wrong Credentials!");
                    alertDialog.setMessage("Incorrect username or password entered");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        View subtitleTextView = findViewById(R.id.sub_title);

        subtitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username);
                username.setText("username");
                EditText password = findViewById(R.id.password);
                password.setText("password");
            }
        });

        EditText password = findViewById(R.id.password);

        password.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d("PasswordKeypress", Integer.toString(keyCode));
                    Button loginBtn = findViewById(R.id.loginBtn);
                    loginBtn.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
