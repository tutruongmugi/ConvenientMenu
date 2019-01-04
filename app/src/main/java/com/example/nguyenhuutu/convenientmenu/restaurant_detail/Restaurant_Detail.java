package com.example.nguyenhuutu.convenientmenu.restaurant_detail;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Comment;
import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Event;
import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Menu;
import com.example.nguyenhuutu.convenientmenu.Fragment.PagerAdapterRestaurant;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Restaurant_Detail extends AppCompatActivity {
    ViewPager viewpager;
    Fragment_Event event;
    Fragment_Menu menu;
    Fragment_Comment comment;
    PagerAdapterRestaurant pagerAdapterRestaurant;
    ImageView imgBackground;
    TextView lbNameRestaurant, ratingPerTotal, addressRestaurantDetail, phoneRestaurantDetail, facebookRestaurantDetail;
    AppBarLayout app_bar;
    TabLayout tabLayout;
    AppCompatRatingBar ratingRestaurant;
    Restaurant infoRestaurant;

    public static String idRestaurant, idUser = "", avatarUser = "avatar.png", userFullName = "";
    public static Bitmap imageAvatarUser;

    public static String covertToUnsigned(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            return null;
        }
    }

    private void loadInfoUser() {
        UserSession user = Helper.getLoginedUser(this);

        if (user.isExists()) {
            if (!user.isRest()) {
                idUser = user.getUsername();

                CMDB.db
                        .collection("customer")
                        .document(idUser)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        userFullName = document.getString("cus_first_name");
                                    } else {

                                    }
                                } else {

                                }
                            }
                        });
            }
            else {
                idUser = "";
            }
        }
        else {
            idUser = "";
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);

        loadInfoUser();

        imageAvatarUser = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        Bundle data = getIntent().getExtras();
        idRestaurant = data.getString("rest_account");

        event = new Fragment_Event();
        menu = new Fragment_Menu(idRestaurant);
        comment = new Fragment_Comment();

        viewpager = (ViewPager) findViewById(R.id.view_pager_restaurant_detail);
        tabLayout = (TabLayout) findViewById(R.id.tabRestaurantDetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRestaurant);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Nhà hàng");

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    getSupportActionBar().setTitle("Nhà hàng");
                } else {
                    getSupportActionBar().setTitle("");
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        pagerAdapterRestaurant = new PagerAdapterRestaurant(getSupportFragmentManager());

        pagerAdapterRestaurant.AddFragment(event, "Sự kiện");
        pagerAdapterRestaurant.AddFragment(menu, "Menu");
        pagerAdapterRestaurant.AddFragment(comment, "Bình luận");

        viewpager.setAdapter(pagerAdapterRestaurant);
        tabLayout.setupWithViewPager(viewpager);

        viewpager.setOffscreenPageLimit(3);

        imgBackground = (ImageView) findViewById(R.id.imgBackground);
        lbNameRestaurant = (TextView) findViewById(R.id.lbNameRestaurant);
        ratingPerTotal = (TextView) findViewById(R.id.ratingPerTotal);
        addressRestaurantDetail = (TextView) findViewById(R.id.addressRestaurantDetail);
        phoneRestaurantDetail = (TextView) findViewById(R.id.phoneRestaurantDetail);
        facebookRestaurantDetail = (TextView) findViewById(R.id.facebookRestaurantDetail);
        ratingRestaurant = (AppCompatRatingBar) findViewById(R.id.ratingRestaurant);

        CMDB.db.collection("restaurant")
                .whereEqualTo("rest_account", idRestaurant)// truyền id nhà hàng vào đây
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                infoRestaurant = Restaurant.loadRestaurant(task.getResult().getDocuments().get(0).getData());
                                CMStorage.storage.child("images/restaurant/" + infoRestaurant.getRestHomeImage())
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                try {
                                                    Glide
                                                            .with(getApplicationContext())
                                                            .load(uri.toString())
                                                            .into((ImageView) imgBackground.findViewById(R.id.imgBackground));
                                                } catch (Exception ex) {
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                            }
                                        });

                                lbNameRestaurant.setText(infoRestaurant.getRestName());
                                if (!infoRestaurant.getRestAddresses().get(0).isEmpty()) {
                                    addressRestaurantDetail.setText("Địa chỉ: " + infoRestaurant.getRestAddresses().get(0));
                                }
                                if (!infoRestaurant.getRestPhone().isEmpty()) {
                                    phoneRestaurantDetail.setText("SĐT: " + infoRestaurant.getRestPhone());
                                }
                                if (!infoRestaurant.getRestFacebook().isEmpty()) {
                                    facebookRestaurantDetail.setText("Facebook: " + infoRestaurant.getRestFacebook());
                                }
                                CMDB.db
                                        .collection("comment_restaurant")
                                        .whereEqualTo("rest_account", infoRestaurant.getRestAccount())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                } else {
                                                    ratingPerTotal.setText("(" + task.getResult().size() + " phiếu)");
                                                }
                                            }
                                        });
//                                ratingPerTotal.setText(infoRestaurant.getMaxStar() + " (" + infoRestaurant.getTotalRating() + " phiếu)");
                                ratingRestaurant.setRating(infoRestaurant.getMaxStar().floatValue());
                            } catch (Exception ex) {
                            }
                            //}
                        } else {
                            Toast.makeText(getApplicationContext(), "Kết nối server thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
