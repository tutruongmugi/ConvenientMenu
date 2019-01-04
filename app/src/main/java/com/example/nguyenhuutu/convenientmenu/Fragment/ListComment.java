package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CommentRestaurant;
import com.example.nguyenhuutu.convenientmenu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListComment extends BaseAdapter {
    Context context;
    int inflat;
    public static List<CommentRestaurant> commentRestaurants;

    public ListComment(Context context, int inflat, List<CommentRestaurant> commentRestaurants) {
        this.inflat = inflat;
        this.context = context;
        this.commentRestaurants = commentRestaurants;
    }

    @Override
    public int getCount() {
        return commentRestaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return commentRestaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(inflat, null);

        CircularImageView imgComment = (CircularImageView) row.findViewById(R.id.imgAvatar);
        final TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvTimeRating = (TextView) row.findViewById(R.id.tvTimeRating);
        TextView tvComment = (TextView) row.findViewById(R.id.tvComment);
        RatingBar rbRating = (RatingBar) row.findViewById(R.id.rbRating);

        final CommentRestaurant item = commentRestaurants.get(position);

        CMDB.db
                .collection("customer")
                .document(item.getUserAccount())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                tvName.setText(document.getString("cus_firstname") + " " + document.getString("cus_lastname"));
                            } else {
                                // notify something
                            }
                        } else {
                            // notify something
                        }
                    }
                });
//        tvName.setText(item.getUserAccount());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(item.getCmtRestDate());
        tvTimeRating.setText(dateString);

        tvComment.setText(item.getCmtRestContent());
        rbRating.setRating(item.getCmtRestStar());

        imgComment.setImageBitmap(item.getImageAvatar(context));

        return row;
    }
}// CustomAdapter
