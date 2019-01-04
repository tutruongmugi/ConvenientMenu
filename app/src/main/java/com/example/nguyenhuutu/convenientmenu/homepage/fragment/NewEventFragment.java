package com.example.nguyenhuutu.convenientmenu.homepage.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Event;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NewEventFragment extends Fragment {
    //private HomePage homePage;
    private LinearLayout listContent;
    private List<Event> dataList;
    private static Integer newEventNumber = 10;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //homePage = (HomePage)getActivity();
        dataList = new ArrayList<Event>();
    }
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        listContent = (LinearLayout)inflater.inflate(R.layout.new_event_fragment, null);

        // get all dish in database
        CMDB.db.collection("event")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    dataList.add(Event.loadEvent(document.getData()));
                                }
                                catch (Exception ex){
                                    Toast.makeText(getActivity(), "Load event error. Please reload", Toast.LENGTH_LONG).show();
                                }
                            }

                            filterEvents(dataList);
                            sortEventFlowDate(dataList);

                            try {
                                for (int index = 0; index < dataList.size(); index++) {
                                    if (index >= newEventNumber) {
                                        break;
                                    }

                                    final Event event;
                                    final CardView eventItemLayout = (CardView) inflater.inflate(R.layout.homepage_event_item, container);
                                    event = dataList.get(index);
                                    ((TextView) eventItemLayout.findViewById(R.id.eventName)).setText(event.getEventName());

                                    CMStorage.storage.child("images/event/" + event.getEventImageFiles().get(0).toString())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    try{
                                                        Glide
                                                                .with(getContext())
                                                                .load(uri.toString())
                                                                .into((ImageView) eventItemLayout.findViewById(R.id.imageEvent));
                                                    }
                                                    catch(Exception ex) {
                                                        //Toast.makeText(getActivity(), "Image can not be load because of server's error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    //Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    CMDB.db.collection("restaurant")
                                            .document(event.getRestAccount())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Restaurant rest = Restaurant.loadRestaurant(document.getData());
                                                            ((TextView)eventItemLayout.findViewById(R.id.restaurantName)).setText(rest.getRestName());
                                                        } else {
                                                            ((TextView)eventItemLayout.findViewById(R.id.restaurantName)).setText("Anonymous");
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(), "Loading have some error!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    eventItemLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Intent eventIntent = new Intent(getActivity(), RestaurantDetail.class);
                                            //restIntent.putExtra("event_id", rest.getEventId());
                                            //startActivity(eventIntent);
                                            Toast.makeText(getActivity(), event.getEventId() + "-" + event.getEventContent(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    ((LinearLayout) listContent.findViewById(R.id.newEventList)).addView(eventItemLayout);
                                }
                            }
                            catch(Exception ex){
                                //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else {

                        }
                    }
                });

        return listContent;
    }

    private void filterEvents(List<Event> eventList) {
        Date nowDate = new Date(); // get now date

        int count = eventList.size();
        int index = 0;
        while (index < count) {
            if (eventList.get(index).getEndDate().compareTo(nowDate) < 0) {
                eventList.remove(index);
                count--;
            } else {
                index++;
            }
        }
    }

    private void sortEventFlowDate(List<Event> eventList){
        Event.compareProperty = Event.DATE;
        Collections.sort(eventList);
    }
}
