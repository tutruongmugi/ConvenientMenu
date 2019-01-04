package com.example.nguyenhuutu.convenientmenu.helper;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class UserSession {
    //properties of the file
    private final String IS_LOGIN = "IsLogin";
    private final String USERNAME = "UserName";
    private final String PASSWORD = "Password";
    private Context _context;

    private String username;
    private boolean isRest;
    private boolean exists;

    public static final int RESTSTAURANT = 0;
    public static final int CUSTOMER = 1;

    public UserSession() {
        this.username = "";
        this.isRest = false;
        this.exists = false;
    }

    public UserSession(String _username, int type) {
        this.username = _username;
        this.setAccountType(type);
        this.exists = true;
    }

    public UserSession(String userSharedDocumentData) {
        try{
            JSONObject jsonObj = new JSONObject(userSharedDocumentData);
            if (jsonObj.getBoolean("isExists") == false) {
                this.username = "";
                this.isRest = false;
                this.exists = false;
            }
            else {
                this.username = jsonObj.getString("username");
                this.isRest = jsonObj.getBoolean("isRest");
                this.exists = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAccountType(int type) {
        if (type == RESTSTAURANT) {
            this.isRest = true;
        }
        else {
            this.isRest = false;
        }
    }

    public String getUsername() { return this.username; }

    public boolean isRest() { return this.isRest; }

    public boolean isExists() { return this.exists; }

    public String createSharedDocumentData() {
        String result = "{}";

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("username", this.username);
            jsonObj.put("isRest", this.isRest);
            jsonObj.put("isExists", this.exists);

            result = jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    //if user login then change values in pref file
//    public void createUserLoginSession(String uName, String uPassword){
//        // Storing login value as TRUE
//        editor.putBoolean(IS_LOGIN, true);
//
//        editor.putString(USERNAME, uName);
//        editor.putString(PASSWORD, uPassword);
//
//        // commit changes
//        editor.commit();
//    }

    //to get all user information
//    public HashMap<String, String> getUserDetails(){
//        //Use hash map to store user credentials
//        HashMap<String, String> userDetails = new HashMap<String, String>();
//
//        // user name
//        userDetails.put(USERNAME, pref.getString(USERNAME, ""));
//
//        // return user
//        return userDetails;
//    }

    //to clear all data of user when logout
//    public void logoutUser(){
//        editor.clear();
//        editor.commit();
//    }

    // Check for user login?
//    public boolean isUserLoggedIn(){
//        return pref.getBoolean(IS_LOGIN, false);
//    }
}
