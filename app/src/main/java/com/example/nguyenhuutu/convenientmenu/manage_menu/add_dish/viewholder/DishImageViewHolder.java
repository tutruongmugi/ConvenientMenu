package com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.nguyenhuutu.convenientmenu.R;

public class DishImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;

    public DishImageViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);

    }
}
