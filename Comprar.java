package com.e_bus.e_bus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.billingclient.api.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comprar extends Fragment implements com.android.billingclient.api.PurchasesUpdatedListener {
    Button buttonComprar;
    ProgressBar progressBarComprar;
    RadioGroup radioGroupComprar;
    private BillingClient mBillingClient;

    double valor = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comprar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tittle_comprar));

        mBillingClient = BillingClient.newBuilder(getActivity()).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });

        progressBarComprar = getView().findViewById(R.id.progressBarComprar);
        radioGroupComprar = getView().findViewById(R.id.radioGroupComprar);

        buttonComprar = getView().findViewById(R.id.buttonComprar);

        buttonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroupComprar.getCheckedRadioButtonId();
                String productId = null;
                valor = 0;
                switch (radioId) {
                    case R.id.radioButtonC1:
                        productId = Constants.PRODUCT_ID_001;
                        valor = .99;
                        break;
                    case R.id.radioButtonC2:
                        productId = Constants.PRODUCT_ID_002;
                        valor = 1.99;
                        break;
                    case R.id.radioButtonC5:
                        productId = Constants.PRODUCT_ID_005;
                        valor = 4.99;
                        break;
                    case R.id.radioButtonC10:
                        productId = Constants.PRODUCT_ID_010;
                        valor = 9.99;
                        break;
                    case R.id.radioButtonC20:
                        productId = Constants.PRODUCT_ID_020;
                        valor = 19.99;
                        break;
                    case R.id.radioButtonC50:
                        productId = Constants.PRODUCT_ID_050;
                        valor = 49.99;
                        break;
                    case R.id.radioButtonC100:
                        productId = Constants.PRODUCT_ID_100;
                        valor = 99.99;
                        break;
                }
                if (!productId.isEmpty()) {
                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                            .setSku(productId)
                            .setType(BillingClient.SkuType.INAPP)
                            .build();
                    mBillingClient.launchBillingFlow(getActivity(), flowParams);
                } else {
                    new MessageBox().show(getActivity().getFragmentManager(), getString(R.string.tittle_error), getString(R.string.comprar_msg_escolha));
                }
            }
        });
    }

    private void insertSaldoNow(final String numero_cartao, final String valor) {
        buttonComprar.setVisibility(View.INVISIBLE);
        progressBarComprar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_insertSaldoNow, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    new MessageBox().show(getActivity().getFragmentManager(), getString(R.string.tittle_mensagen), jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getActivity().getFragmentManager(), e, getString(R.string.tittle_error), e.getLocalizedMessage());

                    new LocalDataBase(getContext()).insert(numero_cartao, valor);

                    BackGroundWorker.getInstance(getContext()).executeSynchronization();
                }
                buttonComprar.setVisibility(View.VISIBLE);
                progressBarComprar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                buttonComprar.setVisibility(View.VISIBLE);
                progressBarComprar.setVisibility(View.INVISIBLE);
                new MessageBox().show(getActivity().getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());

                new LocalDataBase(getContext()).insert(numero_cartao, valor);

                BackGroundWorker.getInstance(getContext()).executeSynchronization();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("valor", valor);
                params.put("numero_cartao", numero_cartao);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            insertSaldoNow(String.valueOf(SharedPreferencesManager.getInstance(getContext()).getCartaoNumber()), String.valueOf(valor));
            for (Purchase purchase : purchases) {
                mBillingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(int responseCode, String purchaseToken) {
                        Log.wtf("!!!", "consumido");
                    }
                });
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            Toast.makeText(getContext(),getString(R.string.comprar_msg_mal_sucedida),Toast.LENGTH_LONG).show();
            //new MessageBox().show(getActivity().getFragmentManager(), "Erro", "Compra mal sucedida");
        }


    }
}
