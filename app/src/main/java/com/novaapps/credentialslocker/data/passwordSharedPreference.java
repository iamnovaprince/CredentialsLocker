package com.novaapps.credentialslocker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class passwordSharedPreference  {
    private static final String pref_name = "PASSWORD_PROTECTION";
    private Context context;
    public passwordSharedPreference(Context context) {
        this.context = context;
    }
    public void setPassword(String Pass) throws NoSuchAlgorithmException {
        SharedPreferences.Editor editor = context.getSharedPreferences(pref_name , MODE_PRIVATE).edit();
        editor.putBoolean("Protected" , true);
        editor.putString("Password",encryptPassword(Pass));
        editor.apply();
    }
    public boolean isPasswordValid(String pass) throws NoSuchAlgorithmException {
        SharedPreferences reader = context.getSharedPreferences(pref_name , MODE_PRIVATE);
        return Objects.equals(reader.getString("Password", "NOT_FOUND"), encryptPassword(pass));
    }

    public boolean isProtected(){
        SharedPreferences reader = context.getSharedPreferences(pref_name , MODE_PRIVATE);
        return reader.getBoolean("Protected",false);
    }

    private String encryptPassword(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] md_bytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));

        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, md_bytes);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
