package com.example.nguyenhuutu.convenientmenu.restaurant_list.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RestListAdapter extends RecyclerView.Adapter<RestListAdapter.MyViewHolder> {
    private List<Restaurant> mDataSet;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout restListHolder;
        public TextView restListName;
        public ImageView restListImage;
        public RatingBar restListRatingBar;
        public TextView restListRatingNumber;

        private Context mContext;

        public void setRestInfo(Restaurant restInfo) {
            this.restInfo = restInfo;
        }

        private  Restaurant restInfo;

        public MyViewHolder(LinearLayout ll, Context context) {
            super(ll);
            restListHolder = ll;

            restListName = ll.findViewById(R.id.rest_list_name);
            restListImage = ll.findViewById(R.id.rest_list_image);
            restListRatingBar = ll.findViewById(R.id.rest_list_rating_bar);
            restListRatingNumber = ll.findViewById(R.id.rest_list_rating_number);

            //take context
            mContext = context;

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent restIntent = new Intent(mContext, Restaurant_Detail.class);
                    restIntent.putExtra("rest_account", restInfo.getRestAccount());
                    mContext.startActivity(restIntent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of data set)
    public RestListAdapter(List<Restaurant> myDataSet, Context context) {
        mDataSet = myDataSet;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RestListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_restaurant_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v, mContext);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setRestInfo(mDataSet.get(position));
        holder.restListName.setText(mDataSet.get(position).getRestName());
        holder.restListRatingBar.setRating(mDataSet.get(position).getMaxStar().floatValue());

        CMStorage.storage.child("images/restaurant/" + mDataSet.get(position).getRestHomeImage())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try{
                            Glide
                                    .with(mContext)
                                    .load(uri.toString())
                                    .into(holder.restListImage);
                        }
                        catch(Exception ex) {
                            //load image err
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //database err
                    }
                });

        CMDB.db
                .collection("comment_restaurant")
                .whereEqualTo("rest_account", mDataSet.get(position).getRestAccount())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            holder.restListRatingNumber.setText("(" + task.getResult().size() + " phiếu)");
                        }
                        else {

                        }
                    }
                });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
