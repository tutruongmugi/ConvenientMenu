package com.example.nguyenhuutu.convenientmenu.restaurant_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nguyenhuutu.convenientmenu.Fragment.PagerAdapterRestaurant;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.restaurant_list.fragment.AllRestaurantFragment;
import com.example.nguyenhuutu.convenientmenu.restaurant_list.fragment.RecentRestaurantFragment;

public class RestaurantList extends Fragment {
    ViewPager viewPager;
    PagerAdapterRestaurant pagerAdapterRestaurant;
    TabLayout tabLayout;

    public RestaurantList() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.restaurant_list_fragment, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabRestaurantList);
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerRestaurantList);

        //create pager adapter
        pagerAdapterRestaurant = new PagerAdapterRestaurant(getFragmentManager());

        pagerAdapterRestaurant.AddFragment(new AllRestaurantFragment(), "All Restaurant");
        pagerAdapterRestaurant.AddFragment(new RecentRestaurantFragment(), "Recent Restaurant");

        //set adapter for view pager
        viewPager.setAdapter(pagerAdapterRestaurant);

        //attach all pager to tab
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerAdapterRestaurant.getItem(tab.getPosition()).onStart();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}