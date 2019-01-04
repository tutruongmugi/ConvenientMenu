package com.example.nguyenhuutu.convenientmenu.search;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Layout;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSearchResViewAdapter extends BaseAdapter {
    Context context;
    int inflat;
    List<Restaurant> resList;
    ArrayList<Restaurant> arrRes = new ArrayList<>();

    public ListSearchResViewAdapter(Context context, int inflat, List<Restaurant> resList) {
        this.context = context;
        this.inflat = inflat;
        this.resList = resList;
        arrRes.addAll(this.resList);
    }

    public int getCount() {
        return resList.size();
    }

    @Override
    public Object getItem(int position) {
        return resList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(inflat, parent, false);
        TextView name = v.findViewById(R.id.txtName);
        RatingBar rate = v.findViewById(R.id.searchrate);
        TextView addr = v.findViewById(R.id.address);
        final ImageView avatar = v.findViewById(R.id.imgObj);

        name.setText(resList.get(position).getRestName());
        rate.setRating(resList.get(position).getMaxStar().floatValue());

        Log.e("rate",String.valueOf(resList.get(position).getMaxStar().floatValue()));

        addr.setText(resList.get(position).getRestAddresses().get(0));
        CMStorage.storage.child("images/restaurant/" + resList.get(position).getRestHomeImage())
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
        resList.clear();
        if(charText.length()==0)
        {
            //resList.addAll(arrRes);
        }
        else
        {
            for(Restaurant res : arrRes)
            {
                if(res.getRestName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    resList.add(res);
                }
            }
            notifyDataSetChanged();

        }
    }
}
