package com.novaapps.credentialslocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.novaapps.credentialslocker.Class.BiometricAuth;
import com.novaapps.credentialslocker.screens.Homescreen;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Disable Dark Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        timer();

    }


    void timer(){

        Handler handler = new  Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeScreen();
            }
        },3000);

    }

    void HomeScreen(){
        startActivity(new Intent(this, Homescreen.class));
        finish();
    }

}