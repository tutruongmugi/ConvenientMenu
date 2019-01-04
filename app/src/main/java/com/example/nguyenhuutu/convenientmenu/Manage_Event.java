package com.example.nguyenhuutu.convenientmenu;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.Fragment.ListEvent;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Manage_Event extends Fragment implements AdapterView.OnItemClickListener {
    private static String TAG="ManageEvent";
    ListView mListView;
    private ListEvent mAdapter;

    public Manage_Event(){

    }

    private void getData(String restaurentName) {
        final List<Event> dataList = new ArrayList<Event>();
        CMDB.db.collection("event").whereEqualTo("rest_account", restaurentName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    int mount = dataList.size();
                    for (int i = 0; i < mount; i++) {
                        final int finalI = i;
                        CMStorage.storage.child("images/event/" + dataList.get(i).getEventImageFiles().get(0))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        dataList.get(finalI).setImageUrl(uri.toString());
                                    }
                                });

                    }
                    for (Event event:
                        dataList ) {
                        Log.d(TAG, "onComplete: "+event.toString());
                    }
                    mAdapter = new ListEvent(getActivity(), R.layout.item_event, dataList);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(Manage_Event.this);
                } else {
                    Toast.makeText(getContext(), "Kết nối server thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_event,container,false); // add xml

        UserSession rest = Helper.getLoginedUser(getActivity());
        String id = rest.getUsername();

        mListView = view.findViewById(R.id.lvEvents);


        getData(id);

        return view;
    }

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mListView = view.findViewById(R.id.lvEvents);

    }*/



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
