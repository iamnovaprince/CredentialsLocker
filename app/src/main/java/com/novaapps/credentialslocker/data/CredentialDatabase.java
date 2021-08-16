package com.novaapps.credentialslocker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.novaapps.credentialslocker.Class.CredentialData;

@Database(entities = {CredentialData.class}, exportSchema = false, version = 1)
public abstract class CredentialDatabase extends RoomDatabase {

    private static final String DB_NAME = "CREDENTIAL_DB" ;
    private static CredentialDatabase instance;

    public static synchronized CredentialDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), CredentialDatabase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract  CredentialDao credentialDao();

}
