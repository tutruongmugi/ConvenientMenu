package com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish.viewholder.DishImageViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class DishImageAdapter extends RecyclerView.Adapter<DishImageViewHolder> {
    private ArrayList<String> dishImageLinks;
    private Activity activity;
    private String dishId = "";

    public DishImageAdapter(Activity _activity, ArrayList<String> _dishImageLinks){
        this.dishImageLinks = _dishImageLinks;
        this.activity = _activity;
    }

    public void setDishId(String _dishId) {
        this.dishId = _dishId;
    }

    public void setDishImageLinks(ArrayList<String> imageLinks) {
        this.dishImageLinks = imageLinks;
    }

    public void addDishImageLink(String imageLink) {
        this.dishImageLinks.add(imageLink);
    }

    public ArrayList<String> getDishImageLinks() {
        return this.dishImageLinks;
    }

    @NonNull
    @Override
    public DishImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        DishImageViewHolder dishViewHolder = new DishImageViewHolder(view);

        return dishViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DishImageViewHolder dishImageViewHolder, int position) {
        if (dishImageLinks.get(position).indexOf("\\") > -1 || dishImageLinks.get(position).indexOf("/") > -1) {
            Glide
                    .with(this.activity)
                    .load(dishImageLinks.get(position))
                    .into(dishImageViewHolder.image);
        }
        else {
            CMStorage.storage.child("images/dish/" + dishId + "/" + dishImageLinks.get(position))
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try{
                                Glide
                                        .with(activity)
                                        .load(uri.toString())
                                        .into(dishImageViewHolder.image);
                            }
                            catch(Exception ex) {
                                //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return this.dishImageLinks.size();
    }
}
