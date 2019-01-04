package com.example.nguyenhuutu.convenientmenu.view_dish;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class ViewPageImageAdapter extends PagerAdapter {

    private List<String> image;
    private LayoutInflater inflater;
    private Context context;
    private Dish dish;
    public ViewPageImageAdapter(List<String>  images, Context context,Dish dishh) {
        this.image = images;
        this.context = context;
        dish = dishh;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    //position này là cái vị trí ảnh mà mình đang xem
    public Object instantiateItem(ViewGroup view, int position)
    {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageLayout = inflater.inflate(R.layout.dish_items, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.imageView);

        CMStorage.storage.child("images/dish/"+dish.getDishId()+"/" + image.get(position))
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri.toString())
                                .into(imageView);
                    }
                });

        view.addView(imageLayout, 0);

        return imageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}