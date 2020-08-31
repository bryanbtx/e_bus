package com.e_bus.e_bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

public class AlterarSenha extends AppCompatActivity {

    EditText editTextAltSenha, editTextAltRsenha;
    Button buttonAltSenha;
    ProgressBar progressBarAltSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        editTextAltSenha = findViewById(R.id.editTextAltSenha);
        editTextAltRsenha = findViewById(R.id.editTextAltRsenha);
        buttonAltSenha = findViewById(R.id.buttonAltSenha);
        progressBarAltSenha = findViewById(R.id.progressBarAltSenha);

        buttonAltSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate.Response response = Validate.senhaToDB(editTextAltSenha.getText().toString(), editTextAltRsenha.getText().toString(),getApplicationContext());
                if (response.erro==0) {
                    alterUsuarioSenha(String.valueOf(SharedPreferencesManager.getInstance(getApplicationContext()).getId()), response.formatedString);
                } else {
                    new MessageBox().show(getFragmentManager(), getString(R.string.tittle_error), response.formatedString);
                }
            }
        });
    }

    private void alterUsuarioSenha(final String id, final String senha) {
        progressBarAltSenha.setVisibility(View.VISIBLE);
        buttonAltSenha.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_alterUsuarioSenha, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    SharedPreferencesManager.getInstance(getApplicationContext()).setSenha(senha);
                    finish();
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(), e, getString(R.string.tittle_error), e.getLocalizedMessage());
                }
                progressBarAltSenha.setVisibility(View.INVISIBLE);
                buttonAltSenha.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarAltSenha.setVisibility(View.INVISIBLE);
                buttonAltSenha.setVisibility(View.VISIBLE);
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("senha", senha);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
