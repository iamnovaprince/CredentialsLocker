package com.novaapps.credentialslocker.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.novaapps.credentialslocker.Class.CredentialData;
import com.novaapps.credentialslocker.Class.EncryptPass;
import com.novaapps.credentialslocker.R;
import com.novaapps.credentialslocker.adapter.IdListAdapter;
import com.novaapps.credentialslocker.data.CredentialDatabase;
import com.novaapps.credentialslocker.data.passwordSharedPreference;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Homescreen extends AppCompatActivity {

    RecyclerView recyclerView;
    IdListAdapter listAdapter ;
    FloatingActionButton _Fab;
    private static String PASSWORD = null;
    private boolean isProtected = false;
    private EditText _Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // XML Initialization
        recyclerView = findViewById(R.id.idListView);
        _Fab = findViewById(R.id.FAB);
        _Search = findViewById(R.id.Search);

        passwordSharedPreference pref = new passwordSharedPreference(getApplicationContext());
        isProtected = pref.isProtected();
        if(isProtected){
            VerifyPassword();
        }
        else{
            SetPassword();
        }

        // Floating Button Set on Click Listener
        _Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAddScreen();
            }
        });

        //Listen to Search Box
        _Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString());
            }
        });

    }

    private void SetAdapter(){
        // Adapter Initialization

        listAdapter = new IdListAdapter(getDataFromDatabase(), Homescreen.this,PASSWORD);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void VerifyPassword() {
        final int[] noOfAttempts = {1};
        // Initialize View
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Homescreen.this , R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.sheet_password_validation, (LinearLayout)findViewById(R.id.passwordContainer));

        final EditText encryptionPassword = bottomSheetView.findViewById(R.id.encryptionPassword);
        Button _save= bottomSheetView.findViewById(R.id.save);
        _save.setText("Decrypt");

        _save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(encryptionPassword.getText().toString())){
                    passwordSharedPreference pref = new passwordSharedPreference(getApplicationContext());
                    try {
                        if(pref.isPasswordValid(encryptionPassword.getText().toString())) {
                            PASSWORD = encryptionPassword.getText().toString();
                            SetAdapter();
                            bottomSheetDialog.cancel();
                        }
                        else{
                            if(noOfAttempts[0] >= 3) {
                                Toast.makeText(Homescreen.this, "Max Limit Reached , Exiting...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(Homescreen.this, "Wrong Password , " + (3 - noOfAttempts[0]) + " Left", Toast.LENGTH_SHORT).show();
                                noOfAttempts[0]++;
                            }
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    private void SetPassword() {
        // Initialize View
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Homescreen.this , R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.sheet_password_validation, (LinearLayout)findViewById(R.id.passwordContainer));

        final EditText encryptionPassword = bottomSheetView.findViewById(R.id.encryptionPassword);
        Button _save= bottomSheetView.findViewById(R.id.save);

        _save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(encryptionPassword.getText().toString())){
                    passwordSharedPreference pref = new passwordSharedPreference(getApplicationContext());
                    try {
                        PASSWORD = encryptionPassword.getText().toString();
                        pref.setPassword(encryptionPassword.getText().toString());
                        SetAdapter();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Homescreen.this, "Password Set", Toast.LENGTH_SHORT).show();
                    PASSWORD = encryptionPassword.getText().toString();
                    bottomSheetDialog.cancel();
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

    private void filterList(String s){
        s = s.toLowerCase();
        List<CredentialData> temp = new ArrayList();
        List<CredentialData> displayedData = getDataFromDatabase();
        if(displayedData.size() > 0) {
            for (CredentialData data : displayedData) {
                if (data.getAppName().toLowerCase().contains(s) || data.getUserName().toLowerCase().contains(s)) {
                    temp.add(data);
                }
            }
            listAdapter.setList(temp);
        }
    }

    // Get List of Data From Database
    private List<CredentialData> getDataFromDatabase(){
        CredentialDatabase credentialDatabase = CredentialDatabase.getInstance(this);
        return  credentialDatabase.credentialDao().getCredentialList();
    }

    // Add Credential Bottom Sheet Implementation
    private void ShowAddScreen() {

        // Initialize View
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Homescreen.this , R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.sheet_add_credentials , (LinearLayout)findViewById(R.id.addCredentialContainer));

        // Initialize Xml Widgets
        final EditText AppName = bottomSheetView.findViewById(R.id.appName);;
        final EditText UserName = bottomSheetView.findViewById(R.id.username);;
        final EditText Password = bottomSheetView.findViewById(R.id.password);;
        Button Save = bottomSheetView.findViewById(R.id.save);;

        // On Save Click
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting Value From Widget to String Variable
                String _appName = AppName.getText().toString();
                String _username = UserName.getText().toString();
                String _password = Password.getText().toString();

                // Checking if Values are not Empty
                if(TextUtils.isEmpty(_username)) {
                    Toast.makeText(Homescreen.this, "Enter a Username", Toast.LENGTH_SHORT).show();
                    UserName.setError("Username Missing");
                }
                else if(TextUtils.isEmpty(_password)) {
                    Toast.makeText(Homescreen.this, "Enter a Password", Toast.LENGTH_SHORT).show();
                    Password.setError("Password Missing");
                }
                else{
                    //Initialize Encrypt Class
                    EncryptPass encryptPass = new EncryptPass();
                    String enc = "";
                    try {

                        enc = encryptPass.EncryptPassword(_password,PASSWORD);
                        if(PASSWORD != null) {
                            add_to_database(_appName, _username, enc);

                            bottomSheetDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Data Added Successfully", Snackbar.LENGTH_LONG).show();
                            listAdapter.setList(getDataFromDatabase());
                        }
                        else{
                            Toast.makeText(Homescreen.this, " You need to Set A Password First", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                        Toast.makeText(Homescreen.this, "Error Encountered ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    // Adding the Values in Database
    private void add_to_database(String appName , String username , String pass) {
        CredentialData credentialData = new CredentialData(0, appName, username, pass);
        CredentialDatabase credentialDatabase = CredentialDatabase.getInstance(this.getApplicationContext());
        credentialDatabase.credentialDao().insertCredential(credentialData);
    }
}