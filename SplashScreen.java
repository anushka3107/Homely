package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash_screen );

        SharedPreferences preferences = getSharedPreferences ( "mypref" , MODE_PRIVATE );
        email = preferences.getString ( "email" , "" );
        password = preferences.getString ( "password" , "" );
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        new Handler ( ).postDelayed ( new Runnable ( ) {
            @Override
            public void run() {
                Intent intent;
                if (!email.equals ( "" ) && !password.equals ( "" )) {
                    intent = new Intent ( getApplicationContext ( ) , NavAct.class );
                } else {
                    intent = new Intent ( getApplicationContext ( ) , LoginAct.class );
                }
                startActivity ( intent );
                finish ( );

            }
        } , 6000);
    }
}