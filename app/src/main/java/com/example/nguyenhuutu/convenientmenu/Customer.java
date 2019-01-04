package com.example.nguyenhuutu.convenientmenu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.register.CustomerRegister;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class Customer extends User {
    private String cus_lastname;
    private String cus_firstname;
    private String cus_email;
    private String cus_account;
    private String cus_password;
    private String cus_avatar_file;

    public Customer(String account, String password, String email, String lastName, String firstName) {
        this.cus_lastname = lastName;
        this.cus_firstname = firstName;
        this.cus_account = account;
        this.cus_email = email;
        this.cus_password = Helper.getCompressPassword(password);
        this.cus_avatar_file = "cus_anonymous.png";
    }
    @Override
    public void login(Activity activity) {

    }

    @Override
    public void register(final Activity activity) {
        final CustomerRegister act = (CustomerRegister)activity;
        Map<String, Object> data = createCustomerData();
        CMDB.db.collection("customer")
                .document(this.cus_account)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        act.callbackRegister(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        act.callbackRegister(false);
                    }
                });
    }

    @Override
    public boolean checkRegisterInfo() {
        return false;
    }

    @Override
    public boolean checkLoginInfo() {
        return false;
    }

    private Map<String, Object> createCustomerData() {
        Map<String , Object> data = new HashMap<>();

        data.put("cus_account", this.cus_account);
        data.put("cus_password", this.cus_password);
        data.put("cus_lastname", this.cus_lastname);
        data.put("cus_firstname", this.cus_firstname);
        data.put("cus_email", this.cus_email);
        data.put("cus_avatar_image_file", this.cus_avatar_file);

        return data;
    }
}
