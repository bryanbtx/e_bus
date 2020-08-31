package com.e_bus.e_bus;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlterarUsuario extends AppCompatActivity {

    private EditText editTextAltNome, editTextAltData, editTextAltTelefone, editTextAltEmail, editTextAltMae;
    private RadioButton radioButtonAltMacho, radioButtonAltFemea;
    Button buttonAltConta, buttonZerar;
    ProgressBar progressBarAltConta;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        editTextAltNome = findViewById(R.id.editTextAltNome);
        editTextAltData = findViewById(R.id.editTextAltData);
        editTextAltTelefone = findViewById(R.id.editTextAltTelefone);
        editTextAltEmail = findViewById(R.id.editTextAltEmail);
        editTextAltMae = findViewById(R.id.editTextAltMae);

        radioButtonAltMacho = findViewById(R.id.radioButtonAltMacho);
        radioButtonAltFemea = findViewById(R.id.radioButtonAltFemea);

        progressBarAltConta = findViewById(R.id.progressBarAltConta);

        buttonZerar = findViewById(R.id.buttonZerar);
        buttonZerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAltData.setText("");
            }
        });

        buttonAltConta = findViewById(R.id.buttonAltConta);
        buttonAltConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexo, msg = "";
                int mudou = 8;
                Validate.Response[] response = new Validate.Response[5];
                String[] stringShared = new String[7];

                response[0] = Validate.nomeToDB(editTextAltNome.getText().toString(), getApplicationContext());
                response[1] = Validate.dataToDB(editTextAltData.getText().toString(), getApplicationContext());
                response[2] = Validate.telefoneToDB(editTextAltTelefone.getText().toString(), getApplicationContext());
                response[3] = Validate.emailToDB(editTextAltEmail.getText().toString(), getApplicationContext());
                if (radioButtonAltMacho.isChecked()) {
                    sexo = "m";
                } else if (radioButtonAltFemea.isChecked()) {
                    sexo = "f";
                } else {
                    sexo = SharedPreferencesManager.getInstance(getApplicationContext()).getSexo();
                    mudou--;
                }
                response[4] = Validate.nomeMaeToDB(editTextAltMae.getText().toString(), getApplicationContext());

                stringShared[0] = SharedPreferencesManager.getInstance(getApplicationContext()).getNome();
                stringShared[1] = SharedPreferencesManager.getInstance(getApplicationContext()).getData();
                stringShared[2] = SharedPreferencesManager.getInstance(getApplicationContext()).getTelefone();
                stringShared[3] = SharedPreferencesManager.getInstance(getApplicationContext()).getEmail();
                stringShared[4] = SharedPreferencesManager.getInstance(getApplicationContext()).getMae();

                for (int i = 0; i < response.length; i++) {
                    Log.wtf("!!!",""+i);
                    if (response[i].erro == 2) {
                        msg += response[i].formatedString + "\n";
                    } else if (response[i].erro == 1) {
                        response[i].formatedString = stringShared[i];
                        mudou--;
                    }
                }
                if (msg.isEmpty()) {
                    if (mudou > 0) {
                        alterUsuario(String.valueOf(SharedPreferencesManager.getInstance(getApplicationContext()).getId()), response[0].formatedString, response[1].formatedString, response[2].formatedString, response[3].formatedString, sexo, response[4].formatedString);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_todos_vazios), Toast.LENGTH_LONG).show();
                    }
                } else {
                    new MessageBox().show(getFragmentManager(), getString(R.string.tittle_error), msg);
                }
            }
        });
        editTextAltData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlterarUsuario.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateText() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        editTextAltData.setText(sdf.format(myCalendar.getTime()));
    }

    private void alterUsuario(final String id, final String nome, final String data, final String telefone, final String email, final String sexo, final String mae) {
        progressBarAltConta.setVisibility(View.VISIBLE);
        buttonAltConta.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_alterUsuario, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(Integer.valueOf(id), SharedPreferencesManager.getInstance(getApplicationContext()).getCPF(), SharedPreferencesManager.getInstance(getApplicationContext()).getRG(), nome, data, telefone, email, sexo, mae, SharedPreferencesManager.getInstance(getApplicationContext()).getSenha());
                    finish();
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(), e, getString(R.string.tittle_error), e.getLocalizedMessage());
                }
                progressBarAltConta.setVisibility(View.INVISIBLE);
                buttonAltConta.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarAltConta.setVisibility(View.INVISIBLE);
                buttonAltConta.setVisibility(View.VISIBLE);
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nome", nome);
                params.put("data_nasc", data);
                params.put("telefone", telefone);
                params.put("email", email);
                params.put("sexo", sexo);
                params.put("nome_mae", mae);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
