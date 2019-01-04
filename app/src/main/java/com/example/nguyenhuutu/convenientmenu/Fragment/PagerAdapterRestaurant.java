package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapterRestaurant extends FragmentStatePagerAdapter {
String TAG = "PagerAdapterRestaurant";
    //integer to count number of tabs
    List<String> titles = new ArrayList<String>();
    List<Fragment> fragments = new ArrayList<Fragment>();

    //Constructor to the class
    public PagerAdapterRestaurant(FragmentManager fm) {
        super(fm);
    }

    public void AddFragment(Fragment fragment, String title) {
        titles.add(title);
        fragments.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
       // Log.d(TAG,position + " "+ fragments.get(position));
        return fragments.get(position);
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return fragments.size();
    }
}