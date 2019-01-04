package com.example.nguyenhuutu.convenientmenu;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Update_Dish extends AppCompatActivity {

    ViewGroup scrollViewgroup;
    List<String> thumbnails = new ArrayList<String>();
    String _restAccount = "restphuongdong";
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddOrUpdate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scrollViewgroup = (ViewGroup) findViewById(R.id.viewgroup);

        final View singleFrame = getLayoutInflater().inflate(
                R.layout.frame_icon_caption, null);
        //singleFrame.setId(0);
        ImageView icon = (ImageView) singleFrame.findViewById(R.id.icon);
        icon.setImageResource(android.R.drawable.ic_menu_add);
        singleFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });
        scrollViewgroup.addView(singleFrame,0);
        final EditText txtName = (EditText) findViewById(R.id.txtNameDish);
        final EditText txtPriceDish = (EditText) findViewById(R.id.txtPriceDish);
        final EditText txtDesDish = (EditText) findViewById(R.id.txtDesDish);
        RadioButton radioFood = (RadioButton)findViewById(R.id.radioFood);
        String temp="DTYPE2";
        if(radioFood.isChecked()){
            temp="DTYPE1";;
        }
        final String typeDish = temp;
        Button btnThem = (Button)findViewById(R.id.btnUpdateOrAdd);
        final Date date = new Date(); // lấy thời gian hệ thống
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> addDish =Dish.createDishData(
                        Dish.createDishId(10),
                        txtName.getText().toString(),
                        Integer.parseInt(txtPriceDish.getText().toString()),
                        txtDesDish.getText().toString(),
                        thumbnails.get(0),
                        thumbnails,
                        typeDish,
                        0,
                        _restAccount,
                        date
                        );

                /*CMDB.db.collection("dish").document().set(addDish).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        StorageReference mountainImagesRef = storage.getReference().child("images/dish/"+Dish.createDishId(10));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Restaurant_Detail.imageAvatarUser.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainImagesRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                //Toast.makeText(getContext(),"Xảy ra lỗi khi gửi bình luận",Toast.LENGTH_LONG).show();
                                //progressBarComment.setVisibility(View.INVISIBLE);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(),"Bình luận của bạn đã được gửi",Toast.LENGTH_LONG).show();

                                CommentRestaurant addComment = new CommentRestaurant(
                                        CommentRestaurant.createCommentRestId(10),
                                        txtComment.getText().toString(),
                                        stringDate,
                                        Restaurant_Detail.idRestaurant,
                                        Restaurant_Detail.idUser,
                                        rbRating.getRating(),
                                        Restaurant_Detail.idUser+Restaurant_Detail.avatarUser
                                );
                                addComment.setImageAvatar(Restaurant_Detail.imageAvatarUser);
                                adapter.commentRestaurants.add(addComment);
                                adapter.notifyDataSetChanged();

                                txtComment.setText("");
                                rbRating.setRating(0);
                                progressBarComment.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });*/
            }
        });
    }

    void AddView(final int i) {
        final View singleFrame = getLayoutInflater().inflate(
                R.layout.frame_icon_caption, null);
       // singleFrame.setId(i);
        ImageView icon = (ImageView) singleFrame.findViewById(R.id.icon);
        icon.setImageBitmap(BitmapFactory.decodeFile(thumbnails.get(i)));
  /*      singleFrame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                scrollViewgroup.
                return false;// Xoá ảnh khi người dùng thêm nhầm ảnh
            }
        });*/
        scrollViewgroup.addView(singleFrame,i);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            thumbnails.add(picturePath);

            AddView(thumbnails.size()-1);
        }
    }
}
