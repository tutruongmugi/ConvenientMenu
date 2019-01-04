package com.example.nguyenhuutu.convenientmenu.login;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.Customer;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.helper.Helper;
import com.example.nguyenhuutu.convenientmenu.helper.UserSession;
import com.example.nguyenhuutu.convenientmenu.homepage.fragment.HomePageFragment;
import com.example.nguyenhuutu.convenientmenu.main.MainActivity;
import com.example.nguyenhuutu.convenientmenu.register.fragment.SwitchRegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Fragment for login screen
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputLayout usernameBound;
    private TextInputLayout passwordBound;

    private Button loginButton;
    private Button registerButton;

    private ProgressDialog process;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        //hold these view
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        usernameBound = view.findViewById(R.id.usernameBound);
        passwordBound = view.findViewById(R.id.passwordBound);
        loginButton = view.findViewById(R.id.loginBtn);
        registerButton = view.findViewById(R.id.registerBtn);

        process = new ProgressDialog(getActivity());
        process.setMessage("Logining. Please Wait for a while.");
        process.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //set onclick for loginBtn
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                //get values for checking
                final String usernameStr = username.getText().toString();
                final String passwordStr = password.getText().toString();

                //check 2 fields whether empty or not
                if (checkValidInfo()) {
                    process.show();
                    CMDB.db
                            .collection("customer")
                            .document(usernameStr)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            if (Helper.getCompressPassword(passwordStr).equals(document.getString("cus_password"))) {
                                                Helper.changeUserSession(getActivity(), new UserSession(usernameStr, UserSession.CUSTOMER));
                                                ((MainActivity)getActivity()).updateMainMenu();
                                                ((MainActivity)getActivity()).setContent(new HomePageFragment());
                                            }
                                            else {
                                                passwordBound.setError(getResources().getString(R.string.wrong_password_mesage));
                                            }
                                        } else {
                                            CMDB.db
                                                    .collection("restaurant")
                                                    .document(usernameStr)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    if (Helper.getCompressPassword(passwordStr).equals(document.getString("rest_password"))) {
                                                                        Helper.changeUserSession(getActivity(), new UserSession(usernameStr, UserSession.RESTSTAURANT));
                                                                        ((MainActivity)getActivity()).updateMainMenu();
                                                                        ((MainActivity)getActivity()).setContent(new HomePageFragment());
                                                                    }
                                                                    else {
                                                                        passwordBound.setError(getResources().getString(R.string.wrong_password_mesage));
                                                                    }
                                                                } else {
                                                                    usernameBound.setError(getResources().getString(R.string.wrong_username_mesage));
                                                                    passwordBound.setError(getResources().getString(R.string.wrong_password_mesage));
                                                                }
                                                            } else {

                                                            }
                                                        }
                                                    });
                                        }
                                    } else {

                                    }
                                }
                            });
                    process.hide();
                }
                break;
            case R.id.registerBtn:

                ((MainActivity)getActivity()).setContent(new SwitchRegisterFragment());
                break;
        }

//        userSession = new UserSession(getActivity());
//        userSession.createUserLoginSession(uUsername, uPassword);
    }

    //to validate information from text input of user
    //if correct, login and create session
    //otherwise, do nothing
//    boolean isInformationCorrect() {
//        boolean result = false;
//
//        //checking code here
//        if((uUsername.equals("123") ) && (uPassword.equals("123"))) {
//            result = true;
//        }
//
//        return result;
//    }

    private boolean checkValidInfo() {
        boolean validUsername = checkValidUsername();
        boolean validPassword = checkValidPassword();
        if (validUsername && validPassword) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkValidUsername() {
        boolean result = true;

        String usernameStr = username.getText().toString();

        if (usernameStr.equals("")) {
            result = false;
            username.setBackground(getResources().getDrawable(R.drawable.transparent));
            usernameBound.setError(getResources().getString(R.string.empty_error_mesage));
        }
        else if (usernameStr.contains(" ")) {
            result = false;
            username.setBackground(getResources().getDrawable(R.drawable.transparent));
            usernameBound.setError(getResources().getString(R.string.invalid_username_error_mesage));
        }
        else {
            username.setBackground(getResources().getDrawable(R.drawable.border_input_bottom));
            usernameBound.setError(null);
        }

        return result;
    }

    private boolean checkValidPassword() {
        boolean result = true;

        String passwordStr = password.getText().toString();

        if (passwordStr.equals("")) {
            result = false;
            passwordBound.setError(getResources().getString(R.string.empty_error_mesage));
        }
        else {
            passwordBound.setError(null);
        }

        return result;
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
