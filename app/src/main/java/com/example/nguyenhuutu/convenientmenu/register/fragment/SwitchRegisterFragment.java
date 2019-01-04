package com.example.nguyenhuutu.convenientmenu.register.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.register.CustomerRegister;
import com.example.nguyenhuutu.convenientmenu.register.RestaurantRegister;

public class SwitchRegisterFragment extends Fragment implements View.OnClickListener {
    private ImageButton restaurantRegister;
    private ImageButton customerRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.switch_register_fragment, null);
        restaurantRegister = view.findViewById(R.id.restaurantRegister);
        customerRegister = view.findViewById(R.id.customerRegister);
        restaurantRegister.setOnClickListener(this);
        customerRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restaurantRegister:
                Intent restaurantRegisterIntent = new Intent(getActivity(), RestaurantRegister.class);
                startActivity(restaurantRegisterIntent);
                break;
            case R.id.customerRegister:
                Intent customerRegisterIntent = new Intent(getActivity(), CustomerRegister.class);
                startActivity(customerRegisterIntent);
                break;
        }
    }
}