package com.example.nguyenhuutu.convenientmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentRestaurant implements Comparable{
    /**
     * Properties
     */
    private String cmtRestid;
    private String cmtRestContent;
    private Date cmtRestDate;
    private String restAccount;
    private String userAccount;
    private Float cmtRestStar;
    private Bitmap imageAvatar;

    private int cmtRestIdMaxNum = 0;

    /**
     * Constructor Methods
     */
    public CommentRestaurant(String _cmtRestId, String _cmtRestContent, Date _cmtRestDate, String _restAccount, String _userAccount, Float _cmtRestStar) {
        this.cmtRestid = _cmtRestId;
        this.cmtRestContent = _cmtRestContent;
        this.cmtRestDate = _cmtRestDate;
        this.restAccount = _restAccount;
        this.userAccount = _userAccount;
        this.cmtRestStar = _cmtRestStar;
    }

    public CommentRestaurant(String _cmtrestContent, String _restAccount, String _userAccount, Float _cmtRestStar) {
        this.cmtRestid = "";
        this.cmtRestContent = _cmtrestContent;
        this.cmtRestDate = new Date();
        this.restAccount = _restAccount;
        this.userAccount = _userAccount;
        this.cmtRestStar = _cmtRestStar;
    }

    /**
     * Getter methods
     */
    public Bitmap getImageAvatar(Context context) {
        if (imageAvatar != null) {
            return imageAvatar;
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        }
    }

    public void setImageAvatar(Bitmap imageAvatar) {
        try {
            this.imageAvatar = imageAvatar;
        } catch (Exception ex) {
        }
    }

    public String getCmtRestid() {
        return cmtRestid;
    }

    public String getCmtRestContent() {
        return cmtRestContent;
    }

    public Date getCmtRestDate() {
        return cmtRestDate;
    }

    public String getRestAccount() {
        return restAccount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public Float getCmtRestStar() {
        return cmtRestStar;
    }

    /**
     * Other Methods
     */

    /**
     * createCommentRestId()
     * - Create new id for CommentRestaurant
     *
     * @param idNum
     * @return String
     */
    public String createCommentRestId(int idNum) {
        String newId;

        this.cmtRestIdMaxNum = idNum;

        if (idNum < 10) {
            newId = String.format("RESTCMT0000%d", idNum);
        } else if (idNum < 100) {
            newId = String.format("RESTCMT000%d", idNum);
        } else if (idNum < 1000) {
            newId = String.format("RESTCMT00%d", idNum);
        } else if (idNum < 10000) {
            newId = String.format("RESTCMT0%d", idNum);
        } else {
            newId = String.format("RESTCMT%d", idNum);
        }

        return newId;
    }

    private void updateCommentRestaurantMaxId(int maxId) {
        Map<String, Object> document = new HashMap<>();
        document.put("cmt_rest_max_id", maxId);
        CMDB.db
                .collection("information")
                .document("cmt_rest_max_id")
                .set(document);
    }

    public void sendCommentRestaurant(final Fragment_Comment fragment) {
        final CommentRestaurant cmtRest = new CommentRestaurant(this.cmtRestid, this.cmtRestContent, this.cmtRestDate, this.restAccount, this.userAccount, this.cmtRestStar);
        CMDB.db
                .collection("information")
                .document("cmt_rest_max_id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                final int idNum = ((Number)task.getResult().get("cmt_rest_max_id")).intValue() + 1;
                                cmtRestid = createCommentRestId(idNum);
                                cmtRest.cmtRestid = cmtRestid;

                                CMDB.db
                                        .collection("comment_restaurant")
                                        .document(cmtRestid)
                                        .set(createCommentRestaurantData())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                updateCommentRestaurantMaxId(idNum);
                                                fragment.notifySendCommentSuccess(cmtRest);
                                            }
                                        });
                            } else {

                            }
                        } else {

                        }
                    }
                });
    }

    /**
     * loadCommentRestaurant()
     * - Load information of a CommentRestaurant
     *
     * @param document
     * @return CommentRestaurant
     */
    public static CommentRestaurant loadCommentRestaurant(Map<String, Object> document) {
        String _cmtRestId = document.get("cmt_rest_id").toString();
        String _cmtRestContent = document.get("cmt_rest_content").toString();
        Date _cmtRestDate = (Date)document.get("cmt_rest_date");
        String _restAccount = document.get("rest_account").toString();
        String _userAccount = document.get("user_account").toString();
        Float _cmtRestStar = ((Number) document.get("cmt_rest_star")).floatValue();

        return new CommentRestaurant(_cmtRestId, _cmtRestContent, _cmtRestDate, _restAccount, _userAccount, _cmtRestStar);
    }

    /**
     * createCommentRestaurantData()
     * -   Create CommentRestaurant's data for query
     *
     * @return
     */
    public Map<String, Object> createCommentRestaurantData() {
        Map<String, Object> document = new HashMap<>();

        document.put("cmt_rest_id", cmtRestid);
        document.put("cmt_rest_content", cmtRestContent);
        document.put("cmt_rest_date", cmtRestDate);
        document.put("rest_account", restAccount);
        document.put("user_account", userAccount);
        document.put("cmt_rest_star", cmtRestStar);

        return document;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        CommentRestaurant cmtRest = (CommentRestaurant)o;
        int result = 0;

        if (this.getCmtRestDate().compareTo(cmtRest.getCmtRestDate()) > 0) {
            result = -1;
        } else {
            result = 1;
        }

        return result;
    }
}
