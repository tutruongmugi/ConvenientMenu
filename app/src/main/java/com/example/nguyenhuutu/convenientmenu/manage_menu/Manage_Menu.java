package com.example.nguyenhuutu.convenientmenu.manage_menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.nguyenhuutu.convenientmenu.Const;
import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Drink;
import com.example.nguyenhuutu.convenientmenu.Fragment.Fragment_Food;
import com.example.nguyenhuutu.convenientmenu.Fragment.PagerAdapterRestaurant;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish.AddDish;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;

public class Manage_Menu extends Fragment {

    ViewPager viewpager;
    PagerAdapterRestaurant pagerAdapterRestaurant;
    Fragment_Food food;
    Fragment_Drink drink;
    ImageButton imgAdd;
    @SuppressLint("ValidFragment")

    public  Manage_Menu()
    {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manage__menu, container, false);

        // get restAccount
        UserSession rest = Helper.getLoginedUser(getActivity());
        String restAccount = rest.getUsername();

        food = new Fragment_Food(restAccount);
        drink = new Fragment_Drink(restAccount);

        imgAdd = view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDishIntent = new Intent(getActivity(), AddDish.class);
                startActivityForResult(addDishIntent, Const.ADD_DISH);
            }
        });
       // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarManage);
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });*/
        viewpager = (ViewPager) view.findViewById(R.id.view_pager_menu);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenu);

        pagerAdapterRestaurant = new PagerAdapterRestaurant(getChildFragmentManager());
        pagerAdapterRestaurant.AddFragment(food, "Món ăn");
        pagerAdapterRestaurant.AddFragment(drink, "Thức uống");

        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(pagerAdapterRestaurant);
        tabLayout.setupWithViewPager(viewpager);
        EditText search_Dish = (EditText) view.findViewById(R.id.search_Dish);
        search_Dish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tabLayout.getTabAt(0).isSelected()) {
                    Fragment_Food.adapter.filter(s);
                } else {
                    Fragment_Drink.adapter.filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        ImageView imagePopupMenu = (ImageView) view.findViewById(R.id.imgAdd);
//        imagePopupMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Thêm món mới
//            }
//        });
        return view;
    }

    public void updateFoodList(String _dishId, int action, boolean _changeDishType) {
        if (action == Const.ADD_DISH) {
            food.addDishIntoList(_dishId);
        }
        else if (action == Const.EDIT_DISH) {
            if (_changeDishType == true) {
                drink.removeDishInList(_dishId);
                food.addDishIntoList(_dishId);
//                drink.notifyDataChanged();
            }
            else {
                food.updateDishInList(_dishId);
            }
        }

//        food.notifyDataChanged();
    }

    public void updateDrinkList(String _dishId, int action, boolean _changeDishType) {
        if (action == Const.ADD_DISH) {
            drink.addDishIntoList(_dishId);
        }
        else if (action == Const.EDIT_DISH) {
            if (_changeDishType == true) {
                food.removeDishInList(_dishId);
                drink.addDishIntoList(_dishId);

//                food.notifyDataChanged();
            }
            else {
                drink.updateDishInList(_dishId);
            }
        }

//        drink.notifyDataChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.ADD_DISH) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("isFood")) {
                if (data.getExtras().getBoolean("isFood") == true) {
                    updateFoodList(data.getExtras().get("dishId").toString(), Const.ADD_DISH, false);
                }
                else{
                    updateDrinkList(data.getExtras().get("dishId").toString(), Const.ADD_DISH, false);
                }
            }
        }
        else if (requestCode == Const.EDIT_DISH){
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("isFood")) {
                if (data.getExtras().getBoolean("isFood") == true) {
                    updateFoodList(data.getExtras().get("dishId").toString(), Const.EDIT_DISH, data.getExtras().getBoolean("changeDishType"));
                }
                else{
                    updateDrinkList(data.getExtras().get("dishId").toString(), Const.EDIT_DISH, data.getExtras().getBoolean("changeDishType"));
                }
            }
        }
    }
}
