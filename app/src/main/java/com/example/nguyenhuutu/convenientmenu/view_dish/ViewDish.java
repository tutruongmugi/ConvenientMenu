package com.example.nguyenhuutu.convenientmenu.view_dish;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CommentDish;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class ViewDish extends AppCompatActivity {
    private ViewPager dishImagesViewer;
    private ViewPageImageAdapter adapter;

    private List<String> listImage = new ArrayList<>();
    private Dish dish;
    private  ArrayList<CommentDish> listComment = new ArrayList<>();
    private TextView mdish_name;
    private RatingBar mdish_rating;
    private TextView mdish_price;
    private TextView mdish_descriptionDish;
    private TextView totalRate;
    private TextView totalCmt;
    private AppBarLayout app_bar;
    //comment on view dish
    //evaluate dish
    private RatingBar mdish_evaluteDish;
    //comment form
    private EditText mdish_txtComment;
    private String idCmt;
    private Float rating;
    //list comment
    private ListView lView;
    DishCommentAdapter adapterC;

    private Toolbar viewDishToolbar;
    UserSession user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dish);

        app_bar = findViewById(R.id.appbar_layout);
        // Get dish id from intent
        String dishId = getDishIdFromIntent();
        // init for dish detail
        initInfoDish();

        user = Helper.getLoginedUser(this);
        //Log.e("user",user.getUsername());
        if(user.isExists()== true) {
            mdish_txtComment.setOnKeyListener(new View.OnKeyListener() {
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
            mdish_txtComment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (mdish_txtComment.getRight() - mdish_txtComment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            SendComment();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        else
        {
            mdish_txtComment.setFocusable(false);
            mdish_txtComment.setHint("Mời bạn đăng nhập để bình luận");
            mdish_txtComment.setClickable(false);
            mdish_evaluteDish.setClickable(false);
            mdish_evaluteDish.setFocusable(false);
        }
        // get dish data from database
        getDataFromFireBase(dishId);
        //get comment and store in firebase
        initCommentDish(dishId);

        viewDishToolbar = findViewById(R.id.viewDishToolbar);
        setSupportActionBar(viewDishToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewDishToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private String getDishIdFromIntent() {
        return getIntent().getExtras().get("dish_id").toString();
    }

    private void getDataFromFireBase(String dishId) {
        CMDB.db.collection("dish").document(dishId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Có dữ liệu của dish
                                dish = Dish.loadDish(document.getData());

                                listImage.add(dish.getDishHomeImage());
                                for (String s: dish.getDishMoreImages()) {
                                    listImage.add(s);
                                }
                                rating = dish.getMaxStar();

                                initDishImage();
                                mdish_name.setText(dish.getDishName());
                                mdish_rating.setRating(dish.getMaxStar());
                                mdish_descriptionDish.setText(dish.getDishDescription());
                                mdish_price.setText("$" + dish.getDishPrice().toString());
                                if(rating*10 %10 == 0)
                                    totalRate.setText("("+rating.toString()+"/5)");
                                else
                                    totalRate.setText("("+Helper.round(rating,1)+"/5)");
                            } else {
                                // code here
                            }
                        } else {
                            // code here
                        }
                    }
                });
    }

    private void initCommentDish(String dishId) {
        lView = findViewById(R.id.listComment);
        getDataComment(dishId);
    }

    private void SendComment() {
            CMDB.db.collection("information").document("cmt_dish_max_id")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists())
                                {
                                    Integer maxId = ((Number)document.get("cmt_dish_max_id")).intValue();
                                    maxId++;
                                    idCmt = CommentDish.createCommentDishId(maxId);
                                    //phải đánh giá mới đc tính
                                    Log.e("rateTrue",String.valueOf(mdish_evaluteDish.getRating()));
                                    if(rating == 0)
                                    {
                                        rating = mdish_evaluteDish.getRating();
                                    }
                                    else
                                    {
                                        rating = (rating+mdish_evaluteDish.getRating())/2;
                                    }
                                    if (mdish_evaluteDish.getRating() != 0) {
                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = new Date();
                                        final String stringDate = dateFormat.format(date);
                                        final Map<String ,Object> commentDish = CommentDish.createCommentDishData(
                                                idCmt,
                                                mdish_txtComment.getText().toString(),
                                                stringDate,
                                                dish.getDishId(),
                                                user.getUsername(),
                                                mdish_evaluteDish.getRating()
                                        );

                                        CMDB.db.collection("comment_dish").document()
                                                .set(commentDish)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        CommentDish addCmt = new CommentDish(
                                                                idCmt,
                                                                mdish_txtComment.getText().toString(),
                                                                stringDate,
                                                                dish.getDishId(),
                                                                user.getUsername(),
                                                                mdish_evaluteDish.getRating()
                                                        );
                                                        //set dieu kien

                                                        adapterC.commentDishList.add(0,addCmt);
                                                        adapterC.notifyDataSetChanged();
                                                        Toast.makeText(getApplicationContext(),"Gửi bình luận thành công",Toast.LENGTH_LONG).show();
                                                        mdish_txtComment.setText("");
                                                        mdish_evaluteDish.setRating(0);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),"Gửi bình luận thất bại",Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                    } else
                                        Toast.makeText(getApplicationContext(), "Mời bạn đánh giá ", Toast.LENGTH_LONG).show();
                                    //update lại max id
                                    CMDB.db.collection("information").document("cmt_dish_max_id").update("cmt_dish_max_id",maxId);
                                    Log.e("rate",rating.toString());
                                    Log.e("rateID",dish.getDishId());
                                    CMDB.db.collection("dish").document(dish.getDishId()).update("max_star",rating);
                                }
                            }
                        }
                    });



    }

    private void getDataComment(String dishId) {
        CMDB.db.collection("comment_dish")
                .whereEqualTo("dish_id", dishId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                        listComment.add(CommentDish.loadCommentDish(document.getData()));
                                }
                                totalCmt.setText("("+String.valueOf(listComment.size())+" phiếu)");
                                adapterC = new DishCommentAdapter(getApplicationContext(),R.layout.dish_comment_item ,listComment);
                                lView.setAdapter(adapterC);
                        }
                    }
                });
    }

    private void initInfoDish() {

        dishImagesViewer = (ViewPager) findViewById(R.id.dish_images_viewer);
        mdish_name = findViewById(R.id.dish_name);
        mdish_rating = findViewById(R.id.dish_rating);
        mdish_price = findViewById(R.id.dish_price);
        mdish_descriptionDish = findViewById(R.id.dish_description);
        totalCmt = findViewById(R.id.vote);
        totalRate = findViewById(R.id.rating_score);

        //comment
        mdish_evaluteDish = findViewById(R.id.evaluate_dish_rating);
        mdish_txtComment = findViewById(R.id.comment_text);


    }

    private void initDishImage() {
        //Image dish
        //phhviet: Code view pager

        adapter = new ViewPageImageAdapter(listImage, this,dish);
        dishImagesViewer.setAdapter(adapter);

        //phhviet: use lib 'me.relex:circleindicator:1.2.2' draw circle indicator
        try {
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(dishImagesViewer);
            adapter.registerDataSetObserver(indicator.getDataSetObserver());

        }
        catch(Exception ex) {
            Log.e("errorInit", ex.toString());
        }

    }
}