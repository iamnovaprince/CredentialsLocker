package com.novaapps.credentialslocker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.novaapps.credentialslocker.Class.CredentialData;

import java.util.List;

@Dao
public interface CredentialDao {

    @Query("Select * from Credential")
    List<CredentialData> getCredentialList();
    @Insert
    void insertCredential(CredentialData credentialData);
    @Update
    void updateCredential(CredentialData credentialData);
    @Delete
    void deleteCredential(CredentialData credentialData);

}
