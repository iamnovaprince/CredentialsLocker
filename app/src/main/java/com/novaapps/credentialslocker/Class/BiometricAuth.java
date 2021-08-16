package com.novaapps.credentialslocker.Class;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricAuth  {
    Context context;
    FragmentActivity activity;
    public BiometricAuth(Context context , FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public String BioAuth()  {
        final int[] code = new int[1];
        final boolean[] isSuccess = {false};
        //Create a thread pool with a single thread//

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Auth")
                .setSubtitle("Touch the Fingerprint sensor")
                .setDescription("")
                .setNegativeButtonText("Cancel")
                .build();

        Executor newExecutor = Executors.newSingleThreadExecutor();

        final BiometricPrompt bioAuth = new BiometricPrompt(activity, newExecutor , new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
                code[0] = errorCode;
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                isSuccess[0] = true;
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        bioAuth.authenticate(promptInfo);


        Log.d("Fuck" , Arrays.toString(isSuccess));
        if (isSuccess[0])
            return "SUCCESS";
        else
            return "FAILURE";


    }



}