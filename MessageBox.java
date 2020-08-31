package com.e_bus.e_bus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import org.json.JSONException;

public class MessageBox extends DialogFragment {
    private String _title, _message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(_title);
        builder.setMessage(_message);

        builder.setPositiveButton(getString(R.string.button_msg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

    public void show(FragmentManager manager, String tittle, String message) {
        _title = tittle;
        _message = message;
        super.show(manager, "");
    }

    public void show(FragmentManager manager, JSONException error, String tittle, String message) {
        Log.wtf("!!!",error.getLocalizedMessage()+"\n"+error.toString());
        _title = tittle;
        _message = message;
        super.show(manager, "");
    }

    public void show(FragmentManager manager, VolleyError error, String tittle, String message) {
        Log.wtf("!!!",error.getLocalizedMessage()+"\n"+error.toString());
        _title = tittle;
        _message = message;
        super.show(manager, "");
    }
}
