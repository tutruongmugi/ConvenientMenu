package com.example.nguyenhuutu.convenientmenu.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.CommentRestaurant;
import com.example.nguyenhuutu.convenientmenu.Const;
import com.example.nguyenhuutu.convenientmenu.LoadImage;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.restaurant_detail.Restaurant_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_Comment extends Fragment {

    ListView listComment;
    public static ListComment adapter;
    RatingBar rbRating;
    EditText txtComment;
    LinearLayout commentForm;
    ProgressBar progressBarComment;

    public Fragment_Comment() {
        // Required empty public constructor
        final List<CommentRestaurant> dataList = new ArrayList<CommentRestaurant>();
        // get all comment restaurant
        CMDB.db.collection("comment_restaurant")
                .whereEqualTo("rest_account", Restaurant_Detail.idRestaurant)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    // add CommentRestaurant to list
                                    dataList.add(CommentRestaurant.loadCommentRestaurant(document.getData()));
                                } catch (Exception ex) {
                                    Log.e("AddCommentRestaurant", ex.toString());
                                }
                            }

                            Collections.sort(dataList);

                            int mount = dataList.size();
                            for (int i = 0; i < mount; i++) {
                                final int finalI = i;

                                CMDB.db
                                        .collection("customer")
                                        .document(dataList.get(i).getUserAccount())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        CMStorage.storage.child("images/customer/" + document.getString("cus_avatar_image_file"))
                                                                .getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        try {
                                                                            LoadImage loadImage = new LoadImage();
                                                                            loadImage.execute(uri.toString(), finalI, Const.COMMENT);
                                                                        } catch (Exception ex) {
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception exception) {
                                                                    }
                                                                });
                                                    } else {

                                                    }
                                                } else {

                                                }
                                            }
                                        });
//                                CMStorage.storage.child("images/comment/" + dataList.get(i).getAvatar())
//                                        .getDownloadUrl()
//                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                try {
//
//                                                    LoadImage loadImage = new LoadImage();
//                                                    loadImage.execute(uri.toString(), finalI, Const.COMMENT);
//                                                } catch (Exception ex) {
//                                                }
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception exception) {
//                                            }
//                                        });
                            }
                            adapter = new ListComment(getActivity(), R.layout.item_comment, dataList);
                            listComment.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Kết nối server thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.tab_comments, container, false);
        listComment = view.findViewById(R.id.lvComment);
        listComment.setAdapter(adapter);

        commentForm = view.findViewById(R.id.commentForm);
        txtComment = view.findViewById(R.id.txtComment);
        rbRating = view.findViewById(R.id.ratComment);
        progressBarComment = view.findViewById(R.id.progressBarComment);

        txtComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            SendComment();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        txtComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtComment.getRight() - txtComment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        SendComment();
                        return true;
                    }
                }
                return false;
            }
        });
        listComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Nhấp vào item comment
            }
        });

        if (Restaurant_Detail.idUser.equals("")) {
            commentForm.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = commentForm.getLayoutParams();
            params.height = 0;
            commentForm.setLayoutParams(params);
        }

        return view;
    }

    public void notifySendCommentSuccess(final CommentRestaurant cmtRest) {
        CMDB.db
                .collection("customer")
                .document(cmtRest.getUserAccount())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                CMStorage.storage.child("images/customer/" + document.getString("cus_avatar_image_file"))
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                try {
                                                    LoadImage loadImage = new LoadImage();
                                                    loadImage.execute(uri.toString(), 0, Const.COMMENT);
                                                } catch (Exception ex) {
                                                    Log.e("AddLoadImage", ex.toString());
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                            }
                                        });
                            } else {
                                // notify something
                            }
                        } else {
                            // notify something
                        }
                    }
                });
        Fragment_Comment.adapter.commentRestaurants.add(cmtRest);
        Collections.sort(adapter.commentRestaurants);
        Fragment_Comment.adapter.notifyDataSetChanged();

        txtComment.setText("");
        rbRating.setRating(0);
    }

    void SendComment() {
        if (rbRating.getRating() != 0 && txtComment.getText().toString()!="") {
            CommentRestaurant cmtRest = new CommentRestaurant(txtComment.getText().toString(), Restaurant_Detail.idRestaurant, Restaurant_Detail.idUser, rbRating.getRating());
            cmtRest.sendCommentRestaurant(this);
        }else {
            Toast.makeText(getContext(),"Hãy đánh giá sao cho chúng tôi", Toast.LENGTH_LONG).show();
        }
    }
}