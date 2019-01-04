package com.example.nguyenhuutu.convenientmenu.restaurant_list.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;

import java.util.ArrayList;

public class RecentRestaurantFragment extends Fragment {
    private ArrayList<Restaurant> myDataSet = new ArrayList<Restaurant>();
    //properties
    private RecyclerView listRestaurant;
    private RecyclerView.Adapter listRestaurantAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RecentRestaurantFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_restaurant_fragment, container, false);
        listRestaurant = view.findViewById(R.id.my_recycler_view);
        listRestaurant.setHasFixedSize(true);

        //load data into restaurant set
        UserSession loginedUserJson = Helper.getLoginedUser(getActivity());
        if (loginedUserJson.isExists()) {
            //if a user login, the restaurants he visits will be here

            //not found
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
