package com.example.nguyenhuutu.convenientmenu.view_dish;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.example.nguyenhuutu.convenientmenu.CommentDish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class DishCommentAdapter extends BaseAdapter {
    Context context;
    public ArrayList<CommentDish> commentDishList;
    int inflat;

    public DishCommentAdapter(Context context,int inflat,ArrayList<CommentDish> commentDishList) {
        this.context = context;
        this.commentDishList = commentDishList;
        this.inflat = inflat;
    }

    @Override
    public int getCount() {
        return commentDishList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentDishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(inflat, parent, false);
        final TextView fullName = v.findViewById(R.id.user_fullname);
        RatingBar star = v.findViewById(R.id.number_of_star);
        TextView commentDate = v.findViewById(R.id.comment_date);
        TextView content = v.findViewById(R.id.comment_content);
        final ImageView avatar = v.findViewById(R.id.user_avatar);


        star.setRating(commentDishList.get(position).getScore());
        commentDate.setText(commentDishList.get(position).getCmtDishDate());
        content.setText(commentDishList.get(position).getCmtDishContent());

        CMDB.db.collection("customer").document(commentDishList.get(position).getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String avatarStr = document.getString("cus_avatar_image_file").toString();
                                String name = document.getString("cus_lastname") +" "+document.getString("cus_firstname");
                                fullName.setText(name);
                                CMStorage.storage.child("images/customer/" + avatarStr)
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(context)
                                                        .load(uri.toString())
                                                        .into(avatar);
                                            }
                                        });
                            } else {
                                // code here
                            }
                        } else {
                            // code here
                        }
                    }
                });

        return v;
    }
}
