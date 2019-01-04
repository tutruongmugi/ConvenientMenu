package com.example.nguyenhuutu.convenientmenu.main;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyenhuutu.convenientmenu.Const;
import com.example.nguyenhuutu.convenientmenu.CMStorage;
import com.example.nguyenhuutu.convenientmenu.eventmanage.ManageEvent;
import com.example.nguyenhuutu.convenientmenu.manage_menu.Manage_Menu;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.RequestServer;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.example.nguyenhuutu.convenientmenu.homepage.fragment.HomePageFragment;
import com.example.nguyenhuutu.convenientmenu.login.LoginFragment;
import com.example.nguyenhuutu.convenientmenu.register.fragment.SwitchRegisterFragment;
import com.example.nguyenhuutu.convenientmenu.restaurant_list.RestaurantList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

//import static org.json.JSONObject.NULL;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private NavigationView mainMenu;
    private DrawerLayout mDrawerLayout;
    public Fragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle dataIntent = getIntent().getExtras();

        if (dataIntent == null || !dataIntent.containsKey("fragment")) {
            contentFragment = new HomePageFragment();
        }
        else {
            contentFragment = getChosenFragment(dataIntent.getInt("fragment"));
        }

        mToolbar= findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mainMenu = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_menu_icon);

        setMainMenu();
        configMainMenu();

        switchContent(contentFragment);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void setMainMenu() {
        UserSession loginedUserJson = Helper.getLoginedUser(this);

        if (loginedUserJson.isExists() == true) {
            if (loginedUserJson.isRest()) {
                mainMenu.inflateMenu(R.menu.main_menu_restaurant_login);
            }
            else {
                mainMenu.inflateMenu(R.menu.main_menu_customer_login);
            }
            setHeaderOfMainMenu(R.layout.nav_header_login, loginedUserJson);
        }
        else {
            mainMenu.inflateMenu(R.menu.main_menu_non_login);
            setHeaderOfMainMenu(R.layout.nav_header_non_login, loginedUserJson);
        }

//        try{
//            loginedUserJson = Helper.getLoginedUser(this);
//
//            if (loginedUserJson.getBoolean("logined") == true) {
//                if (loginedUserJson.getBoolean("isRest") == true) {
//                    mainMenu.inflateMenu(R.menu.main_menu_restaurant_login);
//                }
//                else {
//                    mainMenu.inflateMenu(R.menu.main_menu_customer_login);
//                }
//                setHeaderOfMainMenu(R.layout.nav_header_login, loginedUserJson);
//            }
//            else {
//                mainMenu.inflateMenu(R.menu.main_menu_non_login);
//                setHeaderOfMainMenu(R.layout.nav_header_non_login, loginedUserJson);
//            }
//        }
//        catch(Exception ex) {
//            Toast.makeText(this, "get logined user error: " + ex.toString(), Toast.LENGTH_SHORT).show();
//        }
    }

    private void setHeaderOfMainMenu(int id, UserSession userSession) {
        mainMenu.removeHeaderView(mainMenu.getHeaderView(0));
        mainMenu.addHeaderView(getLayoutInflater().inflate(id, null));

        if (id == R.layout.nav_header_login) {
            UserInfoTask userInfo = new UserInfoTask();
            userInfo.execute(userSession.getUsername(), userSession.isRest());
        }
    }

    public void configMainMenu() {
        View headerView = mainMenu.getHeaderView(0);

        headerView.findViewById(R.id.headerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //mainMenu.getMenu().findItem(R.id.main_menu_home).setVisible(false);
        mainMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            private static final Object NullPointerException = NULL;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                switch (menuItem.getItemId()) {
                    case R.id.main_menu_home:
                        if (!(contentFragment instanceof HomePageFragment)) {
                            setTitle("Convenient Menu");
                            contentFragment = new HomePageFragment();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_restaurant_list:
                        if (!(contentFragment instanceof RestaurantList)) {
                            setTitle("Restaurant List");
                            contentFragment = new RestaurantList();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_info_account:
                        break;
                    case R.id.main_menu_change_password:
                        break;
                    case R.id.main_menu_list_mark:
                        break;
                    case R.id.main_menu_manage_menu:
                        if (!(contentFragment instanceof Manage_Menu)) {
                            setTitle("Quản lý thực đơn");
                            contentFragment = new Manage_Menu();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_manage_event:
                        if (!(contentFragment instanceof ManageEvent)) {
                            setTitle("Quản lý sự kiện");
                            contentFragment = new ManageEvent();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_login:
                        if (!(contentFragment instanceof LoginFragment)) {
                            setTitle("Đăng nhập");
                            contentFragment = new LoginFragment();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_register:
                        if (!(contentFragment instanceof SwitchRegisterFragment)) {
                            setTitle("Đăng ký");
                            contentFragment = new SwitchRegisterFragment();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_logout:
                        Helper.changeUserSession(MainActivity.this, new UserSession());
                        updateMainMenu();

                        if (!(contentFragment instanceof HomePageFragment)) {
                            setTitle("Trang chủ");
                            contentFragment = new HomePageFragment();
                            switchContent(contentFragment);
                        }
                        break;
                    case R.id.main_menu_setting:
                        break;
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        ActionBarDrawerToggle mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//        checkMenuItem();
    }

    private Fragment getChosenFragment(int fragmentId) {
        Fragment fragment = new HomePageFragment();

        switch (fragmentId) {
            case Helper.FRAGMENT_RESTAURANT_LIST:
                break;
            case Helper.FRAGMENT_ACCOUNT_INFO:
                break;
            case Helper.FRAGMENT_CHANGE_PASSWORD:
                break;
            case Helper.FRAGMENT_MARK_LIST:
                break;
            case Helper.FRAGMENT_MANAGE_MENU:
                fragment = new Manage_Menu();
                break;
            case Helper.FRAGMENT_MANAGE_EVENT:
                break;
            case Helper.FRAGMENT_LOGIN:
                fragment = new LoginFragment();
                break;
            case Helper.FRAGMENT_LOGOUT:
                break;
            case Helper.FRAGMENT_REGISTER:
                fragment = new SwitchRegisterFragment();
                break;
            case Helper.FRAGMENT_SETTING:
                break;
        }

        return fragment;
    }

//    public void checkMenuItem() {
//        if (activity instanceof HomePage) {
//            mainMenu.setCheckedItem(0);
//        }
//        else if (activity instanceof SwitchRegister) {
//            mainMenu.setCheckedItem(R.id.main_menu_register);
//        }
//    }

    public void switchContent(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainContent, frag);
        ft.commit();
    }

    public void setContent(Fragment frag) {
        if (frag instanceof HomePageFragment) {
            setTitle(getResources().getString(R.string.home_page_title));
        }
        else if (frag instanceof SwitchRegisterFragment) {
            setTitle(getResources().getString(R.string.switch_register_title));
        }
        else if (frag instanceof LoginFragment) {
            setTitle(getResources().getString(R.string.login_title));
        }
        else if (frag instanceof SwitchRegisterFragment) {
            setTitle(getResources().getString(R.string.switch_register_title));
        }

        contentFragment = frag;
        switchContent(contentFragment);
    }

    public void updateMainMenu() {
        removeOldMainMenu();
        setMainMenu();
    }

    public void removeOldMainMenu() {
        mainMenu.getMenu().removeItem(R.id.main_menu_home);
        mainMenu.getMenu().removeItem(R.id.main_menu_restaurant_list);
        mainMenu.getMenu().removeItem(R.id.main_menu_list_mark);
        mainMenu.getMenu().removeItem(R.id.main_menu_login);
        mainMenu.getMenu().removeItem(R.id.main_menu_logout);
        mainMenu.getMenu().removeItem(R.id.main_menu_register);
        mainMenu.getMenu().removeItem(R.id.main_menu_manage_event);
        mainMenu.getMenu().removeItem(R.id.main_menu_manage_menu);
        mainMenu.getMenu().removeItem(R.id.main_menu_setting);
        mainMenu.getMenu().removeItem(R.id.main_menu_change_password);
        mainMenu.getMenu().removeItem(R.id.main_menu_info_account);
    }

    private void updateInfoHeaderMainMenu(String data) {
        try{
            JSONObject result = new JSONObject(data);
            View headerView = mainMenu.getHeaderView(0);

            final ImageView userAvatar = headerView.findViewById(R.id.userAvatar);
            TextView userAccountName = headerView.findViewById(R.id.userAccountName);
            if (result.getBoolean("isSuccess") == true) {
                if (result.getJSONObject("data").getBoolean("isRest") == true) {
                    userAccountName.setText(result.getJSONObject("data").getString("name"));

                    CMStorage.storage.child("images/restaurant/" + result.getJSONObject("data").getString("homeImageFile"))
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try{
                                        Glide
                                                .with(getApplicationContext())
                                                .load(uri.toString())
                                                .into(userAvatar);
                                    }
                                    catch(Exception ex) {
                                        //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    String name = result.getJSONObject("data").getString("lastname");
                    name += " ";
                    name += result.getJSONObject("data").getString("firstname");
                    userAccountName.setText(name);

                    CMStorage.storage.child("images/customer/" + result.getJSONObject("data").getString("avatarImageFile"))
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try{
                                        Glide
                                                .with(getApplicationContext())
                                                .load(uri.toString())
                                                .into(userAvatar);
                                    }
                                    catch(Exception ex) {
                                        //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            else {
                //Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception ex){
            //Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class UserInfoTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... data) {
            String result = "";
            String url = "https://convenientmenu.herokuapp.com/user/";
            if ((boolean)data[1] == true) {
                url += "restaurant/" + data[0];
            }
            else {
                url += "customer/" + data[0];
            }
            RequestServer request = new RequestServer(url, "GET");
            result = request.executeRequest();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            updateInfoHeaderMainMenu(result);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.ADD_DISH || requestCode == Const.EDIT_DISH) {
            ((Manage_Menu)contentFragment).onActivityResult(requestCode, resultCode, data);
        }
    }
}
