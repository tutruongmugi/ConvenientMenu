package com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.Const;
import com.example.nguyenhuutu.convenientmenu.Dish;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.manage_menu.add_dish.adapter.DishImageAdapter;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class AddDish extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {
    private Toolbar toolbar;
    private ImageButton addDishImageButton;
    private Button addDishButton;
    private RecyclerView dishImageList;
    private ArrayList<String> dishImageLinks;
    private TextInputEditText dishName;
    private TextInputEditText dishPrice;
    private TextInputEditText dishInfo;
    private TextInputLayout dishNameBound;
    private TextInputLayout dishPriceBound;
    private TextInputLayout dishInfoBound;
    private Spinner dishType;
    private ArrayList<String> dishTypeIds;
    private ArrayList<String> dishTypeNames;
    private String selectedDishTypeId;
    private String restAccount;
    private Dish existsDish;
    private boolean isEdit;
    private ProgressDialog process;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        // init some values
        isEdit = false;
        
        toolbar = findViewById(R.id.addDishToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        process = new ProgressDialog(this);
        process.setMessage("Đang Thực hiện. Vui lòng đợi");
        process.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        dishName = findViewById(R.id.dishName);
        dishPrice = findViewById(R.id.dishPrice);
        dishInfo = findViewById(R.id.dishInfo);

        dishNameBound = findViewById(R.id.dishNameBound);
        dishPriceBound = findViewById(R.id.dishPriceBound);
        dishInfoBound = findViewById(R.id.dishInfoBound);

        dishType = findViewById(R.id.dishType);
        dishTypeIds = new ArrayList<String>();
        dishTypeNames = new ArrayList<String>();
        setupDishTypeList();

        dishImageLinks = new ArrayList<String>();
        dishImageList = findViewById(R.id.dishImageList);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dishImageList.setAdapter(new DishImageAdapter(this, dishImageLinks));
        dishImageList.setLayoutManager(myLayoutManager);

        addDishImageButton = findViewById(R.id.addDishImageButton);
        addDishImageButton.setOnClickListener(this);

        addDishButton = findViewById(R.id.addDishButton);
        addDishButton.setOnClickListener(this);

        getExistsDish();
        if (isEdit == true) {
            ((DishImageAdapter)dishImageList.getAdapter()).setDishId(existsDish.getDishId());

            updateUI();
            showExistsDishInfo();
        }
        else {
            // get restAccount
            UserSession loginedUser = Helper.getLoginedUser(this);
            restAccount = loginedUser.getUsername();
        }
    }

    private void getExistsDish() {
        Bundle data = getIntent().getExtras();

        if (data != null && data.containsKey("edit_dish")) {
            isEdit = true;
            existsDish = new Dish(
                    (String) data.get("dish_id"),
                    (String) data.get("dish_name"),
                    (Integer) data.get("dish_price"),
                    (String) data.get("dish_Description"),
                    (String) data.get("dish_home_image"),
                    (ArrayList<String>) data.get("dish_more_images"),
                    (String) data.get("dish_type_id"),
                    (Float) data.get("max_star"),
                    (String) data.get("rest_account"),
                    (Date) data.get("dish_create_date")
            );
        }
    }

    private void updateUI() {
        getSupportActionBar().setTitle("Cập nhật món ăn");
        dishName.setText(existsDish.getDishName());

        addDishButton.setText("Cập nhật món ăn");
    }

    private void showExistsDishInfo() {
        dishPrice.setText(String.valueOf(existsDish.getDishPrice()));
        dishInfo.setText(existsDish.getDishDescription());

        ArrayList<String> dishImages = new ArrayList<>();
        dishImages.add(existsDish.getDishHomeImage());
        ArrayList<String> dishMoreImages = existsDish.getDishMoreImages();
        int count = dishMoreImages.size();
        for (int index = 0; index < count; index++) {
            dishImages.add(dishMoreImages.get(index));
        }

        ((DishImageAdapter)dishImageList.getAdapter()).setDishImageLinks(dishImages);
        dishImageList.getAdapter().notifyDataSetChanged();
    }

    private void setupDishTypeList() {
        CMDB.db
                .collection("dish_type")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isBegin = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dishTypeIds.add(document.getString("dish_type_id").toString());
                                dishTypeNames.add(document.getString("dish_type_name").toString());
                                if (isBegin) {
                                    isBegin = false;
                                    selectedDishTypeId = dishTypeIds.get(0);
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dish_type_spinner_item, dishTypeNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dishType.setAdapter(adapter);

                            dishType.setOnItemSelectedListener(new OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedDishTypeId = dishTypeIds.get(position);
//                                    Toast.makeText(AddDish.this, selectedDishTypeId, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            if (isEdit == true) {
                                int count = dishTypeIds.size();
                                for (int index = 0; index < count; index++) {
                                    if (dishTypeIds.get(index).equals(existsDish.getDishTypeId())) {
//                                        Toast.makeText(AddDish.this, "select dish type", Toast.LENGTH_SHORT).show();
                                        dishType.setSelection(index);
                                        break;
                                    }
                                }
                            }
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null && resultData.getData() != null) {
                uri = resultData.getData();
                ((DishImageAdapter)dishImageList.getAdapter()).addDishImageLink(getUriRealPath(getApplicationContext(), uri));
                dishImageList.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addDishImageButton) {
            Intent chooseImageFile = new Intent();
            chooseImageFile.addCategory(Intent.CATEGORY_OPENABLE);

            chooseImageFile.setType("image/*");
            chooseImageFile.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(chooseImageFile, "Chọn ảnh"), PICK_IMAGE_REQUEST);
        }
        else if (v.getId() == R.id.addDishButton) {
            boolean ready = true;

            if (((String)dishName.getText().toString()).trim().equals("")) {
                ready = false;
                dishNameBound.setError("Thông tin này còn trống");
            }
            else {
                dishNameBound.setError(null);
            }

            if (((String)dishPrice.getText().toString()).trim().equals("")) {
                ready = false;
                dishPriceBound.setError("Thông tin này còn trống");
            }
            else {
                dishPriceBound.setError(null);
            }

            if (ready == true) {
                dishImageLinks = ((DishImageAdapter) dishImageList.getAdapter()).getDishImageLinks();
                
                if (dishImageLinks.size() == 0) {
                    Helper.showAlert(this, "Thông tin", "Món ăn phải có ít nhất một hình ảnh");
                }
                else {
                    try {
                        String dishNameStr = dishName.getText().toString();
                        Integer dishPriceNumber = Integer.parseInt(dishPrice.getText().toString());
                        String dishInfoStr = dishInfo.getText().toString();
                        String dishHomeImage = dishImageLinks.get(0);
                        dishImageLinks.remove(0);

                        process.show();
                        Dish dish;
                        if (isEdit == true) {
                            //Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
                            dish = new Dish(
                                    existsDish.getDishId(),
                                    dishNameStr,
                                    dishPriceNumber,
                                    dishInfoStr,
                                    dishHomeImage,
                                    dishImageLinks,
                                    selectedDishTypeId,
                                    existsDish.getMaxStar(),
                                    existsDish.getRestAccount(),
                                    existsDish.getCreateDateRaw()
                            );
                        } else {
                            dish = new Dish(dishNameStr, dishPriceNumber, dishInfoStr, dishHomeImage, dishImageLinks, selectedDishTypeId, restAccount);
                        }

                        dish.addDish(this);
                    } catch (Exception ex) {
                        //Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDishTypeId = dishTypeIds.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void notifyAddDishSuccess(String _dishId) {
        // process something
        process.hide();
        Intent intent=new Intent();
        if (selectedDishTypeId.equals(Const.DISH_TYPE_FOOD)) {
            intent.putExtra("isFood", true);
        }
        else {
            intent.putExtra("isFood", false);
        }
        intent.putExtra("dishId", _dishId);

        if (isEdit == true) {
            if (selectedDishTypeId.equals(existsDish.getDishTypeId())) {
                intent.putExtra("changeDishType", false);
            }
            else {
                intent.putExtra("changeDishType", true);
            }

            setResult(Const.EDIT_DISH, intent);
        }
        else{
            setResult(Const.ADD_DISH, intent);
        }

//        Toast.makeText(this, "Add dish success", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void notifyAddDishFailed() {
        // process something
        Toast.makeText(this, "Add dish failed", Toast.LENGTH_SHORT).show();
    }

    /* Get uri related content real local file path. */
    private String getUriRealPath(Context ctx, Uri uri)
    {
        String ret = "";

        if( isAboveKitKat() )
        {
            // Android OS above sdk version 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        }else
        {
            // Android OS below sdk version 19
            ret = getImageRealPath(getContentResolver(), uri, null);
        }

        return ret;
    }

    private String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";

        if(ctx != null && uri != null) {

            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else {
                    ret = getImageRealPath(getContentResolver(), uri, null);
                }
            }else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){

                // Get uri related document id.
                String documentId = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    documentId = DocumentsContract.getDocumentId(uri);
                }

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if(isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if("video".equals(docType))
                        {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if("audio".equals(docType))
                        {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }

                }else if(isDownloadDoc(uriAuthority))
                {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);

                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private boolean isAboveKitKat()
    {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ret = DocumentsContract.isDocumentUri(ctx, uri);
            }
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }
}
