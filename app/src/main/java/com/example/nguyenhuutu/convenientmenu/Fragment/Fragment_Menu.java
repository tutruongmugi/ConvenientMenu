package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.nguyenhuutu.convenientmenu.R;

@SuppressLint("ValidFragment")
public class Fragment_Menu extends Fragment {

    ViewPager viewpager;
    PagerAdapterRestaurant pagerAdapterRestaurant;
    Fragment_Food food;
    Fragment_Drink drink;
    TabLayout tabLayout;
    public Fragment_Menu(String id) {
        // Required empty public constructor
          food = new Fragment_Food(id);
          drink = new Fragment_Drink(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.tab_menu, container, false);

        viewpager = (ViewPager) view.findViewById(R.id.view_pager_menu);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenu);

        pagerAdapterRestaurant = new PagerAdapterRestaurant(getChildFragmentManager());

        pagerAdapterRestaurant.AddFragment(food,"Món ăn");
        pagerAdapterRestaurant.AddFragment(drink,"Thức uống");
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(pagerAdapterRestaurant);
        tabLayout.setupWithViewPager(viewpager);

        EditText search_Dish = (EditText)view.findViewById(R.id.search_Dish);
        search_Dish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(tabLayout.getTabAt(0).isSelected())
                {
                    Fragment_Food.adapter.filter(s);
                }else
                {
                    Fragment_Drink.adapter.filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
}