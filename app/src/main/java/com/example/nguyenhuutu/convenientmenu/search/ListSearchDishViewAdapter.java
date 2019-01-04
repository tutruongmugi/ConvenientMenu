package com.example.nguyenhuutu.convenientmenu.search;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSearchDishViewAdapter extends BaseAdapter {
    Context context;
    int inflat;
    List<Dish> dishList;
    ArrayList<Dish> arrDish = new ArrayList<>();

    public ListSearchDishViewAdapter(Context context, int inflat, List<Dish> dishList) {
        this.context = context;
        this.inflat = inflat;
        this.dishList = dishList;
        arrDish.addAll(this.dishList);
    }

    public int getCount() {
        return dishList.size();
    }

    @Override
    public Object getItem(int position) {
        return dishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(inflat, parent, false);
        final TextView name = v.findViewById(R.id.txtName);
        RatingBar setRate = v.findViewById(R.id.searchrate);
        final TextView addr = v.findViewById(R.id.address);
        final ImageView avatar = v.findViewById(R.id.imgObj);
            float star = dishList.get(position).getMaxStar();
            setRate.setRating(star);
        name.setText(dishList.get(position).getDishName());

        CMDB.db.collection("restaurant")
                .whereEqualTo("rest_account",dishList.get(position).getRestAccount())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Restaurant tmp =  Restaurant.loadRestaurant(document.getData());
                                addr.setText(tmp.getRestName());
                            }
                        }
                    }
                });

        CMStorage.storage.child("images/dish/" + dishList.get(position).getDishHomeImage())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri.toString())
                                .into(avatar);
                    }
                });



        return v;
    }
    public void filter(String charText)

    {

        charText = charText.toLowerCase(Locale.getDefault());
        dishList.clear();
        if(charText.length()==0)
        {
            //dishList.addAll(arrDish);
        }
        else
        {
            for(Dish dish : arrDish)
            {
                if(dish.getDishName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    dishList.add(dish);
                }
            }
            notifyDataSetChanged();

        }
    }
}
