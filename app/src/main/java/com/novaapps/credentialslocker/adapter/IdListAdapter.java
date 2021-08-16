package com.novaapps.credentialslocker.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.novaapps.credentialslocker.Class.CredentialData;
import com.novaapps.credentialslocker.Class.EncryptPass;
import com.novaapps.credentialslocker.R;
import com.novaapps.credentialslocker.data.CredentialDatabase;
import com.novaapps.credentialslocker.screens.Homescreen;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class IdListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<CredentialData> cred;
    private Context context;
    public  String PASS;

    public IdListAdapter(List<CredentialData> cred , Context context , String pass) {
        this.context = context;
        this.cred = cred;
        PASS = pass ;
    }

    public void setList(List<CredentialData> cred){
       this.cred = cred ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.id_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.appName.setText(cred.get(position).getAppName());
        holder.userName.setText(cred.get(position).getUserName());
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show_View_Screen(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cred!=null?cred.size():0;
    }

    private void Show_View_Screen(final int position){
        // Initialize View
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context , R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.sheet_view_credential,null);

        // Initialize Xml Widgets
        TextView appName = bottomSheetView.findViewById(R.id.appName);
        TextView username = bottomSheetView.findViewById(R.id.username);
        final TextView password = bottomSheetView.findViewById(R.id.password);

        // Getting Value From Cred Object and Showing it in Recycle View Widgets
        appName.setText(cred.get(position).getAppName());
        username.setText(cred.get(position).getUserName());
//        password.setText(cred.get(position).getPassWord());

        final boolean[] isPublic = {false};

        // On Delete Click Listener
        bottomSheetView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CredentialDatabase credentialDatabase = CredentialDatabase.getInstance(context);
                credentialDatabase.credentialDao().deleteCredential(cred.get(position));
                cred.remove(position);
                notifyDataSetChanged();
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
            }
        });

        //Toggle Password View
        bottomSheetView.findViewById(R.id.password_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPublic[0]) {
                    password.setText(R.string.hiddenPass);
                    isPublic[0] = false;
                }
                else {
                    EncryptPass encryptPass = new EncryptPass();
                    try {
                        password.setText(encryptPass.DecryptPass(cred.get(position).getPassWord(), PASS));
                        isPublic[0] = true;
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // On Click of Copy Username Button
        bottomSheetView.findViewById(R.id.copyUsername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Copy Username to Clipboard
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Username", cred.get(position).getUserName());
                Toast.makeText(context, "Username Copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

        // On Click of Copy Password Button
        bottomSheetView.findViewById(R.id.copyPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Copy Password to Clipboard
                EncryptPass encryptPass = new EncryptPass();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = null;
                try {
                    clip = ClipData.newPlainText("Password", encryptPass.DecryptPass(cred.get(position).getPassWord() , PASS));
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Password Copied", Toast.LENGTH_SHORT).show();
                assert clip != null;
                clipboard.setPrimaryClip(clip);
            }
        });

        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView appName ;
    TextView userName ;
    LinearLayout itemContainer;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        appName = itemView.findViewById(R.id.appName);
        userName = itemView.findViewById(R.id.username);
        itemContainer = itemView.findViewById(R.id.item_container);
    }

}


