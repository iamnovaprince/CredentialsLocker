package com.novaapps.credentialslocker.Class;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Credential")
public class CredentialData implements Comparable{
    @PrimaryKey(autoGenerate = true)
    private int id ;
    @ColumnInfo(name = "App Name")
    private String appName;
    @ColumnInfo(name = "Username")
    private String userName;
    @ColumnInfo(name = "Password")
    private String passWord;

    public CredentialData(int id, String appName, String userName, String passWord) {
        this.id = id;
        this.appName = appName;
        this.userName = userName;
        this.passWord = passWord;
    }
    @Ignore
    public CredentialData(String appName, String userName, String passWord) {
        this.appName = appName;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getAppName() {
        return appName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        CredentialData credentialData = (CredentialData) o ;
        if(credentialData.getId() == this.id)
            return 0;
        return 1;
    }
}
