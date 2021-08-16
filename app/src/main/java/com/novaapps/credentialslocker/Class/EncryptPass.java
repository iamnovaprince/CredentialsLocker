package com.novaapps.credentialslocker.Class;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptPass {

    public String EncryptPassword(String PlainPass , String AuthPass) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey key = generateKey(AuthPass);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE , key);
        byte[] encVAl = cipher.doFinal(PlainPass.getBytes());
        return Base64.encodeToString(encVAl , Base64.DEFAULT);
    }

    public String DecryptPass(String EncryptedPass , String AuthPass) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey key = generateKey(AuthPass);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE , key);
        byte[] decodeValue = Base64.decode(EncryptedPass , Base64.DEFAULT);
        byte[] decVAl = cipher.doFinal(decodeValue);
        return  new String(decVAl);
    }

    private SecretKeySpec generateKey(String authPass) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] pass = authPass.getBytes();
        digest.update(pass,0,pass.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key,"AES");
    }

}
