package com.e_bus.e_bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Esqueceu extends AppCompatActivity {
    EditText editTextEsqueceuCPF;
    Button buttonEsqueceuEmail;
    ProgressBar progressBarEsqueceu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu);

        editTextEsqueceuCPF=findViewById(R.id.editTextEsqueceuCPF);
        buttonEsqueceuEmail=findViewById(R.id.buttonEsqueceuEmail);
        progressBarEsqueceu=findViewById(R.id.progressBarEsqueceu);

        buttonEsqueceuEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(editTextEsqueceuCPF.getText().toString());
            }
        });
    }

    private void sendEmail(final String cpf) {
        progressBarEsqueceu.setVisibility(View.VISIBLE);
        buttonEsqueceuEmail.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_sendEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        new MessageBox().show(getFragmentManager(),getString(R.string.tittle_mensagen),jsonObject.getString("email")+"\n"+jsonObject.getString("message"));

                    } else {
                        new MessageBox().show(getFragmentManager(),getString(R.string.tittle_error),jsonObject.getString("message"));
                        SharedPreferencesManager.getInstance(getApplicationContext()).setLastLogin(false);
                    }
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(),e,getString(R.string.tittle_error),e.getLocalizedMessage());
                }
                progressBarEsqueceu.setVisibility(View.INVISIBLE);
                buttonEsqueceuEmail.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
                progressBarEsqueceu.setVisibility(View.INVISIBLE);
                buttonEsqueceuEmail.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cpf", cpf);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
