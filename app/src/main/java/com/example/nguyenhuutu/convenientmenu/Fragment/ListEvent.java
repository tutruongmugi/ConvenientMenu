package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nguyenhuutu.convenientmenu.Event;
import com.example.nguyenhuutu.convenientmenu.R;

import java.util.ArrayList;
import java.util.List;

public class ListEvent extends BaseAdapter {
    Context context;
    int inflat;
    public static List<Event> event;

    public ListEvent(Context context, int inflat, List<Event> event) {
        this.inflat = inflat;
        this.context = context;
        this.event = event;
    }

    @Override
    public int getCount() {
        return event.size();
    }

    @Override
    public Object getItem(int position) {
        return event.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(inflat, null);

        ImageView imgEvent = (ImageView) row.findViewById(R.id.imgEvent);
        TextView tvTitleEvent = (TextView) row.findViewById(R.id.tvTitleEvent);
        TextView tvTimeEvent = (TextView) row.findViewById(R.id.tvTimeEvent);
        TextView tvDescriptionEvent = (TextView) row.findViewById(R.id.tvDescriptionEvent);

        Event item = event.get(position);

        tvDescriptionEvent.setText(item.getEventContent());
        tvTitleEvent.setText(item.getEventName());
        tvTimeEvent.setText(item.getBeginDateFormat() + " - " + item.getEndDateFormat());
        imgEvent.setImageBitmap(item.getImageEvent(context));

        return row;
    }

}// CustomAdapter

