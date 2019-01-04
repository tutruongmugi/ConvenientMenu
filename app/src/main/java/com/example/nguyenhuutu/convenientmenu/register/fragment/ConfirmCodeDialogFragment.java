package com.example.nguyenhuutu.convenientmenu.register.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguyenhuutu.convenientmenu.CMDB;
import com.example.nguyenhuutu.convenientmenu.R;
import com.example.nguyenhuutu.convenientmenu.register.RestaurantRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ConfirmCodeDialogFragment extends DialogFragment implements View.OnClickListener {
    private EditText code;
    private EditText id;
    private Button confirmButton;
    private RestaurantRegister activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_restaurant, container);
        id = view.findViewById(R.id.id);
        code = view.findViewById(R.id.code);
        confirmButton = view.findViewById(R.id.confirmButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Mã xác nhận");
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmButton) {
            if (code.getText().toString().isEmpty() == false && id.getText().toString().isEmpty() == false) {
                CMDB.db
                        .collection("code")
                        .document(id.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String codeStr = code.getText().toString();
                                        String codeCmp = document.getData().get("code").toString();

                                        if (codeStr.matches(codeCmp.toString())) {
                                            getDialog().hide();
                                            ((RestaurantRegister)getActivity()).notifyConfirmedRestaurant();
                                        }
                                        else {
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "not have doc", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "get unsuccess", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {

            }
        }
        else {

        }
    }
}
