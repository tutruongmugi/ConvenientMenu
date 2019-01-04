package com.example.nguyenhuutu.convenientmenu.restaurant_list.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.restaurant_list.adapter.RestListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllRestaurantFragment extends Fragment {
    private ArrayList<Restaurant> myDataSet = new ArrayList<Restaurant>();
    //properties
    private RecyclerView listRestaurant;
    private RecyclerView.Adapter listRestaurantAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AllRestaurantFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_restaurant_fragment, container, false);
        listRestaurant = view.findViewById(R.id.my_recycler_view);
        listRestaurant.setHasFixedSize(true);

        //load data into restaurant set
        CMDB.db.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    myDataSet.add(Restaurant.loadRestaurant(document.getData()));
                                }
                                catch (Exception ex){
                                    //
                                }
                            }

                            //use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(getContext());
                            listRestaurant.setLayoutManager(mLayoutManager);

                            listRestaurantAdapter = new RestListAdapter(myDataSet, getContext());
                            listRestaurant.setAdapter(listRestaurantAdapter);
                        }
                        else {
                            Toast.makeText(getActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}