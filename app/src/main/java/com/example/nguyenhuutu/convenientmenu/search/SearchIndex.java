package com.example.nguyenhuutu.convenientmenu.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.example.nguyenhuutu.convenientmenu.view_dish.ViewDish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SearchIndex extends AppCompatActivity  {
    SearchView searchView;
    ListView resView;
    List<Restaurant> resList = new ArrayList<>();
    ListSearchResViewAdapter adapterRes;

    ListSearchDishViewAdapter adapterDish;
    List<Dish> dishList = new ArrayList<>();
    ListView dishView;
    TextView txtRes;
    TextView txtDish;
    RelativeLayout resL,dishL;
    TextView txtNotFound;
    TextView txtsearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //create
        searchView = (SearchView) findViewById(R.id.searchview);
        resView = findViewById(R.id.listviewRes);
        dishView= findViewById(R.id.listviewDish);
        txtDish = findViewById(R.id.txtDish);
        txtRes = findViewById(R.id.txtRes);
        resL = findViewById(R.id.reslayout);
        dishL = findViewById(R.id.dishlayout);
        txtNotFound = findViewById(R.id.notfound);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        txtsearch = searchView.findViewById(id);
        txtsearch.setTextColor(Color.BLACK);
        txtNotFound.setVisibility(View.INVISIBLE);
        txtDish.setVisibility(View.INVISIBLE);
        txtRes.setVisibility(View.INVISIBLE);

        try {
            CMDB.db.collection("restaurant")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    try {
                                        Restaurant tmp = Restaurant.loadRestaurant(document.getData());
                                        resList.add(tmp);
                                        //Log.e("sizeList",);
                                    }
                                    catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }


                                adapterRes = new ListSearchResViewAdapter(getApplicationContext(), R.layout.item_search, resList);

                                if(resList.size() >= 4)
                                {
                                    resL.getLayoutParams().height=700;
                                    resL.requestLayout();
                                }
                                else
                                {
                                    resL.getLayoutParams().height=WRAP_CONTENT ;
                                    resL.requestLayout();
                                }
                            } else {
                                // code here
                            }
                        }
                    });
        }catch (Exception ex){Log.e("errSe",ex.toString());}

        CMDB.db.collection("dish")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    Dish tmp = Dish.loadDish(document.getData());
                                    dishList.add(tmp);

                                    //Log.e("sizeList",);
                                }
                                catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                                }
                            }


                            adapterDish = new ListSearchDishViewAdapter(getApplicationContext(), R.layout.item_search, dishList);

                        } else {
                            // code here
                        }
                    }
                });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapterRes.filter(text);
                adapterDish.filter(text);
                txtDish.setVisibility(View.VISIBLE);
                txtRes.setVisibility(View.VISIBLE);
                resView.setAdapter(adapterRes);
                dishView.setAdapter(adapterDish);
                if(dishList.size()==0 && resList.size()==0 && text.length()!=0)
                {
                    txtNotFound.setVisibility(View.VISIBLE);
                    txtNotFound.setText("Không tìm thấy kết quả");

                }
                else
                {
                    txtNotFound.setVisibility(View.INVISIBLE);
                }
                if(dishList.size()==0)
                {
                    dishL.setVisibility(View.INVISIBLE);
                }
                else
                {
                    dishL.setVisibility(View.VISIBLE);
                }
                if(resList.size()==0)
                {
                    resL.setVisibility(View.INVISIBLE);
                    ((RelativeLayout.LayoutParams)dishL.getLayoutParams()).addRule(RelativeLayout.BELOW,R.id.searchview);
                }
                else{
                    resL.setVisibility(View.VISIBLE);

                    if(resList.size() >= 4)
                    {
                        resL.getLayoutParams().height=700;
                        resL.requestLayout();
                    }
                    else
                    {
                        resL.getLayoutParams().height=WRAP_CONTENT ;
                        resL.requestLayout();
                    }
                    ((RelativeLayout.LayoutParams)dishL.getLayoutParams()).addRule(RelativeLayout.BELOW,R.id.reslayout);
                }
                return false;
            }
        });
        dishView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dishIntent = new Intent(getApplicationContext(), ViewDish.class);
                dishIntent.putExtra("dish_id", dishList.get(position).getDishId());
                startActivity(dishIntent);
            }
        });
        resView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resIntent = new Intent(getApplicationContext(), Restaurant_Detail.class);
                resIntent.putExtra("rest_account", resList.get(position).getRestAccount());
                startActivity(resIntent);
            }
        });
    }



}
