package com.example.nguyenhuutu.convenientmenu;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.register.RestaurantRegister;
import com.example.nguyenhuutu.convenientmenu.search.ListSearchResViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant extends User implements Comparable {
    /**
     * Properties
     */

    private static final Number ZERO = 0;

    private String restAccount;
    private String restPassword;
    private String restName;
    private String restEmail;
    private List<String> restAddresses;
    private String restDescription;
    private List<String> restMoreImages;
    private String restHomeImage;
    private Double maxStar;
    private Long viewedNumber;
    private String restPhone;
    private String restFacebook;
    private Long totalRating;
    public static int compareProperty;

    public final static int STAR = 0;
    public final static int VIEW = 1;

    /**
     * Methods List
     */
    public Restaurant(String name){
        this.restAccount = "";
        this.restPassword = "";
        this.restName = name;
        this.restEmail= "";
        this.restAddresses = new ArrayList<>();
        this.restDescription = "";
        this.restHomeImage = "";
        this.restMoreImages = new ArrayList<>();
        this.maxStar = ZERO.doubleValue();
        this.viewedNumber = ZERO.longValue();
    }

    public Restaurant(){
        this.restAccount = "";
        this.restPassword = "";
        this.restName = "";
        this.restEmail= "";
        this.restAddresses = new ArrayList<>();
        this.restDescription = "";
        this.restHomeImage = "";
        this.restMoreImages = new ArrayList<>();
        this.maxStar = ZERO.doubleValue();
        this.viewedNumber = ZERO.longValue();
    }

    public Restaurant(String accountStr,String passwordStr,String emailStr,String resNameStr){
        this.restAccount = accountStr;
        this.restPassword = Helper.getCompressPassword(passwordStr);
        this.restName = resNameStr;
        this.restEmail=emailStr;
        this.restAddresses = new ArrayList<>();
        this.restDescription = "";
        this.restHomeImage = "";
        this.restMoreImages = new ArrayList<>();
        this.maxStar = ZERO.doubleValue();
        this.viewedNumber = ZERO.longValue();
    }

    public Restaurant(String _restAccount, String _restPassword, String _restName, String _restDescription, List<String> _restAddresses, String _restHomeImage, List<String> _restMoreImages, Double _maxStar, Long _viewedNumber, Long _totalRating, String _restPhone, String _restFacebook,String _restEmail) {

        this.restAccount = _restAccount;
        this.restPassword = Helper.getCompressPassword(_restPassword);
        this.restName = _restName;
        this.restAddresses = _restAddresses;
        this.restDescription = _restDescription;
        this.restHomeImage = _restHomeImage;
        this.restMoreImages = _restMoreImages;
        this.maxStar = _maxStar;
        this.viewedNumber = _viewedNumber;
        this.totalRating = _totalRating;
        this.restPhone = _restPhone;
        this.restFacebook = _restFacebook;
        this.restEmail= _restEmail;
    }

    /**
     * Getter methods for properties
     */
    public String getRestAccount() {
        return this.restAccount;
    }

    public String getRestPassword() {
        return this.restPassword;
    }

    public String getRestName() {
        return this.restName;
    }

    public List<String> getRestAddresses() {
        return this.restAddresses;
    }

    public String getRestDescription() {
        return this.restDescription;
    }

    public String getRestHomeImage() {
        return this.restHomeImage;
    }

    public List<String> getRestMoreImages() {
        return this.restMoreImages;
    }

    public Double getMaxStar() {
        return maxStar;
    }

    public Long getViewedNumber() {
        return viewedNumber;
    }
    public  Long getTotalRating(){return this.totalRating;}
    public String getRestPhone(){return this.restPhone;}
    public String getRestFacebook(){return this.restFacebook;}

    public String getRestEmail() {
        return this.restEmail;
    }


    /**
     * loadRestaurant()
     *  - Load data of a restaurant
     * @return Restaurant
     */
    public static Restaurant loadRestaurant(Map<String, Object> document) {
        String _restAccount = (String)document.get("rest_account");
        String _restPassword= (String)document.get("rest_password");
        String _restName = (String)document.get("rest_name");
        String _restDescription = (String)document.get("rest_description");
        List<String> _restAddresses = (ArrayList)document.get("rest_addresses");
        String _restHomeImage = (String)document.get("rest_home_image_file");
        List<String> _restMoreImages = (ArrayList)document.get("rest_more_image_files");
        Double _maxStar = ((Number)document.get("max_star")).doubleValue();
        Long _viewedNumber = (Long)document.get("viewed_number");
        Long _totalRating = (Long)document.get("rest_total_rating");
        String _restPhone = (String)document.get("rest_phone");
        String _restFacebook = (String)document.get("rest_facebook");
        String _restEmail = (String)document.get("rest_email");

        return new Restaurant(_restAccount, _restPassword, _restName, _restDescription, _restAddresses, _restHomeImage, _restMoreImages, _maxStar, _viewedNumber,_totalRating,_restPhone,_restFacebook,_restEmail);
    }


    public Map<String, Object> createRestaurantData() {
        Map<String, Object> restData = new HashMap<>(); // Save data of dish

        restData.put("rest_account", restAccount);
        restData.put("rest_password", restPassword);
        restData.put("rest_name", restName);
        restData.put("rest_description", restDescription);
        restData.put("rest_addresses", restAddresses);
        restData.put("rest_home_image", restHomeImage);
        restData.put("rest_more_images", restMoreImages);
        restData.put("max_star", maxStar);
        restData.put("viewed_number", viewedNumber);
        restData.put("rest_total_rating",totalRating);
        restData.put("rest_phone",restPhone);
        restData.put("rest_facebook",restFacebook);
        restData.put("rest_email",restEmail);
        return restData;
    }

    /**
     * createRestaurantData()
     *  - Create restaurant's data for query
     * @param _restAccount
     * @param _restPassword
     * @param _restName
     * @param _restDescription
     * @param _restAddresses
     * @param _restHomeImage
     * @param _restMoreImages
     * @return Map<String, Object>
     */
    public static Map<String, Object> createRestaurantData(String _restAccount, String _restPassword, String _restName, String _restDescription, List<String> _restAddresses, String _restHomeImage, List<String> _restMoreImages, Double _maxStar, Long _viewedNumber,Long _totalRating, String _restPhone, String _restFacebook,String _RestEmail) {
        Map<String, Object> restData = new HashMap<>(); // Save data of dish

        restData.put("rest_account", _restAccount);
        restData.put("rest_password", _restPassword);
        restData.put("rest_name", _restName);
        restData.put("rest_description", _restDescription);
        restData.put("rest_addresses", _restAddresses);
        restData.put("rest_home_image", _restHomeImage);
        restData.put("rest_more_images", _restMoreImages);
        restData.put("max_star", _maxStar);
        restData.put("viewed_number", _viewedNumber);
        restData.put("rest_total_rating",_totalRating);
        restData.put("rest_phone",_restPhone);
        restData.put("rest_facebook",_restFacebook);
        restData.put("rest_email",_RestEmail);
        return restData;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Restaurant restCmp = (Restaurant)o;
        int result = 0;

        if (compareProperty == STAR) {
            if (this.getMaxStar() > restCmp.getMaxStar()) {
                result = -1;
            } else {
                result = 1;
            }
        }
        else if (compareProperty == VIEW) {
            if (this.getViewedNumber() > restCmp.getViewedNumber()) {
                result = -1;
            } else {
                result = 1;
            }
        }

        return result;
    }

    @Override
    public void login(Activity activity) {

    }

    @Override
    public void register(Activity activity) {
        final RestaurantRegister act = (RestaurantRegister)activity;
        Map<String, Object> data = createRestaurantData();

        CMDB.db.collection("restaurant")
                .document(this.restAccount)
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
}
