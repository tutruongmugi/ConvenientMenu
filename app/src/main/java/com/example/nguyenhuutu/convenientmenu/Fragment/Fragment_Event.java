package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.Const;
import com.example.nguyenhuutu.convenientmenu.Event;
import com.example.nguyenhuutu.convenientmenu.LoadImage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressLint("ValidFragment")
public class Fragment_Event extends Fragment {
    ListView listEvent;
    public  static ListEvent adapter;

    @SuppressLint("ValidFragment")
    public Fragment_Event() {
        final List<Event> dataList = new ArrayList<Event>();
        CMDB.db.collection("event").whereEqualTo("rest_account", Restaurant_Detail.idRestaurant).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        try {
                            Event item = Event.loadEvent(document.getData());
                            dataList.add(item);

                        } catch (Exception ex) {
                        }
                    }

                    filterEvents(dataList);

                    int mount = dataList.size();
                    for (int i = 0; i < mount; i++) {
                        final int finalI = i;
                        CMStorage.storage.child("images/event/" + dataList.get(i).getEventImageFiles().get(0))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        try {
                                            LoadImage loadImage = new LoadImage();
                                            loadImage.execute(uri.toString(), finalI, Const.EVENT);
                                        } catch (Exception ex) {
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                    }
                    adapter = new ListEvent(getActivity(), R.layout.item_event, dataList);
                    listEvent.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Kết nối server thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_events, container, false);
        listEvent = view.findViewById(R.id.lvEvents);
        listEvent.setAdapter(adapter);
        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Nhấp vào Item Event
            }
        });
        return view;
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
}
