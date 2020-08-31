package com.e_bus.e_bus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Login extends AppCompatActivity {

    private EditText editTextCPF, editTextSenha;
    private Button buttonLogar;
    private ProgressBar progressBarLogin;
    CheckBox checkBoxCPF, checkBoxSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BackGroundWorker.getInstance(getApplicationContext()).executeSynchronization();

        editTextCPF = findViewById(R.id.editTextCPF);
        editTextSenha = findViewById(R.id.editTextSenha);

        progressBarLogin = findViewById(R.id.progressBarLogin);

        buttonLogar = findViewById(R.id.buttonLogar);
        Button buttonEsqueceu = findViewById(R.id.buttonEsqueceu);
        Button buttonCriar = findViewById(R.id.buttonCriar);

        checkBoxCPF = findViewById(R.id.checkBoxCPF);
        checkBoxSenha = findViewById(R.id.checkBoxSenha);
        if (SharedPreferencesManager.getInstance(getApplicationContext()).isCPFChecked()) {
            checkBoxCPF.setChecked(true);
            editTextCPF.setText(SharedPreferencesManager.getInstance(getApplicationContext()).getCPF());
            if (SharedPreferencesManager.getInstance(getApplicationContext()).isSenhaChecked()) {
                checkBoxSenha.setChecked(true);
                editTextSenha.setText(SharedPreferencesManager.getInstance(getApplicationContext()).getSenha());
            } else {
                checkBoxSenha.setChecked(false);
            }

        } else {
            checkBoxCPF.setChecked(false);
            checkBoxSenha.setChecked(false);
            checkBoxSenha.setEnabled(false);
        }
        checkBoxCPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance(getApplicationContext()).saveLogin(checkBoxCPF.isChecked(), checkBoxSenha.isChecked());
                checkBoxSenha.setEnabled(checkBoxCPF.isChecked());
                if (!checkBoxSenha.isEnabled()) {
                    checkBoxSenha.setChecked(false);
                }
            }
        });
        checkBoxSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance(getApplicationContext()).saveLogin(checkBoxCPF.isChecked(), checkBoxSenha.isChecked());
            }
        });

        buttonEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), Esqueceu.class);
                startActivity(myIntent);
            }
        });
        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(0, editTextCPF.getText().toString(), "", "", "", "", "", "", "", editTextSenha.getText().toString());
                selectUsuario(editTextCPF.getText().toString(), editTextSenha.getText().toString(), checkBoxCPF.isChecked(), checkBoxSenha.isChecked());
            }
        });
        buttonCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), Criar.class);
                startActivity(myIntent);
            }
        });
        if (checkBoxSenha.isChecked() && SharedPreferencesManager.getInstance(getApplicationContext()).getLastLogin()) {
            selectUsuario(SharedPreferencesManager.getInstance(getApplicationContext()).getCPF(), SharedPreferencesManager.getInstance(getApplicationContext()).getSenha(), true, true);
        }
    }

    private void selectUsuario(final String login, final String senha, final boolean saveCPF, final boolean saveSenha) {
        progressBarLogin.setVisibility(View.VISIBLE);
        buttonLogar.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_selectUsuario, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(jsonObject.getInt("id"), login, jsonObject.getString("rg"), jsonObject.getString("nome"), jsonObject.getString("data_nasc"), jsonObject.getString("telefone"), jsonObject.getString("email"), jsonObject.getString("sexo"), jsonObject.getString("nome_mae"), senha);
                        SharedPreferencesManager.getInstance(getApplicationContext()).saveLogin(saveCPF, saveSenha);
                        if (SharedPreferencesManager.getInstance(getApplicationContext()).isLoggedIn()) {
                            SharedPreferencesManager.getInstance(getApplicationContext()).setLastLogin(true);
                            selectCartao(jsonObject.getString("id"));
                        } else {
                            SharedPreferencesManager.getInstance(getApplicationContext()).setLastLogin(false);
                        }

                    } else {
                        new MessageBox().show(getFragmentManager(), getString(R.string.tittle_error), jsonObject.getString("message"));
                        SharedPreferencesManager.getInstance(getApplicationContext()).setLastLogin(false);
                    }
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(),e, getString(R.string.tittle_error), e.getLocalizedMessage());
                }
                progressBarLogin.setVisibility(View.INVISIBLE);
                buttonLogar.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
                progressBarLogin.setVisibility(View.INVISIBLE);
                buttonLogar.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cpf", login);
                params.put("senha", senha);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void selectCartao(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_selectCartao, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        SharedPreferencesManager.getInstance(getApplicationContext()).setCartao(jsonObject.getInt("id"), jsonObject.getString("numero_cartao"), jsonObject.getDouble("saldo"), jsonObject.getString("status"));
                        Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        new MessageBox().show(getFragmentManager(), getString(R.string.tittle_error), jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(),e, getString(R.string.tittle_error), e.getLocalizedMessage());
                }
                progressBarLogin.setVisibility(View.INVISIBLE);
                buttonLogar.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarLogin.setVisibility(View.INVISIBLE);
                buttonLogar.setVisibility(View.VISIBLE);
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
