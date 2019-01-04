package com.example.nguyenhuutu.convenientmenu;

import android.app.Activity;

public abstract class User {
    protected String account;
    protected String password;

    public abstract void login(final Activity activity);
    public abstract void register(final Activity activity);
    public abstract boolean checkRegisterInfo();
    public abstract boolean checkLoginInfo();
}
