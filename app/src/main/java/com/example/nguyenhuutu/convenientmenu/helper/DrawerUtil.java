package com.example.nguyenhuutu.convenientmenu.helper;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.example.nguyenhuutu.convenientmenu.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class DrawerUtil {
    public static void getMainMenuDrawer(final Activity activity, Toolbar toolbar) {
        PrimaryDrawerItem drawerHome = new PrimaryDrawerItem().withIdentifier(0)
                .withName(R.string.main_menu_home).withIcon(R.drawable.ic_home);
        PrimaryDrawerItem drawerRestList = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.main_menu_restaurant_list).withIcon(R.drawable.ic_restaurant_list);
        PrimaryDrawerItem drawerMarkList = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.main_menu_mark).withIcon(R.drawable.ic_bookmark);
        PrimaryDrawerItem drawerLogin = new PrimaryDrawerItem().withIdentifier(3)
                .withName(R.string.main_menu_login).withIcon(R.drawable.ic_login);
        PrimaryDrawerItem drawerLogout = new PrimaryDrawerItem().withIdentifier(4)
                .withName(R.string.main_menu_logout).withIcon(R.drawable.ic_logout);
        PrimaryDrawerItem drawerRegister = new PrimaryDrawerItem().withIdentifier(5)
                .withName(R.string.main_menu_register).withIcon(R.drawable.ic_register);
        PrimaryDrawerItem drawerChangePassword = new PrimaryDrawerItem().withIdentifier(6)
                .withName(R.string.main_menu_change_password).withIcon(R.drawable.ic_change_password);
        PrimaryDrawerItem drawerAccountInfo = new PrimaryDrawerItem().withIdentifier(7)
                .withName(R.string.main_menu_account_info).withIcon(R.drawable.ic_account_info);
        PrimaryDrawerItem drawerSetting = new PrimaryDrawerItem().withIdentifier(8)
                .withName(R.string.main_menu_setting).withIcon(R.drawable.ic_setting);
        PrimaryDrawerItem drawerManageMenu = new PrimaryDrawerItem().withIdentifier(9)
                .withName(R.string.main_menu_manage_menu).withIcon(R.drawable.ic_manage_menu);
        PrimaryDrawerItem drawerManageEvent = new PrimaryDrawerItem().withIdentifier(10)
                .withName(R.string.main_menu_manage_event).withIcon(R.drawable.ic_manage_event);

        //create the drawer and remember the `Drawer` result object
//        Drawer result = new DrawerBuilder()
//                .withActivity(activity)
//                .withToolbar(toolbar)
//                .withActionBarDrawerToggle(true)
//                .withActionBarDrawerToggleAnimated(true)
//                .withCloseOnClick(true)
//                .withSelectedItem(-1)
//                .withSliderBackgroundColorRes(R.color.colorMainMenu)
//                .addDrawerItems(
//                        drawerHome,
//                        drawerRestList,
//                        drawerSetting,
//                        new DividerDrawerItem(),
//                        drawerMarkList,
//                        drawerManageMenu,
//                        drawerManageEvent,
//                        drawerChangePassword,
//                        drawerAccountInfo,
//                        drawerLogin,
//                        drawerLogout,
//                        drawerRegister
//                )
//                .build();
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(R.color.colorMainMenu)
//                .inflateMenu(R.menu.drawer_menu)
                .withDrawerLayout(R.layout.main_menu)
                .addDrawerItems()
                .build();
    }
}
