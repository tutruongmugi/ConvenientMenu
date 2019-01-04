package com.example.nguyenhuutu.convenientmenu.Fragment;

import android.content.Intent;
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
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.LoadImage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.view_dish.ViewDish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Fragment_Drink extends Fragment {
    ListView listDish;
    public static ListDrink adapter;
    private String restAccount;
    final List<Dish> dataList = new ArrayList<Dish>();

    public Fragment_Drink(String _restAccount) {
        restAccount = _restAccount;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_drink, container, false);

        listDish = view.findViewById(R.id.list_drink);
        loadDrinkList();

        return view;
    }

    private void setItemClickListener() {
        listDish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dishIntent = new Intent(getActivity(), ViewDish.class);
                dishIntent.putExtra("dish_id", ((ListDrink)listDish.getAdapter()).getItem(position).getDishId());
                startActivity(dishIntent);
            }
        });
    }

    private void loadDrinkList() {
        CMDB.db.collection("dish")
                .whereEqualTo("rest_account", restAccount)
                .whereEqualTo("dish_type_id","DTYPE2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    dataList.add(Dish.loadDish(document.getData()));
                                } catch (Exception ex) {

                                }
                            }
//                            int mount = dataList.size();
//                            for (int i = 0; i < mount; i++) {
//                                final int finalI = i;
//                                CMStorage.storage.child("images/dish/" + dataList.get(i).getDishId() + "/" + dataList.get(i).getDishHomeImage())
//                                        .getDownloadUrl()
//                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                try {
//                                                    LoadImage loadImage = new LoadImage();
//                                                    loadImage.execute(uri.toString(), finalI, Const.DRINK);
//                                                } catch (Exception ex) {
//                                                }
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception exception) {
//                                            }
//                                        });
//                            }
                            adapter = new ListDrink(getActivity(), R.layout.item_menu, dataList);
                            listDish.setAdapter(adapter);
                            setItemClickListener();
                        } else {
                            Toast.makeText(getContext(), "Kết nối server thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void addDishIntoList(String _dishId) {
        CMDB.db
                .collection("dish")
                .document(_dishId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ((ListDrink)listDish.getAdapter()).add(0, Dish.loadDish(document.getData()));
                                ((ListDrink)listDish.getAdapter()).notifyDataSetChanged();
                            } else {
                                // code here
                            }
                        } else {
                            // code here
                        }
                    }
                });
    }

    public void updateDishInList(String _dishId) {
        CMDB.db
                .collection("dish")
                .document(_dishId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Dish tempDish = Dish.loadDish(document.getData());

                                int count = ((ListDrink)listDish.getAdapter()).getCount();
                                for (int index = 0; index < count; index++) {
                                    if (tempDish.getDishId().equals(((ListDrink)listDish.getAdapter()).getItem(index).getDishId())) {
                                        ((ListDrink)listDish.getAdapter()).replace(index, tempDish);
                                        ((ListDrink)listDish.getAdapter()).notifyDataSetChanged();
                                        break;
                                    }
                                }
                            } else {
                                // code here
                            }
                        } else {
                            // code here
                        }
                    }
                });
    }

    public void removeDishInList(String _dishId) {
        int count = ((ListDrink)listDish.getAdapter()).getCount();
        for (int index = 0; index < count; index++) {
            if (((ListDrink)listDish.getAdapter()).getItem(index).getDishId().equals(_dishId)) {
                ((ListDrink)listDish.getAdapter()).remove(index);
                break;
            }
        }
    }

    public void notifyDataChanged() {
        ((ListDrink)listDish.getAdapter()).notifyDataSetChanged();
    }
}
