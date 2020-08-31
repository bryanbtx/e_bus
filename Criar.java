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

public class Criar extends AppCompatActivity {

    private EditText editTextCPF, editTextRG, editTextNome, editTextData, editTextTelefone, editTextEmail, editTextMae, editTextCsenha, editTextRsenha;
    private RadioButton radioButtonMacho;
    Button buttonCriarConta;
    ProgressBar progressBarCriarConta;
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
        setContentView(R.layout.activity_criar);

        editTextCPF = findViewById(R.id.editTextCCPF);
        editTextRG = findViewById(R.id.editTextRG);
        editTextNome = findViewById(R.id.editTextNome);
        editTextData = findViewById(R.id.editTextData);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMae = findViewById(R.id.editTextMae);
        editTextCsenha = findViewById(R.id.editTextCsenha);
        editTextRsenha = findViewById(R.id.editTextRsenha);

        radioButtonMacho = findViewById(R.id.radioButtonMacho);

        progressBarCriarConta = findViewById(R.id.progressBarCriarConta);

        buttonCriarConta = findViewById(R.id.buttonAltConta);
        buttonCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexo, msg = "";
                Validate.Response[] response = new Validate.Response[8];
                response[0] = Validate.cpfToDB(editTextCPF.getText().toString(),getApplicationContext());
                response[1] = Validate.rgToDB(editTextRG.getText().toString(),getApplicationContext());
                response[2] = Validate.nomeToDB(editTextNome.getText().toString(),getApplicationContext());
                response[3] =Validate.dataToDB(editTextData.getText().toString(),getApplicationContext());
                response[4] = Validate.telefoneToDB(editTextTelefone.getText().toString(),getApplicationContext());
                response[5] = Validate.emailToDB(editTextEmail.getText().toString(),getApplicationContext());
                sexo = radioButtonMacho.isChecked() ? "m" : "f";
                response[6] = Validate.nomeMaeToDB(editTextMae.getText().toString(),getApplicationContext());
                response[7] = Validate.senhaToDB(editTextCsenha.getText().toString(), editTextRsenha.getText().toString(),getApplicationContext());
                for (int i = 0; i < response.length; i++) {
                    if (response[i].erro!=0) {
                        msg += response[i].formatedString + "\n";
                    }
                }
                if (msg.isEmpty()) {
                    insertUsuario(response[0].formatedString, response[1].formatedString, response[2].formatedString, response[3].formatedString, response[4].formatedString, response[5].formatedString, sexo, response[6].formatedString, response[7].formatedString);
                } else {
                    new MessageBox().show(getFragmentManager(), getString(R.string.tittle_error), msg);
                }
            }
        });
        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Criar.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateText() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        editTextData.setText(sdf.format(myCalendar.getTime()));
    }

    private void insertUsuario(final String cpf, final String rg, final String nome, final String data, final String telefone, final String email, final String sexo, final String mae, final String senha) {
        progressBarCriarConta.setVisibility(View.VISIBLE);
        buttonCriarConta.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_insertUsuario, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getFragmentManager(),e, getString(R.string.tittle_error), e.getLocalizedMessage());
                }
                progressBarCriarConta.setVisibility(View.INVISIBLE);
                buttonCriarConta.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarCriarConta.setVisibility(View.INVISIBLE);
                buttonCriarConta.setVisibility(View.VISIBLE);
                new MessageBox().show(getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cpf", cpf);
                params.put("rg", rg);
                params.put("nome", nome);
                params.put("data_nasc", data);
                params.put("telefone", telefone);
                params.put("email", email);
                params.put("sexo", sexo);
                params.put("nome_mae", mae);
                params.put("senha", senha);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
