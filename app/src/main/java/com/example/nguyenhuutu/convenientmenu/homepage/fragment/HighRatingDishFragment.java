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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.view_dish.ViewDish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighRatingDishFragment extends Fragment {
    //private HomePage homePage;
    private LinearLayout listContent;
    private List<Dish> dataList;
    private static Integer highRatingDishNumber = 10;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<Dish>();
    }
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listContent = (LinearLayout)inflater.inflate(R.layout.high_rating_dish_fragment, null);

        // get all dish in database
        CMDB.db.collection("dish")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dataList.add(Dish.loadDish(document.getData()));
                            }
                            sortDishFlowStar(dataList);

                            try {
                                for (int index = 0; index < dataList.size(); index++) {
                                    if (index >= highRatingDishNumber) {
                                        break;
                                    }
                                    final Dish dish;
                                    final CardView dishItemLayout = (CardView) inflater.inflate(R.layout.homepage_dish_item, null);
                                    dish = dataList.get(index);
                                    ((TextView) dishItemLayout.findViewById(R.id.dishName)).setText(dish.getDishName());
                                    ((RatingBar) dishItemLayout.findViewById(R.id.ratingDish)).setRating(((Number) dish.getMaxStar()).floatValue());

                                    CMStorage.storage.child("images/dish/" + dish.getDishId() + "/" + dish.getDishHomeImage())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    try{
                                                        Glide
                                                                .with(getContext())
                                                                .load(uri.toString())
                                                                .into((ImageView) dishItemLayout.findViewById(R.id.imageDish));
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

                                    CMDB.db.collection("comment_dish")
                                            .whereEqualTo("dish_id", dish.getDishId())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        ((TextView) dishItemLayout.findViewById(R.id.ratingNumber)).setText("(" + task.getResult().getDocuments().size() + " phiếu)");
                                                    } else {
                                                        ((TextView) dishItemLayout.findViewById(R.id.ratingNumber)).setText("0 phiếu)");
                                                    }
                                                }
                                            });

                                    CMDB.db.collection("restaurant")
                                            .document(dish.getRestAccount())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Restaurant rest = Restaurant.loadRestaurant(document.getData());
                                                            ((TextView)dishItemLayout.findViewById(R.id.restaurantName)).setText(rest.getRestName());
                                                        } else {
                                                            ((TextView)dishItemLayout.findViewById(R.id.restaurantName)).setText("Anonymous");
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(), "Loading has some erors!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    dishItemLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent dishIntent = new Intent(getActivity(), ViewDish.class);
                                            dishIntent.putExtra("dish_id", dish.getDishId());
                                            startActivity(dishIntent);
                                        }
                                    });

                                    ((LinearLayout) listContent.findViewById(R.id.highRatingDishList)).addView(dishItemLayout);
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

    private void sortDishFlowStar(List<Dish> dishList){
        Dish.compareProperty = Dish.STAR;
        Collections.sort(dishList);
    }
}
