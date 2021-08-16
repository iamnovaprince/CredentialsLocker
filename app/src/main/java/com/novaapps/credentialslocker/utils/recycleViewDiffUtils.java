package com.novaapps.credentialslocker.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.novaapps.credentialslocker.Class.CredentialData;

import java.util.List;

public class recycleViewDiffUtils extends DiffUtil.Callback {

    List<CredentialData> oldData ;
    List<CredentialData> newData ;

    public recycleViewDiffUtils(List<CredentialData> oldData, List<CredentialData> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData != null ? oldData.size():0;
    }

    @Override
    public int getNewListSize() {
        return newData != null ? newData.size():0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newData.get(newItemPosition).compareTo(oldData.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        CredentialData newCred = newData.get(newItemPosition);
        CredentialData oldCred = newData.get(oldItemPosition);

        Bundle bundle = new Bundle();

        if (newCred.getAppName().equals(oldCred.getAppName())){
            bundle.putString("appName",newCred.getAppName());
        }
        if (newCred.getUserName().equals(oldCred.getUserName())){
            bundle.putString("userName",newCred.getUserName());
        }

        if(bundle.size() == 0)
            return null;

        return bundle;

    }
}
