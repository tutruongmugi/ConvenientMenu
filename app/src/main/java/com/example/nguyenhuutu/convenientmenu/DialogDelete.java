package com.example.nguyenhuutu.convenientmenu;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class DialogDelete {
    public Dialog dialog;
    public Button btnOK;
    public DialogDelete(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogdelete);
        btnOK = dialog.findViewById(R.id.ok);
        Button btnClose = dialog.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
