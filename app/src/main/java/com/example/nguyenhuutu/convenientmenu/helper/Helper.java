package com.example.nguyenhuutu.convenientmenu.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.User;
import com.example.nguyenhuutu.convenientmenu.register.CustomerRegister;
import com.example.nguyenhuutu.convenientmenu.register.RestaurantRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.json.JSONObject.NULL;

public class Helper {
    public static final int FRAGMENT_HOMEPAGE = 0;
    public static final int FRAGMENT_RESTAURANT_LIST = 1;
    public static final int FRAGMENT_LOGIN = 2;
    public static final int FRAGMENT_LOGOUT = 3;
    public static final int FRAGMENT_REGISTER = 4;
    public static final int FRAGMENT_SETTING = 5;
    public static final int FRAGMENT_MARK_LIST = 6;
    public static final int FRAGMENT_MANAGE_MENU = 7;
    public static final int FRAGMENT_MANAGE_EVENT = 8;
    public static final int FRAGMENT_ACCOUNT_INFO = 9;
    public static final int FRAGMENT_CHANGE_PASSWORD = 10;

    private static String LocalDbName = "convenient_menu";
    private static String UserSessionSharedDocument = "UserSession";
    private static String LoginedUserSessionSharedDocument = "LoginedUser";
    private static String LoginedRecentUserSessionSharedDocument = "LoinedRecentUser";
    public static float getOneDP() {
        float oneDP = 1;

        return oneDP;
    }
    //phhviet: convert px->dp
    public static int convertPxToDp(Context context, float number)
    {
        return Math.round(number / (context.getResources().getDisplayMetrics().density));
    }
    public static Point getDisplaySize(Display display) {
        // display size in pixels
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    //phhviet: làm tròn số thập phân, decimalPlace số chữ số thập phân . Đang làm tròn lên
    public static float round(float d, int decimalPlace){
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    public static void changeUserSession(Activity activity, UserSession userSession) {
        SharedPreferences pref = activity.getSharedPreferences(UserSessionSharedDocument, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LoginedUserSessionSharedDocument, userSession.createSharedDocumentData());
        editor.commit();
    }

    public static UserSession getLoginedUser(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(UserSessionSharedDocument, Context.MODE_PRIVATE);
        String loginedUserJson = pref.getString(LoginedUserSessionSharedDocument, "{}");

        return new UserSession(loginedUserJson);
    }

    public static void setSampleUserInLocal(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("recent_logined_user", Context.MODE_PRIVATE);
        JSONObject user = new JSONObject();
        try {
            user.put("logined", true);
            user.put("username", "tunh");
            user.put("password", "Nht13101997");
            user.put("isRest", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("logined_user", user.toString());
        editor.commit();
    }

    public static String md5(String inputString) {
        String outputString = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(inputString.getBytes());

            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (int index = 0; index < messageDigest.length; index++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[index]));
            }

            outputString = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return outputString;
    }

    public static String getCompressPassword(String inputString) {
        return md5("CM" + inputString);
    }

    public static void checkExistsAccount(final Activity activity, final String accountString) {
        CMDB.db
                .collection("customer")
                .document(accountString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (activity instanceof CustomerRegister) {
                                    ((CustomerRegister)activity).notifyExistsAccount();
                                }
                                else {
                                    ((RestaurantRegister)activity).notifyExistsAccount();
                                }
                            } else {
                                CMDB.db
                                        .collection("restaurant")
                                        .document(accountString)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        if (activity instanceof CustomerRegister) {
                                                            ((CustomerRegister) activity).notifyExistsAccount();
                                                        } else {
                                                            ((RestaurantRegister) activity).notifyExistsAccount();
                                                        }
                                                    } else {

                                                    }
                                                }
                                            }
                                        });
                            }
                        } else {

                        }
                    }
                });
    }

    public static void showAlert(Context context, String title, String message) {
        (new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }))
//                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
                .show();
    }
}
