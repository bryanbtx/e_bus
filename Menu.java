package com.e_bus.e_bus;

import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Menu extends Fragment {

    Button buttonMenuSinc;
    TextView textViewSaldo;
    ProgressBar progressBarSaldo;
    ImageView imageViewQR;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tittle_menu));

        buttonMenuSinc=getView().findViewById(R.id.buttonMenuSinc);
        textViewSaldo=getView().findViewById(R.id.textViewSaldo);
        progressBarSaldo=getView().findViewById(R.id.progressBarSaldo);
        imageViewQR=getView().findViewById(R.id.imageViewQR);

        buttonMenuSinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSaldo.setVisibility(View.VISIBLE);
                textViewSaldo.setVisibility(View.INVISIBLE);
                imageViewQR.setVisibility(View.INVISIBLE);
                buttonMenuSinc.setVisibility(View.INVISIBLE);
                selectCartao(String.valueOf(SharedPreferencesManager.getInstance(getContext()).getId()));
            }
        });

        progressBarSaldo.setVisibility(View.VISIBLE);
        textViewSaldo.setVisibility(View.INVISIBLE);
        imageViewQR.setVisibility(View.INVISIBLE);
        buttonMenuSinc.setVisibility(View.INVISIBLE);
        selectCartao(String.valueOf(SharedPreferencesManager.getInstance(getContext()).getId()));
    }

    private void selectCartao(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_selectCartao, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        SharedPreferencesManager.getInstance(getContext()).setCartao(jsonObject.getInt("id"),jsonObject.getString("numero_cartao"),jsonObject.getDouble("saldo"),jsonObject.getString("status"));

                        textViewSaldo.setText(getString(R.string.string_saldo)+getString(R.string.string_moeda)+String.format("%.2f",jsonObject.getDouble("saldo")));
                        if(jsonObject.getString("status").equals("b"))
                        {
                            new MessageBox().show(getActivity().getFragmentManager(), getString(R.string.tittle_error), getString(R.string.msg_cartao_bloqueado));
                            imageViewQR.setImageBitmap(null);
                        }
                        else {
                            try {
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                BitMatrix bitMatrix = multiFormatWriter.encode(SharedPreferencesManager.getInstance(getContext()).getCartaoNumber(), BarcodeFormat.QR_CODE, 600, 600);
                                BarcodeEncoder encoder = new BarcodeEncoder();
                                Bitmap bitmap = encoder.createBitmap(bitMatrix);

                                imageViewQR.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                                new MessageBox().show(getActivity().getFragmentManager(), getString(R.string.tittle_error), e.getLocalizedMessage());
                            }
                        }
                    } else {
                        new MessageBox().show(getActivity().getFragmentManager(),getString(R.string.tittle_error),jsonObject.getString("message"));

                        textViewSaldo.setText(getString(R.string.text_saldo_padrao));
                        imageViewQR.setImageBitmap(null);
                    }
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getActivity().getFragmentManager(),e,getString(R.string.tittle_error),e.getLocalizedMessage());

                    textViewSaldo.setText(getString(R.string.text_saldo_padrao));
                    imageViewQR.setImageBitmap(null);
                }
                progressBarSaldo.setVisibility(View.INVISIBLE);
                textViewSaldo.setVisibility(View.VISIBLE);
                imageViewQR.setVisibility(View.VISIBLE);
                buttonMenuSinc.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarSaldo.setVisibility(View.INVISIBLE);
                textViewSaldo.setVisibility(View.VISIBLE);
                imageViewQR.setVisibility(View.VISIBLE);
                buttonMenuSinc.setVisibility(View.VISIBLE);
                new MessageBox().show(getActivity().getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());

                textViewSaldo.setText(getString(R.string.text_saldo_padrao));
                imageViewQR.setImageBitmap(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
