package com.example.nguyenhuutu.convenientmenu.homepage.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuchViewedRestaurantFragment extends Fragment {
    //private HomePage homePage;
    private LinearLayout listContent;
    private List<Restaurant> dataList;
    private static Integer muchViewedRestaurantNumber = 10;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //homePage = (HomePage)getActivity();
        dataList = new ArrayList<Restaurant>();
    }
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listContent = (LinearLayout)inflater.inflate(R.layout.much_viewed_restaurant_fragment, null);

        CMDB.db.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    dataList.add(Restaurant.loadRestaurant(document.getData()));
                                }
                                catch (Exception ex){
                                    //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                                }
                            }

                            sortRestaurantFlowView(dataList);

                            try {
                                for (int index = 0; index < dataList.size(); index++) {
                                    if (index >= muchViewedRestaurantNumber) {
                                        break;
                                    }

                                    final Restaurant rest;
                                    final CardView restItemLayout = (CardView) inflater.inflate(R.layout.homepage_restaurant_item, null);
                                    rest = dataList.get(index);
                                    ((TextView) restItemLayout.findViewById(R.id.restaurantName)).setText(rest.getRestName());
                                    ((RatingBar) restItemLayout.findViewById(R.id.ratingRestaurant)).setRating(((Number) rest.getMaxStar()).floatValue());

                                    CMStorage.storage.child("images/restaurant/" + rest.getRestHomeImage())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    try{
                                                        Glide
                                                                .with(getContext())
                                                                .load(uri.toString())
                                                                .into((ImageView) restItemLayout.findViewById(R.id.imageRestaurant));
                                                    }
                                                    catch(Exception ex) {
                                                        //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    //Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    CMDB.db.collection("comment_restaurant")
                                            .whereEqualTo("cmt_rest_star", rest.getMaxStar())
                                            .whereEqualTo("rest_account", rest.getRestAccount())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        ((TextView) restItemLayout.findViewById(R.id.ratingNumber)).setText("(" + task.getResult().getDocuments().size() + " phiếu)");
                                                    } else {
                                                        ((TextView) restItemLayout.findViewById(R.id.ratingNumber)).setText("0 phiếu)");
                                                    }
                                                }
                                            });

                                    //((TextView) restItemLayout.findViewById(R.id.addressRestaurant)).setText(rest.getRestAddresses().get(0));

                                    restItemLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent restIntent = new Intent(getActivity(), Restaurant_Detail.class);
                                            restIntent.putExtra("rest_account", rest.getRestAccount());
                                            startActivity(restIntent);
                                        }
                                    });

                                    ((LinearLayout) listContent.findViewById(R.id.muchViewedRestaurantList)).addView(restItemLayout);
                                }
                            }
                            catch(Exception ex){
                                //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else {

                        }
                    }
                });

        return listContent;
    }

    private void sortRestaurantFlowView(List<Restaurant> restList){
        Restaurant.compareProperty = Restaurant.VIEW;
        Collections.sort(restList);
    }
}
